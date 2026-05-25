package model;

import java.time.LocalDate;

public class Paziente {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private LocalDate dataNascita;

    // costruttore vuoto (utile per creazione con setter)
    public Paziente() {}

    // costruttore con parametri (opzionale)
    public Paziente(String nome, String cognome, String codiceFiscale, LocalDate dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.dataNascita = dataNascita;
    }

    // getter e setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }
}