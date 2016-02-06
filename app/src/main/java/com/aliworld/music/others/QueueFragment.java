package com.aliworld.music.others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.aliworld.music.R;
import com.aliworld.music.listeners.MyListener;
import com.aliworld.music.playlist.PlayLists;
import com.aliworld.music.queue.Queue;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;

/**
 * Created by MujeebulHasan on 11-10-2015.
 */
public class QueueFragment extends ListFragment implements DragSortListView.DropListener, DragSortListView.RemoveListener, AdapterView.OnItemLongClickListener {


    private ArrayAdapter<String> adapter;
    private ArrayList<String> list;
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MyListener.OnClickForDragList(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.queue_fragment, container, false);
        DragSortListView lv = (DragSortListView) v.findViewById(android.R.id.list);
        DragSortController controller = new DragSortController(lv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DRAG);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        controller.setFlingHandleId(R.id.drag_handle);
        lv.setOnTouchListener(controller);
        lv.setFloatViewManager(controller);
        lv.setDragEnabled(true);
        lv.setDropListener(this);
        lv.setRemoveListener(this);
        lv.setOnItemLongClickListener(this);
        list = new Queue().getQueueSongsName();
        adapter = new ArrayAdapter<>(context, R.layout.list_item_handle_right, R.id.text, list);
        setListAdapter(adapter);
        return v;
    }


    @Override
    public void drop(int from, int to) {
        String item = adapter.getItem(from);
        adapter.notifyDataSetChanged();
        adapter.remove(item);
        adapter.insert(item, to);
        new Queue().swap(from, to);
    }

    @Override
    public void remove(int which) {
        adapter.remove(adapter.getItem(which));
        new Queue().removeSong(which);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("PlayLists");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
        arrayAdapter.add("Remove from Queue");
        arrayAdapter.add("Add to Playlist");
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                String stringName = arrayAdapter.getItem(j);
                if (stringName.equals("Remove from Queue")){
                    remove(i);
                }else if(stringName.equals("Add to Playlist")){

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
                        public void onClick(DialogInterface dialogInterface, int k) {
                            String stringName = arrayAdapter.getItem(k);
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
                                    public void onClick(DialogInterface dialogInterface, int j) {
                                        if (et.getText().length() > 0) {
                                            PlayLists playLists1 = new PlayLists(context);
                                            playLists1.createNewPlayList(et.getText() + "");
                                            playLists.insertSongIntoPlayList(et.getText()+"",
                                                    new Queue().getSong(i).songData+":");
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
                                        new Queue().getSong(i).songData+":");
                            }


                        }
                    });
                    builder.show();

                }
            }
        });
        builder.show();
        return true;
    }
}
