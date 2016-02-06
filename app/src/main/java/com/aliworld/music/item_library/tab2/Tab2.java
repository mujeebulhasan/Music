package com.aliworld.music.item_library.tab2;

/**
 * Created by MujeebulHasan on 23-09-2015.
 */

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
 * Created by hp1 on 21-01-2015.
 */
public class Tab2 extends Fragment implements AdapterView.OnItemClickListener {

    private Context context;
    public static String CURRENT_ALBUM;

    public Tab2() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2, container, false);
        GridView gridView = (GridView) v.findViewById(R.id.gridViewAlbum);
        gridView.setAdapter(new Tab2GridAdapter(getContext()));
        gridView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        TextView tv= (TextView) view.findViewById(R.id.albumName);
        String albumName=tv.getText().toString();
        CURRENT_ALBUM=albumName;
        Intent intent=new Intent(context, AlbumClickActivity.class);
        intent.setAction("ACTION_ALBUM");
        intent.putExtra("ALBUM_NAME",albumName);
        startActivity(intent);
    }
}