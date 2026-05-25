package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private Controller controller;
    private JButton bottoneDegenti, bottoneDegenze, bottoneEsci;

    public DashboardFrame(Controller controller) {
        this.controller = controller;
        setTitle("Dashboard Principale");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(3, 1, 15, 15));
        pannello.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        bottoneDegenti = new JButton("Gestione Degenti");
        bottoneDegenze = new JButton("Gestione Degenze");
        bottoneEsci = new JButton("Logout");

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