package com.superpowered.playerexample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.superpowered.playerexample.MainActivity;

import java.util.Locale;

public class Play_fragment extends Fragment {


    private Button play_pause;
    private Button play_previous;
    private Button play_next;
    private Button play_loop;
    private Button play_loop_list;
    private  TextView play_start_time;
    private  TextView play_end_time;
    private  TextView play_song_title;
    private ImageView song_album;
    private SeekBar player_controller;
    private  int SEEK_DELAY;
    private  Handler seekHandler;
    // Handler for updating ui
    private   Handler handler;
    // Update song progress every second
   // private final Runnable updateSongProgress = new Runnable() {
   //     @Override
   //     public void run() {
     //       int currentPosition = getCurrentPosition();
      //      int duration = getDuration();
            // Update your UI with the current position and duration
      //      updateUI(currentPosition, duration);
     //       handler.postDelayed(this, 1000); // Update every second
     //   }
 //   };
    public Play_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Play_fragment newInstance() {
        Play_fragment fragment = new Play_fragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // handler.postDelayed(updateSongProgress,0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_fragment, container, false);
        MainActivity mainActivity = new MainActivity();
        // all ui is initialized
        play_song_title = view.findViewById(R.id.song_name);
        song_album = view.findViewById(R.id.player_album);
        play_pause = view.findViewById(R.id.play_pause);
        play_previous = view.findViewById(R.id.previous_song);
        play_next = view.findViewById(R.id.next_song);
        play_loop = view.findViewById(R.id.loop);

        play_loop_list = view.findViewById(R.id.loop_view);
        player_controller = view.findViewById(R.id.main_track_controller);
        play_start_time = view.findViewById(R.id.start_time);
        play_end_time = view.findViewById(R.id.ent_time);
        SEEK_DELAY = 150;
        seekHandler = new Handler();
        handler = new Handler();

        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // player seekbar seek handler
        player_controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(b)
                {
                    seekHandler.removeCallbacksAndMessages(null);
                    seekHandler.postDelayed(()->{
                    setPosition(progress);
                },SEEK_DELAY);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    private void updateUI(int currentPosition, int duration) {
        // update your ui elements here
        play_start_time.setText(formateTime(currentPosition));
        play_end_time.setText(formateTime(duration));
        player_controller.setMax(duration);
        player_controller.setProgress(currentPosition);
    }
    // Formate time in milliseconds to a string {hh:mm:ss)
    private String formateTime(int milliseconds)
    {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
       // int hours = milliseconds / (1000 * 60 * 60); i am not handling hours of data
        //if data with a duration of an hour or above i must handle it like showing
        //a Tost like file length is too large
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
    public native int getCurrentPosition();
    public native int getDuration();
    public native void setPosition(int position);
}