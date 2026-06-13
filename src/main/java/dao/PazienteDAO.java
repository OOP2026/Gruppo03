package dao;

import model.Paziente;

import java.sql.SQLException;
import java.util.List;

/**
 * interfaccia per gestire i pazienti nel database.
 * definisce i metodi base per salvare, cercare, aggiornare e cancellare i degenti.
 */
public interface PazienteDAO {

    // operazioni di inserimento

    /**
     * salva un nuovo paziente nel database.
     *
     * @param p il paziente da salvare
     * @throws SQLException se il database ha dei problemi durante l'inserimento
     */
    void save(Paziente p) throws SQLException;

    // operazioni di lettura e ricerca

    /**
     * cerca un paziente usando il suo codice fiscale.
     *
     * @param codiceFiscale il codice fiscale del paziente da cercare
     * @return il paziente trovato, o null se non esiste nell'archivio
     * @throws SQLException se c'è un problema durante la ricerca
     */
    Paziente findByCodiceFiscale(String codiceFiscale) throws SQLException;

    /**
     * prende tutti i pazienti registrati nell'ospedale.
     *
     * @return la lista completa di tutti i pazienti
     * @throws SQLException se c'è un errore nel caricamento dei dati
     */
    List<Paziente> findAll() throws SQLException;

    // operazioni di modifica e cancellazione

    /**
     * aggiorna i dati anagrafici di un paziente già esistente.
     *
     * @param p il paziente con i dati aggiornati
     * @throws SQLException se fallisce la modifica nel database
     */
    void update(Paziente p) throws SQLException;

    /**
     * elimina un paziente dal database usando il suo codice fiscale.
     *
     * @param codiceFiscale il codice fiscale del paziente da cancellare
     * @throws SQLException se si verifica un errore durante l'eliminazione
     */
    void delete(String codiceFiscale) throws SQLException;
}