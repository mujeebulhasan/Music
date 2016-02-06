package com.aliworld.music.item_library.tab1;

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

/**
 * Created by MujeebulHasan on 25-09-2015.
 */
public class Tab1ListAdapter extends BaseAdapter {


    static class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView albumName;
        TextView artistName;
    }

    Context context;
    private DisplayImageOptions options;
    LayoutInflater inflater;
    final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");


    Tab1ListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.ic_mp_song_playback)
                .showImageForEmptyUri(R.drawable.ic_mp_song_playback)
                .showImageOnFail(R.drawable.ic_mp_song_playback)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public int getCount() {
        return MainActivity.songs.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.tab1_list_row, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.textView00);
            viewHolder.albumName = (TextView) view.findViewById(R.id.textView01);
            viewHolder.artistName = (TextView) view.findViewById(R.id.textView02);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MainActivity.genericSongClass GSC = MainActivity.songs.get(i);
        viewHolder.title.setText(GSC.songTitle);
        viewHolder.albumName.setText(GSC.songAlbum);
        viewHolder.artistName.setText(GSC.songArtist);
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, Long.parseLong(MainActivity.songs.get(i).albumId));
        ImageLoader.getInstance().displayImage(albumArtUri.toString(),
                (ImageView) view.findViewById(R.id.imageView00), options,
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
