package com.example.TripHive_Backend.util;

import com.example.TripHive_Backend.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AppUtil {
    public static User getCurrentUser() {
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }
}
