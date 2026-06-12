package dao;

import model.Stanza;

import java.sql.SQLException;
import java.util.List;

public interface StanzaDAO {
    void save(Stanza s) throws SQLException;
    List<Stanza> findByReparto(String nomeReparto) throws SQLException;
}