package org.estaciona.rapido.resources;

import java.util.List;

import org.estaciona.rapido.dto.BusinessHour;
import org.estaciona.rapido.dto.BusinessHourProposal;
import org.estaciona.rapido.dto.ExceptionResponse;
import org.estaciona.rapido.dto.PriceModel;
import org.estaciona.rapido.dto.PriceModelProposal;
import org.estaciona.rapido.exceptions.NoBusinessHoursException;
import org.estaciona.rapido.exceptions.NoPriceModelException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.persistence.ScenarioEntity;
import org.estaciona.rapido.services.BusinessHourService;
import org.estaciona.rapido.services.PriceModelService;
import org.estaciona.rapido.services.ScenarioService;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Path("configuration/rules/default")
@ApplicationScoped
public class RulesResource {
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
