package org.estacionarapido.dto;

import java.time.OffsetDateTime;

// public class EstacionaRapidoExceptionResponse {
//     String errorMessage;
//     OffsetDateTime at;
    
//     public EstacionaRapidoExceptionResponse(String errorMessage, OffsetDateTime at){
//         this.errorMessage = errorMessage;
//         this.at = at;
//     }
// }

public record ExceptionResponse(String errorMessage, OffsetDateTime at) {}
