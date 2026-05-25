package model;

import java.time.LocalDateTime;

public class Prestazione {
    private String esito;
    private TipoPrestazione tipo;
    private LocalDateTime dataOraInizio;
    private float durata;
    private Ricovero ricovero;
    private Medico medico;

    // getter e setter
    public String getEsito() {
        return esito;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public TipoPrestazione getTipo() {
        return tipo;
    }

    public void setTipo(TipoPrestazione tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public float getDurata() {
        return durata;
    }

    public void setDurata(float durata) {
        this.durata = durata;
    }

    public Ricovero getRicovero() {
        return ricovero;
    }

    public void setRicovero(Ricovero ricovero) {
        this.ricovero = ricovero;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}