package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * classe che gestisce il collegamento fisico tra il nostro programma java e il database postgresql.
 * contiene le credenziali (indirizzo, utente e password) per entrare nel db.
 */
public class ConnessioneDatabase {

    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_ospedale";
    private static final String USER = "postgres";
    private static final String PASS = "root";

    /**
     * crea e restituisce la connessione attiva al database.
     * viene chiamato dai vari file dao ogni volta che devono fare una query (salvare, cercare, ecc).
     *
     * @return l'oggetto connection pronto per interrogare il database
     * @throws SQLException se non riesce a collegarsi (es. se postgres è spento o la password è sbagliata)
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}