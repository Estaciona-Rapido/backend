package org.estacionarapido.resources.parking;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.estacionarapido.dto.ExceptionResponse;
import org.estacionarapido.exceptions.HasAlreadyPaidException;
import org.estacionarapido.exceptions.NoCheckoutException;
import org.estacionarapido.exceptions.TooOldCheckoutException;
import org.estacionarapido.services.ParkingService;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("parking")
@ApplicationScoped
public class CheckoutResource {
    @Inject
    ParkingService service;

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
        } catch (HasAlreadyPaidException | TooOldCheckoutException exception2) {
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
}
