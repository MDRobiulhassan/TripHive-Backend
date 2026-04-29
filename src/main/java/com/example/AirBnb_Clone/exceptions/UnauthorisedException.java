package com.example.AirBnb_Clone.exceptions;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(String message) {
        super(message);
    }
}
