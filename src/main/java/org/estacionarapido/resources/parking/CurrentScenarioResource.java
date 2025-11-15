package org.estacionarapido.resources.parking;

import org.estacionarapido.dto.Scenario;
import org.estacionarapido.exceptions.NoScenariosException;
import org.estacionarapido.services.ParkingService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;

@Path("parking")
@ApplicationScoped
public class CurrentScenarioResource {
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
}
