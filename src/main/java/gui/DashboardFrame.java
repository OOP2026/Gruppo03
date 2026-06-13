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
 * come archivio degenti e gestione degenze.
 */
public class DashboardFrame extends JFrame {

    // riferimento al controller per la comunicazione con il database
    private final Controller controller;

    // etichette di testo per mostrare le statistiche
    private JLabel labelLettiTotali;
    private JLabel labelLettiOccupati;
    private JLabel labelLettiLiberi;

    // pulsanti per la navigazione verso le altre schermate
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

        // impostazioni base della finestra
        setTitle("Menù");
        setSize(500, 350);
        // chiude l'intero programma se l'utente clicca la x rossa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // centra la schermata al centro del monitor
        setLocationRelativeTo(null);

        // crea un pannello con layout a griglia disposto su una singola colonna verticale
        JPanel pannello = new JPanel(new GridLayout(6, 1, 10, 10));

        // imposta margini abbondanti per stringere i bottoni verso il centro
        pannello.setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 70));

        // inizializza le etichette vuote
        labelLettiTotali = new JLabel();
        labelLettiOccupati = new JLabel();
        labelLettiLiberi = new JLabel();

        // inizializza i bottoni con il testo visibile
        bottoneDegenti = new JButton("Archivio Degenti");
        bottoneDegenze = new JButton("Gestione Degenze");
        bottoneEsci = new JButton("Logout");

        // aggiunge gli elementi al pannello seguendo l'ordine dall'alto verso il basso
        pannello.add(labelLettiTotali);
        pannello.add(labelLettiOccupati);
        pannello.add(labelLettiLiberi);
        pannello.add(bottoneDegenti);
        pannello.add(bottoneDegenze);
        pannello.add(bottoneEsci);

        // aggiunge il pannello finito alla finestra principale
        add(pannello);

        // definisce cosa succede quando si clicca sui vari pulsanti

        // apre la finestra per gestire l'archivio
        bottoneDegenti.addActionListener(e -> new ArchivioDegentiFrame(controller));

        // apre la finestra per i ricoveri passando anche il riferimento di questa dashboard per poterla aggiornare
        bottoneDegenze.addActionListener(e -> new GestioneDegenzaFrame(controller, this));

        // disconnette l'utente e riapre la schermata di accesso
        bottoneEsci.addActionListener(e -> {
            new AccessoFrame(controller);
            dispose();
        });

        // carica subito i numeri del database per mostrare statistiche aggiornate
        aggiornaDashboard();

        // mostra la finestra grafica
        setVisible(true);
    }

    /**
     * chiede al controller i numeri aggiornati dei letti totali, occupati e liberi
     * e aggiorna le scritte sulla schermata.
     */
    public void aggiornaDashboard() {
        // sovrascrive il testo delle etichette interrogando i metodi di conteggio del controller
        labelLettiTotali.setText("Letti totali: " + controller.contaLettiTotali());
        labelLettiOccupati.setText("Letti occupati: " + controller.contaLettiOccupati());
        labelLettiLiberi.setText("Letti liberi: " + controller.contaLettiLiberi());
    }
}