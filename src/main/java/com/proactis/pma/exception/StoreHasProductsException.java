package com.proactis.pma.exception;

public class StoreHasProductsException extends RuntimeException {

    private String message = "Invalid Action. Store has products";

    public String getMessage() {
        return message;
    }

}
