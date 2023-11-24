package com.superpowered.playerexample;

public class audio_data {
    public long id;

     public String title;
     public String artist;

     public String album;

     public String data;

     public int duration;

     public long size;
     public String mimeType;
     public int rackNumber;
     public int year;
     public String genre;
     public String composer;
     public String albumArtist;
     public String albumArtURI;

    public audio_data(long id, String title, String artist, String album, String data, int duration, long size, String mimeType, int rackNumber, int year, String genre, String composer, String albumArtist,String albumArturi) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.data = data;
        this.duration = duration;
        this.size = size;
        this.mimeType = mimeType;
        this.rackNumber = rackNumber;
        this.year = year;
        this.genre = genre;
        this.composer = composer;
        this.albumArtist = albumArtist;
        this.albumArtURI = albumArturi;

    }

    public String getAlbumArtURI() {
        return albumArtURI;
    }

    public void setAlbumArtURI(String albumArtURI) {
        this.albumArtURI = albumArtURI;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getRackNumber() {
        return rackNumber;
    }

    public void setRackNumber(int rackNumber) {
        this.rackNumber = rackNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }
    /*

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

*/

}

