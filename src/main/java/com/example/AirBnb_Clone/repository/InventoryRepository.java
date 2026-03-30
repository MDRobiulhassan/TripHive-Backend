package com.example.AirBnb_Clone.repository;

import com.example.AirBnb_Clone.entity.Hotel;
import com.example.AirBnb_Clone.entity.Inventory;
import com.example.AirBnb_Clone.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByRoom(Room room);

    @Query("""
                SELECT i.hotel
                FROM Inventory i
                WHERE i.hotel.city = :city
                  AND i.date >= :checkInDate
                  AND i.date <= :checkOutDate
                  AND i.closed = false
                  AND (i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
                GROUP BY i.hotel
                HAVING COUNT(DISTINCT i.date) = :dateCount
            """)
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("numberOfRooms") Integer numberOfRooms,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );


    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
              AND i.date BETWEEN :checkInDate AND :checkOutDate
              AND i.closed = false
              AND (i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventories(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("numberOfRooms") Integer numberOfRooms
    );

    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);
}
