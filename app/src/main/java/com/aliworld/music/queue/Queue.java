package com.aliworld.music.queue;

import android.util.Log;

import com.aliworld.music.MainActivity;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 04-10-2015.
 */
public class Queue {

    public static ArrayList<MainActivity.genericSongClass> queue = new ArrayList<>();

    public ArrayList<MainActivity.genericSongClass> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<MainActivity.genericSongClass> _queue) {
        queue = _queue;
        MainActivity.musicSrv.setList(queue);
    }

    public ArrayList<MainActivity.genericSongClass> getAllSongs() {
        ArrayList<MainActivity.genericSongClass> songs = new ArrayList<>();
        for (MainActivity.genericSongClass g : MainActivity.songs) {
            songs.add(g);
        }
        return songs;
    }

    public void addSong(MainActivity.genericSongClass song) {
        queue.add(song);
        MainActivity.musicSrv.setList(queue);
    }

    public void addSongsArray(ArrayList<MainActivity.genericSongClass> songsToAdd) {
        for (MainActivity.genericSongClass song : songsToAdd) {
            queue.add(song);
        }
    }

    public void addAtNextPos(MainActivity.genericSongClass song) {
        if (queue.size() != 0)
            queue.add(MainActivity.musicSrv.getSongPosition() + 1, song);
        else {
            queue.add(0, song);
            MainActivity.musicSrv.setList(queue);
        }
    }

    public MainActivity.genericSongClass getSong(int position) {
        MainActivity.genericSongClass song = null;
        if (!queue.isEmpty()) {
            song = queue.get(position);
        }
        return song;
    }

    public void removeSong(int position) {
        if (!queue.isEmpty()) {
            queue.remove(position);
            MainActivity.musicSrv.setList(queue);
            Log.i("VALUE", MainActivity.songs.size() + "");
        }
    }

    public void swap(int from, int to) {
        if (from != to) {
            MainActivity.genericSongClass item = queue.get(from);
            queue.remove(item);
            queue.add(to, item);
        }
    }

    public ArrayList<String> getQueueSongsName() {
        Queue queue1 = new Queue();
        ArrayList<MainActivity.genericSongClass> songs = queue1.getQueue();
        ArrayList<String> names = new ArrayList<>();
        for (MainActivity.genericSongClass s : songs) {
            String name = "";
            name = s.songTitle;
            names.add(name);
        }
        return names;
    }

    public MainActivity.genericSongClass getSong(String path) {
        for (MainActivity.genericSongClass g : MainActivity.songs) {
            if (g.songData.equals(path)) {
                return g;
            }
        }
        return null;
    }

    public int size() {
        return queue.size();
    }

    public ArrayList<MainActivity.genericSongClass> artistSongs(String artistName) {
        ArrayList<MainActivity.genericSongClass> arr = new ArrayList<>();

        for (MainActivity.genericSongClass g : MainActivity.songs) {
            if (g.songArtist.equals(artistName)) {
                arr.add(g);
            }
        }

        return arr;
    }

    public ArrayList<MainActivity.genericSongClass> albumSongs(String albumName) {
        ArrayList<MainActivity.genericSongClass> arr = new ArrayList<>();

        for (MainActivity.genericSongClass g : MainActivity.songs) {
            if (g.songAlbum.equals(albumName)) {
                arr.add(g);
            }
        }

        return arr;
    }
}
