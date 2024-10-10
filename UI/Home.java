package ca.uqac.projetmobile.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.uqac.projetmobile.AdapterEvents;
import ca.uqac.projetmobile.Adresse;
import ca.uqac.projetmobile.Event;
import ca.uqac.projetmobile.MapsActivity;
import ca.uqac.projetmobile.R;

public class Home extends Fragment implements AdapterView.OnItemSelectedListener{
    private FloatingActionButton mapButton;
    private Spinner spinner;
    private String theme;
    private ArrayList<Event> listeEvents = new ArrayList<Event>();
    private ArrayList<Event> filteredEvents = new ArrayList<Event>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabase;
    private Button filterButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        theme="Aucun filtre";
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        //recuperation events
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference refEvents = mDatabase.child("events");
        refEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listeEvents.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String streetnb = (String) snapshot.child("adresse").child("streetNB").getValue();
                    String street = (String) snapshot.child("adresse").child("street").getValue();
                    String state = (String) snapshot.child("adresse").child("state").getValue();
                    String postcode = (String) snapshot.child("adresse").child("postcode").getValue();
                    String title= (String) snapshot.child("title").getValue();
                    String description= (String) snapshot.child("description").getValue();
                    String uid =(String) snapshot.child("uid").getValue();
                    String date =(String) snapshot.child("date").getValue();
                    String theme= (String) snapshot.child("theme").getValue();
                    String nbplace=(String) snapshot.child("nbplace").getValue();
                    String id=(String) snapshot.child("id").getValue();

                    Adresse ad = new Adresse(streetnb, street, state, postcode);
                    Event ev=new Event(title,  description, ad, uid,theme,date, nbplace,id);
                    listeEvents.add(ev);
                    //Log.i("TAGGG", ev.date);
                }
                try {
                    filter();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


             mapButton=(FloatingActionButton)v.findViewById(R.id.mapButton);
             mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MapsActivity.class);
                myIntent.putExtra("events",  listeEvents);
                startActivity(myIntent);
            }
        });


        spinner = (Spinner) v.findViewById(R.id.filtre);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.filter_themes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

       filterButton=(v.findViewById(R.id.filterButto));
       filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    filter();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });



        return v;
    }

    public void filter() throws ParseException {
        Date currentdate = new Date(System.currentTimeMillis());
        filteredEvents.clear();
        for(int i=0;i < listeEvents.size();i++){
            Date datechosen = new SimpleDateFormat("dd/MM/yyyy").parse(listeEvents.get(i).getDate());
            if(!(currentdate.compareTo(datechosen)>0)){
            if(!theme.equals("Aucun filtre")) {
                if (listeEvents.get(i).getTheme().equals(theme)) {
                    filteredEvents.add(listeEvents.get(i));
                }
            }else{
                filteredEvents.add(listeEvents.get(i));
            }
        }}
        // specify an adapter (see also next example)
        mAdapter = new AdapterEvents(getContext(),filteredEvents);
        recyclerView.setAdapter(mAdapter);



    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        parent.getItemAtPosition(pos);
        switch (pos){
            case 0:
                theme="Aucun filtre";
                break;
            case 1:
                theme="Sport";
                break;
            case 2:
                theme="Musique";
                break;
            case 3 :
                theme="Photo";
                break;
            case 4 :
                theme="Video";
                break;
            case 5 :
                theme="Culture";
                break;
            case 6 :
                theme="Sciences";
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
