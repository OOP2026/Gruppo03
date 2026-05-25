package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private Controller controller;
    private JButton bottoneDegenti, bottoneDegenze, bottoneEsci;

    public DashboardFrame(Controller controller) {
        this.controller = controller;
        setTitle("dashboard principale");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(3, 1, 10, 10));
        bottoneDegenti = new JButton("gestione degenti");
        bottoneDegenze = new JButton("gestione degenze");
        bottoneEsci = new JButton("logout");

        pannello.add(bottoneDegenti);
        pannello.add(bottoneDegenze);
        pannello.add(bottoneEsci);
        add(pannello);

        bottoneDegenti.addActionListener(e -> new ArchivioDegentiFrame(controller));
        bottoneDegenze.addActionListener(e -> new GestioneDegenzaFrame(controller));
        bottoneEsci.addActionListener(e -> {
            new AccessoFrame(controller);
            dispose();
        });
        setVisible(true);
    }
}