package com.example.muzik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Playsong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
        updateseek.interrupt();
    }

    TextView textView;
    SeekBar seekBar;
    ImageView play,previous,next;
    MediaPlayer mp;
    ArrayList<File> songs;
    int position;
    String  textcontent;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        textView= findViewById(R.id.tv1);
        play= findViewById(R.id.pause);
        previous=findViewById(R.id.previous);
        next =findViewById(R.id.next);
        seekBar=findViewById(R.id.seekbar);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textcontent= intent.getStringExtra("currentsong");
        textView.setText(textcontent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mp= MediaPlayer.create(this,uri);
        mp.start();
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });
        updateseek= new Thread(){
            public void run(){
                int currentPosition= 0;
                try{
                    while (currentPosition < mp.getDuration()) {
                        currentPosition=mp.getCurrentPosition();

                        seekBar.setProgress(currentPosition);
                        sleep(800);

                    }
                }
                catch(Exception e){
                    e.printStackTrace();

                }
            }
        };
        updateseek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mp.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mp.start();

                }

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.stop();
                mp.release();
                if(position!=0){
                    position=position-1;
                }
                else{
                    position= songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mp.getDuration());
                textcontent= songs.get(position).getName().toString();
                textView.setText(textcontent);


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.stop();
                mp.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position= 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mp= MediaPlayer.create(getApplicationContext(),uri);
                mp.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mp.getDuration());
                textcontent= songs.get(position).getName().toString();
                textView.setText(textcontent);
            }
        });



    }
}