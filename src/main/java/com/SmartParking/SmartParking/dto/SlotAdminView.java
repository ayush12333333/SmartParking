package com.SmartParking.SmartParking.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotAdminView {
    private Long id;
    private String location;
    private String slotNumber;
    private LocalDateTime bookedAt;
    private Boolean available;

    private String bookedByName;
    private String bookedByEmail;
}
