package org.estacionarapido.exceptions;

public class HasAlreadyPaidException extends Exception {
    public HasAlreadyPaidException(){
        super("This checkout was already paid.");
    }
}
