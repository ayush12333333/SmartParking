package com.SmartParking.SmartParking.controller;


import com.SmartParking.SmartParking.dto.BookingDTO;
import com.SmartParking.SmartParking.dto.BookingResponse;
import com.SmartParking.SmartParking.entity.Booking;
import com.SmartParking.SmartParking.entity.ParkingSlot;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.entity.UserRole;
import com.SmartParking.SmartParking.repository.BookingRepository;
import com.SmartParking.SmartParking.service.BookingService;
import com.SmartParking.SmartParking.service.ParkingSlotService;
import com.SmartParking.SmartParking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final ParkingSlotService parkingService;
@Autowired
private BookingRepository  bookingRepo;
    private final BookingService bookingService;
    private final UserService userService; // To get current logged-in user
    @PostMapping("/book/{slotId}")
    public ResponseEntity<?> bookSlot(@PathVariable Long slotId,
                                      @RequestParam Integer hours,
                                      @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        BookingResponse response = bookingService.bookSlot(slotId, hours, user);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId,
     @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        bookingService.cancelBooking(bookingId, user);
        return ResponseEntity.ok("Booking cancelled");
    }

    @PostMapping("/checkout/{bookingId}")
    public ResponseEntity<?> checkout(@PathVariable Long bookingId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        double finalFare = bookingService.checkout(bookingId, currentUser);
        return ResponseEntity.ok("Checkout successful. Total fare: ₹" + finalFare);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<?> myBookings( @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());


        List<Booking> bookings = bookingRepo.findByUserIdAndActiveTrue(user.getId());

        List<BookingResponse> responses = bookings.stream()
                .map(booking -> BookingResponse.builder()
                        .slotId(booking.getSlot().getId())
                        .slotNumber(booking.getSlot().getSlotNumber())
                        .location(booking.getSlot().getLocation())
                        .vehicleType(booking.getSlot().getVehicleType())
                        .checkInTime(booking.getCheckInTime())
                        .reservedHours(booking.getReservedHours())
                        .fareAmount(booking.getFareAmount())
                        .message("Booking record found")
                        .build())
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/bookings-by-email")
    public ResponseEntity<?> getBookingsByEmail(@RequestParam String email,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Admins only.");
        }

        List<ParkingSlot> slots = parkingService.getBookingsByUser(email);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/admin/all-bookings")
    public ResponseEntity<?> getAllBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(403).body("Access denied: Admins only.");
        }

        List<Booking> allBookings = bookingRepo.findAll();

        List<BookingDTO> dtoList = allBookings.stream().map(booking -> {
            return new BookingDTO(
                    booking.getId(),
                    booking.getUser().getEmail(),
                    booking.getSlot().getSlotNumber(),
                    booking.getSlot().getLocation(),
                    booking.getCheckInTime(),
                    booking.getCheckOutTime(),
                    booking.getFareAmount()
            );
        }).toList();

        return ResponseEntity.ok(dtoList);
    }




}
