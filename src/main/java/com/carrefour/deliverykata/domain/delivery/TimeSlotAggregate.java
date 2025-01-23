package com.carrefour.deliverykata.domain.delivery;

import com.carrefour.deliverykata.domain.common.command.ReserveTimeSlotCommand;
import com.carrefour.deliverykata.domain.delivery.events.TimeSlotReservedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@NoArgsConstructor
@Aggregate
public class TimeSlotAggregate {

    @AggregateIdentifier
    public Long timeSlotId;
    public boolean reserved;

    @CommandHandler
    public TimeSlotAggregate(ReserveTimeSlotCommand command) {
        AggregateLifecycle.apply(new TimeSlotReservedEvent(command.timeSlotId(), true));
    }

    @EventSourcingHandler
    public void on(TimeSlotReservedEvent timeSlotReservedEvent) {
        this.timeSlotId = timeSlotReservedEvent.timeSlotId();
        this.reserved = timeSlotReservedEvent.reserved();
    }
}
