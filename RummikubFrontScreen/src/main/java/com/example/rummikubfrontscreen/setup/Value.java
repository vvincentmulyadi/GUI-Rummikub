package com.example.rummikubfrontscreen.setup;

// The code defines an enum called `Value` which represents the possible values of a tile in a Rummikub
// game. Each value is associated with a symbol and a numerical value.
public enum Value {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    // SEVEN("7"),
    // EIGHT("8"),
    // NINE("9"),
    // TEN("10"),
    // ELEVEN("11"),
    // TWELVE("12"),
    // THIRTEEN("13"),
    JOKER("100");

    private String valueSymbol;
    private int value;

    Value(String valueSymbol) {
        this.valueSymbol = valueSymbol;
        this.value = Integer.parseInt(valueSymbol);
    }

    public String getSymbol() {
        return this.valueSymbol;
    }

    public int getValue() {
        return this.value;
    }

    public String getValueSymbol() {
        return this.valueSymbol;
    }

    public static Value getValueBySymbol(String symbol) {
        for (Value value : values()) {
            if (value.valueSymbol.equals(symbol)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant with symbol " + symbol);
    }

}
