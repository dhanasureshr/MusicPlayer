package com.superpowered.playerexample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Play_List_Fragment extends Fragment {

    private RecyclerView recyclerViewPlaylists;
    private PlaylistAdapter playlistAdapter;

    private  PlaylistData selectedPlaylist;
    ArrayList<PlaylistData> playlists;

    public Play_List_Fragment() {
        // Required empty public constructor
    }


    public static Play_List_Fragment newInstance() {
        Play_List_Fragment fragment = new Play_List_Fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play__list_, container, false);
        recyclerViewPlaylists = view.findViewById(R.id.recyclerview_playlist);
        recyclerViewPlaylists.setLayoutManager(new LinearLayoutManager(getContext()));

        playlists = new ArrayList<PlaylistData>();
      /*  // Initialize your list of playlists (replace this with your actual data)
        List<String> playlists = new ArrayList<>();
        playlists.add("Playlist 1");
        playlists.add("Playlist 2");
        // ...*/



        playlistAdapter = new PlaylistAdapter(playlists);
        recyclerViewPlaylists.setAdapter(playlistAdapter);

        // Set up item click listener to handle playlist item clicks
        playlistAdapter.setOnItemClickListener(new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle playlist item click, e.g., navigate to the songs fragment
                // You can pass the playlist name or ID to the songs fragment
                selectedPlaylist = playlists.get(position);
                navigateToSongsFragment(selectedPlaylist);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    // Example method to navigate to the SongsFragment
    private void navigateToSongsFragment(PlaylistData playlistName) {
        // Implement navigation logic here (replace with your navigation code)
        // You might use FragmentTransaction or a navigation component
    }
}