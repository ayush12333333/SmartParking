package com.SmartParking.SmartParking.service;

import com.SmartParking.SmartParking.dto.BookingResponse;
import com.SmartParking.SmartParking.entity.Booking;
import com.SmartParking.SmartParking.entity.ParkingSlot;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.repository.BookingRepository;
import com.SmartParking.SmartParking.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class BookingService {

    @Autowired
  private  ParkingSlotRepository slotRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Transactional
    public BookingResponse bookSlot(Long slotId, int hours, User user) {
        ParkingSlot slot = slotRepo.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Slot already booked");
        }

        LocalDateTime checkInTime = LocalDateTime.now();
        LocalDateTime checkOutTime = checkInTime.plusHours(hours);

        Double fare = slot.getBaseFare() != null ? slot.getBaseFare() * hours : 0.0;
        slot.setAvailable(false);
        slot.setBooked(true);
        slot.setBookedAt(checkInTime);
        slot.setBookedBy(user);
        slotRepo.save(slot); // Save updated slot state

        //  Now create the booking
        Booking booking = Booking.builder()
                .user(user)
                .slot(slot)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .reservedHours(hours)
                .fareAmount(fare)
                .active(true)
                .build();

        bookingRepo.save(booking);

        return BookingResponse.builder()
                .message("Slot booked successfully!")
                .slotId(slot.getId())
                .slotNumber(slot.getSlotNumber())
                .location(slot.getLocation())
                .vehicleType(slot.getVehicleType())
                .checkInTime(checkInTime)
                .reservedHours(hours)
                .fareAmount(fare)
                .build();
    }



    public List<Booking> getMyActiveBookings(User user) {
        return bookingRepo.findByUserAndActiveTrue(user);
    }



    public void cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepo.findBySlotIdAndActiveTrue(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if the booking belongs to the current user
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        // Cancel the booking
        booking.setActive(false);
        bookingRepo.save(booking);

        // Make slot available again
        ParkingSlot slot = booking.getSlot();
        slot.setAvailable(true);
        slot.setBooked(false);
        slot.setBookedAt(null);
        slot.setBookedBy(null);
        slotRepo.save(slot);
    }

@Transactional
    public double checkout(Long bookingId, User user) {
        Booking booking = bookingRepo.findBySlotIdAndActiveTrue(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().equals(user) || !booking.isActive()) {
            throw new RuntimeException("Invalid booking");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();
        booking.setCheckOutTime(checkOutTime);

        long totalHours = Duration.between(booking.getCheckInTime(), checkOutTime).toHours();
        long extraHours = totalHours - booking.getReservedHours();
        double penalty = 0;

        if (extraHours > 0) {
            penalty = extraHours * 10; // ₹10 per extra hour
        }

        double finalFare = booking.getFareAmount() + penalty;
        booking.setFareAmount(finalFare);
        booking.setActive(false); // completed
        bookingRepo.save(booking);

        //  Mark slot available again
        ParkingSlot slot = booking.getSlot();
        slot.setAvailable(true);
        slot.setBooked(false);
        slot.setBookedAt(null);
        slot.setBookedBy(null);
        slotRepo.save(slot);  // <-- Must save slot!

        return finalFare;
    }


    public List<Booking> getBookingsByUser(User user) {
        return bookingRepo.findByUser(user);
    }

    public List<ParkingSlot> getBookingsByUser(String email) {
        return slotRepo.findByBookedBy_Email(email);
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll(); // make sure this returns proper booking list with slot + user
    }


}
