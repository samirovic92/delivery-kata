package com.carrefour.deliverykata.infrastructure.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Timeslot")
public class TimeSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean reserved;

    public TimeSlotEntity(DeliveryMode mode, LocalDateTime startTime, LocalDateTime endTime, boolean reserved) {
        this.deliveryMode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reserved = reserved;
    }

    public TimeSlot toDomain() {
        return new TimeSlot(
                this.id,
                this.startTime,
                this.endTime,
                this.reserved
        );
    }
}
