package com.example.TripHive_Backend.exceptions;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(String message) {
        super(message);
    }
}
