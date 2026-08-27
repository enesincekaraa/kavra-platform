package com.enesincekara.kavra.inventory.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DemandProfileTest {

    @Test
    void shouldCreateDemandProfileFromHistoricalDemand() {
        DemandProfile profile = profileWith(
                new BigDecimal("40"),
                28
        );

        assertThat(profile.averageDailyDemand())
                .isEqualByComparingTo("40");

        assertThat(profile.observationWindowDays()).isEqualTo(28);
        assertThat(profile.hasObservedDemand()).isTrue();
    }

    @Test
    void shouldAllowZeroAverageDailyDemand() {
        DemandProfile profile = profileWith(
                BigDecimal.ZERO,
                28
        );

        assertThat(profile.hasObservedDemand()).isFalse();
    }

    @Test
    void shouldRejectNegativeAverageDailyDemand() {
        assertThatThrownBy(() -> profileWith(
                new BigDecimal("-1"),
                28
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "averageDailyDemand must be zero or greater"
                );
    }

    @Test
    void shouldRejectInvalidObservationWindow() {
        assertThatThrownBy(() -> profileWith(
                new BigDecimal("40"),
                0
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "observationWindowDays must be greater than zero"
                );
    }

    private static DemandProfile profileWith(
            BigDecimal averageDailyDemand,
            int observationWindowDays
    ) {
        return new DemandProfile(
                "tenant-retail-1",
                "SKU-1001",
                "ISTANBUL-01",
                averageDailyDemand,
                observationWindowDays,
                Instant.parse("2026-08-28T13:00:00Z")
        );
    }
}