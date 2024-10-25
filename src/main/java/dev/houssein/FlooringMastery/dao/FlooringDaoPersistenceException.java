package dev.houssein.FlooringMastery.dao;

public class FlooringDaoPersistenceException extends Exception{

    public FlooringDaoPersistenceException(String message) {
        super(message);
    }

    public FlooringDaoPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}