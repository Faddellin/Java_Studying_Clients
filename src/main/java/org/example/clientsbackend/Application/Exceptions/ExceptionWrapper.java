package org.example.clientsbackend.Application.Exceptions;

import lombok.Getter;

import java.util.Dictionary;
import java.util.Hashtable;

@Getter
public class ExceptionWrapper extends Exception {
    private Class<? extends Exception> exceptionClass;
    private Dictionary<String, String> errors = new Hashtable<String, String>();

    public ExceptionWrapper(Exception originalException) {
        this.exceptionClass = originalException.getClass();
    }

    public void addError(String reason, String error){
        errors.put(reason, error);
    }
}
