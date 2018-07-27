package bemo.bemo.Model;

public class ActiveOrder {
    String customerId,tipe,biaya,status,date;
    double lat,lng,latDestination, lngDestination;

    public ActiveOrder() {
    }

    public ActiveOrder(String customerId,String tipe,String biaya,String status,String date, double lat, double lng, double latDestination, double lngDestination) {
        this.customerId = customerId;
        this.lat = lat;
        this.lng = lng;
        this.latDestination = latDestination;
        this.lngDestination = lngDestination;
        this.tipe = tipe;
        this.biaya = biaya;
        this.status = status;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLatDestination() {
        return latDestination;
    }

    public void setLatDestination(double latDestination) {
        this.latDestination = latDestination;
    }

    public double getLngDestination() {
        return lngDestination;
    }

    public void setLngDestination(double lngDestination) {
        this.lngDestination = lngDestination;
    }
}
