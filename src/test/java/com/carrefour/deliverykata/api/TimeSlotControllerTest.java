package com.carrefour.deliverykata.api;

import com.carrefour.deliverykata.domain.delivery.models.DeliveryMode;
import com.carrefour.deliverykata.domain.delivery.models.TimeSlot;
import com.carrefour.deliverykata.domain.delivery.TimeSlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(TimeSlotController.class)
class TimeSlotControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TimeSlotService timeSlotService;

    @Test
    @WithMockUser(username = "test-client", roles = {"CLIENT"})
    void test_Get_Time_Slots() {
        // Given

        TimeSlot timeSlot1 = new TimeSlot(1L, LocalDateTime.of(2025, 1, 10, 10, 0), LocalDateTime.of(2025, 1, 10, 12, 0), false);
        TimeSlot timeSlot2 = new TimeSlot(2L, LocalDateTime.of(2025, 1, 15, 14, 0), LocalDateTime.of(2025, 1, 15, 16, 0), true);

        when(timeSlotService.getAvailableTimeSlots(DeliveryMode.DRIVE)).thenReturn(List.of(timeSlot1, timeSlot2));

        // When + Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/timeslots")
                        .queryParam("deliveryMode", DeliveryMode.DRIVE)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TimeSlot.class)
                .hasSize(2)
                .contains(timeSlot1, timeSlot2)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .extracting("id")
                            .containsExactly(1L, 2L);
                });
    }
}
