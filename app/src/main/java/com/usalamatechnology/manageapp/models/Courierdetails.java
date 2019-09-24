package com.usalamatechnology.manageapp.models;

 public class Courierdetails {
     private String number_plate;
     private String amount;
     private String courier_id;

     public Courierdetails(String number_plate, String amount, String courier_id){
         this.number_plate = number_plate;
         this.amount = amount;
         this.courier_id = courier_id;

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

     public String getCourier_id() {
         return courier_id;
     }

     public void setCourier_id(String courier_id) {
         this.courier_id = courier_id;
     }
 }
