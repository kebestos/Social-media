package ca.uqac.projetmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import ca.uqac.projetmobile.Messagerie.MessageActivity;
import ca.uqac.projetmobile.R;
import ca.uqac.projetmobile.Model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
   private Context mContext;
   private List<User>mUsers;

    private DatabaseReference mCustomDataBase;
    private String mProfileImageUrl;
    private String uidOwnerEvent;

   public UserAdapter(Context mContext, List<User> mUsers){
       this.mUsers = mUsers;
       this.mContext = mContext;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.users_list_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.username.setText(mUsers.get(i).getNom());
        final User user = mUsers.get(i);

        uidOwnerEvent = mUsers.get(i).getuID();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(uidOwnerEvent);

        getProfileImage(viewHolder);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getuID());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       public TextView username;
       public ImageView profile_image;

       public ViewHolder(View itemView) {
           super(itemView);

           username = itemView.findViewById(R.id.username);
           profile_image = itemView.findViewById(R.id.profile_image);

       }
   }

   private void getProfileImage(final ViewHolder holder){
       mCustomDataBase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                   Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                   if (map.get("profileImageUrl") != null) {
                       mProfileImageUrl = map.get("profileImageUrl").toString();
                       //Glide.with(app).load(mProfileImageUrl).into(ImageUser);
                       Glide.with(mContext).load(mProfileImageUrl).into(holder.profile_image);
                   }
               }
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }

}
