package com.aliworld.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aliworld.music.MainActivity;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 05-10-2015.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    public static ArrayList<MainActivity.genericSongClass> songs = new ArrayList();

    public int getSongPosition() {
        return songPosition;
    }

    public static int songPosition = -1;
    private final IBinder musicBind = new MusicBinder();

    public void setList(ArrayList<MainActivity.genericSongClass> theSongs) {
        songs = theSongs;
    }

    public int getCount() {
        return songs.size();
    }

    public void playSong() {
        player.reset();
        MainActivity.genericSongClass playSong = null;
        if (songs.size() != 0) {
            playSong = songs.get(songPosition);
        } else {
            Toast.makeText(getApplicationContext(), "Empty list.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            player.setDataSource(playSong.songData);
            player.prepare();
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void play_pause() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
    }

    public void addSong(MainActivity.genericSongClass song) {
        if (songs.size() != 0)
            songs.add(songPosition + 1, song);
        else {
            songs.add(0, song);
            songPosition = 0;
            playSong();
        }

    }

    public void setSong(int songIndex) {
        songPosition = songIndex;
    }

    public void removeSong(int position) {
        songs.remove(position);
    }


    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void playPrev() {
        songPosition--;
        if (songPosition < 0) songPosition = songs.size() - 1;
        playSong();
    }

    //skip to next
    public void playNext() {
        songPosition++;
        if (songPosition >= songs.size()) songPosition = 0;
        playSong();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    public int getPosition() {
        return player.getCurrentPosition();
    }


    public int getDur() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn * 1000);
    }

    public void go() {
        player.start();
    }


    public int getCurrentDuration() {
        return player.getCurrentPosition();
    }
}
