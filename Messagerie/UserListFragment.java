package ca.uqac.projetmobile.Messagerie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.projetmobile.Adapter.UserAdapter;
import ca.uqac.projetmobile.R;
import ca.uqac.projetmobile.Model.User;

public class UserListFragment extends Fragment {
    View view;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    public UserListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_user_list_fragment,container,false);
        recyclerView= view.findViewById(R.id.recycler_viewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();
        return view;
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (final DataSnapshot snapshot : dataSnapshot.child(userid).child("ImFollowing").getChildren()){
                    final String followerid = (String) snapshot.getKey();
                    String nom = (String) dataSnapshot.child(followerid).child("nom").getValue();
                    String profileImg = (String) dataSnapshot.child(followerid).child("profileImageUrl").getValue();
                    String uId = followerid;
                    String email = (String) dataSnapshot.child(followerid).child("email").getValue();
                    User user = new User (nom,uId,profileImg,email);
                    mUsers.add(user);


                  /*  String nom = (String) snapshot.child("nom").getValue();
                    String profileImg = (String) snapshot.child("profileImg").getValue();
                    String uId = (String) snapshot.getKey();
                    String email = (String) snapshot.child("email").getValue();

                   User user = new User(nom, uId,profileImg,email);
                   if(!user.getuID().equals(firebaseUser.getUid())){
                       mUsers.add(user);
                   } */
                }
                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
