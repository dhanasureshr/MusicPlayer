package com.superpowered.playerexample;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recycler_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recycler_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<audio_data> audio_list = new ArrayList<>();

    public RecyclerView recyclerView;

    public Recycle_adapter recycleAdpter;


    public Recycler_fragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recycler_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Recycler_fragment newInstance(String param1, String param2) {
        Recycler_fragment fragment = new Recycler_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


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


            audio_list = getAudio_file();


            recyclerView = view.findViewById(R.id.frag_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recycleAdpter = new Recycle_adapter(getContext(), audio_list);
            recycleAdpter.setOnItemClickListener((Recycle_adapter.OnItemClickListener) getContext()); // Set the listener
            recyclerView.setAdapter(recycleAdpter);


        }



    }

    private ArrayList<audio_data> getAudio_file() {

        ContentResolver contentResolver = getContext().getContentResolver();

        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.GENRE,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.ALBUM_ARTIST,
                MediaStore.Audio.Media.ALBUM_ID};

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";


       try(Cursor cursor = contentResolver.query(uri, projection, selection, null,null)) {

           if (cursor != null) {
               while (cursor.moveToNext()) {
                /*
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String albumArtUri = getAlbumArtUri(albumId);

                audio_data audioData = new audio_data(title,path,album,albumArtUri);
                audio_list.add(audioData);

                */

                   long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                   String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                   String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                   String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                   String Data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                   int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                   long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                   String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                   int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
                   int year = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
                   String genre = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE));
                   String composer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
                   String albumArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST));
                   long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                   String albumArtUri = getAlbumArtUri(albumId);

                   audio_data audioData = new audio_data(id, title, artist, album, Data, duration, size, mimeType, trackNumber, year, genre, composer, albumArtist, albumArtUri);
                   audio_list.add(audioData);

               }

           }

       } catch (Exception e)
       {
           Toast.makeText(getContext(), "AudioContentResolver error retriving " +e.getMessage(), Toast.LENGTH_SHORT).show();
       }

         //cursor.close(); // because try and catch i need not to this explecetly

        return audio_list;
    }


    private String getAlbumArtUri(long albumId) {
        Uri albumArtUri = Uri.parse("content://media/external/audio/albumart/" + albumId);
        return albumArtUri.toString();
    }
}