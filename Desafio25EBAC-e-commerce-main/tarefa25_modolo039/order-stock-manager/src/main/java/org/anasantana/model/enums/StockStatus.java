package org.anasantana.model.enums;

public enum StockStatus {

    /*
     *  Attention:
     * When adding a new status, assign a unique and sequential integer value
     * to maintain consistency and prevent logic failures in dependent systems.
     */

    IN_STOCK(1),         // Quantidade saudável
    LOW_STOCK(3),        // Em nível crítico
    OUT_OF_STOCK(4);     // Esgotado

    private final int code;

    private StockStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StockStatus valueOf(int code) {
        for (StockStatus value : StockStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid code for StockStatus!");
    }

    public static StockStatus evaluate(int quantity) {
        if (quantity == 0) {
            return OUT_OF_STOCK;
        } else if (quantity <= 10) {
            return LOW_STOCK;
        } else {
            return IN_STOCK;
        }
    }
}
