package com.example.AirBnb_Clone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String city;

    @Column(columnDefinition = "TEXT[]")
    private List<String> photos;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @Column(columnDefinition = "TEXT[]")
    private List<String> amenities;

    private Boolean active;

    @Embedded
    private HotelContactInfo contactInfo;

    @ManyToOne(optional = false,fetch =  FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room> rooms;
}
