package ca.uqac.projetmobile;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Adresse implements Parcelable {

    public String streetNB;
    public String street;
    public String state;
    public String postcode;
public Adresse(){

}
    public  Adresse(String streetNB, String street, String state, String postcode){
        this.streetNB=streetNB;
        this.street=street;
        this.state=state;
    this.postcode=postcode;
    }

    protected Adresse(Parcel in) {
        streetNB = in.readString();
        street = in.readString();
        state = in.readString();
        postcode = in.readString();
    }

    public static final Creator<Adresse> CREATOR = new Creator<Adresse>() {
        @Override
        public Adresse createFromParcel(Parcel in) {
            return new Adresse(in);
        }

        @Override
        public Adresse[] newArray(int size) {
            return new Adresse[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return  streetNB + ", " + street + ", " +state + ", " + postcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(streetNB);
        parcel.writeString(street);
        parcel.writeString(state);
        parcel.writeString(postcode);
    }
}
