package model;

public class Letto {
    private String codice;
    private Stanza stanza;

    // costruttore vuoto
    public Letto() {}

    public Letto(String codice, Stanza stanza) {
        this.codice = codice;
        this.stanza = stanza;
    }

    // getter e setter
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