package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

public class AccessoFrame extends JFrame {
    private Controller controller;
    private JTextField campoUtente;
    private JPasswordField campoPassword;
    private JButton pulsanteAccesso;

    public AccessoFrame(Controller controller) {
        this.controller = controller;
        setTitle("Login gestione pazienti");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel pannello = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel labelUtente = new JLabel("utente");
        JLabel labelPassword = new JLabel("password");
        campoUtente = new JTextField();
        campoPassword = new JPasswordField();
        pulsanteAccesso = new JButton("entra");

        pannello.add(labelUtente);
        pannello.add(campoUtente);
        pannello.add(labelPassword);
        pannello.add(campoPassword);
        pannello.add(new JLabel());
        pannello.add(pulsanteAccesso);
        add(pannello);

        pulsanteAccesso.addActionListener(e -> {
            String utente = campoUtente.getText();
            String password = new String(campoPassword.getPassword());
            if (controller.controllaAccesso(utente, password)) {
                JOptionPane.showMessageDialog(null, "accesso effettuato");
                new DashboardFrame(controller);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "dati errati");
            }
        });
        setVisible(true);
    }
}