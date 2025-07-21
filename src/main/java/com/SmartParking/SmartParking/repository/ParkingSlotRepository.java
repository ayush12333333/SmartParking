package com.SmartParking.SmartParking.repository;

import com.SmartParking.SmartParking.entity.ParkingSlot;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    // Get all available slots
    List<ParkingSlot> findByAvailableTrue();

    // Get available slots filtered by location
    List<ParkingSlot> findByLocationAndAvailableTrue(String location);

    // Get available slots filtered by vehicle type
    List<ParkingSlot> findByVehicleTypeAndAvailableTrue(VehicleType vehicleType);

    // Get available slots filtered by both location and vehicle type
    List<ParkingSlot> findByLocationAndVehicleTypeAndAvailableTrue(String location, VehicleType vehicleType);

    // Get all slots booked by a user (by object)
    List<ParkingSlot> findByBookedBy(User user);

    // OR (Optional) Get all slots booked by a user (by email)
    List<ParkingSlot> findByBookedBy_Email(String email);

    // Count slots by location (used to generate slot numbers)
    long countByLocation(String location);
}
