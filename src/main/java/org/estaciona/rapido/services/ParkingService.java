package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.estaciona.rapido.dto.ParkingRecord;
import org.estaciona.rapido.dto.ParkingRegisterProposal;
import org.estaciona.rapido.dto.PriceOption;
import org.estaciona.rapido.dto.Scenario;
import org.estaciona.rapido.dto.ScenarioBrief;
import org.estaciona.rapido.exceptions.ClosedException;
import org.estaciona.rapido.exceptions.HasAlreadyPaid;
import org.estaciona.rapido.exceptions.NoCheckoutException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.exceptions.TooOldCheckout;
import org.estaciona.rapido.persistence.BusinessHourEntity;
import org.estaciona.rapido.persistence.FrequencyEnum;
import org.estaciona.rapido.persistence.OperationEntity;
import org.estaciona.rapido.persistence.PriceModelEntity;
import org.estaciona.rapido.persistence.ScenarioEntity;

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
        Scenario result = new Scenario();
        result.open = isAvaliable(origin);
        result.id = origin.id;
        result.name = origin.name;
        result.capacity = origin.capacity;
        result.prices = new ArrayList<PriceOption>();
        for (PriceModelEntity priceModel : origin.prices) {
            if (priceModel.isActivated) {
                PriceOption iPriceOption = new PriceOption();
                iPriceOption.id = priceModel.id;
                iPriceOption.name = priceModel.name;
                iPriceOption.value = priceModel.value;
                iPriceOption.frequencyValue = priceModel.frequencyValue;
                iPriceOption.frequencyType = priceModel.frequencyType.toString();
                result.prices.add(iPriceOption);
            }
        }
        return result;

    }

    @Transactional
    public void register(ParkingRegisterProposal proposal) throws IndexOutOfBoundsException, NoScenariosException, ClosedException
    {
        ScenarioEntity currentScenario = getCurrentScenarioEntity();
        if (!isAvaliable(currentScenario)) {
            throw new ClosedException();
        }
        else if (!currentScenario.prices.stream().anyMatch(price -> (price.id == proposal.price_model_id) && (price.isActivated))) {
            throw new IndexOutOfBoundsException(proposal.price_model_id);
        } else {
            OperationEntity parking_operation = new OperationEntity();
            parking_operation.plate = proposal.getPlate();
            parking_operation.entry = OffsetDateTime.now();
            parking_operation.hasPaid = false;
            parking_operation.price_model = em.find(PriceModelEntity.class, proposal.price_model_id);
            em.persist(parking_operation);
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
        BigDecimal periodInMinutes = new BigDecimal(calculatePeriodInMinutes(plateRecord.price_model.frequencyValue, plateRecord.price_model.frequencyType));
        return plateRecord.price_model.value.multiply(differenceBetweenTimes.divide(periodInMinutes, 0, RoundingMode.DOWN));
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
        OffsetDateTime leaveMoment = OffsetDateTime.now();
        BigDecimal total = getTotal(plate, leaveMoment);
        operationEntity.leave = leaveMoment;
        operationEntity.total = total;
        return total;
    }

    @Transactional
    public void confirmCheckout(String plate) throws NonUniqueResultException, NoResultException, TooOldCheckout, HasAlreadyPaid, NoCheckoutException
    {
        OperationEntity operationEntity = 
            em.createNamedQuery("OperationEntities.getByPlate", OperationEntity.class)
            .setParameter("plate", plate)
            .getSingleResult();
        if (operationEntity.leave == null || operationEntity.total == null) {
            throw new NoCheckoutException();
        } else if (operationEntity.hasPaid == true) {
            throw new HasAlreadyPaid();
        } else if (operationEntity.leave.isBefore(OffsetDateTime.now().minusMinutes(5))) {
            throw new TooOldCheckout();
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
