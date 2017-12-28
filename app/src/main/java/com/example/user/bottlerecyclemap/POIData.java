package com.example.user.bottlerecyclemap;

/**
 * Created by user on 2017-04-07.
 */

public class POIData {
    private String title;
    private String phone;
    private String newAddress;
    private Integer zipcode;
    private String address;
    private double lat, lng;
    private String addressBCode;

    public POIData(String title, String phone, String address, String newAddress, Integer zipcode, double lat, double lng, String addressBCode) throws NullPointerException{
        this.title = title;
        this.phone = phone;
        this.address = address;
        this.newAddress = newAddress;
        this.zipcode = zipcode;
        this.lat = lat;
        this.lng = lng;
        this.addressBCode = addressBCode;
    }

    public String getTitle() {
        return title;
    }

    public String getPhone() {
        return phone;
    }

    public String getNewAddress() {
        return newAddress;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddressBCode() {
        return addressBCode;
    }
}
