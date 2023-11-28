package com.superpowered.playerexample;

public class PlaylistData {
    public String playlist_name;
    public String playlist_total_items_count;

    public PlaylistData(String playlist_name, String playlist_total_items_count) {
        this.playlist_name = playlist_name;
        this.playlist_total_items_count = playlist_total_items_count;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public String getPlaylist_total_items_count() {
        return playlist_total_items_count;
    }

    public void setPlaylist_total_items_count(String playlist_total_items_count) {
        this.playlist_total_items_count = playlist_total_items_count;
    }
}
