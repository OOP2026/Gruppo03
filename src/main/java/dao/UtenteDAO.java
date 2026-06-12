package dao;

import model.Utente;
import java.sql.SQLException;
import java.util.List;

public interface UtenteDAO {
    void save(Utente u) throws SQLException;
    Utente findByLogin(String login) throws SQLException;
    List<Utente> findAll() throws SQLException;
    boolean checkCredentials(String login, String password) throws SQLException;
}