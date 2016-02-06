package com.aliworld.music.item_library.tab1;

/**
 * Created by MujeebulHasan on 23-09-2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aliworld.music.R;
import com.aliworld.music.listeners.MyListener;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment {

    private Tab1ListAdapter arrayAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab1, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        arrayAdapter = new Tab1ListAdapter(getContext());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new MyListener());
        return v;
    }

}