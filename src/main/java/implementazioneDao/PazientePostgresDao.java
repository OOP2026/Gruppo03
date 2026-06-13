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
 * implementazione specifica per postgresql dell'interfaccia pazientedao.
 * contiene le query sql per salvare, cercare, aggiornare o eliminare i pazienti dal database.
 */
public class PazientePostgresDao implements PazienteDAO {

    @Override
    public void save(Paziente p) throws SQLException {
        // query di inserimento. in caso di codice fiscale duplicato ignora il comando per non far crashare il programma
        String sql = "INSERT INTO paziente(codice_fiscale, nome, cognome, data_nascita) "
                + "VALUES(?, ?, ?, ?) "
                + "ON CONFLICT(codice_fiscale) DO NOTHING";

        // apertura della connessione sicura con il database
        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // popolamento dei parametri base
            ps.setString(1, p.getCodiceFiscale());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getCognome());

            // gestisce la conversione della data da formato java a formato sql
            if (p.getDataNascita() != null) {
                ps.setDate(4, Date.valueOf(p.getDataNascita()));
            } else {
                // se la data è assente inserisce un valore nullo nel database
                ps.setNull(4, Types.DATE);
            }

            // esecuzione della query sul server
            ps.executeUpdate();
        }
    }

    @Override
    public Paziente findByCodiceFiscale(String codiceFiscale) throws SQLException {
        // query di ricerca basata sul codice fiscale univoco
        String sql = "SELECT codice_fiscale, nome, cognome, data_nascita "
                + "FROM paziente "
                + "WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // imposta il parametro di ricerca
            ps.setString(1, codiceFiscale);

            // esegue la ricerca e controlla i risultati
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // converte la riga del database in un oggetto java
                    return creaPazienteDaResultSet(rs);
                }
            }
        }

        // restituisce nullo se il paziente non è stato trovato
        return null;
    }

    @Override
    public List<Paziente> findAll() throws SQLException {
        // query per recuperare l'intero archivio ordinato alfabeticamente
        String sql = "SELECT codice_fiscale, nome, cognome, data_nascita "
                + "FROM paziente "
                + "ORDER BY cognome, nome";

        // prepara la lista vuota
        List<Paziente> pazienti = new ArrayList<>();

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // scorre tutte le righe e le aggiunge alla lista
            while (rs.next()) {
                pazienti.add(creaPazienteDaResultSet(rs));
            }
        }

        return pazienti;
    }

    @Override
    public void update(Paziente p) throws SQLException {
        // query per aggiornare i dati di un paziente già esistente
        String sql = "UPDATE paziente "
                + "SET nome = ?, cognome = ?, data_nascita = ? "
                + "WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // inserisce i nuovi valori da salvare
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());

            // conversione della data
            if (p.getDataNascita() != null) {
                ps.setDate(3, Date.valueOf(p.getDataNascita()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            // usa il codice fiscale per identificare in modo univoco quale riga modificare
            ps.setString(4, p.getCodiceFiscale());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String codiceFiscale) throws SQLException {
        // query per eliminare definitivamente un paziente dal sistema
        String sql = "DELETE FROM paziente WHERE codice_fiscale = ?";

        try (Connection c = ConnessioneDatabase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, codiceFiscale);
            ps.executeUpdate();
        }
    }

    /**
     * metodo di appoggio interno. prende una riga grezza in uscita dal database
     * e la trasforma in un vero e proprio oggetto paziente.
     *
     * @param rs il risultato della query sql
     * @return l'oggetto paziente pronto all'uso
     * @throws SQLException se manca qualche colonna o c'è un errore di lettura
     */
    private Paziente creaPazienteDaResultSet(ResultSet rs) throws SQLException {
        // crea un contenitore vuoto
        Paziente p = new Paziente();

        // copia i campi testuali
        p.setCodiceFiscale(rs.getString("codice_fiscale"));
        p.setNome(rs.getString("nome"));
        p.setCognome(rs.getString("cognome"));

        // estrae la data in formato sql
        Date data = rs.getDate("data_nascita");

        // se la data esiste la converte nel formato localdate usato internamente da java
        if (data != null) {
            p.setDataNascita(data.toLocalDate());
        }

        return p;
    }
}