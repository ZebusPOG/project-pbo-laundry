package laundry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import laundry.model.Pelanggan;
import laundry.util.DatabaseConnection;

/** Akses tabel pelanggan di database db_laundry. */
public class PelangganDAO implements BaseDAO<Pelanggan> {

    @Override
    public void insert(Pelanggan p) throws SQLException {
        String sql = "INSERT INTO pelanggan (nama, no_hp, alamat) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoHp());
            ps.setString(3, p.getAlamat());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPelanggan(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Pelanggan p) throws SQLException {
        String sql = "UPDATE pelanggan SET nama = ?, no_hp = ?, alamat = ? WHERE id_pelanggan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setString(2, p.getNoHp());
            ps.setString(3, p.getAlamat());
            ps.setInt(4, p.getIdPelanggan());
            ps.executeUpdate();
        }
    }

    /**
     * Menghapus pelanggan. Jika pelanggan sudah punya transaksi, data tidak dihapus permanen
     * melainkan dinonaktifkan (aktif = 0) agar riwayat transaksi tetap utuh.
     */
    @Override
    public void delete(int id) throws SQLException {
        String sql = new TransaksiDAO().adaPelanggan(id)
                ? "UPDATE pelanggan SET aktif = 0 WHERE id_pelanggan = ?"
                : "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Pelanggan findById(int id) throws SQLException {
        String sql = "SELECT * FROM pelanggan WHERE id_pelanggan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Semua pelanggan yang masih aktif. */
    @Override
    public List<Pelanggan> findAll() throws SQLException {
        return query("SELECT * FROM pelanggan WHERE aktif = 1 ORDER BY id_pelanggan", null);
    }

    /** Mencari pelanggan aktif yang namanya mengandung keyword. */
    public List<Pelanggan> cari(String keyword) throws SQLException {
        return query("SELECT * FROM pelanggan WHERE aktif = 1 AND nama LIKE ? ORDER BY id_pelanggan",
                "%" + keyword + "%");
    }

    private List<Pelanggan> query(String sql, String parameter) throws SQLException {
        List<Pelanggan> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (parameter != null) {
                ps.setString(1, parameter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(map(rs));
                }
            }
        }
        return hasil;
    }

    /** Mengubah satu baris ResultSet menjadi objek Pelanggan. */
    private Pelanggan map(ResultSet rs) throws SQLException {
        Pelanggan p = new Pelanggan(rs.getString("nama"), rs.getString("no_hp"), rs.getString("alamat"));
        p.setIdPelanggan(rs.getInt("id_pelanggan"));
        p.setAktif(rs.getBoolean("aktif"));
        return p;
    }
}
