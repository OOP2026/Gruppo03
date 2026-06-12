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
        String sql = "INSERT INTO utente(login, password, tipo, username) "
                + "VALUES(?, ?, ?, ?) "
                + "ON CONFLICT(login) DO UPDATE SET "
                + "password = EXCLUDED.password, "
                + "tipo = EXCLUDED.tipo, "
                + "username = EXCLUDED.username";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getLogin());
            ps.setString(2, u.getPassword());

            if (u instanceof Amministratore) {
                Amministratore amministratore = (Amministratore) u;
                ps.setString(3, "AMMINISTRATORE");
                ps.setString(4, amministratore.getUsername());
            } else {
                ps.setString(3, "UTENTE");
                ps.setNull(4, Types.VARCHAR);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public Utente findByLogin(String login) throws SQLException {
        String sql = "SELECT login, password, tipo, username "
                + "FROM utente "
                + "WHERE login = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);

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
        String sql = "SELECT login, password, tipo, username "
                + "FROM utente "
                + "ORDER BY login";

        List<Utente> utenti = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                utenti.add(creaUtenteDaResultSet(rs));
            }
        }

        return utenti;
    }

    @Override
    public boolean checkCredentials(String login, String password) throws SQLException {
        String sql = "SELECT 1 "
                + "FROM utente "
                + "WHERE login = ? "
                + "AND password = ? "
                + "AND tipo = 'AMMINISTRATORE'";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * metodo di appoggio interno. prende una riga dal database e crea l'oggetto
     * utente giusto (amministratore o utente semplice) in base al tipo salvato.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto utente specifico creato
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Utente creaUtenteDaResultSet(ResultSet rs) throws SQLException {
        String login = rs.getString("login");
        String password = rs.getString("password");
        String tipo = rs.getString("tipo");
        String username = rs.getString("username");

        if ("AMMINISTRATORE".equals(tipo)) {
            return new Amministratore(login, password, username);
        }

        return new Utente(login, password);
    }
}