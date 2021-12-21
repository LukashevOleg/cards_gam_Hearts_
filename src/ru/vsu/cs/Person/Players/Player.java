package ru.vsu.cs.Person.Players;

import ru.vsu.cs.Person.Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private List<Card> listDeskCards= new ArrayList<>();
    private List<Card> fromExchange = new ArrayList<>();
    private List<Card> listBride= new ArrayList<>();
    private List<Card> listTransferThreeCard= new ArrayList<>();

    private int point = 0;

    private String name;

    public Player(String name)
    {
        this.name = name;
    }

    public boolean isBot()
    {
        return false;
    }

    public boolean isEmpty()
    {
        return listDeskCards.isEmpty();
    }


    //метод только для бота
    public Card chooseCard(List<Card> cardOnTable)
    {
        return null;
    }

    public List<Card> getListTransferThreeCard() {
        return listTransferThreeCard;
    }

    public void takeCardForExchange(Card card)
    {
        int index = listDeskCards.indexOf(card);

        listTransferThreeCard.add(throwCard(index));
    }

    public void plusPint(int i)
    {
        point+=i;
    }

    public void takeBride(List<Card> cardList)
    {
        listBride.addAll(cardList);
    }

    public List<Card> transferThreeCard()
    {
        return listTransferThreeCard;
    }

    public Card throwCard(int index)
    {
        Card c=listDeskCards.get(index);
        listDeskCards.remove(index);
        return c;
    }

    public void throwCard(Card card)
    {
        int index = listDeskCards.indexOf(card);
        throwCard(index);
    }

    public List<Card> getFromExchange() {
        return fromExchange;
    }

    public void takeExchange(List<Card> cardList)
    {
        fromExchange.addAll(cardList);
    }

    public void takeCard(Card card)
    {
        listDeskCards.add(card);
    }

    public void takeCard(List<Card> cardList)
    {
        listDeskCards.addAll(cardList);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name= " + name + '\'' +
                ", point=" + point +
                '}';
    }

    public List<Card> getListDeskCards() {
        return listDeskCards;
    }

    public int getPoint() {
        return point;
    }

    public List<Card> getListBride() {
        return listBride;
    }

    public String getName() {
        return name;
    }
}
