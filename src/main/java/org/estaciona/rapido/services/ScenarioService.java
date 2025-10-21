package org.estaciona.rapido.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import org.estaciona.rapido.dpo.ScenarioBrief;
import org.estaciona.rapido.persistence.ScenarioEntity;

@ApplicationScoped
public class ScenarioService {
    
    @Inject
    EntityManager em;

    @Transactional
    public void newScenario(String name) {
        ScenarioEntity sc = new ScenarioEntity();
        sc.name = name;
        // TODO: add exception for not having default.
        ScenarioEntity defaultScenarioEntity = em.getReference(ScenarioEntity.class, 1);
        sc.capacity = defaultScenarioEntity.capacity;
        // TODO: inserts period of today or tomorrow in the correct format.
        sc.period = defaultScenarioEntity.period;
        // TODO: add the same price model and business hour from default.
        em.persist(sc);
        
    }

    @Transactional
    public ScenarioEntity getCurrentScenario() throws Exception
    {
        OffsetDateTime rightNow = OffsetDateTime.now();
        List<ScenarioEntity> currentScenarios = em.createNamedQuery("ScenarioEntities.getCurrentScenarios", ScenarioEntity.class).setParameter("tmsZ", rightNow).getResultList();
        if (currentScenarios.size() > 1) {
            return currentScenarios.get(1); // TODO: maybe the tiebreaker criteria will change later.
        } else if (currentScenarios.size() == 1) {
            return currentScenarios.get(0);
        } else {
            throw new Exception("There is no scenarios in the current scenario. Please, check the state and position of default scenario in the database.");
        }
    }

    @Transactional
    public List<ScenarioBrief> listExceptionalScenarios() {
        List<ScenarioBrief> ls = em.createQuery("SELECT new org.estaciona.rapido.Scenario.ScenarioBriefDTO(s.id, s.name) FROM ScenarioEntity s", ScenarioBrief.class)
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
}
