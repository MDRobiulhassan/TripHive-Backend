package com.example.TripHive_Backend.repository;

import com.example.TripHive_Backend.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
