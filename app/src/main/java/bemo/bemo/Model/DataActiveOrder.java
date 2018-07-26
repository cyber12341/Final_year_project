package bemo.bemo.Model;

public class DataActiveOrder {
    String nama, img, tujuan,location,status, tipe, biaya;

    public DataActiveOrder() {
    }

    public DataActiveOrder(String nama, String img, String tujuan, String location, String status, String tipe, String biaya) {
        this.nama = nama;
        this.img = img;
        this.tujuan = tujuan;
        this.location = location;
        this.status = status;
        this.tipe = tipe;
        this.biaya = biaya;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }
}
