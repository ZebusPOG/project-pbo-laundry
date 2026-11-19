package laundry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import laundry.model.Layanan;
import laundry.util.DatabaseConnection;

/** Akses tabel layanan di database db_laundry. */
public class LayananDAO implements BaseDAO<Layanan> {

    @Override
    public void insert(Layanan l) throws SQLException {
        String sql = "INSERT INTO layanan (nama_layanan, harga_per_kg, estimasi_hari) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getNamaLayanan());
            ps.setDouble(2, l.getHargaPerKg());
            ps.setInt(3, l.getEstimasiHari());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    l.setIdLayanan(rs.getInt(1));
                }
            }
        }
    }

    /** Mengubah layanan. Harga baru hanya berlaku untuk transaksi berikutnya (transaksi menyimpan salinan harga). */
    @Override
    public void update(Layanan l) throws SQLException {
        String sql = "UPDATE layanan SET nama_layanan = ?, harga_per_kg = ?, estimasi_hari = ? WHERE id_layanan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getNamaLayanan());
            ps.setDouble(2, l.getHargaPerKg());
            ps.setInt(3, l.getEstimasiHari());
            ps.setInt(4, l.getIdLayanan());
            ps.executeUpdate();
        }
    }

    /**
     * Menghapus layanan. Jika layanan sudah dipakai di transaksi, data tidak dihapus permanen
     * melainkan dinonaktifkan (aktif = 0) agar riwayat transaksi tetap utuh.
     */
    @Override
    public void delete(int id) throws SQLException {
        String sql = new TransaksiDAO().adaLayanan(id)
                ? "UPDATE layanan SET aktif = 0 WHERE id_layanan = ?"
                : "DELETE FROM layanan WHERE id_layanan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Layanan findById(int id) throws SQLException {
        String sql = "SELECT * FROM layanan WHERE id_layanan = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Semua layanan yang masih aktif. */
    @Override
    public List<Layanan> findAll() throws SQLException {
        List<Layanan> hasil = new ArrayList<>();
        String sql = "SELECT * FROM layanan WHERE aktif = 1 ORDER BY id_layanan";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(map(rs));
            }
        }
        return hasil;
    }

    private Layanan map(ResultSet rs) throws SQLException {
        Layanan l = new Layanan(rs.getString("nama_layanan"), rs.getDouble("harga_per_kg"), rs.getInt("estimasi_hari"));
        l.setIdLayanan(rs.getInt("id_layanan"));
        l.setAktif(rs.getBoolean("aktif"));
        return l;
    }
}
