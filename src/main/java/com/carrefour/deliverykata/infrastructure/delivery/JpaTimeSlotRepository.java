package com.carrefour.deliverykata.infrastructure.delivery;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JpaTimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {

    List<TimeSlotEntity> findByDeliveryModeAndStartTimeAfterAndReservedFalse(DeliveryMode deliveryMode, LocalDateTime from);

    List<TimeSlotEntity> findByDeliveryModeAndStartTimeBetweenAndReservedFalse(DeliveryMode deliveryMode, LocalDateTime from, LocalDateTime to);
}
