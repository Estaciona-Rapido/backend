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
    public void newScenario(Scenario scenario) {
        ScenarioEntity sc = new ScenarioEntity();
        sc.name = scenario.name;
        sc.capacity = scenario.capacity;
        sc.period = scenario.periodString;
        em.persist(sc);
    }

    @Transactional
    public List<ScenarioEntity> listExceptionalScenarios() {
        List<ScenarioEntity> ls = em.createQuery("SELECT s FROM ScenarioEntity s", ScenarioEntity.class)
                 .setMaxResults(10)
                 .getResultList();
        return ls.subList(1, ls.size());
    }
}
