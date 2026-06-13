package model;

import java.util.ArrayList;
import java.util.List;

/**
 * rappresenta una stanza fisica all'interno di un reparto dell'ospedale.
 * funge da contenitore per i posti letto e mantiene il riferimento al reparto di appartenenza.
 */
public class Stanza {

    // campi identificativi e riferimenti gerarchici della stanza

    private String nome;
    private List<Letto> letti = new ArrayList<>();
    private Reparto reparto;

    // metodi getter e setter per leggere e modificare i dati

    /**
     * restituisce il nome identificativo della stanza.
     *
     * @return il nome della stanza
     */
    public String getNome() {
        return nome;
    }

    /**
     * imposta il nome della stanza.
     *
     * @param nome il nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * restituisce l'elenco dei letti presenti nella stanza.
     *
     * @return la lista dei letti
     */
    public List<Letto> getLetti() {
        return letti;
    }

    /**
     * associa una lista di letti a questa stanza.
     *
     * @param letti la lista di letti da inserire
     */
    public void setLetti(List<Letto> letti) {
        this.letti = letti;
    }

    /**
     * restituisce il reparto di cui fa parte la stanza.
     *
     * @return l'oggetto reparto
     */
    public Reparto getReparto() {
        return reparto;
    }

    /**
     * associa la stanza a un reparto specifico.
     *
     * @param reparto il reparto di appartenenza
     */
    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }
}