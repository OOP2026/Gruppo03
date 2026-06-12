package model;

import java.util.ArrayList;
import java.util.List;

/**
 * rappresenta un reparto dell'ospedale.
 * è un contenitore logico che raggruppa una lista di stanze in cui sono distribuiti i letti.
 */
public class Reparto {
    private String nome;
    private List<Stanza> stanze = new ArrayList<>();

    /**
     * restituisce il nome identificativo del reparto.
     *
     * @return il nome del reparto
     */
    public String getNome() {
        return nome;
    }

    /**
     * imposta il nome del reparto.
     *
     * @param nome il nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * restituisce l'elenco di tutte le stanze che fanno parte di questo reparto.
     *
     * @return la lista delle stanze
     */
    public List<Stanza> getStanze() {
        return stanze;
    }

    /**
     * associa una nuova lista di stanze a questo reparto.
     *
     * @param stanze la lista di stanze da assegnare
     */
    public void setStanze(List<Stanza> stanze) {
        this.stanze = stanze;
    }
}