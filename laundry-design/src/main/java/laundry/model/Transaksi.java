package laundry.model;

import java.time.LocalDateTime;

/** Satu order laundry: milik satu pelanggan dan memakai satu layanan. */
public class Transaksi {

    private int idTransaksi;
    private Pelanggan pelanggan;
    private Layanan layanan;
    private double beratKg;
    private double hargaPerKg;
    private double totalHarga;
    private LocalDateTime tanggalMasuk;
    private StatusTransaksi status = StatusTransaksi.DIPROSES;

    public Transaksi() {
    }

    /** Membuat transaksi baru; harga per kg disalin dari layanan dan total langsung dihitung. */
    public Transaksi(Pelanggan pelanggan, Layanan layanan, double beratKg) {
        this.pelanggan = pelanggan;
        this.layanan = layanan;
        this.beratKg = beratKg;
        this.hargaPerKg = layanan.getHargaPerKg();
        this.tanggalMasuk = LocalDateTime.now().withNano(0);
        hitungTotal();
    }

    /** total = berat x harga per kg */
    public double hitungTotal() {
        totalHarga = beratKg * hargaPerKg;
        return totalHarga;
    }

    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public Pelanggan getPelanggan() { return pelanggan; }
    public void setPelanggan(Pelanggan pelanggan) { this.pelanggan = pelanggan; }

    public Layanan getLayanan() { return layanan; }
    public void setLayanan(Layanan layanan) { this.layanan = layanan; }

    public double getBeratKg() { return beratKg; }
    public void setBeratKg(double beratKg) { this.beratKg = beratKg; }

    public double getHargaPerKg() { return hargaPerKg; }
    public void setHargaPerKg(double hargaPerKg) { this.hargaPerKg = hargaPerKg; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public LocalDateTime getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(LocalDateTime tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public StatusTransaksi getStatus() { return status; }
    public void setStatus(StatusTransaksi status) { this.status = status; }
}
