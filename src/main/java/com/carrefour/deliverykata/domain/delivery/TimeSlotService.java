package com.carrefour.deliverykata.domain.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private Clock clock;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, Clock clock) {
        this.timeSlotRepository = timeSlotRepository;
        this.clock = clock;
    }

    public List<TimeSlot> getAvailableTimeSlots(DeliveryMode deliveryMode) {

        if(DeliveryMode.DELIVERY_TODAY.equals(deliveryMode)) {
            var from = LocalDateTime.now(clock);
            var to = LocalDateTime.now(clock).withHour(23).withMinute(59).withSecond(59);
            return timeSlotRepository.findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, from, to);
        }

        if(DeliveryMode.DELIVERY_ASAP.equals(deliveryMode)) {
            var from = LocalDateTime.now(clock);
            var to = LocalDateTime.now(clock).plusDays(3);
            return timeSlotRepository.findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, from, to);
        }

        return timeSlotRepository.findAvailableTimeSlotByMode(deliveryMode);
    }
}
