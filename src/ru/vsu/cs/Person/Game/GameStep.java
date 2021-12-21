package ru.vsu.cs.Person.Game;

public enum GameStep
{

    NEW_GAME("Начало игры"),
    EXCHANGE("Обмен картами"),
    BRIDE("Взятки"),
    COUNT_POINTS("Подсчет баллов"),
    ENDGAME("Конец игры");


    private final String s;
    GameStep(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }
}
