package ru.vsu.cs.Person.Cards;

public class Card
{
    private Cards card;
    private Suits suit;
    private String id;

    public Card(Cards card, Suits suit)
    {
        this.card = card;
        this.suit = suit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cards getCard() {
        return card;
    }

    public int getValueCard()
    {
        return card.getValue();
    }

    public Suits getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "card=" + card.getName() +
                ", suit=" + suit.getName() +
                '}';
    }
}
