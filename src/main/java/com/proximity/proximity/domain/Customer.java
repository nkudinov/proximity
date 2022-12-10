package com.proximity.proximity.domain;

public class Customer {
    public int customerId;
    public String customerName;
    public double lat;
    public String geoHash;

    public Customer(int customerId, String customerName, double lat, double lon) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.lat = lat;
        this.lon = lon;
    }

    public double lon;
}
