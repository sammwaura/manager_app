package com.usalamatechnology.manageapp;

/**
 * Created by Sir Edwin on 8/30/2015.
 */

public interface HomeIObserver {
    // change signature of method as per your need
    void onCardClicked( int pos,String userid, String type,String time,String address,String city,String more,String amount,String category);
    void onCardApproved( int pos,String userid, String type,String time,String address,String city,String more,String amount,String category);
    void onEditRate( int pos,String userid, String type,String time,String address,String city,String more,String amount,String category);
}
