package com.carrefour.deliverykata.domain.delivery;

import com.carrefour.deliverykata.domain.common.models.FindTimeSlotResponse;
import com.carrefour.deliverykata.domain.common.queries.FindTimeSlotQuery;
import com.carrefour.deliverykata.domain.delivery.events.TimeSlotReservedEvent;
import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TimeSlotServiceTest {

    private TimeSlotRepository timeSlotRepository = mock();
    private Clock clock = mock();
    private TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository, clock);

    @BeforeEach
    public void setup() {
        var fixedDatTime = LocalDateTime.of(LocalDate.of(2025, 1, 21), LocalTime.of(12,0,0));
        when(clock.instant()).thenReturn(fixedDatTime.atZone(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    public void should_get_available_time_slot_for_MODE_DRIVE() {
        // Given
        var deliveryMode = DeliveryMode.DRIVE;
        var startTime = LocalDateTime.now();
        var endTime = LocalDateTime.now().plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(1L, startTime, endTime, false));
        when(timeSlotRepository.findAvailableTimeSlotByMode(DeliveryMode.DRIVE)).thenReturn(expectedTimeSlot);

        // When
        var timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
        verify(timeSlotRepository).findAvailableTimeSlotByMode(DeliveryMode.DRIVE);
    }

    @Test
    public void should_get_available_time_slot_for_MODE_DELIVERY() {
        // Given
        var deliveryMode = DeliveryMode.DELIVERY;
        var startTime = LocalDateTime.now();
        var endTime = LocalDateTime.now().plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(1L, startTime, endTime, false));
        when(timeSlotRepository.findAvailableTimeSlotByMode(DeliveryMode.DELIVERY)).thenReturn(expectedTimeSlot);

        // When
        var timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
        verify(timeSlotRepository).findAvailableTimeSlotByMode(DeliveryMode.DELIVERY);
    }

    @Test
    public void should_get_available_time_slot_for_MODE_DELIVERY_TODAY() {
        // Given
        var deliveryMode = DeliveryMode.DELIVERY_TODAY;
        var startTime = LocalDateTime.now(clock);
        var endTime = LocalDateTime.now(clock).plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(1L, startTime, endTime, false));

        var currentDate = LocalDateTime.now(clock);
        var endOfDay = LocalDateTime.of(LocalDate.of(2025, 1, 21), LocalTime.of(23,59,59));
        when(timeSlotRepository.findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, currentDate, endOfDay))
                .thenReturn(expectedTimeSlot);

        // When
        var timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
        verify(timeSlotRepository).findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, currentDate, endOfDay);
    }

    @Test
    public void should_get_available_time_slot_for_MODE_DELIVERY_ASAP() {
        // Given
        var deliveryMode = DeliveryMode.DELIVERY_ASAP;
        var startTime = LocalDateTime.now(clock);
        var endTime = LocalDateTime.now(clock).plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(1L, startTime, endTime, false));

        var from = LocalDateTime.now(clock);
        var to = LocalDateTime.now(clock).plusDays(3);
        when(timeSlotRepository.findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, from, to))
                .thenReturn(expectedTimeSlot);

        // When
        var timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
        verify(timeSlotRepository).findAvailableTimeSlotByModeAndDate(DeliveryMode.DELIVERY, from, to);
    }

    @Test
    public void should_empty_list_if_no_time_slot_available() {
        // Given
        var deliveryMode = DeliveryMode.DRIVE;
        when(timeSlotRepository.findAvailableTimeSlotByMode(DeliveryMode.DRIVE)).thenReturn(List.of());

        // When
        var timeSlots = timeSlotService.getAvailableTimeSlots(deliveryMode);

        // Then
        assertThat(timeSlots).isEmpty();
        verify(timeSlotRepository).findAvailableTimeSlotByMode(DeliveryMode.DRIVE);
    }

    @Test
    public void should_find_time_slot() {
        // Given
        var findTimeSlotQuery = new FindTimeSlotQuery(1L);
        var expectedTimeSlot = new FindTimeSlotResponse(1L, true);
        when(timeSlotRepository.findById(1L))
                .thenReturn(Optional.of(new TimeSlot(1L, LocalDateTime.now(), LocalDateTime.now(), true)));

        // When
        var timeSlot = timeSlotService.findTimeSlot(findTimeSlotQuery);

        // Then
        assertThat(timeSlot).isEqualTo(expectedTimeSlot);
    }

    @Test
    public void should_return_null_if_no_time_slot_exist() {
        // Given
        var findTimeSlotQuery = new FindTimeSlotQuery(1L);
        var expectedTimeSlot = new FindTimeSlotResponse(1L, true);
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        var timeSlot = timeSlotService.findTimeSlot(findTimeSlotQuery);

        // Then
        assertThat(timeSlot).isNull();
    }

    @Test
    public void should_reserve_time_slot() {
        // Given
        var event = new TimeSlotReservedEvent(1l, true);

        // When
        timeSlotService.on(event);

        // Then
        verify(timeSlotRepository).reserveTimeSlot(1l);
    }
}