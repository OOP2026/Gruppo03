package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * rappresenta un ricovero ospedaliero.
 * gestisce il periodo di degenza di un paziente, tracciando le date chiave
 * e le prestazioni mediche effettuate durante la sua permanenza in un letto specifico.
 */
public class Ricovero {
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraDimissioniPreviste;
    private LocalDateTime dataOraDimissioneEffettuate;
    private Paziente paziente;
    private Letto letto;
    private List<Prestazione> prestazioni = new ArrayList<>();

    /**
     * costruttore vuoto per permettere la creazione dell'oggetto e il popolamento da db.
     */
    public Ricovero() {}

    /**
     * crea un nuovo ricovero definendo il paziente, il letto assegnato e il momento dell'ingresso.
     *
     * @param paziente il paziente che viene ricoverato
     * @param letto    il posto letto assegnato
     * @param inizio   la data e ora dell'ingresso
     */
    public Ricovero(Paziente paziente, Letto letto, LocalDateTime inizio) {
        this.paziente = paziente;
        this.letto = letto;
        this.dataOraInizio = inizio;
    }

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