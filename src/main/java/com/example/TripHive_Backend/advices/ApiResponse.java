package com.example.TripHive_Backend.advices;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T>{

    private LocalDateTime timestamps;
    private T data;
    private ApiError error;

    public ApiResponse(){
        this.timestamps = LocalDateTime.now();
    }

    public ApiResponse(T Data){
        this();
        this.data = Data;
    }

    public ApiResponse(ApiError error){
        this();
        this.error = error;
    }
}
