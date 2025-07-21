package com.SmartParking.SmartParking.repository;

import com.SmartParking.SmartParking.entity.Booking;
import com.SmartParking.SmartParking.entity.ParkingSlot;
import com.SmartParking.SmartParking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByUserAndActiveTrue(User user);
    List<Booking> findByUserIdAndActiveTrue(Long userId);
    Optional<Booking> findBySlotAndActiveTrue(ParkingSlot slot); // to check if slot already booked

    List<Booking> findByUser(User user);
}
