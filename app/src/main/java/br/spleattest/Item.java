package br.spleattest;

public class Item {

    String name;
    String image_link;
    double latitude;
    double longitude;

    public Item(String name,String image_link, double latitude, double longitude) {
        this.name = name;
        this.image_link = image_link;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_link() {
        return  image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude() {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return  longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
