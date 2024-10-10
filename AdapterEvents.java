package ca.uqac.projetmobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import ca.uqac.projetmobile.UI.Profile;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterEvents extends RecyclerView.Adapter<AdapterEvents.MyViewHolder> {

    Context context;
    ArrayList<Event> events;

    private DatabaseReference mCustomDataBase;
    private String mProfileImageUrl;
    private String uidOwnerEvent;
    private String dateEvent;

    public AdapterEvents(Context c, ArrayList<Event> e ){
        context = c;
        events = e;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.events_cardview,viewGroup,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final Event eventD =events.get(i);
        holder.title.setText(events.get(i).getTitle());
        holder.adresse.setText(events.get(i).getAdresse().toString());
        holder.description.setText(events.get(i).getDescription());

        dateEvent = eventD.getDate();
        if(EventIsPast()){
            holder.redCircle.setVisibility(View.VISIBLE);
        }
        else{
            holder.redCircle.setVisibility(View.INVISIBLE);
        }

        uidOwnerEvent = events.get(i).getUid();
        mCustomDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(uidOwnerEvent);

        getInfos(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context ,DetailEvent.class);
                intent.putExtra("event",    eventD);
                context.startActivity(intent);
            }
        });

        }



    @Override
    public int getItemCount() {
        return events.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,adresse;
        CircleImageView ownerEventPicture;
        ImageView redCircle;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleEvent);
            adresse = (TextView) itemView.findViewById(R.id.adresseText);
            description = (TextView) itemView.findViewById(R.id.descriptionEventTextV);
            ownerEventPicture = (CircleImageView) itemView.findViewById(R.id.OwnerPictureCircleV);
            redCircle = (ImageView) itemView.findViewById(R.id.redCircle);
        }
    }

    private void getInfos(final MyViewHolder holder) {
        mCustomDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("profileImageUrl")!=null){
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        //Glide.with(app).load(mProfileImageUrl).into(ImageUser);
                        Glide.with(context).load(mProfileImageUrl).into(holder.ownerEventPicture);
                    }
                    /*if(map.get("uidFacebook")!= null){
                        Bitmap picturProfileOwnerEventBitmap = getFacebookProfilePicture(map.get("uidFacebook").toString());
                        Glide.with(context).asBitmap().load(picturProfileOwnerEventBitmap).into(holder.ownerEventPicture);
                    }*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=small");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    boolean EventIsPast(){
        Calendar calendar = Calendar.getInstance();
        int yearCurrent = calendar.get(Calendar.YEAR);
        int monthCurrent = calendar.get(Calendar.MONTH);
        int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);

        monthCurrent += 1;

        //dateEvent = CurrentEvent.getDate();
        String [] dateParts = dateEvent.split("/");
        int dayEvent = Integer.parseInt(dateParts[0]);
        int monthEvent = Integer.parseInt(dateParts[1]);
        int yearEvent = Integer.parseInt(dateParts[2]);

        if(yearEvent < yearCurrent){
            return true;
        }
        else if(monthEvent < monthCurrent && yearEvent == yearCurrent){
            return true;
        }
        else if(dayEvent < dayCurrent && monthEvent == monthCurrent && yearEvent == yearCurrent){
            return true;
        }
        else
            return false;
    }
}
