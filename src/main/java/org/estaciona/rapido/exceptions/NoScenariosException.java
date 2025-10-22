package org.estaciona.rapido.exceptions;

public class NoScenariosException extends Exception{
    public NoScenariosException(){
        super("There is no scenarios in the current date and time. Please, check the state and position of default scenario in the database.");
    }
}
