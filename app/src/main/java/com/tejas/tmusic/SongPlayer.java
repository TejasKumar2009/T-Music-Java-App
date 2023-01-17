package com.tejas.tmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class SongPlayer extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeekbar.interrupt();
    }

    ImageView backbtn, playpause_btn, previous_btn, next_btn;
    TextView songTextView, current_timestamp, song_time;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    Thread updateSeekbar;
    String songTitle;
    int position;
    ArrayList<Song> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        // Creating Back Button
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Initializing Variables
        playpause_btn = findViewById(R.id.play_pause_button);
        previous_btn = findViewById(R.id.previous);
        next_btn = findViewById(R.id.next);
        songTextView = findViewById(R.id.song_name_playing);
        current_timestamp = findViewById(R.id.current_timestamp);
        song_time = findViewById(R.id.song_time);
        seekBar = findViewById(R.id.seekBar);

        // Getting Bundle from MainActivity Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // Getting data from intent and updating title
        audioList = (ArrayList) bundle.getParcelableArrayList("songList");
        songTitle = intent.getStringExtra("currentSong");

        songTextView.setText(songTitle);
        songTextView.setSelected(true);
        position = intent.getIntExtra("position", 0);

        // Initializing Media Player and Its methods
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioList.get(position).getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "This is not working", Toast.LENGTH_SHORT).show();
        }

        seekBar.setMax(mediaPlayer.getDuration());
        String converted_song_time = convertMilisecsToMin(String.valueOf(mediaPlayer.getDuration()));
        song_time.setText(converted_song_time);
        mediaPlayer.start();

        // Handling Play and Pause Events
        playpause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playpause_btn.setImageResource(R.drawable.play_button);
                }
                else{
                    mediaPlayer.start();
                    playpause_btn.setImageResource(R.drawable.pause_button);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                current_timestamp.setText(convertMilisecsToMin(String.valueOf(i)));
                if (b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Handling Previous Button Event
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=0){position -= 1;}
                else{position = audioList.size()-1;}
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioList.get(position).getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                seekBar.setMax(mediaPlayer.getDuration());
                songTitle = audioList.get(position).getName().toString();
                songTextView.setText(songTitle);
                String converted_song_time = convertMilisecsToMin(String.valueOf(mediaPlayer.getDuration()));
                song_time.setText(converted_song_time);
                playpause_btn.setImageResource(R.drawable.pause_button);
                mediaPlayer.start();
            }
        });

        // Handling Next Button Event
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=audioList.size()-1){position += 1;}
                else{position = 0;}
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioList.get(position).getPath());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                seekBar.setMax(mediaPlayer.getDuration());
                songTitle = audioList.get(position).getName().toString();
                songTextView.setText(songTitle);
                String converted_song_time = convertMilisecsToMin(String.valueOf(mediaPlayer.getDuration()));
                song_time.setText(converted_song_time);
                playpause_btn.setImageResource(R.drawable.pause_button);
                mediaPlayer.start();
            }
        });


        updateSeekbar = new Thread(){
            @Override
            public void run(){
                 int currentPosition = 0;
                 try {
                     while (currentPosition < mediaPlayer.getDuration()) {
                         currentPosition = mediaPlayer.getCurrentPosition();
                         seekBar.setProgress(currentPosition);
                         sleep(1000);

                     }
                 } catch(Exception e){
                     e.printStackTrace();
                 }
            }
        };
        updateSeekbar.start();
    }


    String convertMilisecsToMin(String duration){
        int totalAudioSeconds = Integer.parseInt(duration)/1000;
        int audioMinutes = totalAudioSeconds/60;
        int audioSecondsInt = totalAudioSeconds-(audioMinutes*60);
        String audioSeconds = String.valueOf(audioSecondsInt);
        if (audioSeconds.length()==1){
            audioSeconds = "0"+audioSecondsInt;
        }
        String musicDuration = audioMinutes + ":" + audioSeconds;
        return musicDuration;
    }

//    May Be Implement below method in future

//    String addRequiredSpaces(String name){
//        String spaces = "";
//        int name_length = name.length();
//        if (name_length <=20) {
//            int noOfSpacesReq = Math.round((25-name_length)/2);
//            for (int i = 0; i>=noOfSpacesReq; i++){
//                spaces += " ";
//            }
//            String new_name = spaces + name + spaces;
//            return new_name;
//        } else{
//            return name;
//        }
//    }

}