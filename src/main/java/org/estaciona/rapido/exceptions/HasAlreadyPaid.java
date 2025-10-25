package org.estaciona.rapido.exceptions;

public class HasAlreadyPaid extends Exception {
    public HasAlreadyPaid(){
        super("This checkout was already paid.");
    }
}
