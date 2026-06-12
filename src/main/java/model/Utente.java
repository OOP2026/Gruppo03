package model;

/**
 * rappresenta un utente generico del sistema con le relative credenziali di accesso.
 * è la classe base da cui possono essere estesi ruoli più specifici come l'amministratore.
 */
public class Utente {
    private String login;
    private String password;

    /**
     * crea un nuovo utente inizializzando le credenziali di accesso.
     *
     * @param login    il nome utente per il login
     * @param password la password associata
     */
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * verifica se le credenziali inserite corrispondono a quelle memorizzate.
     *
     * @param login    la stringa di login da verificare
     * @param password la password da verificare
     * @return true se le credenziali sono corrette, false altrimenti
     */
    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

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