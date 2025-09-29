package org.estaciona.rapido;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vehicle")
public class OperatorResource {

    @Path("capacity")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "MUDAR PARA POST DEPOIS";
    }
    
    @Path("in")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "MUDAR PARA POST DEPOIS";
    }
}
