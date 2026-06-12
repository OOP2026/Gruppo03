package dao;

import model.Letto;
import java.sql.SQLException;
import java.util.List;

public interface LettoDAO {
    void save(Letto l) throws SQLException;
    Letto findByCodice(String codice) throws SQLException;
    List<Letto> findAll() throws SQLException;
    List<Letto> findLiberiByReparto(String nomeReparto) throws SQLException;
    void update(Letto l) throws SQLException;
}