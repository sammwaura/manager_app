package com.usalamatechnology.manageapp;

public class Paymentdetails {

    private String number_plate;
    private int fare;
    private int courier;
    private int amount;
    private String name_of_passenger;
    private int phone_no_of_passenger;
    private int id_no_of_passenger;
    private String destination;


    public Paymentdetails(String number_plate, int fare, int courier, int amount,String name_of_passenger, int phone_no_of_passenger, int id_no_of_passenger,String destination) {
        this.number_plate = number_plate;
        this.fare  = fare;
        this.courier = courier;
        this.amount = amount;
        this.name_of_passenger = name_of_passenger;
        this.phone_no_of_passenger = phone_no_of_passenger;
        this.id_no_of_passenger = id_no_of_passenger;
        this.destination = destination;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName_of_passenger() {
        return name_of_passenger;
    }

    public void setName_of_passenger(String name_of_passenger) {
        this.name_of_passenger = name_of_passenger;
    }

    public int getPhone_no_of_passenger() {
        return phone_no_of_passenger;
    }

    public void setPhone_no_of_passenger(int phone_no_of_passenger) {
        this.phone_no_of_passenger = phone_no_of_passenger;
    }

    public int getId_no_of_passenger() {
        return id_no_of_passenger;
    }

    public void setId_no_of_passenger(int id_no_of_passenger) {
        this.id_no_of_passenger = id_no_of_passenger;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
