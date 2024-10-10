package ca.uqac.projetmobile;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private ArrayList<Event> listE = new ArrayList<Event>();

    private CircleImageView PictureOwnerEvent;
    private TextView TitleV;
    private TextView descriptionV;
    private TextView particpantnumberV;
    private CardView CardViewEventDetail;
    private String NbPlacesRestantes;
    private String OwnerEventId;
    private String mProfileImageUrl;
    private Event CurrentEvent;
    private static final int REQUEST_CODE_READ_CONTACTS = 99; // Can be any value >= 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        CardViewEventDetail = (CardView) findViewById(R.id.OwnerEventCardView);
        CardViewEventDetail.setVisibility(View.GONE);
        PictureOwnerEvent = (CircleImageView) findViewById(R.id.OwnerPictureCircleV);
        TitleV = (TextView) findViewById(R.id.titleEvent);
        descriptionV = (TextView) findViewById(R.id.descriptionEventTextV);
        particpantnumberV  = (TextView) findViewById(R.id.NumberPlace);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");

        Intent intent = getIntent();
        //get the attached extras from the intent
        //we should use the same key as we used to attach the data.
        listE=  intent.getParcelableArrayListExtra("events");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CardViewEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentEvent != null){
                    Intent intent = new Intent (MapsActivity.this ,DetailEvent.class);
                    intent.putExtra("event",    CurrentEvent);
                    startActivity(intent);
                }
            }
        });
    }

    private void getInfo(Event e){
        CardViewEventDetail.setVisibility(View.VISIBLE);

        TitleV.setText(e.getTitle());
        descriptionV.setText(e.getDescription());
        OwnerEventId = e.getUid();
        CurrentEvent = e;

        DatabaseReference newRef = mDatabase.child(e.getId());
        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

        setPictureProfileOwnerEvent();
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
                        if(!MapsActivity.this.isDestroyed())
                            Glide.with(MapsActivity.this).load(mProfileImageUrl).into(PictureOwnerEvent);                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void initMapStuff() {
        //move map to a fixed initial location if required
        if (checkPermission()) {
            //or move map to user location. - this is defined in next step
            getLastLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            requestPermission();

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
initMapStuff();

        final Map<String, Event> markers = new HashMap<String, Event>();

           int i;

           for(Event e : listE){
               Log.i("myTag", e.getAdresse().toString());
               Adresse ad= e.getAdresse();
               LatLng address = getLocationFromAddress(this, ad.toString());
               if(address != null){
                   Marker mkr =   mMap.addMarker(new MarkerOptions().position(address).title(e.getTitle()));
                   markers.put(mkr.getId(), e);

               }
           }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
          public boolean onMarkerClick(Marker m) {
              Event event = markers.get(m.getId());
               getInfo(event);
               return true;
            }
       });

    }
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }



    public void getLastLocation() {
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                if (mMap != null) {
                                    Log.i("MapDemoActivity", ""+ location.getLatitude() + " " +location.getLongitude());

                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) { e.printStackTrace(); }
    }


    private void requestPermission() {
        requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_ASK_PERMISSIONS);



    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case  REQUEST_CODE_ASK_PERMISSIONS:

                if (grantResults.length > 0) {

                    boolean finelocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarselocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (checkPermission()) {

                            if (mMap != null) {
                                Log.i("MapDemoActivity", "BOUCLE");

                                getLastLocation();
                                mMap.setMyLocationEnabled(true);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            }
                        }
                     else {
                        //show error msg
                    }
        }
                break;
        }
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(this.getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }
}
