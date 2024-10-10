package ca.uqac.projetmobile.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ca.uqac.projetmobile.AddEventActivity;
import ca.uqac.projetmobile.ChildFragmentEventsAdded;
import ca.uqac.projetmobile.ChildFragmentEventsCreated;
import ca.uqac.projetmobile.R;
import ca.uqac.projetmobile.ViewPageAdapter;

public class MyEvents extends Fragment {
    private FloatingActionButton addEvent;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myevents, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabMessage);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerMessage);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFragment(new ChildFragmentEventsCreated(),"Mes évènements");
        adapter.addFragment(new ChildFragmentEventsAdded(),"Évènements ajoutés");
        viewPager.setAdapter(adapter);
    }



}
