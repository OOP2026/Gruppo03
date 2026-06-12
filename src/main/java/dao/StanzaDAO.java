package dao;

import model.Stanza;

import java.sql.SQLException;
import java.util.List;

/**
 * interfaccia per gestire le stanze nel database.
 * contiene i metodi base per salvare e cercare le stanze associate ai reparti.
 */
public interface StanzaDAO {

    /**
     * salva una nuova stanza nel database.
     *
     * @param s la stanza da registrare
     * @throws SQLException se c'è un problema durante il salvataggio
     */
    void save(Stanza s) throws SQLException;

    /**
     * cerca tutte le stanze che appartengono a un reparto specifico.
     *
     * @param nomeReparto il nome del reparto di cui vogliamo vedere le stanze
     * @return la lista delle stanze trovate in quel reparto
     * @throws SQLException se la ricerca nel database fallisce
     */
    List<Stanza> findByReparto(String nomeReparto) throws SQLException;
}