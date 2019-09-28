package com.usalamatechnology.manageapp.models;

public class Paymentdetails {

    private String number_plate;
    private String rate;
    private String destination;


    public Paymentdetails(String number_plate, String rate, String destination) {
        this.number_plate = number_plate;
        this.rate = rate;
        this.destination = destination;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
