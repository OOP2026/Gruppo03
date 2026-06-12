package gui;

import controller.Controller;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Finestra per inserire e visualizzare i pazienti presenti nell'archivio.
 */
public class ArchivioDegentiFrame extends JFrame {

    private final Controller controller;

    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoCodiceFiscale;
    private JTextField campoDataNascita;

    private JButton bottoneInserisci;
    private JTable tabella;

    public ArchivioDegentiFrame(Controller controller) {
        this.controller = controller;

        setTitle("Archivio Degenti");
        setSize(850, 500);
        setLocationRelativeTo(null);

        JPanel contenitore = new JPanel(new BorderLayout(10, 20));
        contenitore.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel parteAlta = new JPanel(new GridLayout(5, 2, 10, 15));

        JLabel labelNome = new JLabel("Nome:");
        JLabel labelCognome = new JLabel("Cognome:");
        JLabel labelCodiceFiscale = new JLabel("Codice fiscale:");
        JLabel labelDataNascita = new JLabel("Data nascita (AAAA-MM-GG es: 1990-03-19): ");

        campoNome = new JTextField();
        campoCognome = new JTextField();
        campoCodiceFiscale = new JTextField();
        campoDataNascita = new JTextField();

        bottoneInserisci = new JButton("Inserisci degente");

        parteAlta.add(labelNome);
        parteAlta.add(campoNome);

        parteAlta.add(labelCognome);
        parteAlta.add(campoCognome);

        parteAlta.add(labelCodiceFiscale);
        parteAlta.add(campoCodiceFiscale);

        parteAlta.add(labelDataNascita);
        parteAlta.add(campoDataNascita);

        parteAlta.add(new JLabel());
        parteAlta.add(bottoneInserisci);

        contenitore.add(parteAlta, BorderLayout.NORTH);

        String[] colonne = {"Nome", "Cognome", "Codice fiscale", "Data nascita"};
        DefaultTableModel modello = new DefaultTableModel(colonne, 0);
        tabella = new JTable(modello);

        JScrollPane scroll = new JScrollPane(tabella);
        contenitore.add(scroll, BorderLayout.CENTER);

        add(contenitore);

        bottoneInserisci.addActionListener(e -> inserisciDegente());

        aggiornaTabella();

        setVisible(true);
    }

    private void inserisciDegente() {
        String nome = campoNome.getText();
        String cognome = campoCognome.getText();
        String codiceFiscale = campoCodiceFiscale.getText();
        String dataNascita = campoDataNascita.getText();

        String messaggio = controller.registraDegente(
                nome,
                cognome,
                codiceFiscale,
                dataNascita
        );

        JOptionPane.showMessageDialog(this, messaggio);

        if (messaggio.startsWith("OK")) {
            aggiornaTabella();
            pulisciCampi();
        }
    }

    private void aggiornaTabella() {
        DefaultTableModel modello = (DefaultTableModel) tabella.getModel();
        modello.setRowCount(0);

        Object[][] dati = controller.recuperaTabellaDegenti();

        for (int i = 0; i < dati.length; i++) {
            modello.addRow(dati[i]);
        }
    }

    private void pulisciCampi() {
        campoNome.setText("");
        campoCognome.setText("");
        campoCodiceFiscale.setText("");
        campoDataNascita.setText("");
    }
}