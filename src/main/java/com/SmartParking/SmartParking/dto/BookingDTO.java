package com.SmartParking.SmartParking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

// this is for when admin want to see all slot activity
public class BookingDTO {
    private Long bookingId;
    private String userEmail;
    private String slotNumber;
    private String location;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Double fareAmount;
}

