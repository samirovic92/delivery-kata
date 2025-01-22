package com.carrefour.deliverykata.infrastructure.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest
class TimeSlotAdapterTest {

    @Autowired
    private JpaTimeSlotRepository jpaTimeSlotRepository;
    @MockitoBean
    private Clock clock;
    private TimeSlotAdapter timeSlotAdapter;

    @BeforeEach
    public void setup() {
        var fixedDatTime = LocalDateTime.of(LocalDate.of(2025, 1, 21), LocalTime.of(12,0,0));
        when(clock.instant()).thenReturn(fixedDatTime.atZone(ZoneId.systemDefault()).toInstant());
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        timeSlotAdapter = new TimeSlotAdapter(jpaTimeSlotRepository, clock);
    }

    @AfterEach
    public void clean() {
        jpaTimeSlotRepository.deleteAll();
    }

    @Test
    public void should_return_available_time_slot_By_mode() {
        // Given
        var createTimeSlot = jpaTimeSlotRepository.save(
                new TimeSlotEntity(
                        DeliveryMode.DRIVE,
                        LocalDateTime.now(clock).plusMinutes(10),
                        LocalDateTime.now(clock).plusMinutes(30),
                        false
                )
        );

        var deliveryMode = DeliveryMode.DRIVE;
        var startTime = LocalDateTime.now(clock).plusMinutes(10);
        var endTime = LocalDateTime.now(clock).plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(createTimeSlot.getId(), startTime, endTime, false));

        // When
        var timeSlots = timeSlotAdapter.findAvailableTimeSlotByMode(deliveryMode);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
    }

    @Test
    public void should_not_return_available_time_slot_By_mode() {
        // Given
        var deliveryMode = DeliveryMode.DRIVE;

        // When
        var timeSlots = timeSlotAdapter.findAvailableTimeSlotByMode(deliveryMode);

        // Then
        assertThat(timeSlots).isEmpty();
    }

    @Test
    public void should_return_available_time_slot_By_mode_And_Date() {
        // Given
        var createTimeSlot = jpaTimeSlotRepository.save(
                new TimeSlotEntity(
                        DeliveryMode.DRIVE,
                        LocalDateTime.now(clock),
                        LocalDateTime.now(clock).plusMinutes(10),
                        false
                )
        );

        var deliveryMode = DeliveryMode.DRIVE;
        var startTime = LocalDateTime.now(clock);
        var endTime = LocalDateTime.now(clock).plusMinutes(30);
        var expectedTimeSlot = List.of(new TimeSlot(createTimeSlot.getId(), startTime, endTime, false));
        var from = LocalDateTime.now(clock).minusDays(1);
        var to = LocalDateTime.now(clock).plusDays(1);

        // When
        var timeSlots = timeSlotAdapter.findAvailableTimeSlotByModeAndDate(deliveryMode, from, to);

        // Then
        assertThat(timeSlots).isEqualTo(expectedTimeSlot);
    }

    @Test
    public void should_not_return_available_time_slot_By_mode_And_Date() {
        // Given
        jpaTimeSlotRepository.save(
                new TimeSlotEntity(
                        DeliveryMode.DRIVE,
                        LocalDateTime.now(clock),
                        LocalDateTime.now(clock).plusMinutes(10),
                        false
                )
        );

        var deliveryMode = DeliveryMode.DRIVE;
        var from = LocalDateTime.now(clock).plusDays(1);
        var to = LocalDateTime.now(clock).plusDays(2);

        // When
        var timeSlots = timeSlotAdapter.findAvailableTimeSlotByModeAndDate(deliveryMode, from, to);

        // Then
        assertThat(timeSlots).isEmpty();
    }
}