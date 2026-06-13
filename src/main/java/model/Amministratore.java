package model;

/**
 * rappresenta un utente con permessi avanzati.
 * oltre alle credenziali base, ha un nome utente visualizzabile
 * e può eseguire operazioni gestionali sul sistema.
 */
public class Amministratore extends Utente {

    // variabile specifica per questa classe che estende l'utente base
    private String username;

    /**
     * crea un nuovo amministratore.
     *
     * @param login il nome utente per il login
     * @param password la password associata
     * @param username il nome reale dell'amministratore
     */
    public Amministratore(String login, String password, String username) {
        // richiama il costruttore della classe padre per impostare le credenziali
        super(login, password);
        // imposta il parametro aggiuntivo specifico di questa classe
        this.username = username;
    }

    // metodi operativi

    // metodo per la registrazione di un nuovo paziente nel sistema
    public boolean registraPaziente(Paziente p) {
        // logica di salvataggio nel database da implementare
        return true;
    }

    // associa un nuovo ricovero a un posto letto specifico
    public boolean registraRicovero(Ricovero r, Letto l) {
        // logica di assegnazione da implementare
        return false;
    }

    // ricerca il primo posto letto libero all'interno di un reparto
    public String cercaLettoDisponibile(Reparto rep) {
        // logica di ricerca nel database da implementare
        return "";
    }

    // metodi getter e setter

    /**
     * restituisce il nome visualizzabile dell'amministratore.
     *
     * @return il nome dell'amministratore
     */
    public String getUsername() {
        return username;
    }

    /**
     * imposta un nuovo nome visualizzabile per l'amministratore.
     *
     * @param username il nuovo nome da impostare
     */
    public void setUsername(String username) {
        this.username = username;
    }
}