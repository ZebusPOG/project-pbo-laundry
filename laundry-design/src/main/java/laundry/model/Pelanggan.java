package laundry.model;

/** Data pelanggan laundry. */
public class Pelanggan {

    private int idPelanggan;
    private String nama;
    private String noHp;
    private String alamat;
    private boolean aktif = true;

    public Pelanggan() {
    }

    public Pelanggan(String nama, String noHp, String alamat) {
        this.nama = nama;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    /** Teks yang tampil di JComboBox pelanggan. */
    @Override
    public String toString() {
        return (noHp == null || noHp.isEmpty()) ? nama : nama + " (" + noHp + ")";
    }
}
