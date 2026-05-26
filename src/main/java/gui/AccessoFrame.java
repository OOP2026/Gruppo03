package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * funzionalità:
 * finestra di login dell'applicazione.
 * si occupa di chiedere all'utente le credenziali (utente e password)
 * e di chiedere al controller se sono valide.
 * se il login riesce, apre la dashboard principale; altrimenti mostra un errore.
 */
public class AccessoFrame extends JFrame {

    // riferimento al controller centrale, che contiene la logica di autenticazione
    private Controller controller;

    //campo nome utente
    private JTextField campoUtente;

    //campo password
    private JPasswordField campoPassword;


    private JButton pulsanteAccesso;

    /**
     * funzionalità:
     * costruttore della finestra di accesso.
     * riceve il controller principale che verrà usato per verificare le credenziali.
     *
     */

    public AccessoFrame(Controller controller) {

        //salvo il controller per usarlo nell'evento del bottone
        this.controller = controller;

        setTitle("Login - sistema gestione pazienti");

        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //centra la finestra nello schermo del computer
        setLocationRelativeTo(null);

        // creo un pannello con un layout a griglia fatto da 3 righe, 2 colonne, spazi orizzontali e verticali di 10px
        JPanel pannello = new JPanel(new GridLayout(3, 2, 10, 10));

        //bordo
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //creazione label
        JLabel labelUtente = new JLabel("Utente:");
        JLabel labelPassword = new JLabel("Password:");


        campoUtente = new JTextField();
        campoPassword = new JPasswordField();


        pulsanteAccesso = new JButton("Accedi");

        e
        pannello.add(labelUtente);
        pannello.add(campoUtente);
        pannello.add(labelPassword);
        pannello.add(campoPassword);
        pannello.add(new JLabel());   // spazio vuoto
        pannello.add(pulsanteAccesso);

        add(pannello);

        //quando utnte clicca su accedi
        pulsanteAccesso.addActionListener(e -> {

            String utente = campoUtente.getText();

            String password = new String(campoPassword.getPassword());

            //verifica se le credenziali sono corrette
            if (controller.controllaAccesso(utente, password)) {

                //credenziali correttd
                JOptionPane.showMessageDialog(null, "Accesso effettuato con successo!");
                //apri dashboard
                new DashboardFrame(controller);
               //chiudi finiestra login
                dispose();
            } else {
                //rimani sulla stessa finestra
                JOptionPane.showMessageDialog(null, "Credenziali errate. Riprova.");}
        });

        setVisible(true);
    }
}