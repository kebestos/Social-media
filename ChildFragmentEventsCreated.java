package ca.uqac.projetmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChildFragmentEventsCreated extends Fragment {
    private View view;
    private DatabaseReference userRef,eventRef;
    private FirebaseAuth mAuth;
    private String currentID;
    private RecyclerView rvEvents;
    private ArrayList<Event> createdEvents, listEvents;
    private AdapterEvents adapter;
    private Button createEvent;


    public ChildFragmentEventsCreated() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events_created_child_fragment,container,false);
        rvEvents = (RecyclerView) view.findViewById(R.id.rvEventsCreated);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        createEvent = (Button) view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(view.getContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });
        createdEvents = new ArrayList<Event>();

        listEvents = new ArrayList<Event>();


        mAuth = FirebaseAuth.getInstance();
        currentID =mAuth.getCurrentUser().getUid();
        //Log.d("EQUALS",currentID);
        eventRef = FirebaseDatabase.getInstance().getReference().child("events");
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createdEvents.clear();

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Event event = dataSnapshot1.getValue(Event.class);
                    listEvents.add(event);

                    /*if (event.getUid() == currentID){
                    }*/
                }

                for(int i = 0; i<listEvents.size();i++){
                    if(listEvents.get(i).getUid().equals(currentID) ){
                        createdEvents.add(listEvents.get(i));
                    }
                }

                adapter = new AdapterEvents(getContext(),createdEvents);
                rvEvents.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
  /*  @Override
     public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(eventRef,Event.class)
                .build();

        FirebaseRecyclerAdapter<Event,EventsViewHolder>adapter = new FirebaseRecyclerAdapter<Event, EventsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final EventsViewHolder holder, int position, @NonNull Event model) {
                eventRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            if(dataSnapshot1.child("uid").getValue()==currentID){
                                String titleD = dataSnapshot1.child("title").getValue().toString();
                                String adressD = dataSnapshot1.child("adresse").getValue().toString();
                                String descriptionD = dataSnapshot1.child("description").getValue().toString();

                                holder.title.setText(titleD);
                                holder.adress.setText(adressD);
                                holder.description.setText(descriptionD);

                            }



                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_cardview,viewGroup,false);
                EventsViewHolder viewHolder = new EventsViewHolder(view);
                return viewHolder;
            }
        };

        rvEvents.setAdapter(adapter);
        adapter.startListening();
     }
     public static class EventsViewHolder extends RecyclerView.ViewHolder
     {
        TextView title,adress,description;

        public EventsViewHolder(View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title);
            adress = itemView.findViewById(R.id.adress);
            description = itemView.findViewById(R.id.description);


        }
     } */

}
