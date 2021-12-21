package ru.vsu.cs.Person.Cards;

public enum Cards
{
    TWO(2, "двойка"),
    THREE(3, "тройка"),
    FOUR(4, "четверка"),
    FIVE(5, "пятерка"),
    SIX(6, "шестерка"),
    SEVEN(7, "семерка"),
    EIGHT(8, "восьмерка"),
    NINE(9, "девятка"),
    TEN(10, "десятка"),
    JACK(11, "валет"),
    QUEEN(12, "дама"),
    KING(13, "король"),
    ACE(14, "туз");

    private final int value;
    private final String name;

    Cards(int i, String name) {
        this.value=i;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
