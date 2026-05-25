package model;

import java.util.ArrayList;
import java.util.List;

public class Stanza {
    private String nome;
    private List<Letto> letti = new ArrayList<>();
    private Reparto reparto;  // molti a uno: una stanza appartiene a un reparto

    // getter e setter
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