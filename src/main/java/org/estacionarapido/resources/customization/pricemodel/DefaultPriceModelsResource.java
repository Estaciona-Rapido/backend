package org.estacionarapido.resources.customization.pricemodel;

import java.util.List;

import org.estacionarapido.dto.PriceModel;
import org.estacionarapido.dto.PriceModelProposal;
import org.estacionarapido.exceptions.NoPriceModelException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.resources.EstacionaRapidoExceptionResponse;
import org.estacionarapido.services.PriceModelService;
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
public class DefaultPriceModelsResource {
    @Inject
    PriceModelService priceModelService;

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
