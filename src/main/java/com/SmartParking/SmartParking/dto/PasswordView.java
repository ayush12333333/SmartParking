package com.SmartParking.SmartParking.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordView {

    private String oldPassword;
    private String newPassword;
}
