package laundry.model;

import laundry.util.Rupiah;

/** Jenis layanan laundry beserta harga per kg. */
public class Layanan {

    private int idLayanan;
    private String namaLayanan;
    private double hargaPerKg;
    private int estimasiHari = 2;
    private boolean aktif = true;

    public Layanan() {
    }

    public Layanan(String namaLayanan, double hargaPerKg, int estimasiHari) {
        this.namaLayanan = namaLayanan;
        this.hargaPerKg = hargaPerKg;
        this.estimasiHari = estimasiHari;
    }

    public int getIdLayanan() { return idLayanan; }
    public void setIdLayanan(int idLayanan) { this.idLayanan = idLayanan; }

    public String getNamaLayanan() { return namaLayanan; }
    public void setNamaLayanan(String namaLayanan) { this.namaLayanan = namaLayanan; }

    public double getHargaPerKg() { return hargaPerKg; }
    public void setHargaPerKg(double hargaPerKg) { this.hargaPerKg = hargaPerKg; }

    public int getEstimasiHari() { return estimasiHari; }
    public void setEstimasiHari(int estimasiHari) { this.estimasiHari = estimasiHari; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    /** Teks yang tampil di JComboBox layanan. */
    @Override
    public String toString() {
        return namaLayanan + " - " + Rupiah.format(hargaPerKg) + "/kg";
    }
}
