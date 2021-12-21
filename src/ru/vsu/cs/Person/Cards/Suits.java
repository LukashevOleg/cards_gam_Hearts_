package ru.vsu.cs.Person.Cards;

public enum Suits
{
    CLUBS("крести"),
    DIAMONDS("бубны"),
    HEARTS("червы"),
    SPADES("пики");


    private String name;
    Suits(String name) {
        this.name =name;
    }

    public String getName() {
        return name;
    }
}
