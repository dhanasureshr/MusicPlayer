package com.superpowered.playerexample;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AudioEnginBridge {
    static {
        System.loadLibrary("audio_engin");
    }
    private Map<String, Integer> pathToIndexMap = new HashMap<>();
    private List<String> uniquePaths = new ArrayList<>();
    private native void openfilefrom_path(String path);
    private  native void toggle_playback_test_playandpause();
    private native void nativeInit(int samplerate, int buffersize);
    private native void nativePlay(int index);
    private native void nativePause();
    private native void nativeStop();
    private native boolean nativeIsPlaying();
    private native int getCurrentPosition();
    private native int getDuration();
    private native void setPosition(int position);
    private native void onForeground();
    private native void onBackground();
    private native void Cleanup();
    private native void nativePlayNext(String[] playNextList);
    private native void nativeAddSongToPlaylist(String path);
    private native boolean onUserInterfaceUpdate();
    private List<String> defaultPlaylist;
    private List<String> playNextList;
    private List<String> likedPlaylist;

    public AudioEnginBridge() {
        defaultPlaylist = new ArrayList<>();
        playNextList = new ArrayList<>();
        likedPlaylist = new ArrayList<>();
    }
    public void initializesuperpowered(int sampleRate, int buffersize){
        nativeInit(sampleRate,buffersize);
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
    public void playnextPlaylist(String[] playNextList)
    {
        nativePlayNext(playNextList);
    }

    public boolean on_plmuiupdate() {
        return onUserInterfaceUpdate();
    }

    public void toggle_playback(){
        toggle_playback_test_playandpause();
    }

    public void OpenFileFromPath(String path)
    {
        openfilefrom_path(path);
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
