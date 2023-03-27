package com.kopaev.SecuritySample.Exceptions;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String message){
        super(message);
    }
}
