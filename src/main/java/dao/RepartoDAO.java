package dao;

import model.Reparto;
import java.sql.SQLException;
import java.util.List;

/**
 * interfaccia per gestire i reparti nel database.
 * contiene i metodi base per salvare e cercare i vari reparti dell'ospedale.
 */
public interface RepartoDAO {

    /**
     * salva un nuovo reparto nel database.
     *
     * @param r il reparto da aggiungere
     * @throws SQLException se c'è un problema durante il salvataggio
     */
    void save(Reparto r) throws SQLException;

    /**
     * cerca un reparto specifico partendo dal suo nome (es. "cardiologia").
     *
     * @param nome il nome del reparto da cercare
     * @return il reparto trovato, oppure null se non esiste
     * @throws SQLException se c'è un errore durante la ricerca
     */
    Reparto findByNome(String nome) throws SQLException;

    /**
     * prende tutti i reparti registrati nell'ospedale.
     *
     * @return la lista completa dei reparti
     * @throws SQLException se c'è un problema nel caricamento dei dati
     */
    List<Reparto> findAll() throws SQLException;
}