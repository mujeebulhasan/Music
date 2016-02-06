package com.aliworld.music.listeners;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliworld.music.MainActivity;
import com.aliworld.music.R;
import com.aliworld.music.item_library.tab2.Tab2;
import com.aliworld.music.item_library.tab3.Tab3;
import com.aliworld.music.item_library.tab4.Tab4;
import com.aliworld.music.others.QueueFragment;
import com.aliworld.music.playlist.PlayLists;
import com.aliworld.music.queue.Queue;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by MujeebulHasan on 06-10-2015.
 */
public class MyListener implements View.OnClickListener, AdapterView.OnItemClickListener,
        SeekBar.OnSeekBarChangeListener, PopupMenu.OnMenuItemClickListener {

    public static Handler updateHandler = new Handler();
    static SeekBar progressLayout;
    static ImageButton prev, play_pause, play_pause_1, next, shuffle, repeat;
    static ImageView albumArtMini;
    static ImageView albumArtBig;
    static TextView np_title, np_album;
    public static MainActivity.genericSongClass currentSong;
    public static int currentIndex = -1;
    public static boolean first_time = true;
    static MainActivity context;
    static public int DRN;
    public static RelativeLayout queue;
    public static int three_dot_clicked_item_pos;
    public static String three_dot_album_or_artist_name = "";
    static ImageView favourite;

    public MyListener() {
    }

    public static void setUpButtons(MainActivity _context, ImageButton _prev, ImageButton _play_pause, ImageButton _play_pause_1,
                                    ImageButton _next, SeekBar _progressLayout, ImageView _albumArtMini, ImageView _albumArtBig,
                                    TextView _np_title, TextView _np_album, RelativeLayout _queue,
                                    ImageButton _shuffle, ImageButton _repeat, ImageView _favourite) {
        prev = _prev;
        play_pause = _play_pause;
        play_pause_1 = _play_pause_1;
        next = _next;
        progressLayout = _progressLayout;
        albumArtMini = _albumArtMini;
        albumArtBig = _albumArtBig;
        np_title = _np_title;
        np_album = _np_album;
        context = _context;
        queue = _queue;
        shuffle = _shuffle;
        repeat = _repeat;
        favourite = _favourite;
    }

    public static void updateUi() {

        if (currentIndex != -1) {
            currentSong = new Queue().getSong(currentIndex);
            np_title.setText(currentSong.songTitle);
            np_album.setText(currentSong.songAlbum);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(currentSong.songData);
            byte[] b = retriever.getEmbeddedPicture();
            if (b != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                albumArtMini.setImageBitmap(bmp);
                albumArtBig.setImageBitmap(bmp);
                //NavigationDrawerFragment.navigationHeader.setBackground(new BitmapDrawable(context.getResources(),bmp));
            } else {
                albumArtMini.setImageResource(R.drawable.ic_mp_song_playback);
                albumArtBig.setImageResource(R.drawable.ic_mp_song_list);
            }

            PlayLists p = new PlayLists(context);
            String[] array = p.getSongsPath("favourite");
            if (array != null) {
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(array));
                if (arrayList.contains(currentSong.songData)) {
                    favourite.setImageResource(R.drawable.favorite_active);
                } else {
                    favourite.setImageResource(R.drawable.favorite_not_active_empty);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.prev:
                if (currentIndex != -1) {
                    playPrev();
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    first_time = false;
                }
                break;

            case R.id.play_pause:

                if (first_time) {

                    if (currentIndex != -1) {
                        MainActivity.songPicked(currentIndex);
                        first_time = false;
                        updateHandler.postDelayed(timerRunnable, 1000);
                        return;
                    }
                    return;
                }

                if (MainActivity.musicSrv.isPlaying()) {
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    MainActivity.musicSrv.play_pause();
                } else {
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    MainActivity.musicSrv.play_pause();
                }

                break;

            case R.id.next:
                if (currentIndex != -1) {
                    playNext();
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    first_time = false;
                }
                break;

            case R.id.queue_play:

                if (first_time) {

                    if (currentIndex != -1) {
                        MainActivity.songPicked(currentIndex);
                        first_time = false;
                        return;
                    }
                    return;
                }

                if (MainActivity.musicSrv.isPlaying()) {
                    play_pause_1.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                    MainActivity.musicSrv.play_pause();
                } else {
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    MainActivity.musicSrv.play_pause();
                }

                break;

            case R.id.myFAB:
                if (albumArtBig.getVisibility() == View.VISIBLE) {
                    Fragment fragment = new QueueFragment();
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container1, fragment).commitAllowingStateLoss();
                    albumArtBig.setVisibility(View.GONE);
                    queue.setVisibility(View.VISIBLE);
                } else {
                    queue.setVisibility(View.GONE);
                    albumArtBig.setVisibility(View.VISIBLE);
                }
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int id = adapterView.getId();

        switch (id) {

            case R.id.listView:
                Queue queue = new Queue();
                queue.setQueue(queue.getAllSongs());
                currentIndex = i;
                MainActivity.songPicked(i);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);
                break;

            case R.id.album_listView:
                Queue queue1 = new Queue();
                String current_album = Tab2.CURRENT_ALBUM;
                queue1.setQueue(queue1.albumSongs(current_album));
                currentIndex = i;
                MainActivity.songPicked(i);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);
                break;

            case R.id.artist_listView:
                Queue queue2 = new Queue();
                String current_artist = Tab3.CURRENT_ARTIST;
                queue2.setQueue(queue2.artistSongs(current_artist));
                currentIndex = i;
                MainActivity.songPicked(i);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);
                break;

            case R.id.playListSongs:
                Queue queue3 = new Queue();
                String currentPlaylist = Tab4.CURRENT_PLAYLIST;
                PlayLists playLists = new PlayLists(context);
                String[] songsPath = playLists.getSongsPath(currentPlaylist);
                ArrayList<MainActivity.genericSongClass> songs=new ArrayList<>();
                for (int j=0;j<songsPath.length;j++){
                    songs.add(queue3.getSong(songsPath[j]));
                }
                queue3.setQueue(songs);
                currentIndex = i;
                MainActivity.songPicked(i);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);

                break;
        }
    }

    public static void OnClickForDragList(int i) {
        currentIndex = i;
        MainActivity.songPicked(i);
        updateUi();
        play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
        play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
        first_time = false;
        updateHandler.postDelayed(timerRunnable, 1000);
    }

    public static void playNext() {
        MainActivity.musicSrv.playNext();
        currentIndex = MainActivity.musicSrv.getSongPosition();
        updateUi();
        String durn = MainActivity.musicSrv.songs.get(currentIndex).songDuration;
        String min_sec[] = durn.split(":");
        int min = Integer.parseInt(min_sec[0]);
        int sec = Integer.parseInt(min_sec[1]);
        int drn = min * 60 + sec;
        DRN = drn;
        progressLayout.setProgress(0);
        progressLayout.setMax(drn);
        updateHandler.postDelayed(timerRunnable, 1000);
    }


    public static void playPrev() {
        MainActivity.musicSrv.playPrev();
        currentIndex = MainActivity.musicSrv.getSongPosition();
        updateUi();
        String durn = MainActivity.musicSrv.songs.get(currentIndex).songDuration;
        String min_sec[] = durn.split(":");
        int min = Integer.parseInt(min_sec[0]);
        int sec = Integer.parseInt(min_sec[1]);
        int drn = min * 60 + sec;
        DRN = drn;
        progressLayout.setProgress(0);
        progressLayout.setMax(drn);
        updateHandler.postDelayed(timerRunnable, 1000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if (b && currentIndex != -1) {
            MainActivity.musicSrv.seek(i);
            progressLayout.setProgress(i);
        } else {
            if (i == DRN) {
                MainActivity.musicSrv.playNext();
                currentIndex = MainActivity.musicSrv.getSongPosition();
                String durn = MainActivity.musicSrv.songs.get(currentIndex).songDuration;
                String min_sec[] = durn.split(":");
                int min = Integer.parseInt(min_sec[0]);
                int sec = Integer.parseInt(min_sec[1]);
                int drn = min * 60 + sec;
                DRN = drn;
                progressLayout.setProgress(0);
                progressLayout.setMax(drn);
                updateHandler.postDelayed(timerRunnable, 1000);
                updateUi();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public static Runnable timerRunnable = new Runnable() {
        public void run() {
            if (MainActivity.musicSrv != null) {
                int currentDuration = MainActivity.musicSrv.getCurrentDuration();
                progressLayout.setProgress(currentDuration / 1000);
                updateHandler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.t1play:
                Queue queue = new Queue();
                MainActivity.genericSongClass song = new Queue().getAllSongs().get(three_dot_clicked_item_pos);
                ArrayList<MainActivity.genericSongClass> songs = new ArrayList<>();
                songs.add(song);
                queue.setQueue(songs);
                currentIndex = 0;
                MainActivity.songPicked(0);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);

                return true;

            case R.id.t1playNext:

                Queue queue5 = new Queue();
                if (queue5.size() == 0) {
                    new Queue().addAtNextPos(new Queue().getAllSongs().get(three_dot_clicked_item_pos));
                    currentIndex = 0;
                    MainActivity.songPicked(0);
                    updateUi();
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    first_time = false;
                    updateHandler.postDelayed(timerRunnable, 1000);
                } else {
                    new Queue().addAtNextPos(new Queue().getAllSongs().get(three_dot_clicked_item_pos));
                }
                return true;


            case R.id.t1addToQueue:

                new Queue().addSong(MainActivity.songs.get(three_dot_clicked_item_pos));
                return true;

            case R.id.t1addToPlayList:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("PlayLists");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter.add("New playlist");

                final PlayLists playLists = new PlayLists(context);
                ArrayList<String> arrayList = playLists.getPlayListNames();

                if (arrayList.size() > 3) {
                    for (int i = 3; i < arrayList.size(); i++) {
                        arrayAdapter.add(arrayList.get(i));
                    }
                }

                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stringName = arrayAdapter.getItem(i);
                        //add this song to playlist...

                        if (stringName.equals("New playlist")) {

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("PlayLists");

                            final EditText et = new EditText(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            et.setLayoutParams(lp);
                            alertDialog.setView(et);


                            alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (et.getText().length() > 0) {
                                        PlayLists playLists1 = new PlayLists(context);
                                        playLists1.createNewPlayList(et.getText() + "");
                                        playLists1.insertSongIntoPlayList(et.getText() + "",
                                                MainActivity.songs.get(MyListener.three_dot_clicked_item_pos).songData + ":");
                                    } else {
                                        Toast.makeText(context, "Name can't be empty.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            alertDialog.show();

                        } else {
                            playLists.insertSongIntoPlayList(stringName,
                                    MainActivity.songs.get(MyListener.three_dot_clicked_item_pos).songData + ":");
                        }


                    }
                });
                builder.show();
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.t2play:
                Queue queue1 = new Queue();
                ArrayList<MainActivity.genericSongClass> arr1 = queue1.albumSongs(three_dot_album_or_artist_name);
                queue1.setQueue(arr1);
                currentIndex = 0;
                MainActivity.songPicked(0);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);
                return true;


            case R.id.t2addToQueue:
                Queue queue3 = new Queue();
                ArrayList<MainActivity.genericSongClass> arr3 = queue3.albumSongs(three_dot_album_or_artist_name);
                queue3.addSongsArray(arr3);
                return true;


            case R.id.t2addToPlayList:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("PlayLists");
                final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter1.add("New playlist");

                final PlayLists playLists1 = new PlayLists(context);
                ArrayList<String> arrayList1 = playLists1.getPlayListNames();

                if (arrayList1.size() > 3) {
                    for (int i = 3; i < arrayList1.size(); i++) {
                        arrayAdapter1.add(arrayList1.get(i));
                    }
                }


                builder1.setAdapter(arrayAdapter1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stringName = arrayAdapter1.getItem(i);

                        if (stringName.equals("New playlist")) {

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("PlayLists");

                            final EditText et = new EditText(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            et.setLayoutParams(lp);
                            alertDialog.setView(et);


                            alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (et.getText().length() > 0) {
                                        PlayLists playLists1 = new PlayLists(context);
                                        playLists1.createNewPlayList(et.getText() + "");
                                        String path = "";
                                        ArrayList<MainActivity.genericSongClass> arrayList2 =
                                                new Queue().albumSongs(three_dot_album_or_artist_name);
                                        for (MainActivity.genericSongClass g : arrayList2) {
                                            path += g.songData + ":";
                                        }
                                        playLists1.insertSongIntoPlayList(et.getText() + "", path);
                                    } else {
                                        Toast.makeText(context, "Name can't be empty.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            alertDialog.show();


                        } else {
                            String path = "";
                            ArrayList<MainActivity.genericSongClass> arrayList2 =
                                    new Queue().albumSongs(three_dot_album_or_artist_name);
                            for (MainActivity.genericSongClass g : arrayList2) {
                                path += g.songData + ":";
                            }
                            playLists1.insertSongIntoPlayList(stringName, path);
                        }
                    }
                });
                builder1.show();
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.t3play:
                Queue queue2 = new Queue();
                ArrayList<MainActivity.genericSongClass> arr2 = queue2.artistSongs(three_dot_album_or_artist_name);
                queue2.setQueue(arr2);
                currentIndex = 0;
                MainActivity.songPicked(0);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);
                return true;


            case R.id.t3addToQueue:
                Queue queue4 = new Queue();
                ArrayList<MainActivity.genericSongClass> arr4 = queue4.artistSongs(three_dot_album_or_artist_name);
                queue4.addSongsArray(arr4);
                return true;


            case R.id.t3addToPlayList:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("PlayLists");
                final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter2.add("New playlist");

                final PlayLists playLists2 = new PlayLists(context);
                ArrayList<String> arrayList2 = playLists2.getPlayListNames();

                if (arrayList2.size() > 3) {
                    for (int i = 3; i < arrayList2.size(); i++) {
                        arrayAdapter2.add(arrayList2.get(i));
                    }
                }


                builder2.setAdapter(arrayAdapter2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stringName = arrayAdapter2.getItem(i);
                        if (stringName.equals("New playlist")) {

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            alertDialog.setTitle("PlayLists");

                            final EditText et = new EditText(context);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            et.setLayoutParams(lp);
                            alertDialog.setView(et);


                            alertDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (et.getText().length() > 0) {
                                        PlayLists playLists1 = new PlayLists(context);
                                        playLists1.createNewPlayList(et.getText() + "");
                                        String path = "";
                                        ArrayList<MainActivity.genericSongClass> arrayList2 =
                                                new Queue().artistSongs(three_dot_album_or_artist_name);
                                        for (MainActivity.genericSongClass g : arrayList2) {
                                            path += g.songData + ":";
                                        }
                                        playLists1.insertSongIntoPlayList(et.getText() + "", path);
                                    } else {
                                        Toast.makeText(context, "Name can't be empty.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            alertDialog.show();

                        } else {
                            String path = "";
                            ArrayList<MainActivity.genericSongClass> arrayList2 =
                                    new Queue().artistSongs(three_dot_album_or_artist_name);
                            for (MainActivity.genericSongClass g : arrayList2) {
                                path += g.songData + ":";
                            }
                            playLists2.insertSongIntoPlayList(stringName, path);
                        }
                    }
                });
                builder2.show();
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.t4play:


                Queue queue6 = new Queue();
                String playListName = new PlayLists(context).getPlayListNames().get(three_dot_clicked_item_pos);
                String[] songsPath = new PlayLists(context).getSongsPath(playListName);
                if (songsPath != null && songsPath.length > 0) {
                    ArrayList<MainActivity.genericSongClass> arr6 = new ArrayList<>();
                    for (int i = 0; i < songsPath.length; i++) {
                        arr6.add(new Queue().getSong(songsPath[i]));
                    }
                    queue6.setQueue(arr6);
                    currentIndex = 0;
                    MainActivity.songPicked(0);
                    updateUi();
                    play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                    play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                    first_time = false;
                    updateHandler.postDelayed(timerRunnable, 1000);
                }
                return true;

            case R.id.t4addToQueue:
                Queue queue7 = new Queue();
                ArrayList<MainActivity.genericSongClass> arr7 = new ArrayList<>();
                String playListName7 = new PlayLists(context).getPlayListNames().get(three_dot_clicked_item_pos);
                String[] songsPath7 = new PlayLists(context).getSongsPath(playListName7);
                for (int i = 0; i < songsPath7.length; i++) {
                    arr7.add(new Queue().getSong(songsPath7[i]));
                }
                queue7.addSongsArray(arr7);
                return true;

            case R.id.t4Rename:
                return true;

            case R.id.t4Delete:
                new PlayLists(context).deletePlayList(new PlayLists(context).getPlayListNames().get(three_dot_clicked_item_pos));
                return true;

            case R.id.plPlay:

                Queue queue8 = new Queue();
                String currentPlaylist = Tab4.CURRENT_PLAYLIST;
                PlayLists playLists_ = new PlayLists(context);
                String[] songsPath_ = playLists_.getSongsPath(currentPlaylist);
                ArrayList<MainActivity.genericSongClass> songs_=new ArrayList<>();
                for (int j=0;j<songsPath_.length;j++){
                    songs_.add(queue8.getSong(songsPath_[j]));
                }
                queue8.setQueue(songs_);
                currentIndex = three_dot_clicked_item_pos;
                MainActivity.songPicked(three_dot_clicked_item_pos);
                updateUi();
                play_pause.setImageResource(R.drawable.ic_pause_white_36dp);
                play_pause_1.setImageResource(R.drawable.ic_pause_white_36dp);
                first_time = false;
                updateHandler.postDelayed(timerRunnable, 1000);

                return true;


            case R.id.plRemoveFromPlayList:



                return true;



            default:
                return false;
        }
    }

    public static void changeIcon(String button, int icon) {


        if (button.equals("shuffle")) {
            shuffle.setImageResource(icon);

        } else if (button.equals("repeat")) {
            repeat.setImageResource(icon);
        } else if (button.equals("favourite")) {
            favourite.setImageResource(icon);
        }

    }
}
