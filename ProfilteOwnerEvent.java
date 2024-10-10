package ca.uqac.projetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilteOwnerEvent extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mCustomDataBase;
    private String userId;

    private String mProfileImageUrl;

    private TextView nomTextV;
    private String nom;

    private TextView ageNumberTextV;
    private String ageNumber;

    private TextView descriptionTextV;
    private String description;

    private TextView numberFriendsTextV;
    private long numberFriends;

    private TextView eventCounterTextV;
    private long eventCount;

    private TextView UserNote;
    private String note;

    private String OwnerId;

    private CircleImageView InteretView1;
    private CircleImageView InteretView2;
    private CircleImageView InteretView3;
    private CircleImageView InteretView4;
    private CircleImageView InteretView5;
    private CircleImageView InteretView6;

    private CircleImageView circleImageView;

    private Button followButton;
    private Button unFollowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilte_owner_event);

        Intent intent =getIntent();
        intent.getSerializableExtra("ownerid");
        Bundle b =getIntent().getExtras();
        OwnerId = b.getString("ownerid");

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(OwnerId);

        circleImageView = (CircleImageView) findViewById(R.id.profilcircl);

        nomTextV = (TextView) findViewById(R.id.nomId);
        eventCounterTextV =(TextView) findViewById(R.id.eventCounter);
        descriptionTextV = (TextView) findViewById(R.id.describeText);
        ageNumberTextV = (TextView) findViewById(R.id.AgeNumber);
        numberFriendsTextV = (TextView) findViewById(R.id.friendNumberView);
        UserNote = (TextView) findViewById(R.id.GlobalUserNote);

        InteretView1 = (CircleImageView) findViewById(R.id.interet1ImageV);
        InteretView2 = (CircleImageView) findViewById(R.id.interet2ImageV);
        InteretView3 = (CircleImageView) findViewById(R.id.interet3ImageV);
        InteretView4 = (CircleImageView) findViewById(R.id.interet4ImageV);
        InteretView5 = (CircleImageView) findViewById(R.id.interet5ImageV);
        InteretView6 = (CircleImageView) findViewById(R.id.interet6ImageV);

        getInfo();
        getInteretInfos();

        followButton = findViewById(R.id.followButton);
        unFollowButton = findViewById(R.id.unFollowButton);

        checkFollowInfos();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("ImFollowing").child(OwnerId);
                newRef.setValue(true);

                DatabaseReference newRefFollowers = FirebaseDatabase.getInstance().getReference().child("users").child(OwnerId).child("followers").child(userId);
                newRefFollowers.setValue(true);

                UdpateUi();

                unFollowButton.setVisibility(View.VISIBLE);
                followButton.setVisibility(View.INVISIBLE);
            }
        });

        unFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("ImFollowing").child(OwnerId);
                newRef.removeValue();

                DatabaseReference newRefFollowers = FirebaseDatabase.getInstance().getReference().child("users").child(OwnerId).child("followers").child(userId);
                newRefFollowers.removeValue();

                UdpateUi();

                unFollowButton.setVisibility(View.INVISIBLE);
                followButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkFollowInfos() {
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("ImFollowing");
        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get(OwnerId)!=null){
                        unFollowButton.setVisibility(View.VISIBLE);
                        followButton.setVisibility(View.INVISIBLE);
                    }
                    else{
                        unFollowButton.setVisibility(View.INVISIBLE);
                        followButton.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    unFollowButton.setVisibility(View.INVISIBLE);
                    followButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInfo() {
        mCustomDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(ProfilteOwnerEvent.this).load(mProfileImageUrl).into(circleImageView);
                    }
                    if(map.get("nom")!=null){
                        nom = map.get("nom").toString();
                        nomTextV.setText(nom);
                    }
                    if(map.get("createdEvents")!=null){
                        eventCount = dataSnapshot.child("createdEvents").getChildrenCount();
                        String count = ""+ eventCount;
                        eventCounterTextV.setText(count);
                    }
                    if(map.get("description")!=null){
                        description = map.get("description").toString();
                        descriptionTextV.setText(description);
                    }
                    if(map.get("age")!=null){
                        ageNumber = map.get("age").toString();
                        ageNumberTextV.setText(ageNumber);
                    }
                    if(map.get("followers")!=null){
                        numberFriends = dataSnapshot.child("followers").getChildrenCount();
                        String countFriend = ""+ numberFriends;
                        numberFriendsTextV.setText(countFriend);
                    }
                    if(map.get("GlobalNote") != null){
                        note = map.get("GlobalNote").toString();
                        UserNote.setText(note);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInteretInfos() {
        InteretView1.setVisibility(View.INVISIBLE);
        InteretView2.setVisibility(View.INVISIBLE);
        InteretView3.setVisibility(View.INVISIBLE);
        InteretView4.setVisibility(View.INVISIBLE);
        InteretView5.setVisibility(View.INVISIBLE);
        InteretView6.setVisibility(View.INVISIBLE);
        DatabaseReference refInteret= mCustomDataBase.child("interets");
        refInteret.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Sport") != null) {
                        InteretView1.setVisibility(View.VISIBLE);
                    }
                    if (map.get("Musique") != null) {
                        InteretView2.setVisibility(View.VISIBLE);
                    }
                    if (map.get("Photo") != null) {
                        InteretView3.setVisibility(View.VISIBLE);
                    }
                    if (map.get("Video") != null) {
                        InteretView4.setVisibility(View.VISIBLE);
                    }
                    if (map.get("Culture") != null) {
                        InteretView5.setVisibility(View.VISIBLE);
                    }
                    if (map.get("Sciences") != null) {
                        InteretView6.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UdpateUi(){
        mCustomDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("followers")!=null){
                        numberFriends = dataSnapshot.child("followers").getChildrenCount();
                        String countFriend = ""+ numberFriends;
                        numberFriendsTextV.setText(countFriend);
                    }
                    else{
                        String countFriend = "0";
                        numberFriendsTextV.setText(countFriend);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
