package com.superpowered.playerexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListManager {

    int sampleRate,buffersize;
    static {
        System.loadLibrary("superpoweredplayer");
        //System.loadLibrary("PlayerListManager");
    }


    private Map<String, Integer> pathToIndexMap = new HashMap<>();
    private List<String> uniquePaths = new ArrayList<>();


    private native void nativeInit(int samplerate, int buffersize);
    private native void nativePlay(int index);
    private native void nativePause();
    private native void nativeStop();
    private native boolean nativeIsPlaying();
    private native void nativeSetLoopPoints(int startMs, int endMs);
    private native void nativeStartLoop();
    private native void nativeStopLoop();

    private native int getCurrentPosition();

    private native int getDuration();

    private native void setPosition(int position);


    private native void onForeground();

    private native void onBackground();

    private native void Cleanup();

    private native void nativePlayNext(String[] playNextList);
    private native void nativeAddSongToPlaylist(String path);



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
        int newIndex = uniquePaths.size();
        uniquePaths.add(filePath);
        defaultPlaylist.add(filePath);
        pathToIndexMap.put(filePath,newIndex);

        // Call native method to add the file path to the native CPP vector
        try{
            nativeAddSongToPlaylist(filePath);
        }catch (Exception e)
        {
// Handle the exception, e.g., log the error or show a user-friendly message
            e.printStackTrace();
        }

    }

    public void addToPlayNextList(String filePath) {
        int newIndex = uniquePaths.size();
        uniquePaths.add(filePath);
        playNextList.add(filePath);
        pathToIndexMap.put(filePath,newIndex);
        try{
            nativeAddSongToPlaylist(filePath);
        }catch (Exception e)
        {
// Handle the exception, e.g., log the error or show a user-friendly message
            e.printStackTrace();
        }
        // Call native method to add the file path to the native CPP vector

    }

    public void addToLikedPlaylist(String filePath) {
        int newIndex = uniquePaths.size();
        uniquePaths.add(filePath);
        likedPlaylist.add(filePath);
        pathToIndexMap.put(filePath,newIndex);
        try{
            nativeAddSongToPlaylist(filePath);
        }catch (Exception e)
        {
// Handle the exception, e.g., log the error or show a user-friendly message
            e.printStackTrace();
        }
        // Call native method to add the file path to the native CPP vector

    }

    public void playDefaultPlaylist() {
        for (String filePath : defaultPlaylist) {
            Integer index = pathToIndexMap.get(filePath);
            if(index != null)
            {
                nativePlay(index);
            }else {
                // Handle the case where the index is null (optional)
                // Log an error, skip the item, or take appropriate action.


            }

        }
    }

    public void playNext() {
        if (!playNextList.isEmpty()) {
            String nextFilePath = playNextList.remove(0);
            Integer index = pathToIndexMap.get(nextFilePath);
            if(index != null)
            {
                nativePlay(index);
            }else {
                // Handle the case where the index is null (optional)
                // Log an error, skip the item, or take appropriate action.
            }
        }
    }

    public void playLikedPlaylist() {
        for (String filePath : likedPlaylist) {
            Integer index = pathToIndexMap.get(filePath);
            if(index != null)
            {
                nativePlay(index);
            }else {
                // Handle the case where the index is null (optional)
                // Log an error, skip the item, or take appropriate action.
            }
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

    public int getPlayheadcurrentposition()
    {
        return getCurrentPosition();
    }


    public int getSongduration()
    {
        return getDuration();
    }

    public void onPlayheadseekbar(int position)
    {
        setPosition(position);
    }


    public void onPauseapp()
    {
        onBackground();
    }


    public  void onResumeapp()
    {
        onForeground();
    }

    public void closeapp()
    {
        Cleanup();
    }
}
