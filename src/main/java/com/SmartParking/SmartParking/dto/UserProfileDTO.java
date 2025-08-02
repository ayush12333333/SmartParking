package com.SmartParking.SmartParking.dto;

import com.SmartParking.SmartParking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;


    // constructor
    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();

    }


}

