package com.example.AirBnb_Clone.util;

import com.example.AirBnb_Clone.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AppUtil {
    public static User getCurrentUser() {
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }
}
