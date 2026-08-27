package com.enesincekara.kavra.inventory.contract;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InventoryChangedEventTest {
    @Test
    void shouldCreateValidInventoryChangedEvent() {
        InventoryChangedEvent event = eventWith(
                "tenant-retail-1",
                new BigDecimal("120"),
                new BigDecimal("20")
        );

        assertThat(event.schemaVersion()).isEqualTo(1);
        assertThat(event.tenantId()).isEqualTo("tenant-retail-1");

        assertThat(event.onHandQuantity()).isEqualByComparingTo("120");
        assertThat(event.reservedQuantity()).isEqualByComparingTo("20");
    }

    @Test
    void shouldRejectBlankTenantId() {
        assertThatThrownBy(() -> eventWith(
                " ",
                new BigDecimal("120"),
                new BigDecimal("20")
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tenantId must not be blank");
    }

    @Test
    void shouldRejectNegativeOnHandQuantity() {
        assertThatThrownBy(() -> eventWith(
                "tenant-retail-1",
                new BigDecimal("-1"),
                BigDecimal.ZERO
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "onHandQuantity must be zero or greater"
                );
    }

    @Test
    void shouldAllowReservationsToExceedPhysicalStock() {
        InventoryChangedEvent event = eventWith(
                "tenant-retail-1",
                new BigDecimal("10"),
                new BigDecimal("14")
        );

        assertThat(event.reservedQuantity())
                .isGreaterThan(event.onHandQuantity());
    }


    private static InventoryChangedEvent eventWith(
            String tenantId,
            BigDecimal onHandQuantity,
            BigDecimal reservedQuantity
    ){
        return new InventoryChangedEvent(
                UUID.fromString(
                        "7ef59f58-5328-4fca-b303-08fd27f623e4"
                ),
                1,
                tenantId,
                "SAP",
                "SKU-1001",
                "ISTANBUL-01",
                onHandQuantity,
                reservedQuantity,
                Instant.parse("2026-08-28T13:00:00Z")
        );
    }
}
