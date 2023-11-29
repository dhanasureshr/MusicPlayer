package com.superpowered.playerexample;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recycler_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recycler_fragment extends Fragment {


    public ArrayList<Raw_audio_tracks> audio_list = new ArrayList<>();
    public RecyclerView recyclerView;
    public Recycle_adapter recycleAdpter;
    //search code
    SearchView searchview;

    public FineLocalSongs fineLocalSongs;

    public Recycler_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Recycler_fragment newInstance() {
        Recycler_fragment fragment = new Recycler_fragment();

        return fragment;
    }

    // this is the save the recyclerview view position between fragment changes
    @Override
    public void onPause() {
        super.onPause();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if(layoutManager != null)
        {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            savePositionToSharedPreferences(firstVisibleItemPosition);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        int savedVisiblePosition = getPositionFromSharedPreferences();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if(layoutManager != null)
        {
            layoutManager.scrollToPosition(savedVisiblePosition);
        }
    }
     // method to save to shared preferences
    private void savePositionToSharedPreferences(int position){
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("visible_position",position);
        editor.apply();
    }
     // method to get the saved position
    private int getPositionFromSharedPreferences(){
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return preferences.getInt("visible_position",0);
    }
    //---------------------------------end of managing recycler view position
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fineLocalSongs = new FineLocalSongs();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(audio_list.isEmpty())
        {
            audio_list = fineLocalSongs.getAudio_file(this.getContext());
            //search code
            searchview = view.findViewById(R.id.searchview);
            searchview.setQueryHint("Enter song name, artist, or album");
            recyclerView = view.findViewById(R.id.frag_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recycleAdpter = new Recycle_adapter(getContext(), audio_list);
            recyclerView.setAdapter(recycleAdpter);

            // setup for search functionality
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<Raw_audio_tracks> filteredList = filtersongs(newText);
                    recycleAdpter.setSongs(filteredList);
                    return true;
                }

                private ArrayList<Raw_audio_tracks> filtersongs(String query) {
                    query = query.toLowerCase(Locale.getDefault());
                    ArrayList<Raw_audio_tracks> filteredList  = new ArrayList<>();

                    for(Raw_audio_tracks song : audio_list){
                        if(song.getTitle().toLowerCase(Locale.getDefault()).contains(query)||
                        song.getArtist().toLowerCase(Locale.getDefault()).contains(query)||
                        song.getAlbum().toLowerCase(Locale.getDefault()).contains(query)){

                            filteredList.add(song);
                        }
                    }
                    return filteredList;
                }
            });

            recycleAdpter.setOnItemClickListener((Recycle_adapter.OnItemClickListener) getContext()); // Set the listener

        }
    }

}