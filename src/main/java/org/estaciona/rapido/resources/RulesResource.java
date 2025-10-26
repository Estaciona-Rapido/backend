package org.estaciona.rapido.resources;

import java.util.List;

import org.estaciona.rapido.dto.BusinessHour;
import org.estaciona.rapido.dto.PriceModel;
import org.estaciona.rapido.persistence.ScenarioEntity;
import org.estaciona.rapido.services.BusinessHourService;
import org.estaciona.rapido.services.PriceModelService;
import org.estaciona.rapido.services.ScenarioService;

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
    public long getCapacity()
    {
        return scenarioService.getDefaultScenarioCapacity();
    }

    @GET
    @Path("business-hours")
    public List<BusinessHour> getBusinessHoursAtDefaultScenario()
    {
        return businessHourService.getBusinessHoursAtDefaultScenario();
    }

    @GET
    @Path("price-model")
    public List<PriceModel> getDefaultScenarioPriceModels()
    {
        return priceModelService.getDefaultScenarioPriceModels();
    }

    
}
