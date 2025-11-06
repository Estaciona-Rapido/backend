package org.estacionarapido.resources.customization.capacity;

import org.estacionarapido.services.ScenarioService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/configuration/rules/exceptions")
@ApplicationScoped
@RolesAllowed({"admin"})
public class ExceptionalCapacityResource {
    @Inject
    ScenarioService scenarioService;

    @GET
    @Path("{id}/capacity")
    public long getCapacity(int id)
    {
        return scenarioService.getCapacity(id);
    }
}
