package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.Amministratore;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * implementazione specifica per postgresql dell'interfaccia utentedao.
 * contiene le query sql per salvare, cercare e verificare l'accesso degli utenti al database.
 */
public class UtentePostgresDao implements UtenteDAO {

    @Override
    public void save(Utente u) throws SQLException {
        // query di inserimento per un nuovo utente. se il login esiste già aggiorna i dati correnti
        String sql = "INSERT INTO utente(login, password, tipo, username) "
                + "VALUES(?, ?, ?, ?) "
                + "ON CONFLICT(login) DO UPDATE SET "
                + "password = EXCLUDED.password, "
                + "tipo = EXCLUDED.tipo, "
                + "username = EXCLUDED.username";

        // apre la connessione sicura al database
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta le credenziali di base
            ps.setString(1, u.getLogin());
            ps.setString(2, u.getPassword());

            // verifica il ruolo dell'utente per differenziare il salvataggio
            if (u instanceof Amministratore) {
                // se è un amministratore salva anche il suo nome utente specifico
                Amministratore amministratore = (Amministratore) u;
                ps.setString(3, "AMMINISTRATORE");
                ps.setString(4, amministratore.getUsername());
            } else {
                // se è un utente base lascia il campo specifico nullo
                ps.setString(3, "UTENTE");
                ps.setNull(4, Types.VARCHAR);
            }

            // esegue l'operazione sul database
            ps.executeUpdate();
        }
    }

    @Override
    public Utente findByLogin(String login) throws SQLException {
        // query per cercare un utente usando il suo identificativo univoco di accesso
        String sql = "SELECT login, password, tipo, username "
                + "FROM utente "
                + "WHERE login = ?";

        // apre la connessione ed prepara la ricerca
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);

            // esegue l'interrogazione e se trova una corrispondenza costruisce l'oggetto java
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creaUtenteDaResultSet(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Utente> findAll() throws SQLException {
        // query per recuperare la lista di tutti gli utenti registrati ordinati alfabeticamente
        String sql = "SELECT login, password, tipo, username "
                + "FROM utente "
                + "ORDER BY login";

        // prepara la lista vuota
        List<Utente> utenti = new ArrayList<>();

        // apre la connessione ed esegue direttamente la query a cascata
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // scorre i risultati e li aggiunge alla lista convertendoli uno per uno
            while (rs.next()) {
                utenti.add(creaUtenteDaResultSet(rs));
            }
        }

        return utenti;
    }

    @Override
    public boolean checkCredentials(String login, String password) throws SQLException {
        // query di sicurezza per verificare che le credenziali inserite siano corrette e appartengano a un amministratore
        // il select 1 è una tecnica per ottimizzare la query quando serve solo sapere se la riga esiste
        String sql = "SELECT 1 "
                + "FROM utente "
                + "WHERE login = ? "
                + "AND password = ? "
                + "AND tipo = 'AMMINISTRATORE'";

        // apre la connessione e imposta i parametri
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);

            // esegue la ricerca. restituisce vero se la combinazione esiste nel database
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * metodo di appoggio interno. prende una riga dal database e crea l'oggetto
     * utente giusto in base al tipo salvato.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto utente specifico creato
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Utente creaUtenteDaResultSet(ResultSet rs) throws SQLException {

        // estrae i singoli campi dalla riga del database
        String login = rs.getString("login");
        String password = rs.getString("password");
        String tipo = rs.getString("tipo");
        String username = rs.getString("username");

        // usa il campo tipo per decidere quale specifica classe java istanziare
        if ("AMMINISTRATORE".equals(tipo)) {
            return new Amministratore(login, password, username);
        }

        // classe di ripiego per utenti generici
        return new Utente(login, password);
    }
}