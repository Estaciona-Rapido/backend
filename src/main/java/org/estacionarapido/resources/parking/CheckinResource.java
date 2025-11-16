package org.estacionarapido.resources.parking;

import org.estacionarapido.dto.ParkingRegisterProposal;
import org.estacionarapido.exceptions.ClosedException;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.resources.EstacionaRapidoExceptionResponse;
import org.estacionarapido.services.ParkingService;

import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("parking")
@ApplicationScoped
public class CheckinResource {
    @Inject
    ParkingService service;

    @POST
    @Path("register")
    @Transactional
    public Response registerVehicle(@Valid ParkingRegisterProposal proposal)
    {
        try {
            service.register(proposal);
        } catch (ClosedException ce) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.FORBIDDEN, ce.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.NOT_FOUND, "Price model of id "+ proposal.priceModelId + " does not exist.");
        } catch (NoScenariosException nsce) {
            throw EstacionaRapidoExceptionResponse.create(Response.Status.INTERNAL_SERVER_ERROR, nsce.getMessage());
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
