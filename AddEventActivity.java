package ca.uqac.projetmobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference mDatabase;
    private EditText mTitleField;
    private EditText mBodyField;
    private EditText mStreetNBField;
    private EditText mStreetField;
    private EditText mNbPlace;
    private EditText mStateField;
    private EditText mPostcodeField;
    private Spinner spinner;
    private TextView mchoosedate;
    private static final String REQUIRED = "Required";
    private static final String MAXVALUE = "999 participants is allowed";
    private String theme;
    private Button mSubmitButton;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mchoosedate=findViewById(R.id.choose);
        mTitleField = findViewById(R.id.title);
        mBodyField = findViewById(R.id.description);
        mStreetNBField = findViewById(R.id.streetNB);
        mStreetField = findViewById(R.id.street);
        mStateField = findViewById(R.id.state);
        mPostcodeField = findViewById(R.id.postcode);
        mNbPlace= findViewById(R.id.nbPlace);
        mSubmitButton = findViewById(R.id.submit);
        mCancelButton = findViewById(R.id.cancel);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (validerQuestionnaire()){
                    submit();
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), Accueil.class);
                startActivity(myIntent);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.themes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        parent.getItemAtPosition(pos);
        switch (pos){
            case 0:
                theme="Sport";
                break;
            case 1:
                theme="Musique";
                break;
            case 2 :
                theme="Photo";
                break;
            case 3 :
                theme="Video";
                break;
            case 4 :
                theme="Culture";
                break;
            case 5 :
                theme="Sciences";
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private boolean validerQuestionnaire() throws ParseException {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final String adressenb = mStreetNBField.getText().toString();
        final String street = mStreetField.getText().toString();
        final String postcode = mPostcodeField.getText().toString();
        final String state=mStateField.getText().toString();

        final String nbpl=mNbPlace.getText().toString();
        final String date=mchoosedate.getText().toString();
        Date datechosen =    new SimpleDateFormat("dd/MM/yyyy").parse(date);
        Date currentdate = new Date(System.currentTimeMillis());

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return false;
        }

        // date is required
        if (TextUtils.isEmpty(date)) {
           mchoosedate.setError(REQUIRED);
            return false;
        }
       else if(currentdate.compareTo(datechosen)>0) {
            mchoosedate.setError("Invalid date");
            return false;
        }
        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return false;
        }
        //  is required
        if (TextUtils.isEmpty(nbpl)) {
            mNbPlace.setError(REQUIRED);
            return false;
        }

        // Max Value allowed
        if(mNbPlace.getText().length() > 3){
            mNbPlace.setError(MAXVALUE);
            return false;
        }

        //  is required
        if (TextUtils.isEmpty(adressenb)) {
           mStreetNBField.setError(REQUIRED);
            return false;
        }

        //  is required
        if (TextUtils.isEmpty(street)) {
            mStreetField.setError(REQUIRED);
            return false;
        }
        //  is required
        if (TextUtils.isEmpty(state)) {
            mStreetField.setError(REQUIRED);
            return false;
        }
        //  is required
        if (TextUtils.isEmpty(postcode)) {
            mPostcodeField.setError(REQUIRED);
            return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date =
                       String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)  + "/"+ String.valueOf(year);
                mchoosedate.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    private void submit() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();
        final Adresse  adresse=new Adresse(mStreetNBField.getText().toString(),mStreetField.getText().toString(), mStateField.getText().toString(), mPostcodeField.getText().toString());
        final DatabaseReference newRef = mDatabase.child("events").push();
        final String IdEvent=newRef.getKey();
        final Event event = new Event(mTitleField.getText().toString(), mBodyField.getText().toString(), adresse, userID, theme, mchoosedate.getText().toString(),mNbPlace.getText().toString(),IdEvent);

        newRef.setValue(event);
        DatabaseReference refUser=mDatabase.child("users");
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid =  (String)snapshot.getKey();
                            if(userID.equals(uid)){
                                DatabaseReference refUserFind=snapshot.getRef();
                                        refUserFind.child("createdEvents").child(IdEvent).setValue(IdEvent);
                            }
                }
            }

                    @Override public void onCancelled(DatabaseError databaseError) { } });



        Intent i = new Intent(AddEventActivity.this, Accueil.class);
        startActivity(i);
    }
}

