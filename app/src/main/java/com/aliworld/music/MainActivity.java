package com.aliworld.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliworld.music.item_home.HomeFragment;
import com.aliworld.music.item_library.LibraryFragment;
import com.aliworld.music.listeners.MyListener;
import com.aliworld.music.others.NavigationDrawerCallbacks;
import com.aliworld.music.others.NavigationDrawerFragment;
import com.aliworld.music.others.SettingActivity;
import com.aliworld.music.playlist.PlayLists;
import com.aliworld.music.queue.Queue;
import com.aliworld.music.service.MusicService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        NavigationDrawerCallbacks {

    public static ArrayList<genericSongClass> songs = null;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    Handler handler;
    public static int currentPage = -1;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    public static ImageButton queue_play;
    ImageButton prev, shuffle, repeat;
    static ImageButton play_pause;
    ImageButton next;
    ImageView albumArtMini, albumArtBig, favourite;
    TextView np_title, np_album;

    //service interaction
    public static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    static SeekBar progressLayout;
    FloatingActionButton myFAB;
    RelativeLayout queue;
    private PlayLists p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        Log.i(MainActivity.class.getName(), "On Create");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryDark));
        }
        setContentView(R.layout.activity_main);
        progressLayout = (SeekBar) findViewById(R.id.progressLayout);
        progressLayout.setThumb(new ColorDrawable(Color.TRANSPARENT));
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        repeat = (ImageButton) findViewById(R.id.repeat);
        favourite = (ImageView) findViewById(R.id.favourite);
        prev = (ImageButton) findViewById(R.id.prev);
        play_pause = (ImageButton) findViewById(R.id.play_pause);
        next = (ImageButton) findViewById(R.id.next);
        queue_play = (ImageButton) findViewById(R.id.queue_play);
        albumArtMini = (ImageView) findViewById(R.id.albumArtMini);
        albumArtBig = (ImageView) findViewById(R.id.albumArtBig);
        np_title = (TextView) findViewById(R.id.np_title);
        np_album = (TextView) findViewById(R.id.np_album);
        myFAB = (FloatingActionButton) findViewById(R.id.myFAB);
        queue = (RelativeLayout) findViewById(R.id.queue);
        MyListener.setUpButtons(this, prev, play_pause, queue_play, next, progressLayout, albumArtMini, albumArtBig,
                np_title, np_album, queue, shuffle, repeat, favourite);

        prev.setOnClickListener(new MyListener());
        play_pause.setOnClickListener(new MyListener());
        next.setOnClickListener(new MyListener());
        queue_play.setOnClickListener(new MyListener());
        progressLayout.setOnSeekBarChangeListener(new MyListener());

        p = new PlayLists(this);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.class.getName(), MODE_PRIVATE);
        MyListener.currentIndex = sharedPreferences.getInt("currentIndex", -1);
        MyListener.first_time = sharedPreferences.getBoolean("first_time", true);
        Log.i(MainActivity.class.getName(), "Received index is " + sharedPreferences.getInt("currentIndex", -1));
        myFAB.setOnClickListener(new MyListener());
        handler = new Handler();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.setUserData("Ali Ansari", "iammujeebul@gmail.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.getChildAt(1).setOnClickListener(null);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelCollapsed(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        queue_play.setVisibility(View.VISIBLE);
                        if (queue.getVisibility() == View.VISIBLE) {
                            queue.setVisibility(View.GONE);
                            albumArtBig.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onPanelExpanded(View view) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        queue_play.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });

        searchAllSongs();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void saveCurrentIndex(int index) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.class.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentIndex", index);
        editor.putBoolean("first_time", true);
        editor.commit();
        Log.i(MainActivity.class.getName(), "Saved Index is" + index);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MainActivity.class.getName(), "On Start");
    }


    public void savePlaylist() {

        try {
            Queue queue = new Queue();
            ArrayList<MainActivity.genericSongClass> myQueue = queue.getQueue();
            if (myQueue != null) {
                Log.i(MainActivity.class.getName(), "Saved playlist");
                FileOutputStream fos = this.openFileOutput("music_queue", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(myQueue);
                os.close();
                fos.close();
            } else {
                Log.i(MainActivity.class.getName(), "Nothing to save");
            }
        } catch (Exception e) {
            Log.i(MainActivity.class.getName(), e.toString());
            Log.i(MainActivity.class.getName(), "Nothing to save exception");
        }
    }

    public void getPlaylist() {
        Log.i(MainActivity.class.getName(), "Received playlist");
        try {
            FileInputStream fis = this.openFileInput("music_queue");
            ObjectInputStream is = new ObjectInputStream(fis);
            ArrayList<MainActivity.genericSongClass> simpleClass =
                    (ArrayList<MainActivity.genericSongClass>) is.readObject();
            is.close();
            fis.close();
            Queue queue = new Queue();
            queue.setQueue(simpleClass);
        } catch (Exception e) {
            Log.i(MainActivity.class.getName(), e.toString());
            Log.i(MainActivity.class.getName(), "First time exception");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MainActivity.class.getName(), "On Pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MainActivity.class.getName(), "On Resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MainActivity.class.getName(), "On Stop");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                if (currentPage == 0) {
                    return;
                }
                fragment = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                currentPage = 0;
                break;
            case 1:
                if (currentPage == 1) {
                    return;
                }
                fragment = new LibraryFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.container, fragment).commit();
                currentPage = 1;
                break;
            case 2:
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                currentPage = 2;
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(MainActivity.class.getName(), "On Back pressed");

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }


    public static class genericSongClass implements Serializable, Parcelable {
        public String songTitle = "";
        public String songArtist = "";
        public String songData = "";
        public String songAlbum = "";
        public String songDuration = "";
        public String albumId = "";
        public String favourite = "0";

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
        }
    }

    public void searchAllSongs() {
        Cursor cursor = null;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        final String[] projection = new String[]{
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Albums.ALBUM_ID
        };
        //here we can change sorting order of songs
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getBaseContext().getContentResolver().query(uri,
                    projection, selection, null, sortOrder);
            if (cursor != null) {
                songs = new ArrayList<>(
                        cursor.getCount());
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    genericSongClass GSC = new genericSongClass();
                    GSC.songTitle = cursor.getString(0);
                    GSC.songArtist = cursor.getString(1);
                    GSC.songData = cursor.getString(2);
                    GSC.songAlbum = cursor.getString(3);
                    String duration = cursor.getString(4);
                    GSC.songDuration = getDuration(duration);
                    GSC.albumId = cursor.getString(5);
                    songs.add(GSC);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.i("MAIN_ACTIVITY_EXCEPTION", e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getDuration(String duration) {
        int ms = Integer.parseInt(duration);
        int sec = ms / 1000;
        int min = sec / 60;
        int remSec = sec % 60;
        if (remSec <= 9) {
            return min + ":0" + remSec;
        } else {
            return min + ":" + remSec;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(MainActivity.class.getName(), "On Destroyed");
        saveCurrentIndex(MyListener.currentIndex);
        stopService(playIntent);
        musicSrv = null;
        savePlaylist();
        super.onDestroy();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicSrv = binder.getService();
            musicBound = true;
            getPlaylist();
            MyListener.updateUi();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    public static void songPicked(int i) {
        musicSrv.setSong(i);
        musicSrv.playSong();
        queue_play.setImageResource(R.drawable.ic_pause_white_36dp);
        play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
        String durn = musicSrv.songs.get(i).songDuration;
        String min_sec[] = durn.split(":");
        int min = Integer.parseInt(min_sec[0]);
        int sec = Integer.parseInt(min_sec[1]);
        int drn = min * 60 + sec;
        MyListener.DRN = drn;
        progressLayout.setProgress(0);
        progressLayout.setMax(drn);
        MyListener.updateHandler.postDelayed(MyListener.timerRunnable, 1000);
    }

    public void shuffle(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.class.getName(), MODE_PRIVATE);
        boolean shuffle = sharedPreferences.getBoolean("shuffle", false);

        if (shuffle) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("shuffle", false);
            editor.commit();
            MyListener.changeIcon("shuffle", R.drawable.shuffle_white);
            Toast.makeText(this, "Shuffle off", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("shuffle", true);
            editor.commit();
            MyListener.changeIcon("shuffle", R.drawable.shuffle_off);
            Toast.makeText(this, "Shuffle on", Toast.LENGTH_SHORT).show();
        }
    }

    public void favourite(View view) {

        SharedPreferences preferences = getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE);
        boolean table_exist = preferences.getBoolean("favourite_table_exist", false);
        if (!table_exist) {
            p.createNewPlayList("favourite");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("favourite_table_exist", true);
            editor.commit();
        }
        String path_of_this_song = MyListener.currentSong.songData;
        String[] favourites = p.getSongsPath("favourite");

        if (favourites != null) {

            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(favourites));
            if (arrayList.contains(path_of_this_song)) {
                MyListener.changeIcon("favourite", R.drawable.favorite_not_active_empty);
                arrayList.remove(path_of_this_song);
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                //save playlist
                p.deletePlayList("favourite");
                p.createNewPlayList("favourite");
                int i = 0;
                for (String s : arrayList) {
                    p.insertSongIntoPlayList("favourite", arrayList.get(i) + ":");
                    i++;
                }
            } else {
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                MyListener.changeIcon("favourite", R.drawable.favorite_active);
                p.insertSongIntoPlayList("favourite", path_of_this_song + ":");
            }
        } else {
            Log.i(MainActivity.class.getName(), "First time");
            MyListener.changeIcon("favourite", R.drawable.favorite_active);
            p.insertSongIntoPlayList("favourite", path_of_this_song + ":");
        }
    }

    public void repeat(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.class.getName(), MODE_PRIVATE);
        String repeat = sharedPreferences.getString("repeat", "0");
        if (repeat.equals("0")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("repeat", "1");
            editor.commit();
            MyListener.changeIcon("repeat", R.drawable.repeat_one);
            Toast.makeText(this, "Repeat one", Toast.LENGTH_SHORT).show();
        } else if (repeat.equals("1")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("repeat", "2");
            editor.commit();
            MyListener.changeIcon("repeat", R.drawable.repeat_off);
            Toast.makeText(this, "Repeat all", Toast.LENGTH_SHORT).show();
        } else if (repeat.equals("2")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("repeat", "0");
            editor.commit();
            MyListener.changeIcon("repeat", R.drawable.repeat_white);
            Toast.makeText(this, "Repeat off", Toast.LENGTH_SHORT).show();
        }

    }

    public void threeDot(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        int id = view.getId();

        switch (id) {
            case R.id.tab1_list_row_three_dot_menu:
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                TextView v = (TextView) viewGroup.findViewById(R.id.textView00);
                View parent1 = (View) view.getParent();
                ListView listView1 = (ListView) parent1.getParent();
                int pos = listView1.getPositionForView(parent1);
                MyListener.three_dot_clicked_item_pos = pos;
                inflater.inflate(R.menu.tab1_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyListener());
                popup.show();
                break;

            case R.id.tab2_grid_item_three_dot_menu:

                View view1 = (View) view.getParent().getParent();
                TextView tv = (TextView) view1.findViewById(R.id.albumName);
                if (tv == null) {
                    Toast.makeText(this, "tv null", Toast.LENGTH_SHORT).show();
                } else {
                    MyListener.three_dot_album_or_artist_name = (String) tv.getText();
                }
                inflater.inflate(R.menu.tab2_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyListener());
                popup.show();
                break;

            case R.id.tab3_grid_item_three_dot_menu:

                View view2 = (View) view.getParent().getParent();
                TextView tv1 = (TextView) view2.findViewById(R.id.artistName);
                if (tv1 == null) {
                    Toast.makeText(this, "tv null", Toast.LENGTH_SHORT).show();
                } else {
                    MyListener.three_dot_album_or_artist_name = (String) tv1.getText();
                }
                inflater.inflate(R.menu.tab3_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyListener());
                popup.show();
                break;

            case R.id.tab4_list_row_three_dot_menu:
                View parent4 = (View) view.getParent();
                ListView listView3 = (ListView) parent4.getParent();
                MyListener.three_dot_clicked_item_pos = listView3.getPositionForView(parent4);
                inflater.inflate(R.menu.tab4_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyListener());
                popup.show();
                break;
        }
    }
}