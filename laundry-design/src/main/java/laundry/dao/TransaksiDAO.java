package laundry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import laundry.model.Layanan;
import laundry.model.Pelanggan;
import laundry.model.StatusTransaksi;
import laundry.model.Transaksi;
import laundry.util.DatabaseConnection;

/** Akses tabel transaksi (digabung dengan pelanggan dan layanan) di database db_laundry. */
public class TransaksiDAO implements BaseDAO<Transaksi> {

    /** Query dasar: transaksi + data pelanggan + data layanan. */
    private static final String SELECT_JOIN =
            "SELECT t.id_transaksi, t.berat_kg, t.harga_per_kg, t.total_harga, t.tanggal_masuk, t.status, "
            + "p.id_pelanggan, p.nama, p.no_hp, p.alamat, p.aktif AS pelanggan_aktif, "
            + "l.id_layanan, l.nama_layanan, l.harga_per_kg AS harga_layanan, l.estimasi_hari, l.aktif AS layanan_aktif "
            + "FROM transaksi t "
            + "JOIN pelanggan p ON p.id_pelanggan = t.id_pelanggan "
            + "JOIN layanan l ON l.id_layanan = t.id_layanan ";

    @Override
    public void insert(Transaksi t) throws SQLException {
        String sql = "INSERT INTO transaksi (id_pelanggan, id_layanan, berat_kg, harga_per_kg, total_harga, tanggal_masuk, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getPelanggan().getIdPelanggan());
            ps.setInt(2, t.getLayanan().getIdLayanan());
            ps.setDouble(3, t.getBeratKg());
            ps.setDouble(4, t.getHargaPerKg());
            ps.setDouble(5, t.getTotalHarga());
            ps.setTimestamp(6, Timestamp.valueOf(t.getTanggalMasuk()));
            ps.setString(7, t.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setIdTransaksi(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Transaksi t) throws SQLException {
        String sql = "UPDATE transaksi SET id_pelanggan = ?, id_layanan = ?, berat_kg = ?, harga_per_kg = ?, "
                + "total_harga = ?, status = ? WHERE id_transaksi = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getPelanggan().getIdPelanggan());
            ps.setInt(2, t.getLayanan().getIdLayanan());
            ps.setDouble(3, t.getBeratKg());
            ps.setDouble(4, t.getHargaPerKg());
            ps.setDouble(5, t.getTotalHarga());
            ps.setString(6, t.getStatus().name());
            ps.setInt(7, t.getIdTransaksi());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM transaksi WHERE id_transaksi = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Transaksi findById(int id) throws SQLException {
        List<Transaksi> hasil = query(SELECT_JOIN + "WHERE t.id_transaksi = ?", id);
        return hasil.isEmpty() ? null : hasil.get(0);
    }

    @Override
    public List<Transaksi> findAll() throws SQLException {
        return query(SELECT_JOIN + "ORDER BY t.id_transaksi", null);
    }

    public void updateStatus(int id, StatusTransaksi status) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE transaksi SET status = ? WHERE id_transaksi = ?")) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public List<Transaksi> filterByStatus(StatusTransaksi status) throws SQLException {
        return query(SELECT_JOIN + "WHERE t.status = ? ORDER BY t.id_transaksi", status.name());
    }

    /** true jika pelanggan ini sudah punya transaksi. */
    public boolean adaPelanggan(int idPelanggan) throws SQLException {
        return ada("SELECT 1 FROM transaksi WHERE id_pelanggan = ? LIMIT 1", idPelanggan);
    }

    /** true jika layanan ini sudah dipakai di transaksi. */
    public boolean adaLayanan(int idLayanan) throws SQLException {
        return ada("SELECT 1 FROM transaksi WHERE id_layanan = ? LIMIT 1", idLayanan);
    }

    private boolean ada(String sql, int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Transaksi> query(String sql, Object parameter) throws SQLException {
        List<Transaksi> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (parameter != null) {
                ps.setObject(1, parameter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(map(rs));
                }
            }
        }
        return hasil;
    }

    /** Mengubah satu baris hasil JOIN menjadi objek Transaksi lengkap dengan Pelanggan dan Layanan. */
    private Transaksi map(ResultSet rs) throws SQLException {
        Pelanggan p = new Pelanggan(rs.getString("nama"), rs.getString("no_hp"), rs.getString("alamat"));
        p.setIdPelanggan(rs.getInt("id_pelanggan"));
        p.setAktif(rs.getBoolean("pelanggan_aktif"));

        Layanan l = new Layanan(rs.getString("nama_layanan"), rs.getDouble("harga_layanan"), rs.getInt("estimasi_hari"));
        l.setIdLayanan(rs.getInt("id_layanan"));
        l.setAktif(rs.getBoolean("layanan_aktif"));

        Transaksi t = new Transaksi();
        t.setIdTransaksi(rs.getInt("id_transaksi"));
        t.setPelanggan(p);
        t.setLayanan(l);
        t.setBeratKg(rs.getDouble("berat_kg"));
        t.setHargaPerKg(rs.getDouble("harga_per_kg"));
        t.setTotalHarga(rs.getDouble("total_harga"));
        t.setTanggalMasuk(rs.getTimestamp("tanggal_masuk").toLocalDateTime());
        t.setStatus(StatusTransaksi.valueOf(rs.getString("status")));
        return t;
    }
}
