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
        // query per inserire una nuova stanza collegata al suo reparto. se esiste già ignora il salvataggio
        String sql = "INSERT INTO stanza(nome, reparto_nome) "
                + "VALUES(?, ?) "
                + "ON CONFLICT(nome, reparto_nome) DO NOTHING";

        // apre la connessione al database e prepara l'istruzione sql in modo sicuro
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta i parametri usando i dati dell'oggetto java
            ps.setString(1, s.getNome());
            ps.setString(2, s.getReparto().getNome());

            // esegue l'aggiornamento fisico sul database
            ps.executeUpdate();
        }
    }

    @Override
    public List<Stanza> findByReparto(String nomeReparto) throws SQLException {
        // query per cercare tutte le stanze appartenenti a un reparto specifico ordinandole per nome
        String sql = "SELECT nome, reparto_nome "
                + "FROM stanza "
                + "WHERE reparto_nome = ? "
                + "ORDER BY nome";

        // crea una lista vuota pronta per memorizzare i risultati
        List<Stanza> stanze = new ArrayList<>();

        // stabilisce la connessione e prepara la query
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // inserisce il nome del reparto come filtro di ricerca
            ps.setString(1, nomeReparto);

            // esegue la lettura dei dati dal database
            try (ResultSet rs = ps.executeQuery()) {
                // scorre le righe trovate e popola la lista convertendole una ad una
                while (rs.next()) {
                    stanze.add(creaStanzaDaResultSet(rs));
                }
            }
        }

        return stanze;
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database
     * e la trasforma in un vero e proprio oggetto stanza, collegandolo al suo reparto.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto stanza pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Stanza creaStanzaDaResultSet(ResultSet rs) throws SQLException {

        // prima ricostruisce l'oggetto reparto a cui appartiene la stanza
        Reparto reparto = new Reparto();
        reparto.setNome(rs.getString("reparto_nome"));

        // poi crea la stanza vera e propria e le assegna il nome e il collegamento al reparto
        Stanza stanza = new Stanza();
        stanza.setNome(rs.getString("nome"));
        stanza.setReparto(reparto);

        return stanza;
    }
}