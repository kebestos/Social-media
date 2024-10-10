package ca.uqac.projetmobile.UI;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.widget.DatePicker;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.uqac.projetmobile.Accueil;
import ca.uqac.projetmobile.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileModify extends AppCompatActivity {

    private CircleImageView circleImageView;

    Uri pickedImage;
    //private ImageView ImageUser;hd
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomDataBase;

    private String userId;
    private String mProfileImageUrl;

    private TextView nomTextV;
    public String nom;

    private EditText describeEditText;
    private String describUser;

    private TextView ageTextV;
    private String age;

    private TextView eventCounterTextV;
    private long eventCount;

    private TextView numberFriendsTextV;
    private long numberFriends;

    private TextView UserNote;
    private String note;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;

    com.facebook.Profile profile;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modify);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        eventCounterTextV =(TextView) findViewById(R.id.eventCounter);
        numberFriendsTextV = (TextView) findViewById(R.id.friendNumberView);
        describeEditText = (EditText) findViewById(R.id.describeEditText);
        nomTextV = (TextView) findViewById(R.id.nomId);
        ageTextV = (TextView) findViewById(R.id.AgeNumber);
        UserNote = (TextView) findViewById(R.id.GlobalUserNote);



        CardView cardView = (CardView) findViewById(R.id.ageCard);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileModify.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                age = getAge(year,month,day);
                ageTextV.setText(age);
            }
        };

        circleImageView = (CircleImageView) findViewById(R.id.profilcircl);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPermission();
                }
                else
                    openGallery();
            }
        });

        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference newRef = mCustomDataBase.child("description");
                describUser = describeEditText.getText().toString();

                newRef.setValue(describUser);

                DatabaseReference AgeRef = mCustomDataBase.child("age");
                age = ageTextV.getText().toString();

                AgeRef.setValue(age);

                Intent intent = new Intent(ProfileModify.this, Accueil.class);
                startActivity(intent);
            }
        });

        profile = com.facebook.Profile.getCurrentProfile();

        if(profile != null){
            facebookInfos(profile);
        }

        getInfo();
    }

    private void facebookInfos(@NonNull com.facebook.Profile profile) {
        Uri profilePictureUri= profile.getCurrentProfile().getProfilePictureUri(200 , 200);
        nomTextV.setText(profile.getName());
        Glide.with(this).load(profilePictureUri).into(circleImageView);
    }

    private void getInfo() {
        mCustomDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        //Glide.with(app).load(mProfileImageUrl).into(ImageUser);
                        Glide.with(ProfileModify.this).load(mProfileImageUrl).into(circleImageView);
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
                    if(map.get("age") != null){
                        age = map.get("age").toString();
                        ageTextV.setText(age);
                    }
                    if(map.get("description") != null){
                        describUser = map.get("description").toString();
                        describeEditText.setText(describUser);
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

    private void updateProfilePicture(Uri pickedImage, final FirebaseUser currentUser){

        if(this.pickedImage != null){
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos/"+ mAuth.getCurrentUser().getUid() + "/profilePicture/");

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(ProfileModify.this.getContentResolver(),this.pickedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);
            byte[] data = boas.toByteArray();
            UploadTask uploadTask = mStorage.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl",downloadUrl.toString());
                    mCustomDataBase.updateChildren(newImage);

                    return;
                }
            });
        }
        else{
            Log.d("test","update Pic failed");
        }
    }


    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(ProfileModify.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileModify.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(ProfileModify.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(ProfileModify.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else{
            openGallery();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ){
            pickedImage = data.getData();
            circleImageView.setImageURI(pickedImage);
            updateProfilePicture(pickedImage,mAuth.getCurrentUser());
        }
    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH) ||
                today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)){
                    age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
