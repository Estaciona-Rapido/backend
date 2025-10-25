package org.estaciona.rapido.exceptions;

public class TooOldCheckout extends Exception {
    public TooOldCheckout(){
        super("Your last checkout is too old. Please make a new checkout attempt.");
    }
}
