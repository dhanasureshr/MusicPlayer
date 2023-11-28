package com.superpowered.playerexample;

public class Raw_audio_tracks {
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

    public Raw_audio_tracks(long id, String title, String artist, String album, String data, int duration, long size, String mimeType, int rackNumber, int year, String genre, String composer, String albumArtist, String albumArturi) {
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


}

