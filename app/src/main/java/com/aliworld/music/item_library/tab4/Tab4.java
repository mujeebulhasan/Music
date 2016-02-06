package com.aliworld.music.item_library.tab4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aliworld.music.R;
import com.aliworld.music.playlist.PlayListClickActivity;

/**
 * Created by MujeebulHasan on 23-09-2015.
 */
public class Tab4 extends Fragment implements AdapterView.OnItemClickListener {

    private Tab4ListAdapter arrayAdapter;
    private Context context;
    public static String CURRENT_PLAYLIST;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab4, container, false);
        ListView listView = (ListView) v.findViewById(R.id.playLists);
        arrayAdapter = new Tab4ListAdapter(getContext());
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView tv = (TextView) view.findViewById(R.id.playListName);
        String playListName = tv.getText().toString();
        CURRENT_PLAYLIST=playListName;
        Intent intent = new Intent(context, PlayListClickActivity.class);
        intent.putExtra("PLAYLIST_NAME", playListName);
        startActivity(intent);
    }
}