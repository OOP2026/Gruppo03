package model;

import java.time.LocalDateTime;

/**
 * rappresenta un turno di lavoro programmato per un medico.
 * definisce il giorno della settimana, l'intervallo orario di inizio/fine
 * e il medico a cui è assegnato il turno.
 */
public class Turno {
    private String giornoSettimana;
    private LocalDateTime oraInizio;
    private LocalDateTime oraFine;
    private Medico medico;

    public String getGiornoSettimana() {
        return giornoSettimana;
    }

    public void setGiornoSettimana(String giornoSettimana) {
        this.giornoSettimana = giornoSettimana;
    }

    public LocalDateTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalDateTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalDateTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalDateTime oraFine) {
        this.oraFine = oraFine;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}