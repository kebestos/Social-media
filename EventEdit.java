package ca.uqac.projetmobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class EventEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    private Event CurrentEvent;
    private DatabaseReference mDatabase;

    private String title;
    private String Eventid;
    private String theme;
    private String description;
    private String placeNumber;
    private String date;
    private String codePost;
    private String state;
    private String street;
    private String streetNb;
    private String nbPlaceRestante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        final Intent intent =getIntent();
        intent.getSerializableExtra("event");
        Bundle b =getIntent().getExtras();
        CurrentEvent =b.getParcelable("event");

        Eventid = CurrentEvent.getId();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("events").child(Eventid);

        mchoosedate=findViewById(R.id.choose);
        mTitleField = findViewById(R.id.title);
        mBodyField = findViewById(R.id.description);
        mStreetNBField = findViewById(R.id.streetNB);
        mStreetField = findViewById(R.id.street);
        mStateField = findViewById(R.id.state);
        mPostcodeField = findViewById(R.id.postcode);
        mNbPlace= findViewById(R.id.nbPlace);

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.themes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        if (CurrentEvent.getTheme() != null) {
            int spinnerPosition = adapter.getPosition(CurrentEvent.getTheme());
            spinner.setSelection(spinnerPosition);
        }

        setInfos();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeNumber = mNbPlace.getText().toString();
                if(!CurrentEvent.getNbplace().equals(placeNumber) && nbPlaceRestante != null){
                    int placeRestanteInt = Integer.parseInt(nbPlaceRestante);
                    int newPlaceNumberInt = Integer.parseInt(placeNumber);
                    int placeNumberInt = Integer.parseInt(CurrentEvent.getNbplace());
                    int numberParticipant = placeNumberInt-placeRestanteInt;

                    if(newPlaceNumberInt < placeNumberInt){
                        int dif = placeNumberInt - newPlaceNumberInt;
                        placeRestanteInt -= dif;
                        if(placeRestanteInt < 0){
                            Toast.makeText(EventEdit.this, numberParticipant +" personne participent à l'event, indiquez un nombre de place minimum de " + numberParticipant, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            int newPlaceRestanteInt = newPlaceNumberInt - numberParticipant;
                            nbPlaceRestante = String.valueOf(newPlaceRestanteInt);
                            DatabaseReference nbPlaceRestanteref = mDatabase.child("nbplaceRestante");
                            nbPlaceRestanteref.setValue(nbPlaceRestante);

                            title = mTitleField.getText().toString();
                            DatabaseReference titleref = mDatabase.child("title");
                            titleref.setValue(title);

                            description = mBodyField.getText().toString();
                            DatabaseReference descriptionref = mDatabase.child("description");
                            descriptionref.setValue(description);

                            DatabaseReference nbPlaceref = mDatabase.child("nbplace");
                            nbPlaceref.setValue(placeNumber);

                            date = mchoosedate.getText().toString();
                            DatabaseReference dateref = mDatabase.child("date");
                            dateref.setValue(date);

                            DatabaseReference Themeref = mDatabase.child("theme");
                            Themeref.setValue(theme);

                            street = mStreetField.getText().toString();
                            DatabaseReference Streetref = mDatabase.child("adresse").child("street");
                            Streetref.setValue(street);

                            streetNb = mStreetNBField.getText().toString();
                            DatabaseReference StreetNbref = mDatabase.child("adresse").child("streetNB");
                            StreetNbref.setValue(streetNb);

                            codePost = mPostcodeField.getText().toString();
                            DatabaseReference CodePostref = mDatabase.child("adresse").child("postcode");
                            CodePostref.setValue(codePost);

                            state = mStateField.getText().toString();
                            DatabaseReference Stateref = mDatabase.child("adresse").child("state");
                            Stateref.setValue(state);

                            Intent intent1 = new Intent(EventEdit.this,Accueil.class);
                            startActivity(intent1);
                        }
                    }

                }
                else{
                    DatabaseReference nbPlaceRestanteref = mDatabase.child("nbplace");
                    nbPlaceRestanteref.setValue(placeNumber);

                    title = mTitleField.getText().toString();
                    DatabaseReference titleref = mDatabase.child("title");
                    titleref.setValue(title);

                    description = mBodyField.getText().toString();
                    DatabaseReference descriptionref = mDatabase.child("description");
                    descriptionref.setValue(description);

                    date = mchoosedate.getText().toString();
                    DatabaseReference dateref = mDatabase.child("date");
                    dateref.setValue(date);

                    DatabaseReference Themeref = mDatabase.child("theme");
                    Themeref.setValue(theme);

                    street = mStreetField.getText().toString();
                    DatabaseReference Streetref = mDatabase.child("adresse").child("street");
                    Streetref.setValue(street);

                    streetNb = mStreetNBField.getText().toString();
                    DatabaseReference StreetNbref = mDatabase.child("adresse").child("streetNB");
                    StreetNbref.setValue(streetNb);

                    codePost = mPostcodeField.getText().toString();
                    DatabaseReference CodePostref = mDatabase.child("adresse").child("postcode");
                    CodePostref.setValue(codePost);

                    state = mStateField.getText().toString();
                    DatabaseReference Stateref = mDatabase.child("adresse").child("state");
                    Stateref.setValue(state);

                    Intent intent1 = new Intent(EventEdit.this,Accueil.class);
                    startActivity(intent1);
                }

            }
        });

    }

    private void setInfos() {
        mTitleField.setText(CurrentEvent.getTitle());
        mBodyField.setText(CurrentEvent.getDescription());
        mStreetNBField.setText(CurrentEvent.getAdresse().streetNB);
        mStreetField.setText(CurrentEvent.getAdresse().street);
        mStateField.setText(CurrentEvent.getAdresse().state);
        mPostcodeField.setText(CurrentEvent.getAdresse().postcode);
        mNbPlace.setText(CurrentEvent.getNbplace());
        mchoosedate.setText(CurrentEvent.getDate());
        theme = CurrentEvent.getTheme();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("nbplaceRestante") != null) {
                        nbPlaceRestante = map.get("nbplaceRestante").toString();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
        parent.getItemAtPosition(pos);
        switch (pos){
            case 0:
                theme="Sport";
                break;
            case 1 :
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
            case 5:
                theme="Sciences";
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
}
