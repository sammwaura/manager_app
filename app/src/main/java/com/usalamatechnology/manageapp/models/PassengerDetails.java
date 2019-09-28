package com.usalamatechnology.manageapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PassengerDetails implements Parcelable {

    public String passenger_name;
    public String phone_no;

    public PassengerDetails(String passenger_name, String phone_no, String seat_no){
        this.passenger_name = passenger_name;
        this.phone_no = phone_no;
    }

    protected PassengerDetails(Parcel in) {
        passenger_name = in.readString();
        phone_no = String.valueOf(in.readInt());

    }

    public static final Creator <PassengerDetails> CREATOR = new Creator <PassengerDetails>() {
        @Override
        public PassengerDetails createFromParcel(Parcel in) {
            return new PassengerDetails(in);
        }

        @Override
        public PassengerDetails[] newArray(int size) {
            return new PassengerDetails[size];
        }
    };

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
        dest.writeString(passenger_name);
        dest.writeInt(Integer.parseInt(phone_no));
    }
}

