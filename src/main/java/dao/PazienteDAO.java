package dao;
import model.Paziente;

import java.sql.SQLException;
import java.util.List;

public interface PazienteDAO {
    void save(Paziente p) throws SQLException;
    Paziente findByCodiceFiscale(String cf) throws SQLException;
    List<Paziente> findAll() throws SQLException;
    void update(Paziente p) throws SQLException;
    void delete(String cf) throws SQLException;
}