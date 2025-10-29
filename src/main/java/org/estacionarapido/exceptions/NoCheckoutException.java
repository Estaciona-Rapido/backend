package org.estacionarapido.exceptions;

public class NoCheckoutException extends Exception{
    public NoCheckoutException(){
        super("There is no checkout for this plate. Please, do your checkout before confirming it.");
    }
}
