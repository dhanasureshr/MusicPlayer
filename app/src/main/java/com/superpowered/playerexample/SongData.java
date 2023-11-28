package com.superpowered.playerexample;

public class SongData {
    public String song_name;
    public String song_path;

    public SongData(String song_name, String song_path) {
        this.song_name = song_name;
        this.song_path = song_path;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_path() {
        return song_path;
    }

    public void setSong_path(String song_path) {
        this.song_path = song_path;
    }
}
