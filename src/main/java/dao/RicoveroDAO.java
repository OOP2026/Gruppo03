package dao;

import model.Ricovero;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface RicoveroDAO {
    void save(Ricovero r) throws SQLException;
    Ricovero findByPazienteAndDataInizio(String codiceFiscale, LocalDateTime inizio) throws SQLException;
    List<Ricovero> findAll() throws SQLException;
    List<Ricovero> findRicoveriAttivi() throws SQLException;
    void update(Ricovero r) throws SQLException;
}