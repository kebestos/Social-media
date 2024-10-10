package ca.uqac.projetmobile.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.uqac.projetmobile.ChildFragmentEventsAdded;
import ca.uqac.projetmobile.ChildFragmentEventsCreated;
import ca.uqac.projetmobile.Messagerie.ChatFragment;
import ca.uqac.projetmobile.Messagerie.UserListFragment;
import ca.uqac.projetmobile.R;
import ca.uqac.projetmobile.ViewPageAdapter;

public class Messaging extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messaging, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabMessage);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerMessage);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFragment(new ChatFragment(),"Chat");
        adapter.addFragment(new UserListFragment(),"Utilisateurs");
        viewPager.setAdapter(adapter);
    }
}
