package org.estacionarapido.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.estacionarapido.dto.BusinessHour;
import org.estacionarapido.dto.BusinessHourProposal;
import org.estacionarapido.dto.PriceModel;
import org.estacionarapido.dto.PriceModelProposal;
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
import jakarta.ws.rs.core.Response;

@Path("configuration/rules/default")
@ApplicationScoped
@RolesAllowed({"admin"})
public class RulesResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    ScenarioService scenarioService;

    @Inject
    BusinessHourService businessHourService;

    @Inject
    PriceModelService priceModelService;

    @GET
    @Path("capacity")
    public long getDefaultScenarioCapacity()
    {
        return scenarioService.getDefaultScenarioCapacity();
    }

    @GET
    @Path("business-hours")
    public List<BusinessHour> getBusinessHoursAtDefaultScenario()
    {
        return businessHourService.getBusinessHoursAtDefaultScenario();
    }

    @POST
    @Path("business-hours")
    public Response createBusinessHourAtDefaultScenario(BusinessHourProposal businessHourProposal)
    {
        try {
            businessHourService.createBusinessHourAtDefaultScenario(businessHourProposal);
            return Response.ok().build();
        } catch (NoScenariosException noScenariosException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noScenariosException.getMessage());
        }
    }

    @PATCH
    @Path("business-hours")
    public Response setBusinessHourAtDefaultScenario(@RestQuery long id, BusinessHourProposal businessHour)
    {
        try {
            businessHourService.setBusinessHourAtDefaultScenario(id, businessHour);
            return Response.ok().build();
        } catch (NoBusinessHoursException noBusinessHoursException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noBusinessHoursException.getMessage());
        }
    }

    @DELETE
    @Path("business-hours")
    public void deleteBusinessHourAtDefaultScenario(@RestQuery long id)
    {
        try {
            businessHourService.deleteBusinessHourAtDefaultScenario(id);
        } catch (NoBusinessHoursException businessHoursException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, businessHoursException.getMessage());
        }

    }

    @GET
    @Path("price-model")
    public List<PriceModel> getPriceModelsAtDefaultScenario()
    {
        return priceModelService.getDefaultScenarioPriceModels();
    }

    @POST
    @Path("price-model")
    public void createPriceModelAtDefaultScenario(PriceModelProposal priceModelProposal)
    {
        try {
            priceModelService.createPriceModelAtDefaultScenario(priceModelProposal);
        } catch (NoScenariosException noScenariosException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noScenariosException.getMessage());
        }
    }
    
    @PATCH
    @Path("price-model")
    public void setPriceModelAtDefaultScenario(@RestQuery long id, PriceModelProposal priceModelProposal)
    {
        try {
            priceModelService.setPriceModelAtDefaultScenario(id, priceModelProposal);
        } catch (NoPriceModelException noPriceModelException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noPriceModelException.getMessage());
        }
    }

    @DELETE
    @Path("price-model")
    public void deletePriceModelAtDefaultScenario(@RestQuery long id)
    {
        try {
            priceModelService.deletePriceModelAtDefaultScenario(id);
        } catch (NoPriceModelException noPriceModelException) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, noPriceModelException.getMessage());
        }
    }
    

    
}
