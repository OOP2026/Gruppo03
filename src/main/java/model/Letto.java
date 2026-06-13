package model;

/**
 * rappresenta un posto letto fisico all'interno dell'ospedale.
 * ogni letto ha un codice univoco ed è collocato in una specifica stanza.
 */
public class Letto {

    // campi principali della classe

    private String codice;
    private Stanza stanza;

    // costruttori

    /**
     * costruttore vuoto, necessario per le operazioni di popolamento da database.
     */
    public Letto() {}

    /**
     * crea un nuovo posto letto specificando il codice e la stanza di appartenenza.
     *
     * @param codice il codice identificativo del letto
     * @param stanza l'oggetto stanza in cui il letto è posizionato
     */
    public Letto(String codice, Stanza stanza) {
        this.codice = codice;
        this.stanza = stanza;
    }

    // metodi getter e setter per leggere e modificare i dati

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Stanza getStanza() {
        return stanza;
    }

    public void setStanza(Stanza stanza) {
        this.stanza = stanza;
    }
}