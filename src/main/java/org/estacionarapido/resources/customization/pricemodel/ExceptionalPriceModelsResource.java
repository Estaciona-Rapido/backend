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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/configuration/rules/exceptions")
@ApplicationScoped
@RolesAllowed({"admin"})
public class ExceptionalPriceModelsResource {
    @Inject
    PriceModelService priceModelService;

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
