package com.SmartParking.SmartParking.controller;

import com.SmartParking.SmartParking.dto.PasswordView;
import com.SmartParking.SmartParking.dto.UpdateProfileRequest;
import com.SmartParking.SmartParking.dto.UserProfileDTO;
import com.SmartParking.SmartParking.entity.User;
import com.SmartParking.SmartParking.service.ParkingSlotService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.SmartParking.SmartParking.service.UserService;
@RestController
    @RequestMapping("/user")
    public class UserController {

        @Autowired
        private UserService userService;
        @Autowired
        private ParkingSlotService parkingSlotService;
        // Get current user profile
        @GetMapping("/me")
        public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
            User user = userService.getUserByEmail(userDetails.getUsername());
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request
    ) {
        try {
            User user = userService.updateUser(userDetails.getUsername(), request);
            return ResponseEntity.ok(new UserProfileDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordView request
    ) {
        try {
            userService.changePassword(userDetails.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}


