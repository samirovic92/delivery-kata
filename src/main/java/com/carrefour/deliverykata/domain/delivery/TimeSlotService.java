package com.carrefour.deliverykata.domain.delivery;

import com.carrefour.deliverykata.domain.common.models.FindTimeSlotResponse;
import com.carrefour.deliverykata.domain.common.queries.FindTimeSlotQuery;
import com.carrefour.deliverykata.domain.delivery.events.TimeSlotReservedEvent;
import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
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

    @QueryHandler
    public FindTimeSlotResponse findTimeSlot(FindTimeSlotQuery query) {
        return timeSlotRepository.findById(query.timeSlotId())
                .map(timeSlot -> new FindTimeSlotResponse(timeSlot.getId(), timeSlot.getReserved()))
                .orElse(null);
    }

    @EventHandler
    public void on(TimeSlotReservedEvent event) {
        timeSlotRepository.reserveTimeSlot(event.timeSlotId());
    }
}
