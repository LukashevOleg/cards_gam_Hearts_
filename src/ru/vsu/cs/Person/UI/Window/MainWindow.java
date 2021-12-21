package ru.vsu.cs.Person.UI.Window;

import ru.vsu.cs.Person.UI.FormUI;

import javax.swing.*;

public class MainWindow extends JFrame implements FormUI
{
    public MainWindow()
    {
        MainPanel mainPanel = new MainPanel();
        this.add(mainPanel);
        this.setSize(1400,1000);
//        this.setBackground(Color.decode("#104d24"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    @Override
    public void gameProcess() {}

    @Override
    public void exchangeCards() {}
}
