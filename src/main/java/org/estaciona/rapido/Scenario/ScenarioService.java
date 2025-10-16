package org.estaciona.rapido.Scenario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;

import java.util.List;

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
        // TODO: add the same price model and bussiness hour from default.
        em.persist(sc);
        
    }

    @Transactional
    public List<ScenarioBriefDTO> listExceptionalScenarios() {
        List<ScenarioBriefDTO> ls = em.createQuery("SELECT new org.estaciona.rapido.Scenario.ScenarioBriefDTO(s.id, s.name) FROM ScenarioEntity s", ScenarioBriefDTO.class)
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
