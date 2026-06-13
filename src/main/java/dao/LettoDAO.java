package dao;

import model.Letto;
import java.sql.SQLException;
import java.util.List;

/**
 * interfaccia per gestire i letti nel database.
 * contiene le operazioni base per salvare, cercare e aggiornare i posti letto.
 */
public interface LettoDAO {

    // operazioni di scrittura sul database

    /**
     * salva un nuovo letto nel database.
     *
     * @param l il letto da salvare
     * @throws SQLException se il database ha dei problemi durante l'inserimento
     */
    void save(Letto l) throws SQLException;

    // operazioni di lettura e ricerca

    /**
     * cerca un letto usando il suo codice esatto.
     *
     * @param codice il codice identificativo del letto
     * @return il letto trovato, o null se non esiste
     * @throws SQLException se c'è un problema durante la ricerca
     */
    Letto findByCodice(String codice) throws SQLException;

    /**
     * prende tutti i letti registrati in ospedale, senza filtri.
     *
     * @return la lista di tutti i letti
     * @throws SQLException se c'è un errore nel caricamento dei dati
     */
    List<Letto> findAll() throws SQLException;

    /**
     * cerca solo i letti che al momento non sono occupati, filtrati per reparto.
     *
     * @param nomeReparto il reparto in cui cercare i posti liberi
     * @return la lista dei letti disponibili in quel reparto
     * @throws SQLException se c'è un errore durante la ricerca
     */
    List<Letto> findLiberiByReparto(String nomeReparto) throws SQLException;

    // operazioni di modifica

    /**
     * aggiorna i dati di un letto che si trova già nel database.
     *
     * @param l il letto con i dati aggiornati
     * @throws SQLException se fallisce il salvataggio della modifica
     */
    void update(Letto l) throws SQLException;
}