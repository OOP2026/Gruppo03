package dao;

import model.Utente;
import java.sql.SQLException;
import java.util.List;

/**
 * interfaccia per gestire gli utenti nel database.
 * contiene i metodi per salvare, cercare e controllare gli accessi al sistema.
 */
public interface UtenteDAO {

    // operazioni di inserimento

    /**
     * salva un nuovo utente nel database.
     *
     * @param u l'utente da registrare
     * @throws SQLException se c'è un problema durante il salvataggio
     */
    void save(Utente u) throws SQLException;

    // operazioni di lettura e ricerca

    /**
     * cerca un utente usando il suo username.
     *
     * @param login l'username dell'utente da cercare
     * @return l'utente trovato, o null se non esiste
     * @throws SQLException se c'è un errore durante la ricerca
     */
    Utente findByLogin(String login) throws SQLException;

    /**
     * prende tutti gli utenti registrati nel sistema.
     *
     * @return la lista completa di tutti gli utenti
     * @throws SQLException se c'è un problema nel caricamento dei dati
     */
    List<Utente> findAll() throws SQLException;

    // operazioni di verifica e accesso

    /**
     * controlla se l'username e la password inseriti corrispondono a un utente registrato.
     * è il metodo usato dalla schermata di login per far entrare le persone.
     *
     * @param login l'username inserito
     * @param password la password inserita
     * @return true se i dati sono giusti, false se sono sbagliati
     * @throws SQLException se il database dà errore durante il controllo
     */
    boolean checkCredentials(String login, String password) throws SQLException;
}