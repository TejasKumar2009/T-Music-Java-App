package com.tejas.tmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SongsListAdapter extends ArrayAdapter<String>  {

        public ArrayList<String> audioTitle;
        public ArrayList<String> audioDuration;
//        public ArrayList<String> audioDuration;

        public SongsListAdapter(@NonNull Context context, int resource, ArrayList<String> audioTitle, ArrayList<String> audioDuration) {
            super(context, resource, audioTitle);
            this.audioTitle = audioTitle;
            this.audioDuration = audioDuration;
        }

    @Nullable
        @Override
        public String getItem(int position) {
        if (audioTitle.get(position).length()>24) {return audioTitle.get(position).substring(0, 24)+"...";}
        else{return audioTitle.get(position);}
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_music_list, parent, false);

            TextView songName = convertView.findViewById(R.id.song_name);
            TextView songDuration = convertView.findViewById(R.id.song_duration);

            songName.setText(getItem(position));
            songDuration.setText(audioDuration.get(position));


            return convertView;
        }

    }
