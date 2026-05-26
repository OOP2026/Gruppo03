package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * funzionalità
 * finestra principale (dashboard) che appare dopo il login.
 * contiene tre pulsanti: uno per andare alla gestione dei degenti,
 * uno per la gestione delle degenze, e uno per il logout (tornare al login).
 */
public class DashboardFrame extends JFrame {

    // riferimento al controller, per passarlo alle altre finestre
    private Controller controller;

    // i tre pulsanti del menu
    private JButton bottoneDegenti, bottoneDegenze, bottoneEsci;


    public DashboardFrame(Controller controller) {

        // salvo il controller per usarlo quando apro le altre schermate
        this.controller = controller;

        setTitle("Dashboard main");

        setSize(450, 300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // pannello principale con layout a griglia verticale: 3 righe, 1 colonna, spazi di 15px
        JPanel pannello = new JPanel(new GridLayout(3, 1, 15, 15));

        pannello.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        // creo i tre bottoni con testi chiari
        bottoneDegenti = new JButton("Gestione Degenti");
        bottoneDegenze = new JButton("Gestione Degenze");
        bottoneEsci = new JButton("Logout");

        // bottoni alla finestra
        pannello.add(bottoneDegenti);
        pannello.add(bottoneDegenze);
        pannello.add(bottoneEsci);


        add(pannello);

        // ---- azione per il bottone "gestione degenti" ----
        // quando cliccato, apre la finestra dell'archivio degenti
        bottoneDegenti.addActionListener(e -> new ArchivioDegentiFrame(controller));

        // ---- azione per il bottone "gestione degenze" ----
        // apre la finestra per assegnare un ricovero
        bottoneDegenze.addActionListener(e -> new GestioneDegenzaFrame(controller));

        // ---- azione per il bottone "logout" ----
        // torna alla schermata di login e chiude la dashboard
        bottoneEsci.addActionListener(e -> {
            new AccessoFrame(controller);
            dispose();
        });

        //finestra visibile
        setVisible(true);
    }
}