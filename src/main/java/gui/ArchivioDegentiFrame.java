package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        JPanel contenitore = new JPanel(new BorderLayout(10, 10));
        contenitore.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel parteAlta = new JPanel(new GridLayout(4, 2, 10, 10));
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
        parteAlta.add(new JLabel());
        parteAlta.add(bottoneInserisci);
        contenitore.add(parteAlta, BorderLayout.NORTH);

        String[] colonne = {"Nome", "Cognome", "Documento"};
        DefaultTableModel modello = new DefaultTableModel(colonne, 0);
        tabella = new JTable(modello);
        JScrollPane scroll = new JScrollPane(tabella);
        contenitore.add(scroll, BorderLayout.CENTER);
        add(contenitore);

        bottoneInserisci.addActionListener(e -> {
            String nome = campoNome.getText();
            String cognome = campoCognome.getText();
            String documento = campoDocumento.getText();
            if (nome.isEmpty() || cognome.isEmpty() || documento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi");
                return;
            }
            controller.registraDegente(nome, cognome, documento);
            aggiornaTabella();
            campoNome.setText("");
            campoCognome.setText("");
            campoDocumento.setText("");
            JOptionPane.showMessageDialog(null, "Degente registrato");
        });

        aggiornaTabella();
        setVisible(true);
    }

    private void aggiornaTabella() {
        DefaultTableModel modello = (DefaultTableModel) tabella.getModel();
        modello.setRowCount(0);
        Object[][] dati = controller.recuperaTabellaDegenti();
        for (Object[] riga : dati) {
            modello.addRow(riga);
        }
    }
}