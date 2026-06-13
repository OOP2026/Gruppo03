package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * finestra di login dell'applicazione.
 * si occupa di chiedere all'utente le credenziali di accesso
 * e di verificare tramite il controller se sono valide.
 * se l'accesso riesce apre la dashboard principale altrimenti mostra un errore.
 */
public class AccessoFrame extends JFrame {

    // elementi di logica e componenti dell'interfaccia grafica
    private Controller controller;
    private JTextField campoUtente;
    private JPasswordField campoPassword;
    private JButton pulsanteAccesso;

    /**
     * costruttore della finestra di accesso.
     * riceve il controller principale che verrà usato per verificare le credenziali.
     *
     * @param controller il controller centrale che contiene la logica del programma
     */
    public AccessoFrame(Controller controller) {

        // salva il riferimento al controller per l'autenticazione
        this.controller = controller;

        // impostazioni base della finestra grafica
        setTitle("Login del sistema");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // centra la finestra sullo schermo del computer
        setLocationRelativeTo(null);

        // crea il pannello principale con una disposizione a griglia ordinata
        JPanel pannello = new JPanel(new GridLayout(3, 2, 10, 10));

        // imposta un margine vuoto per distanziare i componenti dai bordi della finestra
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // inizializzazione dei componenti grafici di testo e di input
        JLabel labelUtente = new JLabel("Utente:");
        JLabel labelPassword = new JLabel("Password:");

        campoUtente = new JTextField();
        campoPassword = new JPasswordField();

        pulsanteAccesso = new JButton("Accedi");

        // aggiunta dei componenti al pannello seguendo l'ordine della griglia
        pannello.add(labelUtente);
        pannello.add(campoUtente);
        pannello.add(labelPassword);
        pannello.add(campoPassword);
        pannello.add(new JLabel());
        pannello.add(pulsanteAccesso);

        add(pannello);

        // gestione dell'evento di clic sul bottone per l'autenticazione
        pulsanteAccesso.addActionListener(e -> {

            String utente = campoUtente.getText();
            String password = new String(campoPassword.getPassword());

            // controlla la validità delle credenziali tramite il controller centrale
            if (controller.controllaAccesso(utente, password)) {

                // gestione in caso di accesso confermato
                JOptionPane.showMessageDialog(null, "accesso effettuato");

                // Ora chiamerà correttamente il file DashboardFrame.java esterno
                new DashboardFrame(controller);

                // chiude e distrugge la finestra corrente di login per liberare memoria
                dispose();
            } else {
                // gestione in caso di credenziali non valide
                JOptionPane.showMessageDialog(null, "credenziali errate");
            }
        });

        // rende visibile la finestra una volta completata la configurazione
        setVisible(true);
    }
}