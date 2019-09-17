package com.usalamatechnology.manageapp;

public class Paymentdetails {

    private String number_plate;
    private int amount;
    private String no_of_passenger;
    private int rate;
    private String destination;


    public Paymentdetails(String number_plate, int amount,String no_of_passenger, int rate, String destination) {
        this.number_plate = number_plate;
        this.amount = amount;
        this.no_of_passenger = no_of_passenger;
        this.rate = rate;
        this.destination = destination;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNo_of_passenger() {
        return no_of_passenger;
    }

    public void setNo_of_passenger(String no) {
        this.no_of_passenger = no_of_passenger;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
