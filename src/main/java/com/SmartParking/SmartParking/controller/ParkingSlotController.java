package com.SmartParking.SmartParking.controller;

import com.SmartParking.SmartParking.dto.SlotAdminView;
import com.SmartParking.SmartParking.dto.SlotUserView;
import com.SmartParking.SmartParking.entity.ParkingSlot;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.entity.UserRole;
import com.SmartParking.SmartParking.entity.VehicleType;
import com.SmartParking.SmartParking.service.ParkingSlotService;
import com.SmartParking.SmartParking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingService;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createSlot(
            @RequestParam String location,
            @RequestParam VehicleType vehicleType
    ) {
        return ResponseEntity.ok(parkingService.createSlot(location, vehicleType));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam String location,
            @RequestParam VehicleType vehicleType,
            Authentication authentication
    ) {
        User currentUser = userService.getUserByEmail(authentication.getName());

        // User is not allowed to use 'all' filters
        if (currentUser.getRole() != UserRole.ADMIN &&
                (location.equalsIgnoreCase("all") && vehicleType.toString().equalsIgnoreCase("ALL"))) {
            return ResponseEntity.badRequest().body("Users cannot query for all locations or all vehicle types.");
        }

        List<ParkingSlot> slots = parkingService.getAvailableSlots(location, vehicleType);

        if (slots.isEmpty()) {
            return ResponseEntity.ok("No available slots found.");
        }

        if (currentUser.getRole() == UserRole.ADMIN) {
            // ADMIN VIEW
            List<SlotAdminView> adminView = slots.stream().map(slot -> {
                SlotAdminView dto = new SlotAdminView();
                dto.setId(slot.getId());
                dto.setLocation(slot.getLocation());
                dto.setSlotNumber(slot.getSlotNumber());
                dto.setBookedAt(slot.getBookedAt());
                dto.setAvailable(slot.isAvailable());

                if (slot.getBookedBy() != null) {
                    dto.setBookedByName(slot.getBookedBy().getName());
                    dto.setBookedByEmail(slot.getBookedBy().getEmail());
                }

                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(adminView);

        } else {
            // USER VIEW
            List<SlotUserView> userView = slots.stream().map(slot -> {
                SlotUserView dto = new SlotUserView();
                dto.setId(slot.getId());
                dto.setLocation(slot.getLocation());
                dto.setSlotNumber(slot.getSlotNumber());
                dto.setAvailable(slot.isAvailable());
                dto.setFareAmount(slot.getFareAmount());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(userView);
        }
    }
}
