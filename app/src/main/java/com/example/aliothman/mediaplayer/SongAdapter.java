package com.example.aliothman.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AliOthman on 8/31/2017.
 */

public class SongAdapter extends BaseAdapter {

    ArrayList<SongInfo> songInfos;
    Context context;

    public SongAdapter(ArrayList<SongInfo> songInfos, Context context) {
        this.songInfos = songInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return songInfos.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.item , null);
        final SongInfo so = songInfos.get(position);
        TextView textView = (TextView) v.findViewById(R.id.Songnametv);
        textView.setText(so.Song_name);
        TextView textView1 = v.findViewById(R.id.artisinametv);
        textView1.setText(so.artist_name);
        return v;
    }
}
