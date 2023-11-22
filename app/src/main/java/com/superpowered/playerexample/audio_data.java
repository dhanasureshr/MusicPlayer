package com.superpowered.playerexample;

public class audio_data {
    String Track_title;
    String Track_path;

    public audio_data(String track_title,String track_path) {
        Track_title = track_title;
        Track_path = track_path;
    }

    public String getTrack_title() {
        return Track_title;
    }

    public void setTrack_title(String track_title) {
        Track_title = track_title;
    }

    public String getTrack_path() {
        return Track_path;
    }

    public void setTrack_path(String track_path) {
        Track_path = track_path;
    }



}

