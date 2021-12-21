package ru.vsu.cs.Person.Game;

import ru.vsu.cs.Person.Cards.Card;
import ru.vsu.cs.Person.Cards.Cards;
import ru.vsu.cs.Person.Cards.Suits;
import ru.vsu.cs.Person.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private List<Card> DECK_CARD = initializationDeck();
    private List<Card> listCardOnTable= new ArrayList<>();

    public List<Card> getListCardOnTable() {
        return listCardOnTable;
    }

    private List<Player> listPlayer = new ArrayList<>();
    private int whichRound=1;


    int indexCurPlayer;


    public int getWhichRound() {
        return whichRound;
    }

    public void throwCard(Card card)
    {
        listCardOnTable.add(card);
    }

    private List<Card> initializationDeck()
    {
        List<Card> list = new ArrayList<>();
        for(Cards c : Cards.values())
        {
            for(Suits s : Suits.values())
            {
                list.add(new Card(c, s));
            }
        }
        givePictures(list);
        return list;
    }

    private void givePictures(List<Card> list)
    {
        StringBuilder path;
        for(Card c : list)
        {
            path = new StringBuilder(100);
            String firstPart = "C:\\Users\\ACER\\IdeaProjects\\Cards\\cardsPicture\\";
            String lastPart = ".png";
            path.append(firstPart);
            switch (c.getCard())
            {
                case TWO -> path.append("2");
                case THREE -> path.append("3");
                case FOUR -> path.append("4");
                case FIVE -> path.append("5");
                case SIX -> path.append("6");
                case SEVEN -> path.append("7");
                case EIGHT -> path.append("8");
                case NINE -> path.append("9");
                case TEN -> path.append("10");
                case JACK -> path.append("Jack");
                case QUEEN -> path.append("Lady");
                case KING -> path.append("King");
                case ACE -> path.append("Ace");
            }
            switch (c.getSuit())
            {
                case HEARTS -> path.append("Cher");
                case SPADES -> path.append("Pick");
                case CLUBS -> path.append("Kres");
                case DIAMONDS -> path.append("Bub");
            }
            path.append(lastPart);
            c.setId(path.toString());
            path.reverse();
        }
    }

    public Player getCurPlayer()
    {
        return listPlayer.get(indexCurPlayer);
    }

    public int getFirstStepPlayer()
    {
        int i=0;
        for(Player p : listPlayer)
        {
            for(Card c : p.getListDeskCards())
            {
                if(c.getCard() == Cards.TWO)
                {
                    if(c.getSuit() == Suits.CLUBS)
                    {
                        return i;
                    }
                }

            }
            i++;
        }
        return -1;
    }

    public void newGame(List<Player> listPlayer)
    {
        this.listPlayer.addAll(listPlayer);
    }

    public void changeCurPlayer(int i)
    {
        indexCurPlayer =i;
    }

    public void changeCurPlayer()
    {
        if(indexCurPlayer == listPlayer.size()-1)
            indexCurPlayer=0;
        else indexCurPlayer++;
    }

    public int whichPlayerTakeBride()
    {
        Card firstCard = listCardOnTable.get(0);
        int index=indexCurPlayer;
        for(int i=1; i<listCardOnTable.size();i++)
        {
            Card c = listCardOnTable.get(i);
            if(c.getValueCard() > firstCard.getValueCard() && c.getSuit() == firstCard.getSuit())
            {
                if(index + i >listPlayer.size()-1)
                {
                    index = (index + i) - listPlayer.size();
                    firstCard = listCardOnTable.get(index);
                }
                else
                    index += i;
            }
        }
        return index;
    }

    public void handOutCards()
    {
        List<Card> listDropCard= new ArrayList<>();
        int index;
        int count = DECK_CARD.size();
        for(Player p : listPlayer)
        {
            for(int i=0; i< count/listPlayer.size(); i++) {
                index = (int) (Math.random() * (DECK_CARD.size() - 1));
                p.takeCard(DECK_CARD.get(index));
                listDropCard.add(DECK_CARD.get(index));
                DECK_CARD.remove(index);
            }
        }
        DECK_CARD.addAll(listDropCard);
    }

    public void nextRound()
    {
        whichRound++;
        for(Player p : listPlayer)
            p.getListBride().clear();
    }

    public void countPointsPlayer()
    {
        int index=0;
        int i=0;
        for(Player p : listPlayer)
        {

            for(Card c : p.getListBride())
            {
                if(c.getSuit() == Suits.HEARTS)
                {
                    p.plusPint(1);
                    i++;
                }
                else if(c.getValueCard()==12 && c.getSuit() == Suits.SPADES)
                {

                    p.plusPint(13);
                    i++;
                }

                if(i==14)
                {
                    p.plusPint(-26);
                    plusAllPlayer(index);
                    return;
                }
            }
            i=0;
            index++;
        }
    }

    private void plusAllPlayer(int index)
    {
        int i = 0;
        for(Player p : listPlayer)
        {
            if(i != index)
            {
                p.plusPint(26);
            }
        }
    }

    public List<Player> getListPlayer()
    {
        return listPlayer;
    }

    //для обмена в начале раунда
    public Player getPlayerForExchange(int i)
    {
        switch (whichRound){
            case 5:
            case 1: // налево
                    if(i!= listPlayer.size()-1)
                    {
                        return listPlayer.get(i+1);
                    }
                    else {
                        return listPlayer.get(0);
                    }
            case 6:
            case 2://направо
                    Player p = listPlayer.get(i);
                    if(i!= 0)
                    {
                        return listPlayer.get(i-1);
                    }
                    else {
                        return listPlayer.get(listPlayer.size()-1);
                    }
            case 7:
            case 3://напротив
                int count = listPlayer.size()/2;
                    int index = i + count;
                    if( index > listPlayer.size()-1)
                    {
                        return listPlayer.get(index - listPlayer.size()   );
                    }
                    else
                    {
                        return listPlayer.get(index);
                    }
            case 4:
                return listPlayer.get(i);
        }
        return null;
    }

    // соединяем обмен и свои карты
    public void joinCard()
    {
        for(Player p : listPlayer)
        {
            p.takeCard(p.getFromExchange());
            p.getFromExchange().clear();
            p.getListTransferThreeCard().clear();
        }
    }

    public int checkLoser()
        {
        int i=0;
        for(Player p : listPlayer)
        {
            if(p.getPoint() >= 100)
            {
                return i;
            }
            i++;
        }

        i =0;
        Player playerLoser=listPlayer.get(0);
        for(int k=1; k<listPlayer.size(); k++)
        {
            Player p = listPlayer.get(k);
            if(playerLoser.getPoint() < p.getPoint())
            {
                i=k;
                playerLoser = p;
            }
        }
        return i;
    }
}
