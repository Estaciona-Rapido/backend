package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import org.estaciona.rapido.dto.ScenarioBrief;
import org.estaciona.rapido.persistence.BusinessHourEntity;
import org.estaciona.rapido.persistence.PriceModelEntity;
import org.estaciona.rapido.persistence.ScenarioEntity;

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
    public List<ScenarioBrief> listExceptionalScenarios() {
        List<ScenarioBrief> ls = em.createQuery("SELECT new org.estaciona.rapido.dto.ScenarioBrief(s.id, s.name) FROM ScenarioEntity s", ScenarioBrief.class)
                .getResultList();
        return ls.subList(1, ls.size());
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
