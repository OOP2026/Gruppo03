package implementazioneDao;

import dao.PazienteDAO;
import database_connection.ConnessioneDatabase;
import model.Paziente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO PostgreSQL per la tabella paziente.
 *
 * Tabella:
 * paziente(codice_fiscale, nome, cognome, data_nascita)
 */
public class PazientePostgresDao implements PazienteDAO {

    @Override
    public void save(Paziente p) throws SQLException {
        String sql = "INSERT INTO paziente(codice_fiscale, nome, cognome, data_nascita) "
                + "VALUES(?, ?, ?, ?) "
                + "ON CONFLICT(codice_fiscale) DO NOTHING";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getCodiceFiscale());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getCognome());

            if (p.getDataNascita() != null) {
                ps.setDate(4, Date.valueOf(p.getDataNascita()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public Paziente findByCodiceFiscale(String cf) throws SQLException {
        String sql = "SELECT codice_fiscale, nome, cognome, data_nascita "
                + "FROM paziente "
                + "WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creaPazienteDaResultSet(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Paziente> findAll() throws SQLException {
        String sql = "SELECT codice_fiscale, nome, cognome, data_nascita "
                + "FROM paziente "
                + "ORDER BY cognome, nome";

        List<Paziente> pazienti = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pazienti.add(creaPazienteDaResultSet(rs));
            }
        }

        return pazienti;
    }

    @Override
    public void update(Paziente p) throws SQLException {
        String sql = "UPDATE paziente "
                + "SET nome = ?, cognome = ?, data_nascita = ? "
                + "WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());

            if (p.getDataNascita() != null) {
                ps.setDate(3, Date.valueOf(p.getDataNascita()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, p.getCodiceFiscale());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String cf) throws SQLException {
        String sql = "DELETE FROM paziente WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cf);
            ps.executeUpdate();
        }
    }

    private Paziente creaPazienteDaResultSet(ResultSet rs) throws SQLException {
        Paziente p = new Paziente();

        p.setCodiceFiscale(rs.getString("codice_fiscale"));
        p.setNome(rs.getString("nome"));
        p.setCognome(rs.getString("cognome"));

        Date data = rs.getDate("data_nascita");

        if (data != null) {
            p.setDataNascita(data.toLocalDate());
        }

        return p;
    }
}