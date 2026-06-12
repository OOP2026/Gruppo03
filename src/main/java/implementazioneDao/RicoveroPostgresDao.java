package implementazioneDao;

import dao.RicoveroDAO;
import database_connection.ConnessioneDatabase;
import model.Letto;
import model.Paziente;
import model.Ricovero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO PostgreSQL per la tabella ricovero.
 *
 * Tabella:
 * ricovero(
 * id,
 * paziente_cf,
 * letto_codice,
 * data_ora_inizio,
 * data_ora_dimissioni_previste,
 * data_ora_dimissione_effettuate
 * )
 */
public class RicoveroPostgresDao implements RicoveroDAO {

    @Override
    public void save(Ricovero r) throws SQLException {
        String sql = "INSERT INTO ricovero("
                + "paziente_cf, "
                + "letto_codice, "
                + "data_ora_inizio, "
                + "data_ora_dimissioni_previste, "
                + "data_ora_dimissione_effettuate"
                + ") VALUES(?, ?, ?, ?, ?)";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getPaziente().getCodiceFiscale());
            ps.setString(2, r.getLetto().getCodice());

            if (r.getDataOraInizio() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(r.getDataOraInizio()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            if (r.getDataOraDimissioniPreviste() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(r.getDataOraDimissioniPreviste()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            if (r.getDataOraDimissioneEffettuate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(r.getDataOraDimissioneEffettuate()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.executeUpdate();
        }
    }

    @Override
    public Ricovero findByPazienteAndDataInizio(String codiceFiscale, LocalDateTime inizio) throws SQLException {
        String sql = "SELECT id, paziente_cf, letto_codice, data_ora_inizio, "
                + "data_ora_dimissioni_previste, data_ora_dimissione_effettuate "
                + "FROM ricovero "
                + "WHERE paziente_cf = ? "
                + "AND data_ora_inizio = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, codiceFiscale);
            ps.setTimestamp(2, Timestamp.valueOf(inizio));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return creaRicoveroDaResultSet(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Ricovero> findAll() throws SQLException {
        String sql = "SELECT id, paziente_cf, letto_codice, data_ora_inizio, "
                + "data_ora_dimissioni_previste, data_ora_dimissione_effettuate "
                + "FROM ricovero "
                + "ORDER BY data_ora_inizio DESC";

        return eseguiQueryRicoveri(sql);
    }

    @Override
    public List<Ricovero> findRicoveriAttivi() throws SQLException {
        String sql = "SELECT id, paziente_cf, letto_codice, data_ora_inizio, "
                + "data_ora_dimissioni_previste, data_ora_dimissione_effettuate "
                + "FROM ricovero "
                + "WHERE data_ora_dimissione_effettuate IS NULL "
                + "ORDER BY data_ora_inizio DESC";

        return eseguiQueryRicoveri(sql);
    }

    @Override
    public void update(Ricovero r) throws SQLException {
        String sql = "UPDATE ricovero "
                + "SET letto_codice = ?, "
                + "data_ora_dimissioni_previste = ?, "
                + "data_ora_dimissione_effettuate = ? "
                + "WHERE paziente_cf = ? "
                + "AND data_ora_inizio = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getLetto().getCodice());

            if (r.getDataOraDimissioniPreviste() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(r.getDataOraDimissioniPreviste()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            if (r.getDataOraDimissioneEffettuate() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(r.getDataOraDimissioneEffettuate()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            ps.setString(4, r.getPaziente().getCodiceFiscale());
            ps.setTimestamp(5, Timestamp.valueOf(r.getDataOraInizio()));

            ps.executeUpdate();
        }
    }

    private List<Ricovero> eseguiQueryRicoveri(String sql) throws SQLException {
        List<Ricovero> ricoveri = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ricoveri.add(creaRicoveroDaResultSet(rs));
            }
        }

        return ricoveri;
    }

    private Ricovero creaRicoveroDaResultSet(ResultSet rs) throws SQLException {
        Paziente paziente = new Paziente();
        paziente.setCodiceFiscale(rs.getString("paziente_cf"));

        Letto letto = new Letto();
        letto.setCodice(rs.getString("letto_codice"));

        Ricovero ricovero = new Ricovero();
        ricovero.setPaziente(paziente);
        ricovero.setLetto(letto);

        Timestamp inizio = rs.getTimestamp("data_ora_inizio");
        if (inizio != null) {
            ricovero.setDataOraInizio(inizio.toLocalDateTime());
        }

        Timestamp dimissioniPreviste = rs.getTimestamp("data_ora_dimissioni_previste");
        if (dimissioniPreviste != null) {
            ricovero.setDataOraDimissioniPreviste(dimissioniPreviste.toLocalDateTime());
        }

        Timestamp dimissioneEffettuata = rs.getTimestamp("data_ora_dimissione_effettuate");
        if (dimissioneEffettuata != null) {
            ricovero.setDataOraDimissioneEffettuate(dimissioneEffettuata.toLocalDateTime());
        }

        return ricovero;
    }
}