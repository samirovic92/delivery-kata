package com.carrefour.deliverykata.api.Dto;

import com.carrefour.deliverykata.domain.delivery.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeSlotDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static TimeSlotDto toDto(TimeSlot timeSlot) {
        return new TimeSlotDto(
                timeSlot.getId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime()
        );
    }
}
