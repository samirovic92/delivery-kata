package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.api.Dto.TimeSlotDto;
import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/timeslots")
@Tag(name = "TimeSlot Management", description = "Endpoints for TimeSlot orders")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    @Operation(summary = "Available timeslot", description = "get all Available timeslot")
    public Flux<TimeSlotDto> getTimeSlots(@RequestParam DeliveryMode deliveryMode) {
        List<TimeSlotDto> timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode)
                .stream()
                .map(TimeSlotDto::toDto)
                .toList();
        return Flux.fromIterable(timeSlots);
    }
}