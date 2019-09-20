package com.usalamatechnology.manageapp;

public class Faredetails {

    private String number_plate;
    private String amount;
    private String passenger_no;

    public Faredetails(String number_plate, String amount, String passenger_no){
        this.number_plate = number_plate;
        this.amount = amount;
        this.passenger_no = passenger_no;

    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
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
}
