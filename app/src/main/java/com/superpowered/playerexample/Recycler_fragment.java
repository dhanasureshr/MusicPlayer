package com.superpowered.playerexample;

import android.content.ContentResolver;
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


        audio_list = getAudio_file();


        recyclerView = view.findViewById(R.id.frag_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recycleAdpter = new Recycle_adapter(getContext(), audio_list);

        recycleAdpter.setOnItemClickListener((Recycle_adapter.OnItemClickListener) getContext()); // Set the listener


        recyclerView.setAdapter(recycleAdpter);


    }

    private ArrayList<audio_data> getAudio_file() {

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.DATA};


        Cursor cursor = contentResolver.query(audioUri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                audio_data audioData = new audio_data(title,path);
                audio_list.add(audioData);

            }
            cursor.close();
        }
        return audio_list;
    }
}