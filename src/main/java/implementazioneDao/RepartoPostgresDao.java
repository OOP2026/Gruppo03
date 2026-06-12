package implementazioneDao;

import dao.RepartoDAO;
import database_connection.ConnessioneDatabase;
import model.Reparto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * implementazione specifica per postgresql dell'interfaccia repartodao.
 * contiene le query sql per salvare e cercare i reparti nel database.
 */
public class RepartoPostgresDao implements RepartoDAO {

    @Override
    public void save(Reparto r) throws SQLException {
        String sql = "INSERT INTO reparto(nome) "
                + "VALUES(?) "
                + "ON CONFLICT(nome) DO NOTHING";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getNome());
            ps.executeUpdate();
        }
    }

    @Override
    public Reparto findByNome(String nome) throws SQLException {
        String sql = "SELECT nome FROM reparto WHERE nome = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reparto r = new Reparto();
                    r.setNome(rs.getString("nome"));
                    return r;
                }
            }
        }

        return null;
    }

    @Override
    public List<Reparto> findAll() throws SQLException {
        String sql = "SELECT nome FROM reparto ORDER BY nome";

        List<Reparto> reparti = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reparto r = new Reparto();
                r.setNome(rs.getString("nome"));
                reparti.add(r);
            }
        }

        return reparti;
    }
}