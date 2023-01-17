package com.tejas.tmusic;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ListView songsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsListView = findViewById(R.id.songs_listview);
        ArrayList<Song> audioList = new ArrayList<>();
        ArrayList<String> audioTitle = new ArrayList<>();
        ArrayList<String> audioDuration = new ArrayList<>();

        if (checkPermission() == false) {
            requestPermission();
            return;
        }

    String[] projection = {
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
    };

    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
    while(cursor.moveToNext()){
        Song songData = new Song(cursor.getString(1), cursor.getString(0), cursor.getString(2));
        if (new File(songData.getPath()).exists())
            audioList.add(songData);
    }

        if(audioList.size()==0){
        Toast.makeText(this, "No Music Found !", Toast.LENGTH_SHORT).show();
    }
        else{
            for (Song audio : audioList){
                audioTitle.add(audio.name);

                int totalAudioSeconds = Integer.parseInt(audio.duration)/1000;
                int audioMinutes = totalAudioSeconds/60;
                int audioSecondsInt = totalAudioSeconds-(audioMinutes*60);
                String audioSeconds = String.valueOf(audioSecondsInt);
                if (audioSeconds.length()==1){
                    audioSeconds = "0"+audioSecondsInt;
                }
                String musicDuration = audioMinutes + ":" + audioSeconds;

                audioDuration.add(musicDuration);
            }

            SongsListAdapter songsListAdapter = new SongsListAdapter(getApplicationContext(), R.layout.layout_music_list, audioTitle, audioDuration);
            songsListView.setAdapter(songsListAdapter);

            songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), SongPlayer.class);
                    String currentSong = songsListView.getItemAtPosition(i).toString();
                    intent.putExtra("songList", audioList);
                    intent.putExtra("currentSong", currentSong);
                    intent.putExtra("position", i);
                    startActivity(intent);
                }
            });
    }
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("mytag", "Permission Granted hai bhaiya");
            return true;
        }
        else{
            return false;
        }
    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "Allow From Settings it is required!!", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

}