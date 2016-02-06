package com.aliworld.music.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliworld.music.MainActivity;
import com.aliworld.music.R;
import com.aliworld.music.queue.Queue;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 02-11-2015.
 */
public class PlayListSongAdapter extends BaseAdapter {

    int songsCount;
    Context context;
    String[] paths;
    ArrayList<MainActivity.genericSongClass> songs;

    public PlayListSongAdapter(Context applicationContext, int playListCount, String[] paths) {
        this.songsCount = playListCount;
        this.context = applicationContext;
        this.paths = paths;
        songs = new ArrayList<>();
        for (int i = 0; i < songsCount; i++) {
            songs.add(new Queue().getSong(paths[i]));
        }
    }

    @Override
    public int getCount() {
        return songsCount;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.playlist_song_row, viewGroup, false);
        TextView title = (TextView) v.findViewById(R.id.textView00);
        TextView album = (TextView) v.findViewById(R.id.textView01);
        TextView artist = (TextView) v.findViewById(R.id.textView02);
        title.setText(songs.get(i).songTitle);
        album.setText(songs.get(i).songAlbum);
        artist.setText(songs.get(i).songArtist);
        return v;
    }
}
