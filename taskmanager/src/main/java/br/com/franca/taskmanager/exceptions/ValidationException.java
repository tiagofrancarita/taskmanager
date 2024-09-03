package br.com.franca.taskmanager.exceptions;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }

}
