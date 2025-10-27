package org.estaciona.rapido.exceptions;

public class NoBusinessHoursException extends Exception{
    public NoBusinessHoursException(String message){
        super(message);
    }
}
