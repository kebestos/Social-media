package ca.uqac.projetmobile.Model;

public class User {
    public String email, nom, profileImageUrl,uID;


    public User(String email,String nom){
        this.email=email;
        this.nom=nom;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public User(String nom, String uID, String profileImageUrl,String email){
        this.nom=nom;
        this.profileImageUrl = profileImageUrl;
        this.uID = uID;
        this.email= email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImageURL() {
        return profileImageUrl;
    }

    public void setImageURL(String imageURL) {
        this.profileImageUrl = imageURL;
    }
}
