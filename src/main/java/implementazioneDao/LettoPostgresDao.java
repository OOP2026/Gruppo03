package implementazioneDao;

import dao.LettoDAO;
import database_connection.ConnessioneDatabase;
import model.Letto;
import model.Reparto;
import model.Stanza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * implementazione specifica per postgresql dell'interfaccia lettodao.
 * contiene le query sql vere e proprie per salvare o leggere i letti dal database.
 */
public class LettoPostgresDao implements LettoDAO {

    @Override
    public void save(Letto l) throws SQLException {
        String sql = "INSERT INTO letto(codice, stanza_nome, reparto_nome) "
                + "VALUES(?, ?, ?) "
                + "ON CONFLICT(codice) DO NOTHING";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getCodice());

            if (l.getStanza() != null) {
                ps.setString(2, l.getStanza().getNome());

                if (l.getStanza().getReparto() != null) {
                    ps.setString(3, l.getStanza().getReparto().getNome());
                } else {
                    ps.setString(3, null);
                }
            } else {
                ps.setString(2, null);
                ps.setString(3, null);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public Letto findByCodice(String codice) throws SQLException {
        String sql = "SELECT codice, stanza_nome, reparto_nome "
                + "FROM letto "
                + "WHERE codice = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, codice);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creaLettoDaResultSet(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Letto> findAll() throws SQLException {
        String sql = "SELECT codice, stanza_nome, reparto_nome "
                + "FROM letto "
                + "ORDER BY reparto_nome, stanza_nome, codice";

        List<Letto> letti = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                letti.add(creaLettoDaResultSet(rs));
            }
        }

        return letti;
    }

    @Override
    public List<Letto> findLiberiByReparto(String nomeReparto) throws SQLException {
        String sql = "SELECT l.codice, l.stanza_nome, l.reparto_nome "
                + "FROM letto l "
                + "WHERE l.reparto_nome = ? "
                + "AND NOT EXISTS ( "
                + "    SELECT 1 "
                + "    FROM ricovero r "
                + "    WHERE r.letto_codice = l.codice "
                + "    AND r.data_ora_dimissione_effettuate IS NULL "
                + ") "
                + "ORDER BY l.stanza_nome, l.codice";

        List<Letto> lettiLiberi = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nomeReparto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lettiLiberi.add(creaLettoDaResultSet(rs));
                }
            }
        }

        return lettiLiberi;
    }

    @Override
    public void update(Letto l) throws SQLException {
        String sql = "UPDATE letto "
                + "SET stanza_nome = ?, reparto_nome = ? "
                + "WHERE codice = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getStanza().getNome());
            ps.setString(2, l.getStanza().getReparto().getNome());
            ps.setString(3, l.getCodice());

            ps.executeUpdate();
        }
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database (resultset)
     * e la trasforma in un vero e proprio oggetto letto utilizzabile da java.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto letto pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Letto creaLettoDaResultSet(ResultSet rs) throws SQLException {
        Reparto reparto = new Reparto();
        reparto.setNome(rs.getString("reparto_nome"));

        Stanza stanza = new Stanza();
        stanza.setNome(rs.getString("stanza_nome"));
        stanza.setReparto(reparto);

        Letto letto = new Letto();
        letto.setCodice(rs.getString("codice"));
        letto.setStanza(stanza);

        return letto;
    }
}