package com.superpowered.playerexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button player_Button = findViewById(R.id.player);
        Button songs_Button = findViewById(R.id.songs);
        Button afx_Button = findViewById(R.id.Aufx);
        Button liked_Button = findViewById(R.id.liked);

        // songs button click event load the songs list by
        //loading the recycler fragment in the main activity
        songs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding songs recycler fragment to the main activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Recycler_fragment rf = new Recycler_fragment();
                fragmentTransaction.replace(R.id.main_page, rf);
                fragmentTransaction.commit();

            }
        });



        //player button click event load the play fragment and
        //start playing the song by default

        player_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding play fragment to the main activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               // Recycler_fragment rf = new Recycler_fragment();
                Play_fragment pf = new Play_fragment();
                fragmentTransaction.replace(R.id.main_page, pf);
                fragmentTransaction.commit();


                TogglePlayback();
            }
        });



        // Get the device's sample rate and buffer size to enable
        // low-latency Android audio output, if available.
        String samplerateString = null, buffersizeString = null;
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            samplerateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
            buffersizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        }
        if (samplerateString == null) samplerateString = "48000";
        if (buffersizeString == null) buffersizeString = "480";
        int samplerate = Integer.parseInt(samplerateString);
        int buffersize = Integer.parseInt(buffersizeString);

        // Files under res/raw are not zipped, just copied into the APK.
        // Get the offset and length to know where our file is located.
        AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.track);
        int fileOffset = (int) fd.getStartOffset();
        int fileLength = (int) fd.getLength();
        try {
            fd.getParcelFileDescriptor().close();
        } catch (IOException e) {
            Log.e("PlayerExample", "Close error.");
        }
        String path = getPackageResourcePath();         // get path to APK package
        System.loadLibrary("PlayerExample");    // load native library
        NativeInit(samplerate, buffersize, getCacheDir().getAbsolutePath()); // start audio engine
        OpenFileFromAPK(path, fileOffset, fileLength);  // open audio file from APK
        // If the application crashes, please disable Instant Run under Build, Execution, Deployment in preferences.

        // Update UI every 40 ms until UI_update returns with false.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                UI_update();
                handler.postDelayed(this, 40);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 40);
    }


    public void UI_update() {
        boolean p = onUserInterfaceUpdate();
        if (playing != p) {
            playing = p;




        }
    }
/*
    // Play/Pause button event.
    public void PlayerExample_PlayPause(View button) {



        TogglePlayback();
    }
*/
    @Override
    public void onPause() {
        super.onPause();
        onBackground();
    }

    @Override
    public void onResume() {
        super.onResume();
        onForeground();
    }

    protected void onDestroy() {
        super.onDestroy();
        Cleanup();
    }

    // Functions implemented in the native library.
    private native void NativeInit(int samplerate, int buffersize, String tempPath);

    private native void OpenFileFromAPK(String path, int offset, int length);

    private native boolean onUserInterfaceUpdate();

    private native void TogglePlayback();

    private native void onForeground();

    private native void onBackground();

    private native void Cleanup();

    private boolean playing = false;
    private Handler handler;
}