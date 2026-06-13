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
        // query di inserimento per il nuovo reparto. se il nome esiste già nel database l'operazione viene ignorata
        String sql = "INSERT INTO reparto(nome) "
                + "VALUES(?) "
                + "ON CONFLICT(nome) DO NOTHING";

        // apre la connessione in modo sicuro e prepara il comando sql
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta il nome del reparto al posto del punto interrogativo
            ps.setString(1, r.getNome());

            // esegue l'aggiornamento sul database
            ps.executeUpdate();
        }
    }

    @Override
    public Reparto findByNome(String nome) throws SQLException {
        // query di ricerca per trovare un reparto specifico usando il suo nome
        String sql = "SELECT nome FROM reparto WHERE nome = ?";

        // apertura della connessione per l'interrogazione
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // passa il parametro di ricerca alla query
            ps.setString(1, nome);

            // esegue la lettura dei dati
            try (ResultSet rs = ps.executeQuery()) {
                // se c'è un risultato valido costruisce l'oggetto java reparto
                if (rs.next()) {
                    Reparto r = new Reparto();
                    r.setNome(rs.getString("nome"));
                    return r;
                }
            }
        }

        // se il reparto non viene trovato restituisce un valore nullo
        return null;
    }

    @Override
    public List<Reparto> findAll() throws SQLException {
        // query per ottenere l'elenco completo di tutti i reparti in ordine alfabetico
        String sql = "SELECT nome FROM reparto ORDER BY nome";

        // prepara la lista vuota che conterrà i risultati
        List<Reparto> reparti = new ArrayList<>();

        // apre la connessione ed esegue subito la query senza aver bisogno di parametri
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // ciclo che scorre riga per riga la risposta del database
            while (rs.next()) {
                // per ogni riga crea un nuovo reparto e lo aggiunge alla lista
                Reparto r = new Reparto();
                r.setNome(rs.getString("nome"));
                reparti.add(r);
            }
        }

        // restituisce la lista completa al sistema
        return reparti;
    }
}