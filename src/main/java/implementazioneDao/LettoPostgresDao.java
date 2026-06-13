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
        // query per inserire un letto. se esiste già un letto con lo stesso codice la query viene ignorata
        String sql = "INSERT INTO letto(codice, stanza_nome, reparto_nome) "
                + "VALUES(?, ?, ?) "
                + "ON CONFLICT(codice) DO NOTHING";

        // apre la connessione e prepara l'istruzione sql in modo sicuro
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta il codice del letto come primo parametro
            ps.setString(1, l.getCodice());

            // controlla a cascata che la stanza e il reparto non siano nulli per evitare arresti anomali
            if (l.getStanza() != null) {
                ps.setString(2, l.getStanza().getNome());

                if (l.getStanza().getReparto() != null) {
                    ps.setString(3, l.getStanza().getReparto().getNome());
                } else {
                    ps.setString(3, null);
                }
            } else {
                // se non c'è la stanza lascia vuoti i campi correlati nel database
                ps.setString(2, null);
                ps.setString(3, null);
            }

            // esegue l'inserimento fisico nel database
            ps.executeUpdate();
        }
    }

    @Override
    public Letto findByCodice(String codice) throws SQLException {
        // query per cercare un letto tramite il suo codice identificativo
        String sql = "SELECT codice, stanza_nome, reparto_nome "
                + "FROM letto "
                + "WHERE codice = ?";

        // apre la connessione e prepara la query
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // inserisce il parametro di ricerca
            ps.setString(1, codice);

            // esegue la lettura dei dati e controlla se c'è almeno una riga di risultato
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // se lo trova chiama il metodo di supporto per convertire i dati in oggetto java
                    return creaLettoDaResultSet(rs);
                }
            }
        }

        // ritorna nullo se non trova nessun letto con quel codice
        return null;
    }

    @Override
    public List<Letto> findAll() throws SQLException {
        // query per estrarre tutti i letti ordinandoli in modo pulito
        String sql = "SELECT codice, stanza_nome, reparto_nome "
                + "FROM letto "
                + "ORDER BY reparto_nome, stanza_nome, codice";

        // crea una lista vuota pronta ad accogliere i dati
        List<Letto> letti = new ArrayList<>();

        // apre la connessione e lancia direttamente la lettura
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // scorre tutte le righe trovate e le aggiunge alla lista convertendole una per una
            while (rs.next()) {
                letti.add(creaLettoDaResultSet(rs));
            }
        }

        return letti;
    }

    @Override
    public List<Letto> findLiberiByReparto(String nomeReparto) throws SQLException {
        // query complessa per trovare i letti di un reparto che non compaiono in nessun ricovero ancora aperto
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

        // apre la connessione e prepara la query di ricerca
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // inserisce il nome del reparto come filtro
            ps.setString(1, nomeReparto);

            // esegue la query e popola la lista con i letti effettivamente vuoti
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
        // query per modificare i collegamenti di un letto esistente verso la sua stanza e il reparto
        String sql = "UPDATE letto "
                + "SET stanza_nome = ?, reparto_nome = ? "
                + "WHERE codice = ?";

        // apre la connessione e imposta i nuovi valori letti dall'oggetto java
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getStanza().getNome());
            ps.setString(2, l.getStanza().getReparto().getNome());
            ps.setString(3, l.getCodice());

            // salva le modifiche sul database
            ps.executeUpdate();
        }
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database
     * e la trasforma in un vero e proprio oggetto letto utilizzabile da java.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto letto pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Letto creaLettoDaResultSet(ResultSet rs) throws SQLException {

        // ricostruisce prima la dipendenza più alta ovvero il reparto
        Reparto reparto = new Reparto();
        reparto.setNome(rs.getString("reparto_nome"));

        // ricostruisce la stanza collegandola al suo reparto
        Stanza stanza = new Stanza();
        stanza.setNome(rs.getString("stanza_nome"));
        stanza.setReparto(reparto);

        // infine crea il letto e gli associa l'intera gerarchia sottostante
        Letto letto = new Letto();
        letto.setCodice(rs.getString("codice"));
        letto.setStanza(stanza);

        return letto;
    }
}