package ca.uqac.projetmobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ChildFragmentEventsAdded extends Fragment {
    private View view;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userId;
    private RecyclerView rvEvents;
    private ArrayList<Event>listEvents;
    private ArrayList<Event>createdEvents;
    private AdapterEvents adapter;

    public ChildFragmentEventsAdded() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events_added_child_fragment,container,false);
        rvEvents = (RecyclerView) view.findViewById(R.id.rvEventsAdded);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        createdEvents =new  ArrayList<Event>();
        listEvents =new  ArrayList<Event>();

        mAuth = FirebaseAuth.getInstance();
        userId =mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createdEvents.clear();
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final Event event = dataSnapshot1.getValue(Event.class);
                        if (dataSnapshot1.child("participants") != null) {
                            listEvents.add(event);

                            DatabaseReference newRef = mDatabase.child(event.getId()).child("participants");
                            newRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot){
                                    if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                        if (map.get(userId) != null) {
                                            createdEvents.add(event);
                                        }
                                        adapter = new AdapterEvents(getContext(), createdEvents);
                                        rvEvents.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
