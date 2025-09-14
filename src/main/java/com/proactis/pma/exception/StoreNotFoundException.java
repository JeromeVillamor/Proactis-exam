package com.proactis.pma.exception;

public class StoreNotFoundException extends RuntimeException{

    private String message = "Store not found";

    public String getMessage() {
        return message;
    }
}
