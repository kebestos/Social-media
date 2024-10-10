package ca.uqac.projetmobile.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import ca.uqac.projetmobile.Model.Chat;
import ca.uqac.projetmobile.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Chat> mChat;
    private String imageURL;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser fuser;

    private DatabaseReference mCustomDataBase;
    private String mProfileImageUrl;
    private String uidOwnerEvent;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageURL) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       if(i == MSG_TYPE_RIGHT){
           View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
           return new MessageAdapter.ViewHolder(view);

       }else{
           View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
           return new MessageAdapter.ViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
      Chat chat = mChat.get(i);

      viewHolder.show_message.setText(chat.getMessage());
        if(imageURL != null){
            Glide.with(mContext).load(imageURL).into(viewHolder.profile_image);
        }
       else{
            viewHolder.profile_image.setBackgroundResource(R.drawable.profilepic);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

   /* private void getProfileImage(final MessageAdapter.ViewHolder holder) {
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
    } */

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
