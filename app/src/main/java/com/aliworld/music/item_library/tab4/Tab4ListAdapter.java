package com.aliworld.music.item_library.tab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliworld.music.R;
import com.aliworld.music.playlist.PlayLists;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 02-11-2015.
 */
public class Tab4ListAdapter extends BaseAdapter {

    int playListCount;
    Context context;
    ArrayList<String> playListNames;

    public Tab4ListAdapter(Context context){
        this.context=context;
        PlayLists playLists=new PlayLists(context);
        this.playListCount=playLists.getPlayListCount();
        this.playListNames=playLists.getPlayListNames();
    }

    @Override
    public int getCount() {
        return playListCount;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.tab4_list_row, viewGroup, false);
        TextView playListName= (TextView) v.findViewById(R.id.playListName);
        playListName.setText(playListNames.get(i));

        return v;
    }
}
