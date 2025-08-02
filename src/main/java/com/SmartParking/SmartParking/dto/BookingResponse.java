package com.SmartParking.SmartParking.dto;

import com.SmartParking.SmartParking.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// this is for user wants to check my bookings aur post new booking then get a response
public class BookingResponse {
    private String message;
    private Long slotId;
    private String slotNumber;
    private String location;
    private VehicleType vehicleType;
    private LocalDateTime checkInTime;
    private Integer reservedHours;
    private Double fareAmount;
}
