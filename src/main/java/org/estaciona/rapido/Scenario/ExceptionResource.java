package org.estaciona.rapido.Scenario;

import java.util.List;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Path("/exception")
@ApplicationScoped
public class ExceptionResource {
    @Inject
    ScenarioService service;

    @POST
    @Transactional
    public void createExceptionalScenario(@RestQuery String name)
    {
        service.newScenario(name);
    }

    @GET
    @Transactional
    public List<ScenarioBriefDTO> listExceptionalScenarios()
    {
        return service.listExceptionalScenarios();
    }

    // @GET
    // @Path("{id}")
    // @Transactional
    // public ScenarioEntity 

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteExceptionalScenario(long id)
    {
        if (service.deleteExceptionalScenario(id))
        {
            return Response.status(204).build();
        }
        else
        {
            throw new WebApplicationException("Such an Exceptional Scenario does not exist.");
        }
    }
}
