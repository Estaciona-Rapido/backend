package org.estacionarapido.resources.customization.businesshour;

import java.util.List;

import org.estacionarapido.dto.BusinessHour;
import org.estacionarapido.dto.BusinessHourProposal;
import org.estacionarapido.exceptions.NoBusinessHoursException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.resources.EstacionaRapidoExceptionResponse;
import org.estacionarapido.services.BusinessHourService;
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
public class ExceptionalBusinessHourResource {
    @Inject
    BusinessHourService businessHourService;

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

}
