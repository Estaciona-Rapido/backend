package org.estacionarapido.resources.customization.capacity;

import org.estacionarapido.services.ScenarioService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("configuration/rules/default")
@ApplicationScoped
@RolesAllowed({"admin"})
public class DefaultCapacityResource {
    @Inject
    ScenarioService scenarioService;

    @GET
    @Path("capacity")
    public long getDefaultScenarioCapacity()
    {
        return scenarioService.getDefaultScenarioCapacity();
    }
}
