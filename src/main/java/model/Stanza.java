package model;

import java.util.ArrayList;
import java.util.List;

// rappresenta una stanza all'interno di un reparto dell'ospedale
public class Stanza {
    private String nome;
    private List<Letto> letti = new ArrayList<>();
    private Reparto reparto;

    // metodi getter e setter per gestire le info della stanza
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Letto> getLetti() {
        return letti;
    }

    public void setLetti(List<Letto> letti) {
        this.letti = letti;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }
}