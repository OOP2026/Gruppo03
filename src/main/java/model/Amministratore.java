package model;

/**
 * rappresenta un utente con permessi avanzati (amministratore).
 * oltre alle credenziali base, ha un nome utente visualizzabile
 * e può eseguire operazioni gestionali sul sistema.
 */
public class Amministratore extends Utente {
    private String username;

    /**
     * crea un nuovo amministratore.
     *
     * @param login    il nome utente per il login
     * @param password la password associata
     * @param username il nome reale dell'amministratore (es. "Daniele")
     */
    public Amministratore(String login, String password, String username) {
        super(login, password);
        this.username = username;
    }

    // metodo per la registrazione di un nuovo paziente nel sistema
    public boolean registraPaziente(Paziente p) {
        // logica di salvataggio nel database da implementare
        return true;
    }

    // associa un nuovo ricovero a un posto letto
    public boolean registraRicovero(Ricovero r, Letto l) {
        // logica di assegnazione da implementare
        return false;
    }

    // ricerca il primo posto letto libero all'interno di un reparto
    public String cercaLettoDisponibile(Reparto rep) {
        // logica di ricerca nel database da implementare
        return "";
    }

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