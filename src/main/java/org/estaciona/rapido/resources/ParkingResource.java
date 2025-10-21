package org.estaciona.rapido.resources;

import org.estaciona.rapido.dpo.Scenario;
import org.estaciona.rapido.dpo.Parking.ParkingRegisterProposal;
import org.estaciona.rapido.services.ParkingService;

import org.jboss.resteasy.reactive.RestQuery;

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
        } catch (Exception e) {
            throw new InternalServerErrorException("There is no scenarios in the current date and time. Please, check the state and position of default scenario in the database.");
        }
    }

    @POST
    @Path("register")
    @Transactional
    public Response registerVehicle(@Valid ParkingRegisterProposal proposal)
    {
        try {
            service.register(proposal);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException("Price model of id "+ proposal.price_model_id + " does not exist.");
        }
        return Response.status(Response.Status.CREATED).build();
        
    }
}
