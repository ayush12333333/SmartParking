package com.SmartParking.SmartParking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ParkingSlot slot;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Integer reservedHours;

    private Double fareAmount;

    private boolean active; // true = current booking, false = past/cancelled
}

