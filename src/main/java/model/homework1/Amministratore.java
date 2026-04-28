package model.homework1;

public class Amministratore extends Utente {
    private String username;

    // Metodi indicati nel tuo diagramma
    public boolean registraPaziente(Paziente p) {
        return true;
    }

    public boolean registraRicovero(Ricovero r, Letto l) {
        return false;
    }

    public String cercaLettoDisponibile(Reparto rep) {
        return "";
    }

}