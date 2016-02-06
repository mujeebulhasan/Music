package com.aliworld.music.playlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.aliworld.music.MainActivity;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 02-11-2015.
 */
public class PlayLists {

    Context context;
    int playListCount;

    ArrayList<String> playListNames = new ArrayList<>();
    public final String TABLE_NAME = "play_lists";
    public final String MOST_PLAYED = "Most played";
    public final String RECENTLY_PLAYED = "Recently played";
    public final String RECENTLY_ADDED = "Recently added";
    public String raw = "";
    public SQLiteDatabase database;

    public PlayLists(Context context) {
        this.context = context;
        init();
        playListCount = getPlayListCount();
    }

    public ArrayList<String> getPlayListNames() {
        Cursor cursor = database.rawQuery("SELECT name FROM " + TABLE_NAME + ";", null);
        if (cursor != null) {
            int i = 0;
            while (cursor.moveToNext()) {
                String uname = cursor.getString(cursor.getColumnIndex("name"));
                playListNames.add(uname);
                i++;
            }
        }
        return playListNames;
    }


    private void init() {
        database = context.openOrCreateDatabase("musicdb.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        SharedPreferences preferences = context.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE);
        boolean table_exist = preferences.getBoolean("table_exist", false);
        if (!table_exist) {
            //create table and default playlist in table...
            database.execSQL("create table " + TABLE_NAME + "(name varchar(50) primary key,paths varchar(50))");
            createNewPlayList(MOST_PLAYED);
            createNewPlayList(RECENTLY_ADDED);
            createNewPlayList(RECENTLY_PLAYED);
            createNewPlayList("PlayList1");
            SharedPreferences preferences1 = context.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putBoolean("table_exist", true);
            editor.commit();
            Log.i(PlayLists.class.getName(), "Table created...");
        } else {
            Log.i(PlayLists.class.getName(), "Table Already exists...");
        }
    }

    public void createNewPlayList(String name) {
        database.execSQL("insert into " + TABLE_NAME + " values('" + name + "','" + raw + "')");
    }

    public void deletePlayList(String name) {
        //delete query here
        database.execSQL("DELETE FROM "+TABLE_NAME+" WHERE name='"+name+"'");
    }

    public void insertSongIntoPlayList(String playListName, String songPath) {
        String paths = "";
        Cursor cursor = database.query(TABLE_NAME, new String[]{"paths"}, "name=?", new String[]{playListName}, null, null, null);
        if (cursor.moveToNext()) {
            paths = cursor.getString(cursor.getColumnIndex("paths"));
            Log.i(PlayLists.class.getName(), "Value of path" + paths);
            cursor.close();
        }
        paths += songPath;
        database.execSQL("update " + TABLE_NAME + " set paths='" + paths + "' where name='" + playListName + "'");
        Log.i(PlayLists.class.getName(), paths);
    }

    public void deleteSongFromPlaylist(String playListName, String songPath) {
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        String paths = "";
        Cursor cursor = database.query(TABLE_NAME, new String[]{"paths"}, "name=?", new String[]{playListName}, null, null, null);
        if (cursor.moveToNext()) {
            paths = cursor.getString(cursor.getColumnIndex("paths"));
            Log.i(PlayLists.class.getName(), "Value of path" + paths);
            cursor.close();
        }
        String[] allPaths = paths.split(":");

    }


    public int getPlayListCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + ";", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
            return count;
        }
        return 0;
    }

    public int getSongCount(String playListName) {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT paths from " + TABLE_NAME + " WHERE name='" + playListName + "';", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String paths = cursor.getString(cursor.getColumnIndex("paths"));
                if (paths.length() > 0) {
                    String[] path = paths.split(":");
                    count = path.length;
                }
            }
        }
        return count;
    }

    public String[] getSongsPath(String playListName) {
        Cursor cursor = database.rawQuery("SELECT paths from " + TABLE_NAME + " WHERE name='" + playListName + "';", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String paths = cursor.getString(cursor.getColumnIndex("paths"));
                if (paths.length() > 0) {
                    return paths.split(":");
                }
            }
        }
        return null;
    }
}
