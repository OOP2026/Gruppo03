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
 * finestra per inserire e visualizzare i pazienti presenti nell'archivio.
 * mostra un modulo di registrazione in alto e una tabella riassuntiva in basso.
 */
public class ArchivioDegentiFrame extends JFrame {

    // riferimento al controller per gestire la logica applicativa
    private final Controller controller;

    // campi di input per i dati del degente
    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoCodiceFiscale;
    private JTextField campoDataNascita;

    // pulsante per confermare l'inserimento
    private JButton bottoneInserisci;

    // tabella per mostrare l'elenco dei degenti salvati
    private JTable tabella;

    /**
     * crea la finestra dell'archivio degenti e disegna tutta l'interfaccia.
     *
     * @param controller il controller centrale per comunicare con il database
     */
    public ArchivioDegentiFrame(Controller controller) {
        this.controller = controller;

        // impostazioni principali della finestra
        setTitle("Archivio Degenti");
        setSize(850, 500);
        setLocationRelativeTo(null);

        // pannello principale che contiene tutto con layout a zone
        JPanel contenitore = new JPanel(new BorderLayout(10, 20));
        contenitore.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // pannello superiore dedicato all'inserimento dei dati
        JPanel parteAlta = new JPanel(new GridLayout(5, 2, 10, 15));

        // etichette descrittive per i campi
        JLabel labelNome = new JLabel("Nome:");
        JLabel labelCognome = new JLabel("Cognome:");
        JLabel labelCodiceFiscale = new JLabel("Codice fiscale:");
        JLabel labelDataNascita = new JLabel("Data nascita:");

        // inizializzazione dei campi di testo vuoti
        campoNome = new JTextField();
        campoCognome = new JTextField();
        campoCodiceFiscale = new JTextField();
        campoDataNascita = new JTextField();

        bottoneInserisci = new JButton("Inserisci degente");

        // aggiunta degli elementi al pannello superiore seguendo la griglia
        parteAlta.add(labelNome);
        parteAlta.add(campoNome);

        parteAlta.add(labelCognome);
        parteAlta.add(campoCognome);

        parteAlta.add(labelCodiceFiscale);
        parteAlta.add(campoCodiceFiscale);

        parteAlta.add(labelDataNascita);
        parteAlta.add(campoDataNascita);

        // spazio vuoto per allineare il bottone a destra
        parteAlta.add(new JLabel());
        parteAlta.add(bottoneInserisci);

        // posiziona il modulo di inserimento nella parte nord della finestra
        contenitore.add(parteAlta, BorderLayout.NORTH);

        // configurazione della tabella per visualizzare i dati in colonna
        String[] colonne = {"Nome", "Cognome", "Codice fiscale", "Data nascita"};
        DefaultTableModel modello = new DefaultTableModel(colonne, 0);
        tabella = new JTable(modello);

        // aggiunge una barra di scorrimento alla tabella per quando ci sono tanti record
        JScrollPane scroll = new JScrollPane(tabella);
        contenitore.add(scroll, BorderLayout.CENTER);

        add(contenitore);

        // collega l'azione del click sul bottone al metodo di salvataggio
        bottoneInserisci.addActionListener(e -> inserisciDegente());

        // carica i dati iniziali dal database per riempire la tabella all'avvio
        aggiornaTabella();

        // rende la finestra visibile
        setVisible(true);
    }

    /**
     * prende i dati scritti nei campi di testo e chiede al controller di salvare il nuovo paziente.
     * se va a buon fine, ricarica la tabella e svuota i campi.
     */
    private void inserisciDegente() {
        // recupero dei testi inseriti dall'utente nei campi
        String nome = campoNome.getText();
        String cognome = campoCognome.getText();
        String codiceFiscale = campoCodiceFiscale.getText();
        String dataNascita = campoDataNascita.getText();

        // invio dei dati al controller per la validazione e il salvataggio
        String messaggio = controller.registraDegente(
                nome,
                cognome,
                codiceFiscale,
                dataNascita
        );

        // mostra un popup grafico con l'esito dell'operazione
        JOptionPane.showMessageDialog(this, messaggio);

        // se l'inserimento ha avuto successo pulisce il form e aggiorna la vista
        if (messaggio.startsWith("OK")) {
            aggiornaTabella();
            pulisciCampi();
        }
    }

    /**
     * cancella i dati vecchi dalla tabella grafica e chiede al controller l'elenco aggiornato dal database
     * per stamparlo a schermo.
     */
    private void aggiornaTabella() {
        // recupera la struttura dati che governa la tabella
        DefaultTableModel modello = (DefaultTableModel) tabella.getModel();

        // svuota la tabella eliminando tutte le righe visibili
        modello.setRowCount(0);

        // richiede al controller la matrice aggiornata con i dati dal database
        Object[][] dati = controller.recuperaTabellaDegenti();

        // inserisce i nuovi dati ricostruendo le righe una per una
        for (int i = 0; i < dati.length; i++) {
            modello.addRow(dati[i]);
        }
    }

    /**
     * svuota tutti i campi di testo dopo che un inserimento è andato a buon fine.
     */
    private void pulisciCampi() {
        // reimposta i campi con una stringa vuota per permettere nuovi inserimenti
        campoNome.setText("");
        campoCognome.setText("");
        campoCodiceFiscale.setText("");
        campoDataNascita.setText("");
    }
}