package org.estacionarapido.resources.parking;

import org.estacionarapido.services.ParkingService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("parking")
@ApplicationScoped
public class OccupancyResource {
    @Inject
    ParkingService service;
    
    @GET
    @Path("occupancy")
    public long getOccupancy()
    {
        return service.getOccupancy();
    }
}
