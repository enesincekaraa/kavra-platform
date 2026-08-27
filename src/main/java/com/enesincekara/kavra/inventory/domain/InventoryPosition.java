package com.enesincekara.kavra.inventory.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public record InventoryPosition(
        String tenantId,
        String productId,
        String locationId,
        BigDecimal onHandQuantity,
        BigDecimal reservedQuantity,
        Instant observedAt
) {

    public InventoryPosition{
        productId = requireText(productId, "productId");
        locationId = requireText(locationId, "locationId");

        onHandQuantity =
                requireNonNegative(onHandQuantity, "onHandQuantity");

        reservedQuantity =
                requireNonNegative(reservedQuantity, "reservedQuantity");

        Objects.requireNonNull(
                observedAt,
                "observedAt must not be null"
        );
    }

    public BigDecimal availableQuantity() {
        return onHandQuantity.subtract(reservedQuantity);
    }

    public boolean hasStockDeficit() {
        return availableQuantity().signum() < 0;
    }
    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return value.trim();
    }

    private static BigDecimal requireNonNegative(
            BigDecimal value,
            String fieldName
    ) {
        Objects.requireNonNull(value, fieldName + " must not be null");

        if (value.signum() < 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be zero or greater"
            );
        }

        return value;
    }

}
