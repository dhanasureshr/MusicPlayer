package com.superpowered.playerexample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerViewSongs;
    private SongAdapter songAdapter;

    ArrayList<SongData> songs;

    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance() {
        SongsFragment fragment = new SongsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs,container,false);

        recyclerViewSongs = view.findViewById(R.id.recyclerViewSongs);
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        songs = new ArrayList<>();
     /*   // Initialize your list of songs (replace this with your actual data)

        songs.add("Song 1");
        songs.add("Song 2");
*/

        songAdapter = new SongAdapter(songs);
        recyclerViewSongs.setAdapter(songAdapter);


        // You can implement song-related actions here (add, remove, etc.)



        return view;
    }


}