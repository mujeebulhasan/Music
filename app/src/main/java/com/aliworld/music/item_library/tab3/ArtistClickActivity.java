package com.aliworld.music.item_library.tab3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aliworld.music.MainActivity;
import com.aliworld.music.R;
import com.aliworld.music.listeners.MyListener;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 12-10-2015.
 */
public class ArtistClickActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    ArrayList<MainActivity.genericSongClass> artistSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_click_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryDark));
        }
        mToolbar = (Toolbar) findViewById(R.id.artist_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    protected void onResume() {
        super.onResume();
        TextView tv1 = (TextView) findViewById(R.id.artist_tv1);
        Intent i = getIntent();
        if (i.getAction() == "ACTION_ARTIST") {
            Log.i("FROM", "Artist");
            String artistName = i.getStringExtra("ARTIST_NAME");
            tv1.setText(artistName);
            if (artistName != null)
                setTitle(artistName);
            for (MainActivity.genericSongClass g : MainActivity.songs) {
                if (g.songArtist.equals(artistName)) {
                    artistSongs.add(g);
                }
            }
            if (!artistSongs.isEmpty()) {
                ListView listView = (ListView) findViewById(R.id.artist_listView);
                ArrayList<String> al = new ArrayList<>();
                for (MainActivity.genericSongClass g : artistSongs) {
                    al.add(g.songTitle);
                }
                ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);
                listView.setAdapter(arr);
                listView.setOnItemClickListener(new MyListener());
            }
        }

    }
}
