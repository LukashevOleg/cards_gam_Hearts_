package ru.vsu.cs.Person.UI.Console;

import ru.vsu.cs.Person.Cards.Card;
import ru.vsu.cs.Person.Game.Game;
import ru.vsu.cs.Person.Players.Bot;
import ru.vsu.cs.Person.Players.Player;
import ru.vsu.cs.Person.UI.FormUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Console implements FormUI
{
    private Game game = new Game();
    Scanner scanner = new Scanner(System.in);

    public Console()
    {
        List<Player> listPlayer= new ArrayList<>();

        listPlayer.add(new Bot("name1"));
        listPlayer.add(new Bot("name2"));
        listPlayer.add(new Bot("name3"));
        listPlayer.add(new Bot("name4"));

        game.newGame(listPlayer);

        gameProcess();
    }

    @Override
    public void gameProcess()
    {
        int indexLoser;
        int count =0;

        do
        {
            game.handOutCards(); // раздали карты

            int index = game.getFirstStepPlayer(); // нашли кто первый ходит с 2 крести
            System.out.println(index);
            game.changeCurPlayer(index); // Изменили для игры

            Player curPlayer = game.getListPlayer().get(index);

            exchangeCards(); // обмен картами
            while (!curPlayer.getListDeskCards().isEmpty()) {
                for (int i = 0; i < game.getListPlayer().size(); i++)
                {
                    if (curPlayer.isBot()) {
                        Card card = curPlayer.chooseCard(game.getListCardOnTable());
                        game.throwCard(card);
                    }
                    else {
                        printTable();
                        System.out.println("Выбери карту");
                        game.throwCard(curPlayer.throwCard(changeCardIndex()));
                    }
                    game.changeCurPlayer();
                    curPlayer = game.getCurPlayer();
                }

                int whichPlayerTakeBride = game.whichPlayerTakeBride();

                game.changeCurPlayer(whichPlayerTakeBride);

                curPlayer = game.getCurPlayer();
                System.out.println("Забирает взятку " + curPlayer.getName());

                curPlayer.takeBride(game.getListCardOnTable());

                game.getListCardOnTable().clear();
            }

            game.countPointsPlayer();
            game.nextRound();


            printPoint();

            indexLoser=game.checkLoser();
            count++;

            for(Player p : game.getListPlayer())
                p.getListBride().clear();

        } while (indexLoser == -1);


        System.out.println(game.getListPlayer().get(indexLoser).getName() + ", ты проиграл!!!");
    }

    @Override
    public void exchangeCards()
    {
        int i=0;
        for(Player player : game.getListPlayer())
        {
            Player p = game.getPlayerForExchange(i); // игрок, которому передаём
            if(player.isBot())
                p.takeExchange(player.transferThreeCard());
            else
            {
                List<Card> list = new ArrayList<>();
//                List<Card> listDeck = player.getListDeskCards();
                printCard(player);
                System.out.println(player.getName() + ", выбери три карты и введи их номера");
                for(int k=0; k<3; k++) {
                    list.add(player.throwCard(changeCardIndex()));
                    printCard(player);
                }
                p.takeExchange(list);
            }
            i++;
        }
        game.joinCard();
    }

    private void printPoint()
    {
        for(Player p : game.getListPlayer())
        {
            System.out.println("Игрок " + p.getName() + " набрал " + p.getPoint() +" баллов");
        }
    }

    private void printCard(Player player)
    {
        List<Card> listDeck = player.getListDeskCards();
        for(int k=0; k<listDeck.size(); k++)
        {
            System.out.println(k + ": *" +listDeck.get(k) + " *;  ");
        }
    }

    private int changeCardIndex()
    {
        int i = scanner.nextInt();
        if(i<0 || i> game.getCurPlayer().getListDeskCards().size())
        {
            System.out.println("Такой нет! Введи еще раз");
            i = changeCardIndex();
        }
        return i;
    }

    private void printTable()
    {
        Player p = game.getCurPlayer();
        System.out.println("================================");
        System.out.println(game.getListCardOnTable());
        System.out.println("================================");
        System.out.println();
        System.out.println(p.getName() + ", а вот твои карты: ");
        printCard(p);
    }




}
