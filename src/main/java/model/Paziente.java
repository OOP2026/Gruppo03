package model;

import java.time.LocalDate;

/**
 * rappresenta un paziente registrato nell'archivio dell'ospedale.
 * contiene i dati anagrafici base necessari per identificare una persona.
 */
public class Paziente {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private LocalDate dataNascita;

    /**
     * costruttore vuoto, utile per creare un oggetto paziente e settare i dati in un secondo momento.
     */
    public Paziente() {}

    /**
     * crea un nuovo paziente con tutti i suoi dati anagrafici già pronti.
     *
     * @param nome          il nome di battesimo
     * @param cognome       il cognome
     * @param codiceFiscale il codice fiscale univoco
     * @param dataNascita   la data di nascita in formato localdate
     */
    public Paziente(String nome, String cognome, String codiceFiscale, LocalDate dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.dataNascita = dataNascita;
    }

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