package com.usalamatechnology.manageapp.models;

public class Paymentdetails {

    private String id;
    private String number_plate;
    private String rate;
    private String amount;
    private String passenger_no;
    private String destination;
    private String origin;



    public Paymentdetails(String id,String number_plate, String rate,String amount,String passenger_no, String destination, String origin) {
        this.id = id;
        this.number_plate = number_plate;
        this.rate = rate;
        this.amount = amount;
        this.passenger_no = passenger_no;
        this.destination = destination;
        this.origin = origin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassenger_no() {
        return passenger_no;
    }

    public void setPassenger_no(String passenger_no) {
        this.passenger_no = passenger_no;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
