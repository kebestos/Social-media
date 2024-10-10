package ca.uqac.projetmobile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import ca.uqac.projetmobile.UI.Profile;
import de.hdodenhof.circleimageview.CircleImageView;

import static java.text.SimpleDateFormat.*;

public class DetailEvent extends AppCompatActivity implements Serializable {
    private TextView titleView, descriptionView, adresseView, themeV, particpantnumberV, dateView, nameOwnerEventV;
    private TextView TextNote, Note;
    private DatabaseReference eventRef;
    private CircleImageView pictureOwnerEventV, themPicturV;
    private Button cancelParticipateButton,participateButon,editButton,deleteButton,notationButton;
    private LinearLayout notationCircles;
    private ImageView circle1,circle2,circle3,circle4,circle5;

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomDataBase;
    private String userId;
    private String eventId;
    private String OwnerEventId;
    private String mProfileImageUrl;
    private String OwnerNameText;
    private String NbPlacesRestantes;
    private String theme;
    private String dateEvent;
    private Event CurrentEvent;
    private int currentUserNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        final Intent intent =getIntent();
        intent.getSerializableExtra("event");
        Bundle b =getIntent().getExtras();
        CurrentEvent =b.getParcelable("event");

        eventRef = FirebaseDatabase.getInstance().getReference().child("events");

        titleView = (TextView) findViewById(R.id.titleEvent);
        descriptionView = (TextView) findViewById(R.id.descriptionEventTextV);
        adresseView = (TextView) findViewById(R.id.adresseEventDetail);
        themeV = (TextView) findViewById(R.id.themeEventDetail);
        particpantnumberV = (TextView) findViewById(R.id.NumberPlace);
        dateView = (TextView) findViewById(R.id.dateEventDetail);
        nameOwnerEventV = (TextView) findViewById(R.id.NameOwnerV);

        pictureOwnerEventV = (CircleImageView) findViewById(R.id.OwnerPictureCircleV);
        themPicturV = (CircleImageView) findViewById(R.id.interetPictureEventDetail);

        editButton = (Button)  findViewById(R.id.editButton);
        deleteButton = (Button)  findViewById(R.id.deleteEventButton);
        cancelParticipateButton = (Button)  findViewById(R.id.annulerParticipationButton);
        participateButon = (Button)  findViewById(R.id.participeButton);
        notationButton = (Button)  findViewById(R.id.NoteButton);

        notationCircles = (LinearLayout) findViewById(R.id.notationLayout);
        circle1 = (ImageView) findViewById(R.id.circle1);
        circle2 = (ImageView) findViewById(R.id.circle2);
        circle3 = (ImageView) findViewById(R.id.circle3);
        circle4 = (ImageView) findViewById(R.id.circle4);
        circle5 = (ImageView) findViewById(R.id.circle5);
        currentUserNote = 1;

        TextNote = (TextView) findViewById(R.id.globalTextNotation);
        Note = (TextView) findViewById(R.id.NoteMoyenneV);

        titleView.setText(CurrentEvent.getTitle());
        descriptionView.setText(CurrentEvent.getDescription());
        adresseView.setText(CurrentEvent.getAdresse().toString());
        themeV.setText(CurrentEvent.getTheme());
        particpantnumberV.setText(CurrentEvent.getNbplace());
        dateView.setText(CurrentEvent.getDate());

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        eventId = CurrentEvent.getId();
        OwnerEventId = CurrentEvent.getUid();
        theme = CurrentEvent.getTheme();

        if(userId.equals(CurrentEvent.getUid())){
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

            cancelParticipateButton.setVisibility(View.INVISIBLE);
            participateButon.setVisibility(View.INVISIBLE);
        }
        else{
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            CheckParticipation();
        }

        if(!EventIsPast()){
            notationButton.setVisibility(View.INVISIBLE);
            notationCircles.setVisibility(View.INVISIBLE);
            TextNote.setVisibility(View.INVISIBLE);
            Note.setVisibility(View.INVISIBLE);
        }
        else{
            if(userId.equals(CurrentEvent.getUid())){
                notationButton.setVisibility(View.INVISIBLE);
                notationCircles.setVisibility(View.INVISIBLE);
                TextNote.setVisibility(View.VISIBLE);
                Note.setVisibility(View.INVISIBLE);
            }
            else{
                notationButton.setVisibility(View.VISIBLE);
                notationCircles.setVisibility(View.VISIBLE);
                TextNote.setVisibility(View.INVISIBLE);
                Note.setVisibility(View.INVISIBLE);

                getInfoNote();
            }
            getInfoNoteMoyenneExist();

            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            cancelParticipateButton.setVisibility(View.INVISIBLE);
            participateButon.setVisibility(View.INVISIBLE);
        }
        
