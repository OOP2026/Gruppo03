package model;

/**
 * rappresenta un posto letto fisico all'interno dell'ospedale.
 * ogni letto ha un codice univoco ed è collocato in una specifica stanza.
 */
public class Letto {
    private String codice;
    private Stanza stanza;

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