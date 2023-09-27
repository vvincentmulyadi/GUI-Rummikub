package com.example.rummikubfrontscreen.setup;

public enum Value{
    ONE("1"), 
    TWO("2"), 
    THREE("3"), 
    FOUR("4"), 
    FIVE("5"), 
    SIX("6"), 
    SEVEN("7"), 
    EIGHT("8"), 
    NINE("9"), 
    TEN("10"), 
    ELEVEN("11"), 
    TWELVE("12"), 
    THIRTEEN("13"), 
    JOKER("0");

    private String valueSymbol;
    private int value;

    private Value(String valueSymbol){
        this.valueSymbol = valueSymbol;
        this.value = Integer.parseInt(valueSymbol);
    }

    public String getSymbol() {
		return this.valueSymbol;
	}
	
	public int getValue() {
		return this.value;
	}
}
