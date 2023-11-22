package com.superpowered.playerexample;

public class audio_data {
    String Track_title;
    String Track_path;

    String Track_album;
    String AlbumArtUri;


    public audio_data(String track_title,String track_path,String track_album, String albumArtUri) {
        Track_title = track_title;
        Track_path = track_path;
        Track_album = track_album;
        AlbumArtUri = albumArtUri;
    }

    public String getAlbumArtUri() {
        return AlbumArtUri;
    }

    public void setAlbumArtUri(String albumArtUri) {
        this.AlbumArtUri = albumArtUri;
    }

    public String getTrack_album() {
        return Track_album;
    }

    public void setTrack_album(String track_album) {
        Track_album = track_album;
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

