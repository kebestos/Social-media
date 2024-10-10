package ca.uqac.projetmobile.UI;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

import ca.uqac.projetmobile.ChoiceInterestProfile;
import ca.uqac.projetmobile.MainActivity;
import ca.uqac.projetmobile.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment implements View.OnClickListener {

    private Application app;

    private CircleImageView circleImageView;

    Uri pickedImage;
    //private ImageView ImageUser;hd
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomDataBase;

    private String userId;
    private String userIdFb;
    private String mProfileImageUrl;

    private TextView nomTextV;
    private String nom;

    private TextView ageNumberTextV;
    private String ageNumber;

    private TextView descriptionTextV;
    private String description;

    private TextView eventCounterTextV;
    private long eventCount;

    private TextView numberFriendsTextV;
    private long numberFriends;

    private TextView UserNote;
    private String note;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    com.facebook.Profile profile;

    private CircleImageView InteretView1;
    private CircleImageView InteretView2;
    private CircleImageView InteretView3;
    private CircleImageView InteretView4;
    private CircleImageView InteretView5;
    private CircleImageView InteretView6;
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = ((Application) getActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        profile = com.facebook.Profile.getCurrentProfile();
        
        nomTextV = (TextView) v.findViewById(R.id.nomId);
        eventCounterTextV =(TextView) v.findViewById(R.id.eventCounter);
        descriptionTextV = (TextView) v.findViewById(R.id.describeText);
        ageNumberTextV = (TextView) v.findViewById(R.id.AgeNumber);
        numberFriendsTextV = (TextView) v.findViewById(R.id.friendNumberView);
        UserNote = (TextView) v.findViewById(R.id.GlobalUserNote);

        InteretView1 = (CircleImageView) v.findViewById(R.id.interet1ImageV);
        InteretView2 = (CircleImageView) v.findViewById(R.id.interet2ImageV);
        InteretView3 = (CircleImageView) v.findViewById(R.id.interet3ImageV);
        InteretView4 = (CircleImageView) v.findViewById(R.id.interet4ImageV);
        InteretView5 = (CircleImageView) v.findViewById(R.id.interet5ImageV);
        InteretView6 = (CircleImageView) v.findViewById(R.id.interet6ImageV);

        circleImageView = (CircleImageView) v.findViewById(R.id.profilcircl);

        LinearLayout linearLayout = v.findViewById(R.id.ModifyProfile);
        linearLayout.setOnClickListener(this);        

        Button signOutButton  = (Button) v.findViewById((R.id.signOutButton));

        ConstraintLayout constraintLayout = v.findViewById(R.id.layoutInteret);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChoiceInterestProfile.class);
                startActivity(intent);
            }
        });

       if(profile != null){
           Log.d("test","Facebook");
           facebookInfos(profile);
       }

       signOutButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(profile != null){
                   LoginManager.getInstance().logOut();
               }
               mAuth.signOut();
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
           }
       });

       getInfo();
       getInteretInfos();
       return v;
    }

    private void getInteretInfos() {
        if(profile == null){
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
    }

    private void facebookInfos(@NonNull com.facebook.Profile profile) {
        Uri profilePictureUri= profile.getCurrentProfile().getProfilePictureUri(200 , 200);
        nomTextV.setText(profile.getName());
        Glide.with(this).load(profilePictureUri).into(circleImageView);

        InteretView1.setVisibility(View.INVISIBLE);
        InteretView2.setVisibility(View.INVISIBLE);
        InteretView3.setVisibility(View.INVISIBLE);
        InteretView4.setVisibility(View.INVISIBLE);
        InteretView5.setVisibility(View.INVISIBLE);
        InteretView6.setVisibility(View.INVISIBLE);

        userIdFb = profile.getId();
        DatabaseReference refInteret = FirebaseDatabase.getInstance().getReference().child("interetFb").child(userIdFb).child("interets");

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

    private void getInfo() {
        mCustomDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(app).load(mProfileImageUrl).into(circleImageView);
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
                    if(map.get("followers")!=null){
                        numberFriends = dataSnapshot.child("followers").getChildrenCount();
                        String countFriend = ""+ numberFriends;
                        numberFriendsTextV.setText(countFriend);
                    }
                    if(map.get("description")!=null){
                        description = map.get("description").toString();
                        descriptionTextV.setText(description);
                    }
                    if(map.get("age")!=null){
                        ageNumber = map.get("age").toString();
                        ageNumberTextV.setText(ageNumber);
                    }
                    if(map.get("uidFacebook")== null && profile != null){
                        mCustomDataBase.child("uidFacebook").setValue(profile.getId());
                    }
                    if(map.get("nom")== null && profile != null){
                        mCustomDataBase.child("nom").setValue(profile.getName());
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getActivity(),ProfileModify.class);
        startActivity(intent);
    }

}
