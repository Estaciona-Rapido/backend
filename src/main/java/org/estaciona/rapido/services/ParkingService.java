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

import org.estaciona.rapido.dpo.PriceOption;
import org.estaciona.rapido.dpo.Scenario;
import org.estaciona.rapido.dpo.Parking.ParkingRegisterProposal;
import org.estaciona.rapido.persistence.OperationEntity;
import org.estaciona.rapido.persistence.PriceModelEntity;
import org.estaciona.rapido.persistence.ScenarioEntity;

@ApplicationScoped
public class ParkingService {
    @Inject
    EntityManager em;

    private ScenarioEntity getCurrentScenarioEntity() throws Exception
    {
        OffsetDateTime rightNow = OffsetDateTime.now();
        List<ScenarioEntity> currentScenarios = em.createNamedQuery("ScenarioEntities.getCurrentScenarios", ScenarioEntity.class).setParameter("tmsZ", rightNow).getResultList();
        if (currentScenarios.size() > 1) {
            return currentScenarios.get(1); // TODO: maybe the tiebreaker criteria will change later.
        } else if (currentScenarios.size() == 1) {
            return currentScenarios.get(0);
        } else {
            throw new Exception("There is no scenarios in the current date and time. Please, check the state and position of default scenario in the database.");
        }
    }

    @Transactional
    public Scenario getCurrentScenario() throws Exception
    {
        ScenarioEntity origin = getCurrentScenarioEntity();
        Scenario result = new Scenario();
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
                result.prices.add(iPriceOption);
            }
        }
        return result;

    }

    @Transactional
    public void register(ParkingRegisterProposal proposal) throws IndexOutOfBoundsException
    {
        OperationEntity parking_operation = new OperationEntity();
        parking_operation.plate = proposal.getPlate();
        parking_operation.entry = OffsetDateTime.now();
        parking_operation.price_model = em.find(PriceModelEntity.class, proposal.price_model_id);
        if (parking_operation.price_model == null) {
            throw new IndexOutOfBoundsException(proposal.price_model_id);
        } else {
            em.persist(parking_operation);
        }
    }
}
