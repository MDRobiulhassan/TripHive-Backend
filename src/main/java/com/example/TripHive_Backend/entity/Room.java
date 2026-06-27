package com.example.TripHive_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id",nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal basePrice;

    @CreationTimestamp
    @Column(updatable=false)
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @Column(columnDefinition = "TEXT[]")
    private List<String> amenities;

    @Column(columnDefinition = "TEXT[]")
    private List<String> photos;

    @Column(nullable = false)
    private Integer totalCount;
    private Integer capacity;

}
