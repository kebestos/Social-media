package ca.uqac.projetmobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChoiceInterestProfile extends AppCompatActivity {

    private List<String> listChoiceInterest;

    private TextView textViewInterest1;
    private String interet1;
    private TextView textViewInterest2;
    private String interet2;
    private TextView textViewInterest3;
    private String interet3;
    private TextView textViewInterest4;
    private String interet4;
    private TextView textViewInterest5;
    private String interet5;
    private TextView textViewInterest6;
    private String interet6;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_interest_profile);
        Profile profile = com.facebook.Profile.getCurrentProfile();
        mAuth = FirebaseAuth.getInstance();
        if(profile != null){
            userId = profile.getId();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("interetFb").child(userId);
        }
        else{
            userId = mAuth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        }

        listChoiceInterest = new ArrayList<String>();

        CircleImageView cardViewInterest1 = (CircleImageView) findViewById(R.id.interet1);
        textViewInterest1 = (TextView) findViewById(R.id.interet1Text);
        interet1 = textViewInterest1.getText().toString();

        CircleImageView cardViewInterest2 = (CircleImageView) findViewById(R.id.interet2);
        textViewInterest2 = (TextView) findViewById(R.id.interet2Text);
        interet2 = textViewInterest2.getText().toString();

        CircleImageView cardViewInterest3 = (CircleImageView) findViewById(R.id.interet3);
        textViewInterest3 = (TextView) findViewById(R.id.interet3Text);
        interet3 = textViewInterest3.getText().toString();

        CircleImageView cardViewInterest4 = (CircleImageView) findViewById(R.id.interet4);
        textViewInterest4 = (TextView) findViewById(R.id.interet4Text);
        interet4 = textViewInterest4.getText().toString();

        CircleImageView cardViewInterest5 = (CircleImageView) findViewById(R.id.interet5);
        textViewInterest5 = (TextView) findViewById(R.id.interet5Text);
        interet5 = textViewInterest5.getText().toString();

        CircleImageView cardViewInterest6 = (CircleImageView) findViewById(R.id.interet6);
        textViewInterest6 = (TextView) findViewById(R.id.interet6Text);
        interet6 = textViewInterest6.getText().toString();

        getInfos();

        cardViewInterest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest1.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest1.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet1);
                }
                else{
                    textViewInterest1.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet1);
                }
            }
        });

        cardViewInterest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest2.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest2.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet2);
                }
                else{
                    textViewInterest2.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet2);
                }
            }
        });

        cardViewInterest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest3.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest3.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet3);
                }
                else{
                    textViewInterest3.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet3);
                }
            }
        });

        cardViewInterest4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest4.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest4.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet4);
                }
                else{
                    textViewInterest4.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet4);
                }
            }
        });

        cardViewInterest5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest5.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest5.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet5);
                }
                else{
                    textViewInterest5.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet5);
                }
            }
        });

        cardViewInterest6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( textViewInterest6.getTextColors().getDefaultColor() == Color.rgb(9,198,223)){
                    textViewInterest6.setTextColor(Color.rgb(240,136,83));
                    listChoiceInterest.add(interet6);
                }
                else{
                    textViewInterest6.setTextColor(Color.rgb(9,198,223));
                    listChoiceInterest.remove(interet6);
                }
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference removeRef = mDatabase.child("interets");
                removeRef.removeValue();
                if(listChoiceInterest.size() > 0){
                    for(String s : listChoiceInterest){
                        DatabaseReference newRef = mDatabase.child("interets").child(s);
                        newRef.setValue(true);
                    }
                    Intent intHome = new Intent(ChoiceInterestProfile.this, Accueil.class);
                    startActivity(intHome);
                }
                else{
                    Toast.makeText(ChoiceInterestProfile.this, "Choisissez au moins un intérêt",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getInfos() {
        DatabaseReference newRef = mDatabase.child("interets");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Sport")!=null){
                        textViewInterest1.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet1);
                    }
                    if(map.get("Musique")!=null){
                        textViewInterest2.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet2);
                    }
                    if(map.get("Photo")!=null){
                        textViewInterest3.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet3);
                    }
                    if(map.get("Video")!=null){
                        textViewInterest4.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet4);
                    }
                    if(map.get("Culture")!=null){
                        textViewInterest5.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet5);
                    }
                    if(map.get("Sciences")!=null){
                        textViewInterest6.setTextColor(Color.rgb(240,136,83));
                        listChoiceInterest.add(interet6);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
