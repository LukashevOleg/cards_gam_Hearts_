package ru.vsu.cs.Person.UI.Window;

import ru.vsu.cs.Person.Cards.Card;
import ru.vsu.cs.Person.Game.Game;
import ru.vsu.cs.Person.Game.GameStep;
import ru.vsu.cs.Person.Players.Bot;
import ru.vsu.cs.Person.Players.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.vsu.cs.Person.Game.GameStep.*;

public class MainPanel extends JPanel
{
    Game game = new Game();
    Player curPlayer;
    Player humanPlayer;
    int indexCur = -1;
    int countPlayerDoStep=0;
    GameStep gameStatus = GameStep.NEW_GAME;
    private List<JLabel> jLabelList = new ArrayList<>();
    Timer timer;
    private int timeValue=0;


    public MainPanel()
    {
        this.setLayout(null);
//        game.handOutCards();


        gameProcess();

        timer = new Timer(40, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
//                timeValue+=0.001;
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                timer.start();
                int x = e.getX();
                int y = e.getY();
                Card card = whichCard(x, y);
                System.out.println(card);
                switch (gameStatus) {
                    case EXCHANGE -> takeCardForExchange(card);
                    case BRIDE -> takeCardForBride(card);
                }





            }
        });
    }


    private void gameProcess()
    {
        switch (gameStatus) {
            case NEW_GAME -> newGame();
            case EXCHANGE ->
            {//тут мы бежим по всем ботам и остаётся ток нам выбрать три карты и все
                int i = 0;
                for (Player player : game.getListPlayer()) {
                    Player p = game.getPlayerForExchange(i); // игрок, которому передаём
                    if (player.isBot())
                        p.takeExchange(player.transferThreeCard());
//                    else {
//                        curPlayer = player;
//                    }
                    i++;
                }
            }
            case BRIDE ->
            { // стадия взяток
//                countPlayerDoStep=0;
//                if(indexCur == -1) { //скидывают взятку все боты до игрока
                    int i = game.getListPlayer().indexOf(game.getCurPlayer());
                    Player player = game.getCurPlayer();
                    while (countPlayerDoStep < game.getListPlayer().size())
                    {
                        countPlayerDoStep++;
                        if (player.isBot()) {
                            Card card = player.chooseCard(game.getListCardOnTable());
                            if(card == null)
                                System.out.println();
                            game.throwCard(card);
                        } else {
                            indexCur = game.getListPlayer().indexOf(player);
                            System.out.println("Твой ход");
                            break;
                        }
                        game.changeCurPlayer();
                        player = game.getCurPlayer();
                    }
                    if(humanPlayer.getListDeskCards().isEmpty())
                        gameStatus = GameStep.COUNT_POINTS;

                    if(game.getListCardOnTable().size() == 4)
                    {
                        int whichPlayerTakeBride = game.whichPlayerTakeBride();

                        game.changeCurPlayer(whichPlayerTakeBride);

                        curPlayer = game.getCurPlayer();
                        System.out.println("Забирает взятку " + curPlayer.getName());

                        curPlayer.takeBride(game.getListCardOnTable());

                        game.getListCardOnTable().clear();
                        countPlayerDoStep = 0;
                        gameProcess();
                    }
                    System.out.println(game.getListCardOnTable());



//                }
            }
            case COUNT_POINTS ->
            {
                game.countPointsPlayer();
                game.nextRound();
                printPoint();
                if(game.getWhichRound() > 7)
                    gameStatus = ENDGAME;
                else {
                    gameStatus = EXCHANGE;
                    game.handOutCards();
                }
                gameProcess();
            }
            case ENDGAME ->
            {
                int loser = game.checkLoser();
                curPlayer = game.getListPlayer().get(loser);
            }
        }
    }

    public void printPoint()
    {
        for(Player p : game.getListPlayer())
        {
            System.out.println("Игрок " + p.getName() + " набрал " + p.getPoint() +" баллов");
        }
    }

    private void takeCardForBride(Card card)
    {
        humanPlayer.throwCard(card);
        game.throwCard(card);
        game.changeCurPlayer();
        gameProcess();
    }

    private void takeCardForExchange(Card card)
    {
        humanPlayer.takeCardForExchange(card);
        if(humanPlayer.getListTransferThreeCard().size() == 3)
        {
            Player p = game.getPlayerForExchange(game.getListPlayer().indexOf(humanPlayer));
            p.takeExchange(humanPlayer.transferThreeCard());
            game.joinCard(); // объединяем для каждого игрока карты его и которые 3 получил
            System.out.println();
            game.changeCurPlayer(game.getFirstStepPlayer());
            gameStatus = BRIDE;
            gameProcess();
        }

    }

    private void newGame()
    {
        List<Player> listPlayer= new ArrayList<>();

        listPlayer.add(new Bot("name1"));
        listPlayer.add(new Bot("name2"));
        listPlayer.add(new Bot("name3"));
        listPlayer.add(new Player("name4"));

        game.newGame(listPlayer);
        game.handOutCards();
        humanPlayer = game.getListPlayer().get(3);
        gameStatus = EXCHANGE;

        gameProcess();

    }

    public Card whichCard(int x, int y)
    {
        int i=0;
        System.out.println("осталось лэйблов: " + jLabelList.size());
        for(JLabel l : jLabelList)
        {
            if(l.getX()<x && l.getX()+l.getWidth()>x &&
            l.getY()<y && l.getY() + l.getHeight() >y)
            {
                return humanPlayer.getListDeskCards().get(i);
            }
            i++;
        }
        return null;
    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g =(Graphics2D) graphics;
        this.setBackground(Color.decode("#104d24"));
        this.removeAll();
        BufferedImage myPicture = null;
        jLabelList.clear();


        if(gameStatus != ENDGAME) {
            if (!game.getListCardOnTable().isEmpty()) {
                int x = 50, y = 150, height = 150, width = 105;
                for (Card c : game.getListCardOnTable()) {
                    try {
                        if (c != null)
                            myPicture = ImageIO.read(new File(c.getId()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (myPicture != null) {
                        JLabel curLabel = new JLabel(new ImageIcon(myPicture));
                        curLabel.setLayout(null);
                        curLabel.setBounds(x, y, width, height);
                        this.add(curLabel);
                    }
                    x += width;
                }
            }

            g.setColor(Color.BLACK);
            g.fillRect(0, getHeight() - 250, getWidth(), 15);

            //пишем что делаем
            JLabel infoWhatDo = new JLabel(gameStatus.getS());
            infoWhatDo.setLayout(null);
            infoWhatDo.setBounds(450, 0, 800, 150);
            infoWhatDo.setFont(new Font("Serief", Font.ITALIC + Font.BOLD, 80));
            infoWhatDo.setForeground(Color.decode("#a39303"));
            this.add(infoWhatDo);

            //Пишем какой раунд
            JLabel infoRound = new JLabel(String.valueOf(game.getWhichRound()) + " round");
            infoRound.setLayout(null);
            infoRound.setBounds(0, 0, 400, 150);
            infoRound.setFont(new Font("Serief", Font.ITALIC + Font.BOLD, 80));
            infoRound.setForeground(Color.decode("#a39303"));
            this.add(infoRound);

            // карты на столе
            int x = 10, height = 150, y = getHeight() - height - 50, width = 105;
            for (Card c : humanPlayer.getListDeskCards()) {
                try {
                    myPicture = ImageIO.read(new File(c.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (myPicture != null) {
                    JLabel curLabel = new JLabel(new ImageIcon(myPicture));
                    curLabel.setLayout(null);
                    curLabel.setBounds(x, y, width, height);

                    this.add(curLabel);
                    jLabelList.add(curLabel);
                }
                x += width;
            }
        }
        else
        {
            JLabel infoWhatDo = new JLabel(curPlayer.getName() + ", u r LOSER!");
            infoWhatDo.setLayout(null);
            infoWhatDo.setBounds(300, 100, 800, 150);
            infoWhatDo.setFont(new Font("Serief", Font.ITALIC + Font.BOLD, 80));
            infoWhatDo.setForeground(Color.decode("#a39303"));
            this.add(infoWhatDo);
        }


    }

}
