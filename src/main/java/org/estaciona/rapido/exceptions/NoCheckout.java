package org.estaciona.rapido.exceptions;

public class NoCheckout extends Exception{
    public NoCheckout(){
        super("There is no checkout for this plate. Please, do your checkout before confirming it.");
    }
}
