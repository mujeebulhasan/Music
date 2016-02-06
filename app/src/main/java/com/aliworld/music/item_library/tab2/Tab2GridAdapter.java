package com.aliworld.music.item_library.tab2;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliworld.music.MainActivity;
import com.aliworld.music.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by MujeebulHasan on 25-09-2015.
 */
public class Tab2GridAdapter extends BaseAdapter {

    static class ViewHolder {
        ImageView imageView;
        TextView albumName;
        TextView artistName;
    }

    Context context;
    LayoutInflater inflater;
    private DisplayImageOptions options;
    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
    ArrayList<String> albumNames = new ArrayList<>();
    ArrayList<MainActivity.genericSongClass> albums = new ArrayList<>();


    public Tab2GridAdapter(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.ic_mp_album_list)
                .showImageForEmptyUri(R.drawable.ic_mp_album_list)
                .showImageOnFail(R.drawable.ic_mp_album_list)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);

        //arrange album here
        setupAlbums();

    }

    private void setupAlbums() {

        for (int i = 0; i < MainActivity.songs.size(); i++) {

            MainActivity.genericSongClass g = MainActivity.songs.get(i);
            String albumName = g.songAlbum;
            if (albumNames.contains(albumName)) {

            } else {
                albumNames.add(albumName);
                albums.add(g);
            }
        }

        //sort array by name "albums"
        Collections.sort(albums, new Comparator<MainActivity.genericSongClass>() {
            @Override
            public int compare(MainActivity.genericSongClass g1, MainActivity.genericSongClass g2) {
                return g1.songAlbum.compareTo(g2.songAlbum);
            }
        });
    }


    @Override
    public int getCount() {
        return albums.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.tab2_grid_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.albumName = (TextView) view.findViewById(R.id.albumName);
            viewHolder.artistName = (TextView) view.findViewById(R.id.albumArtist);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.albumName.setText(albums.get(i).songAlbum);
        viewHolder.artistName.setText(albums.get(i).songArtist);
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, Long.parseLong(albums.get(i).albumId));
        ImageLoader.getInstance().displayImage(albumArtUri.toString(),
                (ImageView) view.findViewById(R.id.albumPic), options,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });

        return view;
    }
}
