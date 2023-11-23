package com.superpowered.playerexample;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.content.Context;
import android.widget.Button;
import android.view.View;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Recycle_adapter.OnItemClickListener{

    ActivityResultLauncher<String[]> mPermissionResultLanuncher;
    private boolean isStoragepermissiongranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //his.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //check for the storage permission

        mPermissionResultLanuncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)!= null)
                {
                    isStoragepermissiongranted = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(isStoragepermissiongranted)
                    {
                        load_songs_fragment();
                    }
                }
            }
        });

            requestpermisson();

         if(isStoragepermissiongranted)
        {
            load_songs_fragment();
        }




//------------------------------------------end of permissions

        Button player_Button = findViewById(R.id.player);
        Button songs_Button = findViewById(R.id.songs);
        Button afx_Button = findViewById(R.id.Aufx);
        Button liked_Button = findViewById(R.id.liked);



        // songs button click event load the songs list by
        //loading the recycler fragment in the main activity
        songs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_songs_fragment();
            }
        });



        //player button click event load the play fragment and
        //start playing the song by default

        player_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            load_play_fragment();


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




        System.loadLibrary("PlayerExample");    // load native library
        NativeInit(samplerate, buffersize, getCacheDir().getAbsolutePath()); // start audio engine



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

    // Play/Pause button event.
    public void PlayerExample_PlayPause() {



        TogglePlayback();
    }

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

    private native void OpenFileFromPath(String path);

    private native boolean onUserInterfaceUpdate();

    private native void TogglePlayback();

    private native void onForeground();

    private native void onBackground();

    private native void Cleanup();



    private boolean playing = false;
    private Handler handler;

    @Override
    public void onItemClick(String path) {
        Toast.makeText(this, "playing " +path, Toast.LENGTH_SHORT).show();
        OpenFileFromPath(path);
        PlayerExample_PlayPause();
        //load_play_fragment(); // if you enable this on clicking on item it will load the play fragment by default
    }


    public void load_play_fragment(){

        //adding play fragment to the main activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Recycler_fragment rf = new Recycler_fragment();
        Play_fragment pf = new Play_fragment();
        fragmentTransaction.replace(R.id.main_page, pf);
        fragmentTransaction.commit();
    }
    public void load_songs_fragment(){

         //requestpermisson();
         if(!isStoragepermissiongranted) {
             Toast.makeText(this, "storage permission needed you can accept through App info  " , Toast.LENGTH_SHORT).show();
             requestpermisson();
         }
             //adding songs recycler fragment to the main activity
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             Recycler_fragment rf = new Recycler_fragment();
             fragmentTransaction.replace(R.id.main_page, rf);
             fragmentTransaction.commit();

    }

    //request permission method
    private void requestpermisson()
    {
        isStoragepermissiongranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


        List<String> permissionRequest = new ArrayList<String>();

        if(!isStoragepermissiongranted)
        {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!permissionRequest.isEmpty())
        {
            mPermissionResultLanuncher.launch(permissionRequest.toArray(new String[0]));
        }

    }

}