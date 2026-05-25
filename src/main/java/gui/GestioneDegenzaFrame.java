package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

public class GestioneDegenzaFrame extends JFrame {
    private Controller controller;
    private JTextField campoDocumento, campoSettore, campoPosto;
    private JButton bottoneAssegna;

    public GestioneDegenzaFrame(Controller controller) {
        this.controller = controller;
        setTitle("gestione degenze");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel labelDocumento = new JLabel("documento degente");
        JLabel labelSettore = new JLabel("settore");
        JLabel labelPosto = new JLabel("posto letto");
        campoDocumento = new JTextField();
        campoSettore = new JTextField();
        campoPosto = new JTextField();
        bottoneAssegna = new JButton("assegna degenza");

        pannello.add(labelDocumento);
        pannello.add(campoDocumento);
        pannello.add(labelSettore);
        pannello.add(campoSettore);
        pannello.add(labelPosto);
        pannello.add(campoPosto);
        pannello.add(new JLabel());
        pannello.add(bottoneAssegna);
        add(pannello);

        bottoneAssegna.addActionListener(e -> {
            String documento = campoDocumento.getText();
            String settore = campoSettore.getText();
            String posto = campoPosto.getText();
            controller.registraDegenza(documento, settore, posto);
            JOptionPane.showMessageDialog(null, "degenza salvata");
            // opzionale: pulisci campi
            campoDocumento.setText("");
            campoSettore.setText("");
            campoPosto.setText("");
        });
        setVisible(true);
    }
}