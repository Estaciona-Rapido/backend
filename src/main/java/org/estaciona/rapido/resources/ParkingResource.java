package org.estaciona.rapido.resources;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.estaciona.rapido.dto.ExceptionResponse;
import org.estaciona.rapido.dto.ParkingRecord;
import org.estaciona.rapido.dto.ParkingRegisterProposal;
import org.estaciona.rapido.dto.Scenario;
import org.estaciona.rapido.exceptions.ClosedException;
import org.estaciona.rapido.exceptions.HasAlreadyPaid;
import org.estaciona.rapido.exceptions.NoCheckoutException;
import org.estaciona.rapido.exceptions.NoScenariosException;
import org.estaciona.rapido.exceptions.TooOldCheckout;
import org.estaciona.rapido.services.ParkingService;

import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.security.ForbiddenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Path("parking")
@ApplicationScoped
public class ParkingResource {
    @Inject
    ParkingService service;

    @GET
    @Path("current-scenario")
    public Scenario getCurrentScenario() {
        try {
            return service.getCurrentScenario();
        } catch (NoScenariosException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @POST
    @Path("register")
    @Transactional
    public Response registerVehicle(@Valid ParkingRegisterProposal proposal)
    {
        try {
            service.register(proposal);
        } catch (ClosedException ce) {
            throw new ForbiddenException(ce.getMessage());
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Price model of id "+ proposal.price_model_id + " does not exist.");
        } catch (NoScenariosException nsce) {
            throw new InternalServerErrorException(nsce.getMessage());
        }
        return Response.status(Response.Status.CREATED).build();
        
    }

    @POST
    @Path("checkout")
    public BigDecimal getTotalPaymentValue(@RestQuery String plate)
    {
        try {
            return service.checkout(plate);
        } catch (NonUniqueResultException | NoResultException interalError) {
            throw new InternalServerErrorException(interalError);
        }
    }

    @POST
    @Path("confirmCheckout")
    public Response payTotalPaymentValue(@RestQuery String plate)
    {
        try {
            service.confirmCheckout(plate);
            return Response.status(Response.Status.OK).build();
        } catch (NoCheckoutException | NoResultException exception1) {
            throw new NotFoundException(exception1);
        } catch (HasAlreadyPaid | TooOldCheckout exception2) {
            throw new WebApplicationException(Response
                .status(Response.Status.CONFLICT)
                .entity(new ExceptionResponse(exception2.getMessage(), OffsetDateTime.now()))
                .build());
        } catch (NonUniqueResultException exception3) {
            throw new WebApplicationException(Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionResponse(exception3.getMessage(), OffsetDateTime.now()))
                .build());
        }
    }

    @GET
    @Path("history")
    public List<ParkingRecord> getParkingHistory()
    {
        return service.getParkingHistory();
    }
}
