package implementazioneDao;

import dao.StanzaDAO;
import database_connection.ConnessioneDatabase;
import model.Reparto;
import model.Stanza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * implementazione specifica per postgresql dell'interfaccia stanzadao.
 * contiene le query sql per salvare e cercare le stanze associate ai vari reparti nel database.
 */
public class StanzaPostgresDao implements StanzaDAO {

    @Override
    public void save(Stanza s) throws SQLException {
        String sql = "INSERT INTO stanza(nome, reparto_nome) "
                + "VALUES(?, ?) "
                + "ON CONFLICT(nome, reparto_nome) DO NOTHING";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getNome());
            ps.setString(2, s.getReparto().getNome());

            ps.executeUpdate();
        }
    }

    @Override
    public List<Stanza> findByReparto(String nomeReparto) throws SQLException {
        String sql = "SELECT nome, reparto_nome "
                + "FROM stanza "
                + "WHERE reparto_nome = ? "
                + "ORDER BY nome";

        List<Stanza> stanze = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nomeReparto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    stanze.add(creaStanzaDaResultSet(rs));
                }
            }
        }

        return stanze;
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database (resultset)
     * e la trasforma in un vero e proprio oggetto stanza, collegandolo al suo reparto.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto stanza pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Stanza creaStanzaDaResultSet(ResultSet rs) throws SQLException {
        Reparto reparto = new Reparto();
        reparto.setNome(rs.getString("reparto_nome"));

        Stanza stanza = new Stanza();
        stanza.setNome(rs.getString("nome"));
        stanza.setReparto(reparto);

        return stanza;
    }
}