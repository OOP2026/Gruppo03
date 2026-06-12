package dao;

import model.Reparto;
import java.sql.SQLException;
import java.util.List;

public interface RepartoDAO {
    void save(Reparto r) throws SQLException;
    Reparto findByNome(String nome) throws SQLException;
    List<Reparto> findAll() throws SQLException;
}