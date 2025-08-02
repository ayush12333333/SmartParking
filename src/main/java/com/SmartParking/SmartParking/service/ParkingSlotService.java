package com.SmartParking.SmartParking.service;

import com.SmartParking.SmartParking.entity.Booking;
import com.SmartParking.SmartParking.entity.ParkingSlot;

import com.SmartParking.SmartParking.entity.VehicleType;
import com.SmartParking.SmartParking.repository.BookingRepository;
import com.SmartParking.SmartParking.repository.ParkingSlotRepository;
import com.SmartParking.SmartParking.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository slotRepo;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepo;

    // Admin: Create parking slot
    public ParkingSlot createSlot(String location, VehicleType vehicleType) {
        ParkingSlot slot = new ParkingSlot();
        slot.setLocation(location);
        slot.setBooked(false);
        slot.setVehicleType(vehicleType);
        long count = slotRepo.countByLocation(location);
        String slotNumber = location.substring(0, 1).toUpperCase() + "-" + String.format("%03d", count + 1);
        slot.setSlotNumber(slotNumber);
        if (slot.getVehicleType() == VehicleType.CAR) {
            slot.setFareAmount(40);
            slot.setBaseFare(40.0);
        } else if (slot.getVehicleType() == VehicleType.BIKE) {
            slot.setFareAmount(20);
            slot.setBaseFare(20.0);
        }
        slot.setAvailable(true);
        return slotRepo.save(slot);
    }

    // User: View available slots
    public List<ParkingSlot> getAvailableSlots(String location, VehicleType vehicleType) {
        boolean isAllLocation = location.equalsIgnoreCase("ALL");
        boolean isAllVehicle = vehicleType.toString().equalsIgnoreCase("ALL");

        if (isAllLocation && isAllVehicle) {
            // All locations, all vehicle types
            return slotRepo.findByAvailableTrue();
        }

        if (isAllLocation) {
            // All locations, specific vehicle type
            return slotRepo.findByVehicleTypeAndAvailableTrue(vehicleType);
        }

        if (isAllVehicle) {
            // Specific location, all vehicle types
            return slotRepo.findByLocationAndAvailableTrue(location);
        }

        // Specific location, specific vehicle type
        return slotRepo.findByLocationAndVehicleTypeAndAvailableTrue(location, vehicleType);
    }

    public List<ParkingSlot> getBookingsByUser(String email) {
        return slotRepo.findByBookedBy_Email(email);
    }


}

