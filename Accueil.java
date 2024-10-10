package ca.uqac.projetmobile;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.uqac.projetmobile.UI.Home;
import ca.uqac.projetmobile.UI.Messaging;
import ca.uqac.projetmobile.UI.MyEvents;
import ca.uqac.projetmobile.UI.Profile;

public class Accueil extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    //getSupportActionBar().setTitle("Accueil");
                    selectedFragment = new Home();
                    break;
                case R.id.navigation_events:
                    //getSupportActionBar().setTitle("Evènements");
                    selectedFragment = new MyEvents();
                    break;
                case R.id.navigation_messages:
                    //getSupportActionBar().setTitle("Messagerie");
                    selectedFragment = new Messaging();
                    break;
                case R.id.navigation_profile:
                    //getSupportActionBar().setTitle("Profil");
                    selectedFragment = new Profile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //getSupportActionBar().setTitle("Accueil");
        LocationManager locManager;
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
/** Test si le gps est activé ou non */
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            /** on lance notre activity (qui est une dialog) */
            Intent localIntent = new Intent(this, PermissionGps.class);
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(localIntent);
        }
        showFragment(new Home());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }
}
