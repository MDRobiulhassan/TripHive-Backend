package com.example.AirBnb_Clone.repository;

import com.example.AirBnb_Clone.entity.Hotel;
import com.example.AirBnb_Clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findByOwner(User user);
}
