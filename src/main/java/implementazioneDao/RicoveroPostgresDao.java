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
 * implementazione specifica per postgresql dell'interfaccia ricoverodao.
 * contiene le query sql per salvare, aggiornare e leggere i ricoveri dei pazienti dal database.
 */
public class RicoveroPostgresDao implements RicoveroDAO {

    @Override
    public void save(Ricovero r) throws SQLException {
        // query per inserire un nuovo ricovero specificando i riferimenti esterni e le date
        String sql = "INSERT INTO ricovero("
                + "paziente_cf, "
                + "letto_codice, "
                + "data_ora_inizio, "
                + "data_ora_dimissioni_previste, "
                + "data_ora_dimissione_effettuate"
                + ") VALUES(?, ?, ?, ?, ?)";

        // apre la connessione sicura
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta il codice fiscale del paziente e il codice del letto
            ps.setString(1, r.getPaziente().getCodiceFiscale());
            ps.setString(2, r.getLetto().getCodice());

            // gestisce la conversione delle date con orario da formato java a formato sql timestamp
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

            // la dimissione effettiva all'inizio è solitamente nulla
            if (r.getDataOraDimissioneEffettuate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(r.getDataOraDimissioneEffettuate()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            // esegue l'inserimento
            ps.executeUpdate();
        }
    }

    @Override
    public Ricovero findByPazienteAndDataInizio(String codiceFiscale, LocalDateTime inizio) throws SQLException {
        // cerca un ricovero esatto incrociando il codice fiscale del paziente e il momento preciso dell'ammissione
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
        // recupera tutto lo storico dei ricoveri ordinandoli dal più recente al più vecchio
        String sql = "SELECT id, paziente_cf, letto_codice, data_ora_inizio, "
                + "data_ora_dimissioni_previste, data_ora_dimissione_effettuate "
                + "FROM ricovero "
                + "ORDER BY data_ora_inizio DESC";

        // delega l'esecuzione al metodo di appoggio interno
        return eseguiQueryRicoveri(sql);
    }

    @Override
    public List<Ricovero> findRicoveriAttivi() throws SQLException {
        // filtra solo i ricoveri correnti verificando che la data di dimissione effettiva sia ancora vuota
        String sql = "SELECT id, paziente_cf, letto_codice, data_ora_inizio, "
                + "data_ora_dimissioni_previste, data_ora_dimissione_effettuate "
                + "FROM ricovero "
                + "WHERE data_ora_dimissione_effettuate IS NULL "
                + "ORDER BY data_ora_inizio DESC";

        return eseguiQueryRicoveri(sql);
    }

    @Override
    public void update(Ricovero r) throws SQLException {
        // aggiorna i dati di un ricovero in corso usando il codice fiscale e la data di inizio come identificatori unici
        String sql = "UPDATE ricovero "
                + "SET letto_codice = ?, "
                + "data_ora_dimissioni_previste = ?, "
                + "data_ora_dimissione_effettuate = ? "
                + "WHERE paziente_cf = ? "
                + "AND data_ora_inizio = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta i nuovi valori da aggiornare
            ps.setString(1, r.getLetto().getCodice());

            if (r.getDataOraDimissioniPreviste() != null) {
                ps.setTimestamp(2, Timestamp.valueOf(r.getDataOraDimissioniPreviste()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }

            // questo è il campo principale che viene aggiornato quando un paziente viene dimesso
            if (r.getDataOraDimissioneEffettuate() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(r.getDataOraDimissioneEffettuate()));
            } else {
                ps.setNull(3, Types.TIMESTAMP);
            }

            // imposta i parametri della clausola where per trovare la riga esatta
            ps.setString(4, r.getPaziente().getCodiceFiscale());
            ps.setTimestamp(5, Timestamp.valueOf(r.getDataOraInizio()));

            ps.executeUpdate();
        }
    }

    /**
     * metodo di appoggio interno per non ripetere codice. riceve una query sql già pronta
     * la esegue e restituisce direttamente la lista dei ricoveri trovati.
     *
     * @param sql la query da far eseguire a postgres
     * @return la lista dei ricoveri
     * @throws SQLException se c'è un errore nell'esecuzione della query
     */
    private List<Ricovero> eseguiQueryRicoveri(String sql) throws SQLException {
        // prepara la lista vuota
        List<Ricovero> ricoveri = new ArrayList<>();

        // apre la connessione ed esegue la query passata come argomento
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // scorre i risultati e li converte
            while (rs.next()) {
                ricoveri.add(creaRicoveroDaResultSet(rs));
            }
        }

        return ricoveri;
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database
     * e la trasforma in un vero e proprio oggetto ricovero.
     *
     * @param rs il risultato grezzo della query sql
     * @return l'oggetto ricovero pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Ricovero creaRicoveroDaResultSet(ResultSet rs) throws SQLException {

        // ricostruisce l'oggetto paziente associato leggendo il suo codice fiscale
        Paziente paziente = new Paziente();
        paziente.setCodiceFiscale(rs.getString("paziente_cf"));

        // ricostruisce l'oggetto letto associato
        Letto letto = new Letto();
        letto.setCodice(rs.getString("letto_codice"));

        // crea l'oggetto ricovero principale e gli collega le entità dipendenti
        Ricovero ricovero = new Ricovero();
        ricovero.setPaziente(paziente);
        ricovero.setLetto(letto);

        // estrae e converte i tre campi temporali da timestamp sql a localdatetime java
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