package com.aliworld.music.item_library.tab2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
 * Created by MujeebulHasan on 04-10-2015.
 */
public class AlbumClickActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    ArrayList<MainActivity.genericSongClass> albumSongs = new ArrayList<>();
    ArrayList<MainActivity.genericSongClass> artistSongs = new ArrayList<>();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_click_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryDark));
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView tv1 = (TextView) findViewById(R.id.aac_tv1);
        Intent i = getIntent();

        if (i.getAction() == "ACTION_ALBUM") {
            String albumName = i.getStringExtra("ALBUM_NAME");
            tv1.setText(albumName);
            if (albumName != null)
                setTitle(albumName);
            for (MainActivity.genericSongClass g : MainActivity.songs) {
                if (g.songAlbum.equals(albumName)) {
                    albumSongs.add(g);
                }
            }
            if (!albumSongs.isEmpty()) {
                ListView listView = (ListView) findViewById(R.id.album_listView);
                ArrayList<String> al = new ArrayList<>();
                for (MainActivity.genericSongClass g : albumSongs) {
                    al.add(g.songTitle);
                }

                ArrayAdapter<String> arr = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);
                listView.setAdapter(arr);
                listView.setOnItemClickListener(new MyListener());

            }
        }

    }
}
