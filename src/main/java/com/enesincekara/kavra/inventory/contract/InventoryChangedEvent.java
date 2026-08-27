package com.enesincekara.kavra.inventory.contract;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record InventoryChangedEvent(

        UUID eventId,
        int schemaVersion,
        String tenantId,
        String sourceSystem,
        String productId,
        String locationId,
        BigDecimal onHandQuantity,
        BigDecimal reservedQuantity,
        Instant occurredAt
) {
    public InventoryChangedEvent{
        Objects.requireNonNull(eventId, "eventId must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");

        if (schemaVersion < 1) {
            throw new IllegalArgumentException(
                    "schemaVersion must be greater than zero"
            );
        }
        tenantId = requireText(tenantId, "tenantId");
        sourceSystem = requireText(sourceSystem, "sourceSystem");
        productId = requireText(productId, "productId");
        locationId = requireText(locationId, "locationId");

        onHandQuantity =
                requireNonNegative(onHandQuantity, "onHandQuantity");

        reservedQuantity =
                requireNonNegative(reservedQuantity, "reservedQuantity");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }

    private static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        if (value.signum() < 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be zero or greater"
            );
        }
        return value;
    }
}
