package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.estaciona.rapido.dto.ParkingRecord;
import org.estaciona.rapido.dto.ParkingRegisterProposal;
import org.estaciona.rapido.dto.PriceOption;
import org.estaciona.rapido.dto.Scenario;
import org.estaciona.rapido.dto.ScenarioBrief;
import org.estaciona.rapido.exceptions.ClosedException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.persistence.BusinessHourEntity;
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
            throw new NoScenariosException();
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
            parking_operation.price_model = em.find(PriceModelEntity.class, proposal.price_model_id);
            em.persist(parking_operation);
        }
    }

    public List<ParkingRecord> getParkingHistory()
    {
        return em.createQuery("SELECT new org.estaciona.rapido.dto.ParkingRecord(o.id, o.plate, o.entry, o.leave, o.total) FROM OperationEntity o", ParkingRecord.class)
                .getResultList();
    }
}
