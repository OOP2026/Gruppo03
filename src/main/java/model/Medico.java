package model;

import java.util.ArrayList;
import java.util.List;

/**
 * rappresenta un medico registrato nel sistema.
 * estende la classe utente e aggiunge informazioni specifiche come la matricola,
 * la specializzazione e i collegamenti a reparti, turni e prestazioni.
 */
public class Medico extends Utente {
    private String matricola;
    private String specializzazione;
    private Reparto reparto;
    private List<Turno> turni = new ArrayList<>();
    private List<Prestazione> prestazioni = new ArrayList<>();

    /**
     * crea un nuovo medico con i dati di accesso e le informazioni base.
     *
     * @param login            il nome utente per il login
     * @param password         la password associata
     * @param matricola        il codice identificativo del medico
     * @param specializzazione la specializzazione medica del dottore
     */
    public Medico(String login, String password, String matricola, String specializzazione) {
        super(login, password);
        this.matricola = matricola;
        this.specializzazione = specializzazione;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public List<Turno> getTurni() {
        return turni;
    }

    public void setTurni(List<Turno> turni) {
        this.turni = turni;
    }

    public List<Prestazione> getPrestazioni() {
        return prestazioni;
    }

    public void setPrestazioni(List<Prestazione> prestazioni) {
        this.prestazioni = prestazioni;
    }
}