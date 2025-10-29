package org.estacionarapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import org.estacionarapido.dto.ScenarioBrief;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.persistence.BusinessHourEntity;
import org.estacionarapido.persistence.PriceModelEntity;
import org.estacionarapido.persistence.ScenarioEntity;

@ApplicationScoped
public class ScenarioService {
    
    @Inject
    EntityManager em;

    @Transactional
    public void newScenario(String name) {
        ScenarioEntity newScenarioEntity = new ScenarioEntity();
        newScenarioEntity.name = name;
        // TODO: add exception for not having default.
        ScenarioEntity defaultScenarioEntity = em.getReference(ScenarioEntity.class, 1);
        newScenarioEntity.capacity = defaultScenarioEntity.capacity;
        newScenarioEntity.start = OffsetDateTime.now().plusDays(1);
        newScenarioEntity.end = OffsetDateTime.now().plusDays(2);
        em.persist(newScenarioEntity);
        for (PriceModelEntity price : defaultScenarioEntity.prices) {
            PriceModelEntity newPrice = new PriceModelEntity();
            newPrice.name = price.name;
            newPrice.isActivated = price.isActivated;
            newPrice.value = price.value;
            newPrice.frequencyValue = price.frequencyValue;
            newPrice.frequencyType = price.frequencyType;
            newPrice.scenario = newScenarioEntity;
            em.persist(newPrice);
        }
        for (BusinessHourEntity businessHour : defaultScenarioEntity.businessHours) {
            BusinessHourEntity newBusinessHour = new BusinessHourEntity();
            newBusinessHour.isActivated = businessHour.isActivated;
            newBusinessHour.startWeekDay = businessHour.startWeekDay;
            newBusinessHour.endWeekDay = businessHour.endWeekDay;
            newBusinessHour.startTime = businessHour.startTime;
            newBusinessHour.endTime = businessHour.endTime;
            newBusinessHour.scenario = newScenarioEntity;
            em.persist(newBusinessHour);
        }
        
    }

    @Transactional
    public List<ScenarioBrief> getExceptionalScenarios() {
        List<ScenarioBrief> allScenarioBriefList = em.createQuery("SELECT new org.estacionarapido.dto.ScenarioBrief(s.id, s.name) FROM ScenarioEntity s", ScenarioBrief.class)
                .getResultList();
        return allScenarioBriefList.subList(1, allScenarioBriefList.size()); // scenario with id 1 is the default one and is not an exceptional scenario.
    }

    @Transactional
    public void renameExceptionalScenario(long id, String newName) throws NoScenariosException
    {
        if (id < 2 || newName.length() > 50) {
            throw new IllegalArgumentException();
        }
        ScenarioEntity scenarioEntityToBeRenamed = em.find(ScenarioEntity.class, id);
        if (scenarioEntityToBeRenamed == null) {
            throw new NoScenariosException("No scenarios were found to be renamed.");
        }
        scenarioEntityToBeRenamed.name = newName;
    }

    @Transactional
    public boolean deleteExceptionalScenario(long id)
    {
        if (id <= 1) {
            return false;
        }
        ScenarioEntity toDeleteScenarioEntity = em.getReference(ScenarioEntity.class, id);
        if (toDeleteScenarioEntity == null) {
            return false;
        }
        em.remove(toDeleteScenarioEntity);
        return true;
    }

    @Transactional
    public long getCapacity(int scenarioId)
    {
        return em.find(ScenarioEntity.class, scenarioId).capacity;
    }

    @Transactional
    public long getDefaultScenarioCapacity()
    {
        return getCapacity(1);
    }
}
