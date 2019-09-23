package com.usalamatechnology.manageapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

public class PassengerDetails implements Parcelable {

    String passenger_name;
    int phone_no;
    int seat_no;

    public PassengerDetails(String passenger_name, int phone_no, int seat_no){
        this.passenger_name = passenger_name;
        this.phone_no = phone_no;
        this.seat_no = seat_no;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

