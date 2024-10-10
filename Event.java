package ca.uqac.projetmobile;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    public String title;
    public String description;
    public Adresse adresse;
    public String uid;
    public String id;

    public String date;
    public String nbplace;
    public String theme;

    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        adresse = in.readParcelable(Adresse.class.getClassLoader());
        uid = in.readString();
        theme=in.readString();

        date=in.readString();
        nbplace=in.readString();
        id=in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme= theme;
    }

    public String getNbplace() {
        return nbplace;
    }

    public void setNbplace(String nb) {
        this.nbplace= nb;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Event(String title, String description, Adresse adresse, String uid, String theme, String date, String nbplace, String id) {
        this.title = title;
        this.description = description;
        this.adresse = adresse;
        this.uid = uid;
        this.theme = theme;
        this.date = date;
        this.nbplace = nbplace;
        this.id=id;
    }

    public Event() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeParcelable(adresse, i);
        parcel.writeString(uid);

        parcel.writeString(theme);
        parcel.writeString(date);
        parcel.writeString(nbplace);
        parcel.writeString(id);
    }
}


