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
 * schermata per registrare un nuovo ricovero.
 * fa scegliere all'operatore un paziente già salvato, un reparto e gli assegna un letto libero.
 */
public class GestioneDegenzaFrame extends JFrame {

    // riferimenti per la logica di base e l'aggiornamento della vista principale
    private final Controller controller;
    private final DashboardFrame dashboardFrame;

    // menu a tendina per la selezione dei parametri del ricovero
    private JComboBox<String> comboPazienti;
    private JComboBox<String> comboReparti;
    private JComboBox<String> comboLetti;

    // pulsante per confermare l'operazione
    private JButton bottoneRegistra;

    /**
     * crea la finestra per gestire le degenze e imposta tutti i menu a tendina.
     *
     * @param controller il controller centrale per comunicare con il database
     * @param dashboardFrame la finestra del menu principale passata per aggiornarle i contatori
     */
    public GestioneDegenzaFrame(Controller controller, DashboardFrame dashboardFrame) {

        // salva i riferimenti agli oggetti passati
        this.controller = controller;
        this.dashboardFrame = dashboardFrame;

        // configurazione iniziale della finestra
        setTitle("Gestione Degenze");
        setSize(650, 300);
        setLocationRelativeTo(null);

        // creazione del pannello a griglia per allineare le etichette ai menu a tendina
        JPanel pannello = new JPanel(new GridLayout(4, 2, 10, 15));
        pannello.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // inizializzazione dei componenti grafici di selezione
        comboPazienti = new JComboBox<>();
        comboReparti = new JComboBox<>();
        comboLetti = new JComboBox<>();

        bottoneRegistra = new JButton("Registra ricovero");

        // inserimento dei componenti nel pannello seguendo l'ordine della griglia
        pannello.add(new JLabel("Paziente:"));
        pannello.add(comboPazienti);

        pannello.add(new JLabel("Reparto:"));
        pannello.add(comboReparti);

        pannello.add(new JLabel("Letto libero:"));
        pannello.add(comboLetti);

        // inserimento di uno spazio vuoto per spostare il bottone a destra
        pannello.add(new JLabel());
        pannello.add(bottoneRegistra);

        add(pannello);

        // precompila i menu a tendina chiedendo i dati iniziali al database
        caricaPazienti();
        caricaReparti();
        aggiornaLettiLiberi();

        // aggiunge l'azione automatica che aggiorna i letti quando si cambia reparto
        comboReparti.addActionListener(e -> aggiornaLettiLiberi());

        // aggiunge l'azione finale di salvataggio al clic del bottone
        bottoneRegistra.addActionListener(e -> registraRicovero());

        // rende visibile l'interfaccia
        setVisible(true);
    }

    /**
     * chiede al controller la lista dei pazienti e la inserisce nel menu a tendina.
     */
    private void caricaPazienti() {
        // svuota eventuali dati vecchi
        comboPazienti.removeAllItems();

        // recupera l'elenco dei pazienti già formattato come stringa dal controller
        String[] pazienti = controller.recuperaPazientiPerCombo();

        // popola il menu voce per voce
        for (int i = 0; i < pazienti.length; i++) {
            comboPazienti.addItem(pazienti[i]);
        }
    }

    /**
     * chiede al controller i reparti dell'ospedale e li mette nella tendina.
     */
    private void caricaReparti() {
        // ripulisce la tendina
        comboReparti.removeAllItems();

        // recupera l'elenco dei nomi dei reparti
        String[] reparti = controller.recuperaRepartiPerCombo();

        // aggiunge le opzioni disponibili alla tendina
        for (int i = 0; i < reparti.length; i++) {
            comboReparti.addItem(reparti[i]);
        }
    }

    /**
     * guarda quale reparto è stato scelto e ricarica la tendina dei letti
     * mostrando solo quelli vuoti in quel reparto.
     */
    private void aggiornaLettiLiberi() {
        // azzera le vecchie opzioni ogni volta che cambia il reparto
        comboLetti.removeAllItems();

        // legge la voce attualmente selezionata nel menu dei reparti
        String repartoSelezionato = (String) comboReparti.getSelectedItem();

        // interrompe l'operazione se non c'è nessun reparto valido
        if (repartoSelezionato == null) {
            return;
        }

        // interroga il database per avere i posti letto non assegnati di quel reparto specifico
        String[] lettiLiberi = controller.recuperaLettiLiberiPerReparto(repartoSelezionato);

        // riempie l'ultima tendina
        for (int i = 0; i < lettiLiberi.length; i++) {
            comboLetti.addItem(lettiLiberi[i]);
        }
    }

    /**
     * prende i dati scelti nei menu a tendina e chiede al controller di salvare il ricovero.
     * se va a buon fine, aggiorna i letti liberi e i numeri sulla dashboard.
     */
    private void registraRicovero() {
        // estrae le tre selezioni che l'utente ha fatto a schermo
        String pazienteSelezionato = (String) comboPazienti.getSelectedItem();
        String repartoSelezionato = (String) comboReparti.getSelectedItem();
        String lettoSelezionato = (String) comboLetti.getSelectedItem();

        // validazioni iniziali per prevenire invii a vuoto
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

        // isola la parte della stringa che serve al database
        String codiceFiscale = estraiCodiceFiscale(pazienteSelezionato);

        // inoltra la richiesta di inserimento al controller centrale
        String messaggio = controller.registraDegenza(
                codiceFiscale,
                repartoSelezionato,
                lettoSelezionato
        );

        // mostra l'esito dell'operazione di database in un popup a schermo
        JOptionPane.showMessageDialog(this, messaggio);

        // se il ricovero è stato salvato correttamente aggiorna le schermate per riflettere i cambiamenti
        if (messaggio.startsWith("OK")) {
            // toglie il letto appena occupato dalla tendina
            aggiornaLettiLiberi();

            // forza la finestra del menu principale a ricalcolare i numeri dei posti liberi
            if (dashboardFrame != null) {
                dashboardFrame.aggiornaDashboard();
            }
        }
    }

    /**
     * prende la stringa del menu a tendina e la divide
     * per restituire solo il codice fiscale da mandare al database.
     *
     * @param testoCombo il testo intero selezionato nel menu
     * @return solo il codice fiscale tagliato e isolato
     */
    private String estraiCodiceFiscale(String testoCombo) {
        // divide la riga usando il trattino usato come separatore durante la creazione della stringa
        String[] parti = testoCombo.split(" - ");

        // restituisce il primo blocco di testo che corrisponde esattamente al codice fiscale
        if (parti.length > 0) {
            return parti[0];
        }

        // salvataggio d'emergenza nel caso il formato non fosse quello atteso
        return testoCombo;
    }
}