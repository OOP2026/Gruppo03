package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * funzionalità:
 * finestra per assegnare una nuova degenza (ricovero) a un degente già registrato.
 * richiede il codice fiscale del paziente, il settore/reparto e opzionalmente il posto letto.
 * poi chiama il controller per salvare il ricovero.
 */
public class GestioneDegenzaFrame extends JFrame {

    private Controller controller;

    private JTextField campoDocumento, campoSettore, campoPosto;

    private JButton bottoneAssegna;

    /**
     * funzionalità:
     * costruttore: costruisce la finestra con tutti i campi e gestisce l'evento.
     */
    public GestioneDegenzaFrame(Controller controller) {

        //salvo il controller per usarlo nell'evento del bottone
        this.controller = controller;

        //titolo della finestra
        setTitle("Gestione Degenze");

        setSize(500, 300);
        setLocationRelativeTo(null);

        // pannello con layout a griglia: 4 righe, 2 colonne, spazi orizzontali 10, verticali 15
        JPanel pannello = new JPanel(new GridLayout(4, 2, 10, 15));
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelDocumento = new JLabel("Documento degente:");
        JLabel labelSettore = new JLabel("Settore:");
        JLabel labelPosto = new JLabel("Posto letto:");


        campoDocumento = new JTextField();
        campoSettore = new JTextField();
        campoPosto = new JTextField();

        bottoneAssegna = new JButton("Assegna degenza");

        pannello.add(labelDocumento);
        pannello.add(campoDocumento);
        pannello.add(labelSettore);
        pannello.add(campoSettore);
        pannello.add(labelPosto);
        pannello.add(campoPosto);
        pannello.add(new JLabel());
        pannello.add(bottoneAssegna);

        add(pannello);

        //definisco l'azione del bottone "assegna degenza"
        bottoneAssegna.addActionListener(e -> {
            // leggo i valori dai campi di testo
            String documento = campoDocumento.getText();
            String settore = campoSettore.getText();
            String posto = campoPosto.getText();

            // chiamo il controller che tenterà di registrare la degenza
            controller.registraDegenza(documento, settore, posto);

            // messaggio di conferma (indipendentemente dal successo o meno della ricerca del letto...)
            JOptionPane.showMessageDialog(null, "Degenza salvata con successo");

            // pulisco i campi per un nuovo inserimento
            campoDocumento.setText("");
            campoSettore.setText("");
            campoPosto.setText("");
        });

        //rendo visibile la finestra
        setVisible(true);
    }
}