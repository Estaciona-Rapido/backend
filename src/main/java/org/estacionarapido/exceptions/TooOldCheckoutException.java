package org.estacionarapido.exceptions;

public class TooOldCheckoutException extends Exception {
    public TooOldCheckoutException(){
        super("Your last checkout is too old. Please make a new checkout attempt.");
    }
}
