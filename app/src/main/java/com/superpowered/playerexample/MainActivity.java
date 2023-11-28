package com.superpowered.playerexample;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;
import android.os.Handler;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity implements Recycle_adapter.OnItemClickListener{
    public AudioEnginBridge audioEnginBridge;
    // Declare the PlayerListManager instance
    ActivityResultLauncher<String[]> mPermissionResultLanuncher;
    private boolean isStoragepermissiongranted = false;

    private boolean playing = false;

    private Handler handler;

    private PlayListManager playListManager;
    private database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        database = new database(this);

        // Initialize the PlayListManager and pass the database instance
        playListManager = new PlayListManager(new HashMap<>(), database);

        // Load playlists from the database (this is also done in the PlayListManager constructor)
        playListManager.loadPlaylistsFromDatabase();



        ViewpagerInitializer();



        AudioEnginInitializer();





        //check for the storage permission code begins
        mPermissionResultLanuncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)!= null)
                {
                    isStoragepermissiongranted = Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE));
                    if(isStoragepermissiongranted)
                    {
                        load_songs_fragment();

                    }else{
                        showPopupWindow(MainActivity.this.findViewById(R.id.viewPager));
                    }

                }


            }
        });
        requestpermisson();
        load_songs_fragment();
      //  load_songs_list_fragment();
 //------------------------------------------end of permissions

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

    private void ViewpagerInitializer() {

        // code for viewpager begins
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(this); // Pass the activity instance
        viewPager.setAdapter(pagerAdapter);

        //this is the call back
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handlePageSelection(position);
            }
        });


    }

    private void handlePageSelection(int position) {
        String fragmentName = getFragmentName(position);

        Toast.makeText(this, "Selected Fragment: " + fragmentName, Toast.LENGTH_SHORT).show();
    }

    private String getFragmentName(int position) {
        switch (position) {
            case 0:
                return "Play_fragment";
            case 1:
                return "SongListFragment";
            case 2:
                return "NowPlayingFragment";
            default:
                return "MainActivity";
        }
    }

    private void AudioEnginInitializer() {

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
        // Create an instance of PlayerListManager and initialize it
        // this initialization of the audio engin must be happen after all the permissions setup
        // and loading the song data from the local storage before it's getting ready to play then initialize the audio engin
        audioEnginBridge = new AudioEnginBridge();
        audioEnginBridge.initializesuperpowered(samplerate, buffersize);


    }

    public void UI_update() {
        boolean p = audioEnginBridge.on_plmuiupdate();
        if (playing != p) {
            playing = p;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        audioEnginBridge.onPauseapp();
    }
    @Override
    public void onResume() {
        super.onResume();
      audioEnginBridge.onResumeapp();
    }

    protected void onDestroy() {
        super.onDestroy();
        playListManager.savePlaylistsToDatabase();
        // Close the database connection
        database.close();
        audioEnginBridge.closeapp();

    }

    @Override
    public void onItemClick(String path,String title) {
        Toast.makeText(this, "playing " +title, Toast.LENGTH_SHORT).show();
        audioEnginBridge.OpenFileFromPath(path);
        audioEnginBridge.toggle_playback();
     //   load_play_fragment();

    }
    public void load_play_fragment(){
        //adding play fragment to the main activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Recycler_fragment rf = new Recycler_fragment();
        Play_fragment pf = new Play_fragment();
       // fragmentTransaction.replace(R.id.viewPager, pf);
        fragmentTransaction.commit();
    }
    public void load_songs_fragment(){
             //adding songs recycler fragment to the main activity
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             Recycler_fragment rf = new Recycler_fragment();
             //fragmentTransaction.replace(R.id.main_page, rf);
             fragmentTransaction.commit();
    }
    public void load_songs_list_fragment(){
        //adding songs recycler fragment to the main activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Recycler_fragment rf = new Recycler_fragment();
        //fragmentTransaction.replace(R.id.main_page, rf);
        fragmentTransaction.commit();
    }
    //request permission method
    private void requestpermisson()
    {
        isStoragepermissiongranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<>();

        if(!isStoragepermissiongranted)
        {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(!permissionRequest.isEmpty())
        {
            mPermissionResultLanuncher.launch(permissionRequest.toArray(new String[0]));
        }




    }
    private void openAppInfoSettings(Context context) {
        try {
            String packageName = getPackageName();
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Open the specific permission screen for the app
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where the App Info settings activity is not found
            Toast.makeText(context, "Error: App Info settings not available", Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Log the exception for further analysis
        } catch (Exception e) {
            // Handle other exceptions
            Toast.makeText(context, "Error opening App Info settings", Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Log the exception for further analysis
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        load_songs_fragment();
    }
    private void showPopupWindow(View view) {
        try {
            // Create a LayoutInflater object to inflate the popup_layout.xml
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.storagage_permission_popup, null);
            // Specify the width and height of the popup window
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // Create the popup window
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            // Set an animation style for the popup window
           // popupWindow.setAnimationStyle(R.style.PopupAnimation);
            // Show the popup window at the center of the screen, you can adjust the location
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            // Set a dismiss listener for the popup window
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // Dismiss the popup window when the close button is clicked
                    isStoragepermissiongranted = ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    if(!isStoragepermissiongranted){
                        openAppInfoSettings(getApplicationContext());
                    }
                }
            });
            // Set up any other views or interactions within the popupView
            Button closePopupButton = popupView.findViewById(R.id.accept_button);
            closePopupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        } catch (Exception e) {
            // Handle exceptions related to creating or showing the PopupWindow
            Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace(); // Log the exception for further analysis
        }
    }



}