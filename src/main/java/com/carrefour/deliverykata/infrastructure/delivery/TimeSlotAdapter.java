package com.carrefour.deliverykata.infrastructure.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import com.carrefour.deliverykata.domain.delivery.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotAdapter implements TimeSlotRepository {
    private final JpaTimeSlotRepository jpaTimeSlotRepository;
    private Clock clock;

    public TimeSlotAdapter(JpaTimeSlotRepository jpaTimeSlotRepository, Clock clock) {
        this.jpaTimeSlotRepository = jpaTimeSlotRepository;
        this.clock = clock;
    }

    @Override
    public List<TimeSlot> findAvailableTimeSlotByMode(DeliveryMode deliveryMode) {
        var timeSlotEntities = jpaTimeSlotRepository.findByDeliveryModeAndStartTimeAfterAndReservedFalse(deliveryMode, LocalDateTime.now(clock));
        return timeSlotEntities.stream()
                .map(TimeSlotEntity::toDomain)
                .toList();
    }

    @Override
    public List<TimeSlot> findAvailableTimeSlotByModeAndDate(DeliveryMode deliveryMode, LocalDateTime from, LocalDateTime to) {
        var timeSlotEntities = jpaTimeSlotRepository.findByDeliveryModeAndStartTimeBetweenAndReservedFalse(deliveryMode, from, to);
        return timeSlotEntities.stream()
                .map(TimeSlotEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<TimeSlot> findById(Long timeSlotId) {
        return jpaTimeSlotRepository.findById(timeSlotId)
                .map(TimeSlotEntity::toDomain);
    }

    @Override
    public void reserveTimeSlot(Long timeSlotId) {
        var timeSlotOptional = jpaTimeSlotRepository.findById(timeSlotId);
        if (timeSlotOptional.isPresent()) {
            var timeSlotEntity = timeSlotOptional.get();
            timeSlotEntity.setReserved(true);
            jpaTimeSlotRepository.save(timeSlotEntity);
        }
    }
}
