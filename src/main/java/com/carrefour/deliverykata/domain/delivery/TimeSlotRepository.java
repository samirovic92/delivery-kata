package com.carrefour.deliverykata.domain.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository {

    List<TimeSlot> findAvailableTimeSlotByMode(DeliveryMode deliveryMode);

    List<TimeSlot> findAvailableTimeSlotByModeAndDate(DeliveryMode deliveryMode, LocalDateTime from, LocalDateTime to);

    Optional<TimeSlot> findById(Long aLong);

    void reserveTimeSlot(Long timeSlotId);
}
