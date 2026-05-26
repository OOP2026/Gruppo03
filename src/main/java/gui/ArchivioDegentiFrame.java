package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// finestra per l'inserimento e visualizzazione dei degenti
public class ArchivioDegentiFrame extends JFrame {
    private Controller controller;
    private JTextField campoNome, campoCognome, campoDocumento;
    private JButton bottoneInserisci;
    private JTable tabella;

    public ArchivioDegentiFrame(Controller controller) {
        this.controller = controller;
        setTitle("Archivio Degenti");
        setSize(800, 500);
        setLocationRelativeTo(null);
        // pannello principale
        JPanel contenitore = new JPanel(new BorderLayout(10, 20));
        contenitore.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel parteAlta = new JPanel(new GridLayout(4, 2, 10, 15));
        JLabel labelNome = new JLabel("Nome:");
        JLabel labelCognome = new JLabel("Cognome:");
        JLabel labelDocumento = new JLabel("Documento:");
        campoNome = new JTextField();
        campoCognome = new JTextField();
        campoDocumento = new JTextField();
        bottoneInserisci = new JButton("Inserisci degente");

        parteAlta.add(labelNome);
        parteAlta.add(campoNome);
        parteAlta.add(labelCognome);
        parteAlta.add(campoCognome);
        parteAlta.add(labelDocumento);
        parteAlta.add(campoDocumento);
        parteAlta.add(new JLabel()); // spazio vuoto per allineare il bottone a destra
        parteAlta.add(bottoneInserisci);
        contenitore.add(parteAlta, BorderLayout.NORTH);

        // tabella centrale per mostrare l'elenco
        String[] colonne = {"Nome", "Cognome", "Documento"};
        DefaultTableModel modello = new DefaultTableModel(colonne, 0);
        tabella = new JTable(modello);
        JScrollPane scroll = new JScrollPane(tabella);
        contenitore.add(scroll, BorderLayout.CENTER);

        add(contenitore);

        // bottone di inserimento
        bottoneInserisci.addActionListener(e -> {
            String nome = campoNome.getText();
            String cognome = campoCognome.getText();
            String documento = campoDocumento.getText();

            // controllo che i campi non siano vuoti
            if (nome.isEmpty() || cognome.isEmpty() || documento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi");
                return;
            }

            controller.registraDegente(nome, cognome, documento);
            aggiornaTabella();

            // pulisce i campi dopo l'inserimento
            campoNome.setText("");
            campoCognome.setText("");
            campoDocumento.setText("");
            JOptionPane.showMessageDialog(null, "Degente registrato con successo");
        });

        aggiornaTabella();
        setVisible(true);
    }

    // ricarica i dati nella tabella prendendoli dal controller
    private void aggiornaTabella() {
        DefaultTableModel modello = (DefaultTableModel) tabella.getModel();
        modello.setRowCount(0); // svuota la tabella prima di riempirla
        Object[][] dati = controller.recuperaTabellaDegenti();
        for (Object[] riga : dati) {
            modello.addRow(riga);
        }
    }
}