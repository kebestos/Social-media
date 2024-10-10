package ca.uqac.projetmobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class show_event extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView adresse;
    public TextView description;
    public TextView titre;
    public TextView date;
    public TextView nbplaces;



    public show_event() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment show_event.
     */
    // TODO: Rename and change types and number of parameters
    public static show_event newInstance(String param1, String param2) {
        show_event fragment = new show_event();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_show_event, container, false);


        this.adresse=v.findViewById(R.id.adresse_view);
        this.description=v.findViewById(R.id.description_view);
        this.titre=v.findViewById(R.id.title_view);
        this.date=v.findViewById(R.id.date_view);
        this.nbplaces=v.findViewById(R.id.nbPlace_view);
        return v;
    }

    public void changeDescription(String d){
this.description.setText(d);
    }

    public void changeAdresse(String d){
        this.adresse.setText(d);
    }
    public void changeDate(String d){
        this.date.setText(d);
    }
    public void changeTitre(String d){
        this.titre.setText(d);
    }
    public void changeNb(String d){
        this.nbplaces.setText(d);
    }
}
