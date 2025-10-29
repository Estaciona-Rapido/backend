package org.estacionarapido.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.estacionarapido.dto.BusinessHour;
import org.estacionarapido.dto.BusinessHourProposal;
import org.estacionarapido.dto.PriceModel;
import org.estacionarapido.dto.PriceModelProposal;
import org.estacionarapido.dto.ScenarioBrief;
import org.estacionarapido.exceptions.NoBusinessHoursException;
import org.estacionarapido.exceptions.NoPriceModelException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.services.BusinessHourService;
import org.estacionarapido.services.PriceModelService;
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
public class ExceptionalRulesResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ScenarioService scenarioService;

    @Inject
    BusinessHourService businessHourService;

    @Inject
    PriceModelService priceModelService;

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

    @GET
    @Path("{id}/business-hour")
    public List<BusinessHour> getExceptionalScenarioBusinessHours(long id)
    {
        return businessHourService.getBusinessHours(id);
    }

    @POST
    @Path("{id}/business-hours")
    public Response createBusinessHour(BusinessHourProposal businessHourProposal, @PathParam("id") long scenarioId)
    {
        try {
            businessHourService.createBusinessHour(businessHourProposal, scenarioId);
            return Response.ok().build();
        } catch (NoScenariosException noScenariosException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noScenariosException.getMessage());
        }
    }

    @PATCH
    @Path("{id}/business-hours")
    public Response setBusinessHour(@RestQuery long id, BusinessHourProposal businessHour, @PathParam("id") long scenarioId)
    {
        try {
            businessHourService.setBusinessHour(id, businessHour, scenarioId);
            return Response.ok().build();
        } catch (NoBusinessHoursException noBusinessHoursException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noBusinessHoursException.getMessage());
        }
    }

    @DELETE
    @Path("{id}/business-hours")
    public void deleteBusinessHour(@RestQuery long id, @PathParam("id") long scenarioId)
    {
        try {
            businessHourService.deleteBusinessHour(id, scenarioId);
        } catch (NoBusinessHoursException businessHoursException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, businessHoursException.getMessage());
        }
    }

    @GET
    @Path("{id}/capacity")
    public long getCapacity(int id)
    {
        return scenarioService.getCapacity(id);
    }
    
    @GET
    @Path("{id}/price-model")
    public List<PriceModel> getExceptionalScenarioPriceModels(@PathParam("id") long scenarioId)
    {
        return priceModelService.getPriceModels(scenarioId);
    }

    @POST
    @Path("{id}/price-model")
    public void createExceptionalScenarioPriceModel(PriceModelProposal priceModelProposal, @PathParam("id") long scenarioId)
    {
        try {
            priceModelService.createPriceModel(priceModelProposal, scenarioId);
        } catch (NoScenariosException noScenariosException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noScenariosException.getMessage());
        }
    }

    @PATCH
    @Path("{id}/price-model")
    public void setExceptionalScenarioPriceModel(@RestQuery long id, PriceModelProposal priceModelProposal, @PathParam("id") long scenarioId)
    {
        try {
            priceModelService.setPriceModel(id, priceModelProposal, scenarioId);
        } catch (NoPriceModelException noPriceModelException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noPriceModelException.getMessage());
        }
    }

    @DELETE
    @Path("{id}/price-model")
    public void deletePriceModel(@RestQuery long id, @PathParam("id") long scenarioId)
    {
        try {
            priceModelService.deletePriceModel(id, scenarioId);
        } catch (NoPriceModelException noPriceModelException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noPriceModelException.getMessage());
        }
    }

}
