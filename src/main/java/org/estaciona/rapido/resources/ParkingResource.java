package org.estaciona.rapido.resources;

import org.estaciona.rapido.dto.Scenario;
import org.estaciona.rapido.dto.Parking.ParkingRegisterProposal;
import org.estaciona.rapido.exceptions.ClosedException;
import org.estaciona.rapido.exceptions.NoScenariosException;
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
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
}
