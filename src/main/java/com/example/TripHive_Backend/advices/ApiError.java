package com.example.TripHive_Backend.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {
    private String message;
    private HttpStatus status;
    private List<String> subErrors;
}
