package com.aliworld.music.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliworld.music.R;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 01-10-2015.
 */
public class MyDragListAdapter extends BaseAdapter {

    ArrayList<String> al;
    LayoutInflater inflater;
    Context context;

    public MyDragListAdapter(ArrayList<String> al, Context context) {
        this.al = al;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int i) {
        return al.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = inflater.inflate(R.layout.draglist_row_item, viewGroup, false);
        TextView tv = (TextView) v.findViewById(R.id.dr_tv);
        tv.setText(al.get(i));
        return v;
    }
}
