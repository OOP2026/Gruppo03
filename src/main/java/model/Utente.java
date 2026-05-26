package model;
// rappresenta un utente generico del sistema con credenziali di accesso
public class Utente {
    private String login;
    private String password;

    // costruttore per inizializzare le credenziali dell'utente
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // verifica se le credenziali inserite corrispondono a quelle salvate
    public boolean login(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    // metodi getter e setter per login e password
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