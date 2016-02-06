package com.aliworld.music.item_library.tab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.aliworld.music.R;


/**
 * Created by MujeebulHasan on 23-09-2015.
 */
public class Tab3 extends Fragment implements AdapterView.OnItemClickListener {

    private Context context;
    public static String CURRENT_ARTIST;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridViewArtist);
        gridView.setAdapter(new Tab3GridAdapter(getContext()));
        gridView.setOnItemClickListener(this);
        return v;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv= (TextView) view.findViewById(R.id.artistName);
        String artistName=tv.getText().toString();
        CURRENT_ARTIST=artistName;
        Intent intent=new Intent(context, ArtistClickActivity.class);
        intent.setAction("ACTION_ARTIST");
        intent.putExtra("ARTIST_NAME",artistName);
        startActivity(intent);
    }
}