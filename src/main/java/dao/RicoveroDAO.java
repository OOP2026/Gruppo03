package dao;

import model.Ricovero;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * interfaccia per gestire i ricoveri nel database.
 * contiene i metodi per salvare, aggiornare e cercare le degenze dei pazienti.
 */
public interface RicoveroDAO {

    // operazioni di inserimento

    /**
     * salva un nuovo ricovero nel database.
     *
     * @param r il ricovero da registrare
     * @throws SQLException se c'è un problema durante il salvataggio
     */
    void save(Ricovero r) throws SQLException;

    // operazioni di lettura e ricerca

    /**
     * cerca un ricovero specifico usando il codice fiscale del paziente e la data esatta in cui è iniziato.
     *
     * @param codiceFiscale il codice fiscale del paziente ricoverato
     * @param inizio la data e l'ora di inizio del ricovero
     * @return il ricovero trovato, o null se non esiste
     * @throws SQLException se la ricerca fallisce
     */
    Ricovero findByPazienteAndDataInizio(String codiceFiscale, LocalDateTime inizio) throws SQLException;

    /**
     * prende lo storico di tutti i ricoveri, sia quelli passati che quelli attualmente in corso.
     *
     * @return la lista completa di tutti i ricoveri
     * @throws SQLException se c'è un errore nel caricamento dei dati
     */
    List<Ricovero> findAll() throws SQLException;

    /**
     * cerca solo i ricoveri attivi al momento.
     *
     * @return la lista dei ricoveri attualmente in corso
     * @throws SQLException se c'è un errore durante la ricerca
     */
    List<Ricovero> findRicoveriAttivi() throws SQLException;

    // operazioni di modifica

    /**
     * aggiorna i dati di un ricovero già esistente.
     *
     * @param r il ricovero con i dati aggiornati
     * @throws SQLException se fallisce la modifica nel database
     */
    void update(Ricovero r) throws SQLException;
}