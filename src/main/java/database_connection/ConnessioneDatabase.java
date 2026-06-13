package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * classe che gestisce il collegamento fisico tra il nostro programma java e il database postgresql.
 * contiene le credenziali per entrare nel db.
 */
public class ConnessioneDatabase {

    // costanti fisse per la configurazione del server e l'accesso

    private static final String URL = "jdbc:postgresql://localhost:5432/gestione_ospedale";
    private static final String USER = "postgres";
    private static final String PASS = "root";

    // operazioni di connessione

    /**
     * crea e restituisce la connessione attiva al database.
     * viene chiamato dai vari file dao ogni volta che devono fare una query.
     *
     * @return l'oggetto connection pronto per interrogare il database
     * @throws SQLException se non riesce a collegarsi al server
     */
    public static Connection getConnection() throws SQLException {
        // stabilisce la comunicazione reale usando i driver di java
        return DriverManager.getConnection(URL, USER, PASS);
    }
}