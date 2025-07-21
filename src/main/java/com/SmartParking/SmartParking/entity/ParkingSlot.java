package com.SmartParking.SmartParking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private String slotNumber;

    @Column(nullable = false)
    private Double baseFare ;



    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private boolean available;

    private boolean booked;

    private int fareAmount;

    private LocalDateTime bookedAt;

    @ManyToOne
    @JoinColumn(name = "booked_by_id")
    private User bookedBy; // <


}
