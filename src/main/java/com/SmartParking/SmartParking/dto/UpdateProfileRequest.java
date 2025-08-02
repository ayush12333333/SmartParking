package com.SmartParking.SmartParking.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private String phoneNumber;
}
