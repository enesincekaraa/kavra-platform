package com.enesincekara.kavra.inventory.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record DemandProfile(
        String tenantId,
        String productId,
        String locationId,
        BigDecimal averageDailyDemand,
        int observationWindowDays,
        Instant calculatedAt
) {

    public DemandProfile{
        tenantId = requireText(tenantId, "tenantId");
        productId = requireText(productId, "productId");
        locationId = requireText(locationId, "locationId");

        Objects.requireNonNull(
                averageDailyDemand,
                "averageDailyDemand must not be null"
        );

        if (averageDailyDemand.signum() < 0) {
            throw new IllegalArgumentException(
                    "averageDailyDemand must be zero or greater"
            );
        }

        if (observationWindowDays < 1) {
            throw new IllegalArgumentException(
                    "observationWindowDays must be greater than zero"
            );
        }

        Objects.requireNonNull(
                calculatedAt,
                "calculatedAt must not be null"
        );
    }

    public boolean hasObservedDemand() {
        return averageDailyDemand.signum() > 0;
    }


    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return value.trim();
    }
}
