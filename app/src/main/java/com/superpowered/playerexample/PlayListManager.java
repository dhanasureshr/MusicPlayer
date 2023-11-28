package com.superpowered.playerexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class PlayListManager {


    // Map to store playlists with playlist name as the key and list of songs as the value
    private Map<String, ArrayList<SongData>> playlists;


    public PlayListManager(Map<String, ArrayList<SongData>> playlists) {
        this.playlists = playlists;


    }

    // Create a new playlist
    public void createPlaylist(String playlistName) {

            if(isValidPlaylistName(playlistName))
            {
                playlists.put(playlistName, new ArrayList<>());
            }else {
                // Handle invalid playlist name
                // Log an error or throw an exception
                throw new IllegalArgumentException("Invalid playlist name/ playlist name cannot be null");


            }

    }


    private boolean isValidPlaylistName(String playlistName) {

        // Implement validation logic
        // Return true if the playlistName is valid, false otherwise
        return playlistName != null && !playlistName.trim().isEmpty();
    }

    // Update the playlist by replacing its songs with a new list of songs
    public void updatePlaylist(String playlistName, ArrayList<SongData> newSongs) {
        if (playlists.containsKey(playlistName)) {
            playlists.put(playlistName, newSongs);
        }
    }

    // Delete a playlist
    public void deletePlaylist(String playlistName) {
        playlists.remove(playlistName);
    }

    // Add a song to a playlist
    public void addSongToPlaylist(String playlistName, SongData song) {
        try {

            if (playlists.containsKey(playlistName)) {
                ArrayList<SongData> playlist = playlists.get(playlistName);
                if(!playlist.contains(song))
                {
                    playlist.add(song);
                }else{
                    // Handle duplicate song addition
                    // Possibly throw an exception or log a warning
                    throw new IllegalArgumentException("Song already exists in the playlist");
                }

            }
        }catch (Exception e) {
        // // Handle the exception (e.g., log an error)
            throw new RuntimeException("Error adding song to playlist", e);
        }
    }

    // Remove a song from a playlist
    public void removeSongFromPlaylist(String playlistName, SongData song) {
        try {
            if (playlists.containsKey(playlistName)) {
                ArrayList<SongData> playlist = playlists.get(playlistName);
                playlist.remove(song);
            }else{
                throw new IllegalArgumentException("Song not found in the playlist");
            }
        }catch (Exception e)
        {
            // Handle the exception (e.g., log an error)

            throw new RuntimeException("Error removing song from playlist", e);
        }

    }

    public void savePlaylistsToDatabase() {
        // Implementation to save playlists to a database
    }

    public void loadPlaylistsFromDatabase() {
        // Implementation to load playlists from a database
    }


    // Get the list of songs in a playlist
    public ArrayList<SongData> getPlaylist(String playlistName) {
        ArrayList<SongData> playlist = playlists.get(playlistName);
        return (ArrayList<SongData>) Collections.unmodifiableList(playlist != null ? playlist : new ArrayList<SongData>());
    }



}
