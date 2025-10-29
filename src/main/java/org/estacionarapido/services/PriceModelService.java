package org.estacionarapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.estacionarapido.dto.PriceModel;
import org.estacionarapido.dto.PriceModelProposal;
import org.estacionarapido.exceptions.NoPriceModelException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.persistence.FrequencyEnum;
import org.estacionarapido.persistence.PriceModelEntity;
import org.estacionarapido.persistence.ScenarioEntity;

@ApplicationScoped
public class PriceModelService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public List<PriceModel> getPriceModels(long scenarioId)
    {
        List<PriceModelEntity> priceModelEntities = entityManager.find(ScenarioEntity.class, scenarioId).prices;
        List<PriceModel> result = new ArrayList<PriceModel>(priceModelEntities.size());
        for (PriceModelEntity priceModelEntity : priceModelEntities) {
            result.add(new PriceModel(priceModelEntity.id, priceModelEntity.name, priceModelEntity.isActivated, priceModelEntity.value, priceModelEntity.frequencyValue, priceModelEntity.frequencyType.toString()));
        }
        return result;
    }

    @Transactional
    public List<PriceModel> getDefaultScenarioPriceModels()
    {
        return getPriceModels(1);
    }

    @Transactional
    public void createPriceModel(PriceModelProposal priceModelProposal, long scenarioId) throws NoScenariosException
    {
        PriceModelEntity newPriceModelEntity = new PriceModelEntity();
        newPriceModelEntity.isActivated = priceModelProposal.isActivated();
        newPriceModelEntity.name = priceModelProposal.name();
        newPriceModelEntity.value = priceModelProposal.value();
        newPriceModelEntity.frequencyValue = priceModelProposal.frequencyValue();
        newPriceModelEntity.frequencyType = FrequencyEnum.valueOf(priceModelProposal.frequencyType());
        newPriceModelEntity.scenario = entityManager.find(ScenarioEntity.class, scenarioId);
        if (newPriceModelEntity.scenario == null) {
            throw new NoScenariosException("There are no scenarios with this id.");
        } else {
            entityManager.persist(newPriceModelEntity);
        }
    }

    @Transactional
    public void createPriceModelAtDefaultScenario(PriceModelProposal priceModelProposal) throws NoScenariosException
    {
        createPriceModel(priceModelProposal, 1);
    }

    @Transactional
    public void setPriceModel(long id, PriceModelProposal priceModelProposal, long scenarioId) throws NoPriceModelException
    {
        ScenarioEntity originScenarioEntity = entityManager.find(ScenarioEntity.class, scenarioId);
        Optional<PriceModelEntity> optionalPriceModelEntityToBeEdited = originScenarioEntity.prices.stream().filter(iteratorPriceModelEntity -> iteratorPriceModelEntity.id == id).findFirst();
        if (optionalPriceModelEntityToBeEdited.isPresent() == false) {
            throw new NoPriceModelException(String.format("There are no price models to be edited with id %d within scenario with id %d", id, scenarioId));
        } else {
            optionalPriceModelEntityToBeEdited.get().isActivated = priceModelProposal.isActivated();
            optionalPriceModelEntityToBeEdited.get().name = priceModelProposal.name();
            optionalPriceModelEntityToBeEdited.get().value = priceModelProposal.value();
            optionalPriceModelEntityToBeEdited.get().frequencyValue = priceModelProposal.frequencyValue();
            optionalPriceModelEntityToBeEdited.get().frequencyType = FrequencyEnum.valueOf(priceModelProposal.frequencyType());
        }
    }

    @Transactional
    public void setPriceModelAtDefaultScenario(long id, PriceModelProposal priceModelProposal) throws NoPriceModelException
    {
        setPriceModel(id, priceModelProposal, 1);
    }
    @Transactional
    public void deletePriceModel(long id, long scenarioId) throws NoPriceModelException
    {
        ScenarioEntity originScenarioEntity = entityManager.find(ScenarioEntity.class, scenarioId);
        Optional<PriceModelEntity> optionalPriceModelEntityToBeDeleted = originScenarioEntity.prices.stream().filter(itereatorPriceModelEntity -> itereatorPriceModelEntity.id == id).findFirst();
        if (optionalPriceModelEntityToBeDeleted.isPresent() == false) {
            throw new NoPriceModelException(String.format("There are no price models to be deleted with id %d within scenario with id %d", id, scenarioId));
        } else {
            entityManager.remove(optionalPriceModelEntityToBeDeleted.get());
        }
    }

    @Transactional
    public void deletePriceModelAtDefaultScenario(long id) throws NoPriceModelException
    {
        deletePriceModel(id, 1);
    }
}
