package org.estacionarapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.estacionarapido.dto.ParkingRecord;
import org.estacionarapido.dto.ParkingRegisterProposal;
import org.estacionarapido.dto.PriceOption;
import org.estacionarapido.dto.Scenario;
import org.estacionarapido.exceptions.ClosedException;
import org.estacionarapido.exceptions.HasAlreadyPaidException;
import org.estacionarapido.exceptions.NoCheckoutException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.exceptions.TooOldCheckoutException;
import org.estacionarapido.persistence.BusinessHourEntity;
import org.estacionarapido.persistence.FrequencyEnum;
import org.estacionarapido.persistence.OperationEntity;
import org.estacionarapido.persistence.PriceModelEntity;
import org.estacionarapido.persistence.ScenarioEntity;

@ApplicationScoped
public class ParkingService {
    @Inject
    EntityManager em;

    private ScenarioEntity getCurrentScenarioEntity() throws NoScenariosException
    {
        OffsetDateTime rightNow = OffsetDateTime.now();
        List<ScenarioEntity> currentScenarios = em.createNamedQuery("ScenarioEntities.getCurrentScenarios", ScenarioEntity.class).setParameter("tmsZ", rightNow).getResultList();
        if (currentScenarios.size() > 1) {
            return currentScenarios.get(1); // TODO: maybe the tiebreaker criteria will change later.
        } else if (currentScenarios.size() == 1) {
            return currentScenarios.get(0);
        } else {
            throw new NoScenariosException("There is no scenarios in the current date and time. Please, check the state and position of default scenario in the database.");
        }
    }

    private boolean isAvaliable(ScenarioEntity sc)
    {
        OffsetDateTime today = OffsetDateTime.now();
        return em.createNamedQuery("BusinessHourEntities.getAvaliable", BusinessHourEntity.class)
            .setParameter("id", sc.id)
            .setParameter("weekDay", today.getDayOfWeek().getValue())
            .setParameter("time", today.toLocalTime())    
        .getResultList().size() != 0;
    }
    @Transactional
    public Scenario getCurrentScenario() throws NoScenariosException
    {
        ScenarioEntity origin = getCurrentScenarioEntity();
        Scenario result = new Scenario(origin.id,
            origin.name, isAvaliable(origin), origin.capacity,
            new ArrayList<PriceOption>());
        for (PriceModelEntity priceModel : origin.prices) {
            if (priceModel.isActivated) {
                PriceOption iPriceOption = new PriceOption(priceModel.id, 
                    priceModel.name, priceModel.frequencyValue, 
                    priceModel.frequencyType.toString(), priceModel.value);
                result.prices().add(iPriceOption);
            }
        }
        return result;

    }

    @Transactional
    public Long getOccupancy()
    {
        return em.createNamedQuery("OperationEntities.getOccupancy", Long.class).getSingleResult();
    }

    @Transactional
    public void register(ParkingRegisterProposal proposal) throws IndexOutOfBoundsException, NoScenariosException, ClosedException
    {
        ScenarioEntity currentScenario = getCurrentScenarioEntity();
        if (!isAvaliable(currentScenario)) {
            throw new ClosedException();
        }
        else if (currentScenario.prices.stream().noneMatch(price -> (price.id == proposal.priceModelId) && (price.isActivated))) {
            throw new IndexOutOfBoundsException(proposal.priceModelId);
        } else {
            OperationEntity parkingOperation = new OperationEntity();
            parkingOperation.plate = proposal.getPlate();
            parkingOperation.entry = OffsetDateTime.now();
            parkingOperation.hasPaid = false;
            parkingOperation.priceModel = em.find(PriceModelEntity.class, proposal.priceModelId);
            em.persist(parkingOperation);
        }
    }

    private long calculatePeriodInMinutes(int value, FrequencyEnum type)
    {
        return ((long) value) * ((long) type.getValueInMinutes());
    }

    private BigDecimal getTotal(String plate, OffsetDateTime leaveOffsetDateTime)
    {
        OperationEntity plateRecord = em.createNamedQuery("OperationEntities.filterParkedByPlate", OperationEntity.class)
        .setParameter("plate", plate)
        .getResultList().get(0);
        BigDecimal differenceBetweenTimes = new BigDecimal(ChronoUnit.MINUTES.between(plateRecord.entry, leaveOffsetDateTime));
        BigDecimal periodInMinutes = new BigDecimal(calculatePeriodInMinutes(plateRecord.priceModel.frequencyValue, plateRecord.priceModel.frequencyType));
        return plateRecord.priceModel.value.multiply(differenceBetweenTimes.divide(periodInMinutes, 0, RoundingMode.DOWN));
    }
    /**
     * @param plate String
     * @return total to be paid
     * @throws NonUniqueResultException if more than one vehicle parked at that moment has the same plate, what is not expected.
     * @throws NoResultException if no vehicle parked at the moment with tha plate was found.
     */
    @Transactional
    public BigDecimal checkout(String plate) throws NonUniqueResultException, NoResultException 
    {
        OperationEntity operationEntity = 
            em.createNamedQuery("OperationEntities.getParkedByPlate", OperationEntity.class)
            .setParameter("plate", plate)
            .getSingleResult();
        int tolerance = operationEntity.priceModel.scenario.tolerance;
        BigDecimal total;
        operationEntity.leave = OffsetDateTime.now();
        if (ChronoUnit.MINUTES.between(operationEntity.entry, operationEntity.leave) < ((long) tolerance)) {
            total = new BigDecimal(0);
        } else {
            total = getTotal(plate, operationEntity.leave);
        }
        operationEntity.total = total;
        return total;
    }

    @Transactional
    public void confirmCheckout(String plate) throws NonUniqueResultException, NoResultException, TooOldCheckoutException, HasAlreadyPaidException, NoCheckoutException
    {
        OperationEntity operationEntity = 
            em.createNamedQuery("OperationEntities.getByPlate", OperationEntity.class)
            .setParameter("plate", plate)
            .getSingleResult();
        if (operationEntity.leave == null || operationEntity.total == null) {
            throw new NoCheckoutException();
        } else if (operationEntity.hasPaid) {
            throw new HasAlreadyPaidException();
        } else if (operationEntity.leave.isBefore(OffsetDateTime.now().minusMinutes(5))) {
            throw new TooOldCheckoutException();
        } else {
            operationEntity.hasPaid = true;
        }
    }

    @Transactional
    public List<ParkingRecord> getParkingHistory()
    {
        List<OperationEntity> operations = em.createQuery("SELECT operation FROM OperationEntity operation", OperationEntity.class)
                .getResultList();
        List<ParkingRecord> result = new ArrayList<ParkingRecord>(operations.size());
        for (int operationIndex = 0; operationIndex < operations.size(); operationIndex++) {
            OperationEntity operation = operations.get(operationIndex);
            ParkingRecord newParkingRecord = new ParkingRecord(operation.id, operation.plate, operation.entry);
            if (operation.hasPaid) {
                newParkingRecord.leave = operation.leave;
                newParkingRecord.total = operation.total;
            } else {
                newParkingRecord.leave = null;
                newParkingRecord.total = null;
            }
            result.add(newParkingRecord);
        }
        return result;
    }
}
