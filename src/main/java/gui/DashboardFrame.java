package gui;

import controller.Controller;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

/**
 * dashboard principale mostrata dopo che l'utente ha fatto il login con successo.
 * fa da menu centrale: mostra il riepilogo dei letti e permette di aprire le altre finestre
 * (archivio degenti e gestione degenze).
 */
public class DashboardFrame extends JFrame {

    private final Controller controller;

    private JLabel labelLettiTotali;
    private JLabel labelLettiOccupati;
    private JLabel labelLettiLiberi;

    private JButton bottoneDegenti;
    private JButton bottoneDegenze;
    private JButton bottoneEsci;

    /**
     * crea la schermata di menu e posiziona tutti i bottoni e i testi.
     *
     * @param controller il controller centrale per leggere i contatori dal database
     */
    public DashboardFrame(Controller controller) {
        this.controller = controller;

        setTitle("Menù");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(6, 1, 10, 10));
        pannello.setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 70));

        labelLettiTotali = new JLabel();
        labelLettiOccupati = new JLabel();
        labelLettiLiberi = new JLabel();

        bottoneDegenti = new JButton("Archivio Degenti");
        bottoneDegenze = new JButton("Gestione Degenze");
        bottoneEsci = new JButton("Logout");

        pannello.add(labelLettiTotali);
        pannello.add(labelLettiOccupati);
        pannello.add(labelLettiLiberi);
        pannello.add(bottoneDegenti);
        pannello.add(bottoneDegenze);
        pannello.add(bottoneEsci);

        add(pannello);

        bottoneDegenti.addActionListener(e -> new ArchivioDegentiFrame(controller));

        bottoneDegenze.addActionListener(e -> new GestioneDegenzaFrame(controller, this));

        bottoneEsci.addActionListener(e -> {
            new AccessoFrame(controller);
            dispose();
        });

        aggiornaDashboard();

        setVisible(true);
    }

    /**
     * chiede al controller i numeri aggiornati dei letti (totali, occupati e liberi)
     * e aggiorna le scritte sulla schermata.
     */
    public void aggiornaDashboard() {
        labelLettiTotali.setText("Letti totali: " + controller.contaLettiTotali());
        labelLettiOccupati.setText("Letti occupati: " + controller.contaLettiOccupati());
        labelLettiLiberi.setText("Letti liberi: " + controller.contaLettiLiberi());
    }
}