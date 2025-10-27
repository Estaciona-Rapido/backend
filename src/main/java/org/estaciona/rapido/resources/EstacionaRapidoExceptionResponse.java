package org.estaciona.rapido.resources;

import java.time.OffsetDateTime;

import org.estaciona.rapido.dto.ExceptionResponse;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class EstacionaRapidoExceptionResponse {
    public static WebApplicationException create(Response.Status httpStatus, String message)
    {
        return new WebApplicationException(Response.status(httpStatus)
            .entity(new ExceptionResponse(message, OffsetDateTime.now()))
            .build());
    }
}
