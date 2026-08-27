package com.enesincekara.kavra.inventory.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class InventoryPositionTest {

    @Test
    void shouldCalculateAvailableQuantity(){
        InventoryPosition position = positionWith(
                new BigDecimal("320"),
                new BigDecimal("80")
        );

        assertThat(position.availableQuantity())
                .isEqualByComparingTo("240");

        assertThat(position.hasStockDeficit()).isFalse();
    }

    @Test
    void shouldDetectStockDeficitWhenReservationsExceedStock() {
        InventoryPosition position = positionWith(
                new BigDecimal("10"),
                new BigDecimal("14")
        );

        assertThat(position.availableQuantity())
                .isEqualByComparingTo("-4");

        assertThat(position.hasStockDeficit()).isTrue();
    }


    @Test
    void shouldRejectNegativePhysicalStockQuantity() {
        assertThatThrownBy(() -> positionWith(
                new BigDecimal("-1"),
                BigDecimal.ZERO
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "onHandQuantity must be zero or greater"
                );
    }

    private static InventoryPosition positionWith(
            BigDecimal onHandQuantity,
            BigDecimal reservedQuantity
    ) {
        return new InventoryPosition(
                "tenant-retail-1",
                "SKU-1001",
                "ISTANBUL-01",
                onHandQuantity,
                reservedQuantity,
                Instant.parse("2026-08-28T13:00:00Z")
        );
    }
}
