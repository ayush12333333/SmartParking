package com.SmartParking.SmartParking.controller;

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

        @PutMapping("/update")
        public ResponseEntity<?> updateUser(
                @AuthenticationPrincipal UserDetails userDetails,
                @RequestBody User updatedUser
        ) {
            try {
                User user = userService.updateUser(userDetails.getUsername(), updatedUser);
                return ResponseEntity.ok(user);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }


