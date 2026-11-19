package laundry.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Operasi dasar penyimpanan data (CRUD) ke database.
 * Setiap method melempar SQLException agar error database bisa ditampilkan di view (JOptionPane).
 */
public interface BaseDAO<T> {

    void insert(T data) throws SQLException;

    void update(T data) throws SQLException;

    void delete(int id) throws SQLException;

    T findById(int id) throws SQLException;

    List<T> findAll() throws SQLException;
}
