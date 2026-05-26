package model;

import java.util.ArrayList;
import java.util.List;

// rappresenta un reparto dell'ospedale e contiene una lista di stanze
public class Reparto {
    private String nome;
    private List<Stanza> stanze = new ArrayList<>();

    // metodi getter e setter per gestire le info del reparto
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Stanza> getStanze() {
        return stanze;
    }

    public void setStanze(List<Stanza> stanze) {
        this.stanze = stanze;
    }
}