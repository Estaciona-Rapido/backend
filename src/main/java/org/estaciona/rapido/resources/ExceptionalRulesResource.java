package org.estaciona.rapido.resources;

import java.util.List;

import org.estaciona.rapido.dto.BusinessHour;
import org.estaciona.rapido.dto.PriceModel;
import org.estaciona.rapido.dto.ScenarioBrief;
import org.estaciona.rapido.services.BusinessHourService;
import org.estaciona.rapido.services.PriceModelService;
import org.estaciona.rapido.services.ScenarioService;
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

@Path("/configuration/rules/exceptions")
@ApplicationScoped
public class ExceptionalRulesResource {
    @Inject
    ScenarioService scenarioService;

    @Inject
    BusinessHourService businessHourService;

    @Inject
    PriceModelService priceModelService;

    @POST
    @Transactional
    public void createExceptionalScenario(@RestQuery String name)
    {
        scenarioService.newScenario(name);
    }

    @GET
    @Transactional
    public List<ScenarioBrief> listExceptionalScenarios()
    {
        return scenarioService.listExceptionalScenarios();
    }

    @DELETE
    @Path("{id}")
    @Transactional
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

    @GET
    @Path("{id}/business-hour")
    public List<BusinessHour> getExceptionalScenarioBusinessHours(long id)
    {
        return businessHourService.getBusinessHours(id);
    }

    @GET
    @Path("{id}/capacity")
    public long getCapacity(int id)
    {
        return scenarioService.getCapacity(id);
    }
    
    @GET
    @Path("{id}/price-model")
    public List<PriceModel> getExceptionalScenarioPriceModels(long id)
    {
        return priceModelService.getPriceModels(id);
    }

}
