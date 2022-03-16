package com.cryptocurrency.demo.exceptions;


public class PriceException extends RuntimeException {
    public PriceException(String message) {
        super(message);
    }
}
