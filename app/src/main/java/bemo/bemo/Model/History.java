package bemo.bemo.Model;

public class History {
    private String location;
    private String destination;
    private String date;
    private String price;

    public History() {
    }

    public History(String location, String destination, String date, String price) {
        this.location = location;
        this.destination = destination;
        this.date = date;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
