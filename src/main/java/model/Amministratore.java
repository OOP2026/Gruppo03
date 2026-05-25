package model;

public class Amministratore extends Utente {
    private String username;

    public Amministratore(String login, String password, String username) {
        super(login, password);
        this.username = username;
    }

    public boolean registraPaziente(Paziente p) {
        // logica da implementare
        return true;
    }

    public boolean registraRicovero(Ricovero r, Letto l) {
        // logica da implementare
        return false;
    }

    public String cercaLettoDisponibile(Reparto rep) {
        // logica da implementare
        return "";
    }

    // getter e setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}