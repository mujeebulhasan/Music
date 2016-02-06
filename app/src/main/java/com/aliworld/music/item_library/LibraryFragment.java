package com.aliworld.music.item_library;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliworld.music.R;
import com.aliworld.music.others.ViewPagerAdapter;
import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by MujeebulHasan on 23-09-2015.
 */
public class LibraryFragment extends Fragment {
    private Context context;
    ViewPager pager;
    ViewPagerAdapter adapter;
    PagerSlidingTabStrip tabs;
    CharSequence Titles[] = {"Songs", "Albums", "Artists", "PlayLists"};
    int Numboftabs = 4;

    public LibraryFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        adapter = new ViewPagerAdapter(context,getActivity().getSupportFragmentManager(), Titles, Numboftabs);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        //tabs.setDistributeEvenly(true);
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        /*tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });*/
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        return rootView;
    }
}