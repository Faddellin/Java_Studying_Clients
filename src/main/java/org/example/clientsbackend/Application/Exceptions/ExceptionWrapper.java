package org.example.clientsbackend.Application.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Hashtable;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ExceptionWrapper extends Exception {
    private Class<? extends Exception> exceptionClass;
    private final Hashtable<String, String> errors = new Hashtable<>();

    public ExceptionWrapper(Exception originalException) {
        this.exceptionClass = originalException.getClass();
    }

    public void addError(String reason, String error){
        errors.put(reason, error);
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }
}
