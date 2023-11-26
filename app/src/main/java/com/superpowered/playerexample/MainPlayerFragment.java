package com.superpowered.playerexample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainPlayerFragment extends Fragment {

    SeekBar Player_control_seekbar;
    ImageView Song_album_image;
    TextView Song_name;
    ImageView Play_previous_song;
    ImageView Play_or_pause;
    ImageView Play_next_song;



    public MainPlayerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainPlayerFragment newInstance() {
        MainPlayerFragment fragment = new MainPlayerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_player, container, false);
        Player_control_seekbar = view.findViewById(R.id.seekBarSongProgress);
        Song_album_image = view.findViewById(R.id.song_image);
        Song_name = view.findViewById(R.id.song_name);
        Play_previous_song = view.findViewById(R.id.play_previous);
        Play_or_pause = view.findViewById(R.id.play_or_pause);
        Play_next_song = view.findViewById(R.id.play_next);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}