package com.superpowered.playerexample;

import java.util.ArrayList;
import java.util.List;

public class PlayerListManager {

    int sampleRate,buffersize;
    static {
        System.loadLibrary("superpoweredplayer");
    }



    private native void nativeInit(int samplerate, int buffersize);
    private native void nativePlay(String filePath);
    private native void nativePause();
    private native void nativeStop();
    private native boolean nativeIsPlaying();
    private native void nativeSetLoopPoints(int startMs, int endMs);
    private native void nativeStartLoop();
    private native void nativeStopLoop();

    private native void onForeground();

    private native void onBackground();

    private native void Cleanup();

    private List<String> defaultPlaylist;
    private List<String> playNextList;
    private List<String> likedPlaylist;

    public PlayerListManager() {
        nativeInit(sampleRate,buffersize);
        defaultPlaylist = new ArrayList<>();
        playNextList = new ArrayList<>();
        likedPlaylist = new ArrayList<>();
    }

    public void addToDefaultPlaylist(String filePath) {
        defaultPlaylist.add(filePath);
    }

    public void addToPlayNextList(String filePath) {
        playNextList.add(filePath);
    }

    public void addToLikedPlaylist(String filePath) {
        likedPlaylist.add(filePath);
    }

    public void playDefaultPlaylist() {
        for (String filePath : defaultPlaylist) {
            nativePlay(filePath);
        }
    }

    public void playNext() {
        if (!playNextList.isEmpty()) {
            String nextFilePath = playNextList.remove(0);
            nativePlay(nextFilePath);
        }
    }

    public void playLikedPlaylist() {
        for (String filePath : likedPlaylist) {
            nativePlay(filePath);
        }
    }

    public void pause() {
        nativePause();
    }

    public void stop() {
        nativeStop();
    }

    public boolean isPlaying() {
        return nativeIsPlaying();
    }

    public void setLoopPoints(int startMs, int endMs) {
        nativeSetLoopPoints(startMs, endMs);
    }

    public void startLoop() {
        nativeStartLoop();
    }

    public void stopLoop() {
        nativeStopLoop();
    }
}
