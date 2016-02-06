package com.aliworld.music.playlist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aliworld.music.R;
import com.aliworld.music.listeners.MyListener;

/**
 * Created by MujeebulHasan on 02-11-2015.
 */
public class PlayListClickActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    public Toolbar mToolbar;
    public String playListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_click_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryDark));
        }
        mToolbar = (Toolbar) findViewById(R.id.playlist_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        playListName = i.getStringExtra("PLAYLIST_NAME");
        setTitle(playListName);

        //get no of songs from this playlist
        PlayLists playLists=new PlayLists(getApplicationContext());
        int songsCount=playLists.getSongCount(playListName);
        String[] paths=playLists.getSongsPath(playListName);
        ListView listView= (ListView) findViewById(R.id.playListSongs);
        PlayListSongAdapter arrayAdapter = new PlayListSongAdapter(getApplicationContext(),songsCount,paths);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new MyListener());
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();

        ListView listView = (ListView) adapterView;
        int pos = listView.getPositionForView(view);
        MyListener.three_dot_clicked_item_pos = pos;
        inflater.inflate(R.menu.playlist_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyListener());
        popup.show();

        return true;
    }
}
