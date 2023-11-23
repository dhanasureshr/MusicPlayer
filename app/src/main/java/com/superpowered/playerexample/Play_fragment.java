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

import com.superpowered.playerexample.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Play_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Play_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Play_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Play_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Play_fragment newInstance(String param1, String param2) {
        Play_fragment fragment = new Play_fragment();
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
        return inflater.inflate(R.layout.fragment_play_fragment, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = new MainActivity();

        Button play_pause = view.findViewById(R.id.play_pause);
        TextView song_duration = view.findViewById(R.id.start_time);
        String round = String.format("%.0f",getcurrentposition());
        song_duration.setText(round);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               mainActivity.PlayerExample_PlayPause();
            }
        });
    }


    private native double getcurrentposition();


}