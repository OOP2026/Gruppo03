package gui;

import controller.Controller;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Schermata per registrare un ricovero/degenza.
 *
 * Usa tre JComboBox:
 * - paziente già presente nell'archivio;
 * - reparto;
 * - letto libero nel reparto selezionato.
 */
public class GestioneDegenzaFrame extends JFrame {

    private final Controller controller;
    private final DashboardFrame dashboardFrame;

    private JComboBox<String> comboPazienti;
    private JComboBox<String> comboReparti;
    private JComboBox<String> comboLetti;

    private JButton bottoneRegistra;

    public GestioneDegenzaFrame(Controller controller, DashboardFrame dashboardFrame) {
        this.controller = controller;
        this.dashboardFrame = dashboardFrame;

        setTitle("Gestione Degenze");
        setSize(650, 300);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(4, 2, 10, 15));
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboPazienti = new JComboBox<>();
        comboReparti = new JComboBox<>();
        comboLetti = new JComboBox<>();

        bottoneRegistra = new JButton("Registra ricovero");

        pannello.add(new JLabel("Paziente:"));
        pannello.add(comboPazienti);

        pannello.add(new JLabel("Reparto:"));
        pannello.add(comboReparti);

        pannello.add(new JLabel("Letto libero:"));
        pannello.add(comboLetti);

        pannello.add(new JLabel());
        pannello.add(bottoneRegistra);

        add(pannello);

        caricaPazienti();
        caricaReparti();
        aggiornaLettiLiberi();

        comboReparti.addActionListener(e -> aggiornaLettiLiberi());

        bottoneRegistra.addActionListener(e -> registraRicovero());

        setVisible(true);
    }

    private void caricaPazienti() {
        comboPazienti.removeAllItems();

        String[] pazienti = controller.recuperaPazientiPerCombo();

        for (int i = 0; i < pazienti.length; i++) {
            comboPazienti.addItem(pazienti[i]);
        }
    }

    private void caricaReparti() {
        comboReparti.removeAllItems();

        String[] reparti = controller.recuperaRepartiPerCombo();

        for (int i = 0; i < reparti.length; i++) {
            comboReparti.addItem(reparti[i]);
        }
    }

    private void aggiornaLettiLiberi() {
        comboLetti.removeAllItems();

        String repartoSelezionato = (String) comboReparti.getSelectedItem();

        if (repartoSelezionato == null) {
            return;
        }

        String[] lettiLiberi = controller.recuperaLettiLiberiPerReparto(repartoSelezionato);

        for (int i = 0; i < lettiLiberi.length; i++) {
            comboLetti.addItem(lettiLiberi[i]);
        }
    }

    private void registraRicovero() {
        String pazienteSelezionato = (String) comboPazienti.getSelectedItem();
        String repartoSelezionato = (String) comboReparti.getSelectedItem();
        String lettoSelezionato = (String) comboLetti.getSelectedItem();

        if (pazienteSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Nessun paziente disponibile.");
            return;
        }

        if (repartoSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Nessun reparto disponibile.");
            return;
        }

        if (lettoSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Nessun letto libero disponibile per questo reparto.");
            return;
        }

        String codiceFiscale = estraiCodiceFiscale(pazienteSelezionato);

        String messaggio = controller.registraDegenza(
                codiceFiscale,
                repartoSelezionato,
                lettoSelezionato
        );

        JOptionPane.showMessageDialog(this, messaggio);

        if (messaggio.startsWith("OK")) {
            aggiornaLettiLiberi();

            if (dashboardFrame != null) {
                dashboardFrame.aggiornaDashboard();
            }
        }
    }

    private String estraiCodiceFiscale(String testoCombo) {
        String[] parti = testoCombo.split(" - ");

        if (parti.length > 0) {
            return parti[0];
        }

        return testoCombo;
    }
}