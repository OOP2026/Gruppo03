package model;

/**
 * rappresenta un utente generico del sistema con le relative credenziali di accesso.
 * è la classe base da cui possono essere estesi ruoli più specifici come l'amministratore.
 */
public class Utente {

    // credenziali di base per l'autenticazione

    private String login;
    private String password;

    // costruttori

    /**
     * crea un nuovo utente inizializzando le credenziali di accesso.
     *
     * @param login    il nome utente per il login
     * @param password la password associata
     */
    public Utente(String login, String password) {
        // imposta i valori iniziali al momento della creazione
        this.login = login;
        this.password = password;
    }

    // metodi operativi

    /**
     * verifica se le credenziali inserite corrispondono a quelle memorizzate.
     *
     * @param login    la stringa di login da verificare
     * @param password la password da verificare
     * @return true se le credenziali sono corrette, false altrimenti
     */
    public boolean login(String login, String password) {
        // confronta in modo esatto le stringhe passate in input con quelle dell'oggetto
        return this.login.equals(login) && this.password.equals(password);
    }

    // metodi getter e setter per leggere e modificare i dati

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}