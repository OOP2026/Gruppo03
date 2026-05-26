package model;

public class Amministratore extends Utente {
    private String username;

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
    // metodi getter e setter per l'username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}