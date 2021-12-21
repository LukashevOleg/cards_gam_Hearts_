package ru.vsu.cs.Person.Players;

import ru.vsu.cs.Person.Cards.Card;
import ru.vsu.cs.Person.Cards.Suits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Bot extends Player {

    public Bot(String name) {
        super(name);
    }

    @Override
    public boolean isBot() {
        return true;
    }

    @Override
    public List<Card> transferThreeCard() {
        getListDeskCards().sort(Comparator.comparing(Card::getValueCard));
        List<Card> resultList = new ArrayList<>();
        resultList.add(getListDeskCards().get(10));
        resultList.add(getListDeskCards().get(11));
        resultList.add(getListDeskCards().get(12));

        int i=getListDeskCards().size()-1;
        for(int k=0;k<3;k++)
        {
            getListDeskCards().remove(i);
            i=getListDeskCards().size()-1;
        }

        return resultList;
    }

    @Override
    public Card chooseCard(List<Card> listCardOnTable)
    {
        if(listCardOnTable.isEmpty())
        {
            List<Card> listOtherCard= new ArrayList<>();
            for(Card c : getListDeskCards())
            {
                if(c.getSuit() != Suits.HEARTS && !(c.getValueCard()==12 && c.getSuit() == Suits.SPADES))
                {
                    listOtherCard.add(c); // добавляем все кроме черв и дамы пик
                }
            }

            int index;
            if(!listOtherCard.isEmpty())
            {
                index = (int) (Math.random()*(listOtherCard.size()-1));
                Card c = listOtherCard.get(index);
                index = getListDeskCards().indexOf(c);
                getListDeskCards().remove(index);
                return c; // первый ход не черви
            }
            else {
                index = (int)(Math.random()*(getListDeskCards().size()-1));
                Card c = getListDeskCards().get(index);
                getListDeskCards().remove(index);
                return c ; // или берем любую
            }
        }

        Card card = listCardOnTable.get(0);
        List<Card> listSameFirstSuit = new ArrayList<>();

        for(Card c : getListDeskCards())
        {

            if(c.getSuit() == card.getSuit())
            {
                listSameFirstSuit.add(c);
            }
        }

        if(!listSameFirstSuit.isEmpty())
        {
            List<Card> belowThenFirst= new ArrayList<>();
            for(Card c : listSameFirstSuit)
            {
                if(c.getCard().getValue() < card.getCard().getValue())
                {
                    belowThenFirst.add(c);
                }
            }

            int index;
            if(!belowThenFirst.isEmpty())
            {
                index = (int)(Math.random()* (belowThenFirst.size()-1));
                Card c=belowThenFirst.get(index);
                index = getListDeskCards().indexOf(c);
                getListDeskCards().remove(index);
                return c; // если есть меньше первой карты, мы кидаем карту меньше
            }
            else {
                index = (int) (Math.random() * (listSameFirstSuit.size() - 1));
                Card c = listSameFirstSuit.get(index);
                index = getListDeskCards().indexOf(c);
                getListDeskCards().remove(index);
                return c; // если нету меньше кидаем больше, но той же масти
            }
        }
        else {
            List<Card> listHeartsCard= new ArrayList<>();
            for(Card c : getListDeskCards())
            {
                if(c.getSuit() == Suits.HEARTS)
                {
                    listHeartsCard.add(c);
                }
            }

            int index;
            if(!listHeartsCard.isEmpty())
            {
                index = (int) (Math.random()*(listHeartsCard.size()-1));
                Card c = listHeartsCard.get(index);
                index = getListDeskCards().indexOf(c);
                getListDeskCards().remove(index);
                return c; // если нет той же масти, то есть черви, кинем черви
            }
            else {
                index = (int)(Math.random()*(getListDeskCards().size()-1));
                Card c = getListDeskCards().get(index);
                getListDeskCards().remove(index);
                return c ; // если нет червей, то берем любую карту
            }
        }
    }

}
