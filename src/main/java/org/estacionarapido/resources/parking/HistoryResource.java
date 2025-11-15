package org.estacionarapido.resources.parking;

import java.util.List;

import org.estacionarapido.dto.ParkingRecord;
import org.estacionarapido.services.ParkingService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("parking")
@ApplicationScoped
public class HistoryResource {
    @Inject
    ParkingService service;

    @GET
    @Path("history")
    public List<ParkingRecord> getParkingHistory()
    {
        return service.getParkingHistory();
    }
}
