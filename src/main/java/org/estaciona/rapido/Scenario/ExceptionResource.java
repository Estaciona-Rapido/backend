package org.estaciona.rapido.Scenario;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Path("/exception")
@ApplicationScoped
public class ExceptionResource {
    @Inject
    ScenarioService service;

    @POST
    @Transactional
    public void createExceptionalScenario(Scenario scenario)
    {
        service.newScenario(scenario);
    }

    @GET
    @Transactional
    public List<ScenarioEntity> listExceptionalScenarios()
    {
        return service.listExceptionalScenarios();
    }
}