        setPictureProfileOwnerEvent();
        CheckNumberParticipant();
        SetImageTheme();

        circle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserNote = 1;
                circle2.setImageResource(R.drawable.circle_strocke);
                circle3.setImageResource(R.drawable.circle_strocke);
                circle4.setImageResource(R.drawable.circle_strocke);
                circle5.setImageResource(R.drawable.circle_strocke);
            }
        });

        circle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserNote = 2;
                circle2.setImageResource(R.drawable.circle_solid);
                circle3.setImageResource(R.drawable.circle_strocke);
                circle4.setImageResource(R.drawable.circle_strocke);
                circle5.setImageResource(R.drawable.circle_strocke);
            }
        });

        circle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserNote = 3;
                circle2.setImageResource(R.drawable.circle_solid);
                circle3.setImageResource(R.drawable.circle_solid);
                circle4.setImageResource(R.drawable.circle_strocke);
                circle5.setImageResource(R.drawable.circle_strocke);
            }
        });

        circle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserNote = 4;
                circle2.setImageResource(R.drawable.circle_solid);
                circle3.setImageResource(R.drawable.circle_solid);
                circle4.setImageResource(R.drawable.circle_solid);
                circle5.setImageResource(R.drawable.circle_strocke);
            }
        });

        circle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserNote = 5;
                circle2.setImageResource(R.drawable.circle_solid);
                circle3.setImageResource(R.drawable.circle_solid);
                circle4.setImageResource(R.drawable.circle_solid);
                circle5.setImageResource(R.drawable.circle_solid);
            }
        });

        notationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference EventNote = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("notes").child(userId);
                EventNote.setValue(currentUserNote);
                Toast.makeText(DetailEvent.this, "Votre notation a été enregistré", Toast.LENGTH_SHORT).show();

                setNoteMoyenne();

                TextNote.setVisibility(View.VISIBLE);
                Note.setVisibility(View.VISIBLE);
            }
        });

        participateButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference EventPlaceRestante = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
                EventPlaceRestante.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map.get("nbplaceRestante") != null){
                                int nbplaceRestante = Integer.parseInt(map.get("nbplaceRestante").toString());
                                if(nbplaceRestante > 0){
                                    nbplaceRestante = nbplaceRestante - 1;
                                    NbPlacesRestantes = String.valueOf(nbplaceRestante);
                                    particpantnumberV.setText(NbPlacesRestantes);
                                    DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("nbplaceRestante");
                                    newRef.setValue(NbPlacesRestantes);

                                    DatabaseReference UserRef = mCustomDataBase.child("eventParticipate").child(eventId);
                                    UserRef.setValue(true);

                                    DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("participants").child(userId);
                                    EventRef.setValue(true);

                                    participateButon.setVisibility(View.INVISIBLE);
                                    cancelParticipateButton.setVisibility(View.VISIBLE);

                                }
                                else{
                                    Toast.makeText(DetailEvent.this, "Il n'y a plus de place", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else if(map.get("nbplace") != null){
                                int nbplaceRestante = Integer.parseInt(map.get("nbplace").toString());
                                if(nbplaceRestante > 0){
                                    nbplaceRestante = nbplaceRestante - 1;
                                    NbPlacesRestantes = String.valueOf(nbplaceRestante);
                                    particpantnumberV.setText(NbPlacesRestantes);
                                    DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("nbplaceRestante");
                                    newRef.setValue(NbPlacesRestantes);

                                    DatabaseReference UserRef = mCustomDataBase.child("eventParticipate").child(eventId);
                                    UserRef.setValue(true);

                                    DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("participants").child(userId);
                                    EventRef.setValue(true);


                                    participateButon.setVisibility(View.INVISIBLE);
                                    cancelParticipateButton.setVisibility(View.VISIBLE);
                                }
                                else{
                                    Toast.makeText(DetailEvent.this, "Il n'y a plus de place", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        cancelParticipateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference UserRef = mCustomDataBase.child("eventParticipate").child(eventId);
                UserRef.removeValue();

                DatabaseReference EventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("participants").child(userId);
                EventRef.removeValue();

                DatabaseReference EventPlaceRestante = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
                EventPlaceRestante.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if(map.get("nbplaceRestante") != null){
                                int nbplaceRestante = Integer.parseInt(map.get("nbplaceRestante").toString());
                                int nbPlace =  Integer.parseInt(map.get("nbplace").toString());
                                if(nbplaceRestante < nbPlace){
                                    nbplaceRestante = nbplaceRestante + 1;
                                    NbPlacesRestantes = String.valueOf(nbplaceRestante);
                                    particpantnumberV.setText(NbPlacesRestantes);
                                    DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId).child("nbplaceRestante");
                                    newRef.setValue(NbPlacesRestantes);
                                }
                                else{
                                    Toast.makeText(DetailEvent.this, "erreur trop de place", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                participateButon.setVisibility(View.VISIBLE);
                cancelParticipateButton.setVisibility(View.INVISIBLE);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newRefe = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("createdEvents").child(eventId);
                newRefe.removeValue();

                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
                eventRef.removeValue();

                Intent intent1 = new Intent(DetailEvent.this,Accueil.class);
                startActivity(intent1);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (DetailEvent.this ,EventEdit.class);
                intent.putExtra("event", CurrentEvent);
                startActivity(intent);

            }
        });

        pictureOwnerEventV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userId.equals(OwnerEventId)){
                    final String IdOwner = OwnerEventId;
                    Intent myIntent = new Intent(v.getContext(), ProfilteOwnerEvent.class);
                    myIntent.putExtra("ownerid", IdOwner );
                    startActivity(myIntent);
                }
            }
        });
    }

    private void setNoteMoyenne(){
        DatabaseReference EventNote = eventRef.child(eventId).child("notes");
        EventNote.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Long sumNote = Long.valueOf(0);
                    Long iteratorNote = Long.valueOf(0);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Long noteSnap = (Long) snapshot.getValue();
                        iteratorNote += 1;
                        sumNote += noteSnap;
                    }
                    float sum = Math.toIntExact(sumNote);
                    float i = Math.toIntExact(iteratorNote);
                    float moyenne = sum / i;
                    //float roundMoyenne = round(moyenne,2);
                    DatabaseReference EventGlobalNote = eventRef.child(eventId).child("noteMoyenne");
                    String noteMoyenne = String.valueOf(moyenne);
                    EventGlobalNote.setValue(noteMoyenne);
                    Note.setText(noteMoyenne);

                    DatabaseReference OwnerEvent =  FirebaseDatabase.getInstance().getReference().child("users").child(OwnerEventId).child("EventNote").child(eventId);
                    OwnerEvent.setValue(noteMoyenne);

                    CalculateOwnerGlobalNote();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CalculateOwnerGlobalNote() {
        DatabaseReference OwnerEvent =  FirebaseDatabase.getInstance().getReference().child("users").child(OwnerEventId).child("EventNote");
        OwnerEvent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    float sumNote = 0;
                    float iteratorNote = 0;
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String noteSnap = (String) snapshot.getValue();
                        float noteSnapshot = Float.parseFloat(noteSnap);
                        sumNote += noteSnapshot;
                        iteratorNote += 1;
                    }
                    float moyenne = sumNote / iteratorNote;
                    String noteMoyenne = String.valueOf(moyenne);
                    DatabaseReference OwnerEvent =  FirebaseDatabase.getInstance().getReference().child("users").child(OwnerEventId).child("GlobalNote");
                    OwnerEvent.setValue(noteMoyenne);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInfoNoteMoyenneExist(){
        DatabaseReference EventNoteMoyenne = eventRef.child(eventId);
        EventNoteMoyenne.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("noteMoyenne") != null){
                        String NoteM = map.get("noteMoyenne").toString();
                        Note.setText(NoteM);

                        TextNote.setVisibility(View.VISIBLE);
                        Note.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInfoNote(){
        DatabaseReference EventNote = eventRef.child(eventId).child("notes");
        EventNote.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get(userId) != null) {
                        String noteUser = map.get(userId).toString();
                        currentUserNote = Integer.parseInt(noteUser);
                        if(currentUserNote == 2){
                            circle2.setImageResource(R.drawable.circle_solid);
                            circle3.setImageResource(R.drawable.circle_strocke);
                            circle4.setImageResource(R.drawable.circle_strocke);
                            circle5.setImageResource(R.drawable.circle_strocke);
                        }
                        if(currentUserNote == 3){
                            circle2.setImageResource(R.drawable.circle_solid);
                            circle3.setImageResource(R.drawable.circle_solid);
                            circle4.setImageResource(R.drawable.circle_strocke);
                            circle5.setImageResource(R.drawable.circle_strocke);
                        }
                        if(currentUserNote == 4){
                            circle2.setImageResource(R.drawable.circle_solid);
                            circle3.setImageResource(R.drawable.circle_solid);
                            circle4.setImageResource(R.drawable.circle_solid);
                            circle5.setImageResource(R.drawable.circle_strocke);
                        }
                        if(currentUserNote == 5){
                            circle2.setImageResource(R.drawable.circle_solid);
                            circle3.setImageResource(R.drawable.circle_solid);
                            circle4.setImageResource(R.drawable.circle_solid);
                            circle5.setImageResource(R.drawable.circle_solid);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void SetImageTheme() {
        if(theme != null){
            if(theme.equals("Sport")){
                themPicturV.setImageResource(R.drawable.runner);
            }
            if(theme.equals("Musique")){
                themPicturV.setImageResource(R.drawable.music);
            }
            if(theme.equals("Photo")){
                themPicturV.setImageResource(R.drawable.photo);
            }
            if(theme.equals("Video")){
                themPicturV.setImageResource(R.drawable.video);
            }
            if(theme.equals("Culture")){
                themPicturV.setImageResource(R.drawable.culture);
            }
            if(theme.equals("Sciences")){
                themPicturV.setImageResource(R.drawable.science4);
            }
        }
    }

    private void CheckNumberParticipant() {
        DatabaseReference EventPlaceRestante = FirebaseDatabase.getInstance().getReference().child("events").child(eventId);
        EventPlaceRestante.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("nbplaceRestante") != null){
                        NbPlacesRestantes = map.get("nbplaceRestante").toString();
                        particpantnumberV.setText(NbPlacesRestantes);
                    }
                    else if(map.get("nbplace") != null){
                        NbPlacesRestantes = map.get("nbplace").toString();
                        particpantnumberV.setText(NbPlacesRestantes);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setPictureProfileOwnerEvent() {
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(OwnerEventId);
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        if(!DetailEvent.this.isDestroyed())
                            Glide.with(DetailEvent.this).load(mProfileImageUrl).into(pictureOwnerEventV);
                    }
                    if(map.get("nom") != null){
                        OwnerNameText = map.get("nom").toString();
                        nameOwnerEventV.setText(OwnerNameText);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckParticipation() {
        DatabaseReference newRef = mCustomDataBase.child("eventParticipate");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get(eventId)!=null){
                        cancelParticipateButton.setVisibility(View.VISIBLE);
                        participateButon.setVisibility(View.INVISIBLE);
                    }
                    else{
                        cancelParticipateButton.setVisibility(View.INVISIBLE);
                        participateButon.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    cancelParticipateButton.setVisibility(View.INVISIBLE);
                    participateButon.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    boolean EventIsPast(){
        Calendar calendar = Calendar.getInstance();
        int yearCurrent = calendar.get(Calendar.YEAR);
        int monthCurrent = calendar.get(Calendar.MONTH);
        int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);

        monthCurrent += 1;

        dateEvent = CurrentEvent.getDate();
        String [] dateParts = dateEvent.split("/");
        int dayEvent = Integer.parseInt(dateParts[0]);
        int monthEvent = Integer.parseInt(dateParts[1]);
        int yearEvent = Integer.parseInt(dateParts[2]);

        Log.d("test", "Event day : " + dayEvent  + " month : " +monthEvent + " year : " +yearEvent );
        Log.d("test", "Current day : " + dayCurrent  + " month : " +monthCurrent + " year : " +yearCurrent );

        if(yearEvent < yearCurrent){
            return true;
        }
        else if(monthEvent < monthCurrent && yearEvent == yearCurrent){
            return true;
        }
        else if(dayEvent < dayCurrent && monthEvent == monthCurrent && yearEvent == yearCurrent){
            return true;
        }
        else
            return false;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
