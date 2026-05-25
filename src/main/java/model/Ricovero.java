package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ricovero {
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraDimissioniPreviste;
    private LocalDateTime dataOraDimissioneEffettuate;
    private Paziente paziente;
    private Letto letto;
    private List<Prestazione> prestazioni = new ArrayList<>();

    // costruttore vuoto
    public Ricovero() {}

    // costruttore con parametri base
    public Ricovero(Paziente paziente, Letto letto, LocalDateTime inizio) {
        this.paziente = paziente;
        this.letto = letto;
        this.dataOraInizio = inizio;
    }

    // getter e setter
    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraDimissioniPreviste() {
        return dataOraDimissioniPreviste;
    }

    public void setDataOraDimissioniPreviste(LocalDateTime dataOraDimissioniPreviste) {
        this.dataOraDimissioniPreviste = dataOraDimissioniPreviste;
    }

    public LocalDateTime getDataOraDimissioneEffettuate() {
        return dataOraDimissioneEffettuate;
    }

    public void setDataOraDimissioneEffettuate(LocalDateTime dataOraDimissioneEffettuate) {
        this.dataOraDimissioneEffettuate = dataOraDimissioneEffettuate;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Letto getLetto() {
        return letto;
    }

    public void setLetto(Letto letto) {
        this.letto = letto;
    }

    public List<Prestazione> getPrestazioni() {
        return prestazioni;
    }

    public void setPrestazioni(List<Prestazione> prestazioni) {
        this.prestazioni = prestazioni;
    }
}