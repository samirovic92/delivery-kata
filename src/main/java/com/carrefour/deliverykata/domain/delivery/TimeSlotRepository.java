package com.carrefour.deliverykata.domain.delivery;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository {

    List<TimeSlot> findAvailableTimeSlotByMode(DeliveryMode deliveryMode);

    List<TimeSlot> findAvailableTimeSlotByModeAndDate(DeliveryMode deliveryMode, LocalDateTime from, LocalDateTime to);
}
