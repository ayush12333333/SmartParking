package com.SmartParking.SmartParking.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotUserView {
    private Long id;
    private String location;
    private String slotNumber;

    private Boolean available;
    private int fareAmount;

}
