package com.superpowered.playerexample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link db_Viewer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class db_Viewer extends Fragment {


    Button ret,store;
    TextView tv;
    ArrayList<String> arrayL;
    database db;


    public db_Viewer() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static db_Viewer newInstance() {
        db_Viewer fragment = new db_Viewer();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        db = new database(getContext());
        arrayL = new ArrayList<>();
        tv =  view.findViewById(R.id.display);
        ret =  view.findViewById(R.id.button);
        store =  view.findViewById(R.id.store);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 arrayL = db.retrieveFilePaths();
              //  tv.setText((CharSequence) arrayL);
                displayFilePaths(arrayL);


            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> filePathsToStore = new ArrayList<>();
                filePathsToStore.add("/path/to/file1");
                filePathsToStore.add("/path/to/file2");
                filePathsToStore.add("/path/to/file3");

                db.storeFilePaths(filePathsToStore);
            }
        });
    }
    private void displayFilePaths(ArrayList<String> filePaths) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String path : filePaths) {
            stringBuilder.append(path).append("\n");
        }

        tv.setText(stringBuilder.toString());
    }
}

