package org.estacionarapido.resources.customization.scenario;

import java.util.List;

import org.estacionarapido.dto.ScenarioBrief;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.resources.EstacionaRapidoExceptionResponse;
import org.estacionarapido.services.ScenarioService;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("/configuration/rules/exceptions")
@ApplicationScoped
@RolesAllowed({"admin"})
public class ExceptionalScenariosResource {
    //@Inject
    //JsonWebToken jwt;

    @Inject
    ScenarioService scenarioService;

    @POST
    public void createExceptionalScenario(@RestQuery String name)
    {
        scenarioService.newScenario(name);
    }

    @GET
    public List<ScenarioBrief> getExceptionalScenarios()
    {
        return scenarioService.getExceptionalScenarios();
    }

    @PATCH
    @Path("{id}")
    public void renameExceptionalScenario(long id, @RestQuery String name)
    {
        try {
            scenarioService.renameExceptionalScenario(id, name);
        } catch (NoScenariosException noScenariosException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noScenariosException.getMessage());
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteExceptionalScenario(long id)
    {
        if (scenarioService.deleteExceptionalScenario(id))
        {
            return Response.status(204).build();
        }
        else
        {
            throw new WebApplicationException("Such an Exceptional Scenario does not exist.");
        }
    }
}
