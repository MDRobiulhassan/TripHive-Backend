package com.example.AirBnb_Clone.repository;

import com.example.AirBnb_Clone.dto.request.HotelPriceDTO;
import com.example.AirBnb_Clone.entity.Hotel;
import com.example.AirBnb_Clone.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {
    @Query("""
                SELECT new com.example.AirBnb_Clone.dto.request.HotelPriceDTO(i.hotel, AVG(i.price))
                FROM HotelMinPrice i
                WHERE i.hotel.city = :city
                  AND i.date >= :checkInDate
                  AND i.date <= :checkOutDate
                  AND i.hotel.active = true
                GROUP BY i.hotel
            """)
    Page<HotelPriceDTO> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("numberOfRooms") Integer numberOfRooms,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
