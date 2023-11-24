//
// Created by dhana suresh on 11/24/2023.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include <OpenSource/SuperpoweredAndroidAudioIO.h>
#include <Superpowered.h>
#include <SuperpoweredAdvancedAudioPlayer.h>
#include <SuperpoweredSimple.h>
#include <SuperpoweredCPU.h>
#include <malloc.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include <SLES/OpenSLES.h>
#include <vector>
#include <algorithm>
#include <random>
#include <deque>
#include <map>


#define log_print __android_log_print

static SuperpoweredAndroidAudioIO *audioIO;
static Superpowered::AdvancedAudioPlayer *player = nullptr;
static std::vector<std::string> playlist;
static std::vector<std::string> shuffledPlaylist;




// This is called periodically by the audio engine.
static bool audioProcessing(
        void *__unused clientdata, // custom pointer
        short int *audio,           // output buffer
        int numberOfFrames,         // number of frames to process
        int samplerate              // current sample rate in Hz
) {
    player->outputSamplerate = (unsigned int) samplerate;
    float playerOutput[numberOfFrames * 2];

    if (player->processStereo(playerOutput, false, (unsigned int) numberOfFrames)) {
        Superpowered::FloatToShortInt(playerOutput, audio, (unsigned int) numberOfFrames);
        return true;
    } else return false;
}


class SuperpoweredPlayer {
private:

    std::map<std::string, int> pathToIndexMap;  // Add this declaration



public:

    SuperpoweredPlayer(int sampleRate, int bufferSize) {

        // Initialize SuperpoweredAdvancedAudioPlayer and other necessary setup
        // Initialize SuperpoweredAdvancedAudioPlayer and other necessary setup
        Superpowered::Initialize("ExampleLicenseKey-WillExpire-OnNextUpdate");
        player = new Superpowered::AdvancedAudioPlayer( sampleRate, 0);
        audioIO = new SuperpoweredAndroidAudioIO(
                sampleRate,                     // device native sampling rate
                bufferSize,                     // device native buffer size
                false,                          // enableInput
                true,                           // enableOutput
                audioProcessing,                // process callback function
                NULL,                           // clientData
                -1,                             // inputStreamType (-1 = default)
                SL_ANDROID_STREAM_MEDIA         // outputStreamType (-1 = default)
        );


        // Additional initialization code for SuperpoweredAdvancedAudioPlayer
        // Example: player->setPitchShift(1.0f); player->setTempo(1.0f);
    }


    ~SuperpoweredPlayer() {

        // Cleanup and release resources
        cleanResourses();
    }

    Superpowered::AdvancedAudioPlayer *player;




    void play(int index) {
        // Implement playback logic using SuperpoweredAdvancedAudioPlayer
        // Example: SuperpoweredPlayer->open(filePath); SuperpoweredPlayer->play(true);

        if (player != nullptr && index >= 0 && index < shuffledPlaylist.size()) {
            player->open(shuffledPlaylist[index].c_str());
            player->play();
        }

        //player->togglePlayback();
        Superpowered::CPU::setSustainedPerformanceMode(player->isPlaying());// prevent dropouts

    }

    // Function to play the next song in the playNextList
    void playNext(const std::vector<std::string>& playNextList) {
        if (!playNextList.empty()) {
            const std::string& nextFilePath = playNextList[0]; // Access the first element
            auto iter = pathToIndexMap.find(nextFilePath);

            if (iter != pathToIndexMap.end()) {
                int index = iter->second;
                play(index);  // Assuming nativePlay is a member function in your class
            } else {
                // Handle the case where the index is not found in pathToIndexMap
                // Log an error, skip the item, or take appropriate action.
                log_print(ANDROID_LOG_ERROR, "SuperpoweredPlayer", "Error: Index not found for file path: %s", nextFilePath.c_str());
            }
        } else {
            // Handle the case where playNextList is empty
            // Log an error, skip the item, or take appropriate action.
            log_print(ANDROID_LOG_ERROR, "SuperpoweredPlayer", "Error: playNextList is empty.");
        }
    }



    void pause() {
        // Implement pause logic
        // Example: SuperpoweredPlayer->pause();
        player->pause();
    }

    void stop() {
        // Implement stop logic
        // Example: SuperpoweredPlayer->pause(); SuperpoweredPlayer->seek(0);
    }

    bool isPlaying() {
        // Implement isPlaying logic
        // Example: return SuperpoweredPlayer->isPlaying();
        return false;
    }

    void setLoopPoints(int startMs, int endMs) {
        // Implement setting loop points
        // Example: SuperpoweredPlayer->setStartPositionInMs(startMs);
        //          SuperpoweredPlayer->setEndPositionInMs(endMs);
    }

    void startLoop() {
        // Implement starting the loop
        //player->togglePlayback(true);
    }

    void stopLoop() {
        // Implement stopping the loop
        // Example: SuperpoweredPlayer->togglePlayback(false);
    }

    int getPlayHeadCurrentPosition()
    {
        return player->getDisplayPositionMs();
    }

    void setPlayHeadPosition(int position)
    {
        player->setPosition(position, false, false, false, false);
    }

    int getTrackDuration()
    {
        return player->getDurationMs();
    }


    void activityOnPause()
    {
        audioIO->onBackground();
        //app is on pause
    }

    void activityOnResume()
    {
        audioIO->onForeground();
        //app is on resume
    }

    void cleanResourses()
    {
        delete player;
        delete audioIO;
    }

    void shufflePlaylist(std::deque<std::string>& playlist) {
        std::random_device rd;
        std::mt19937 g(rd());
        std::shuffle(playlist.begin(), playlist.end(), g);
    }


// Add this function to update the native CPP vectors
    void addToPlaylist(const char* filePath) {
        // Add the file path to the regular playlist
        playlist.push_back(filePath);

        // If shuffle is enabled, update the shuffled playlist
        if (shuffledPlaylist.size() != playlist.size()) {
            try {
            // Convert the vector to a deque for random access
            std::deque<std::string> dequePlaylist(shuffledPlaylist.begin(), shuffledPlaylist.end());

            // Shuffle the deque
            std::random_device rd;
            std::mt19937 g(rd());
            std::shuffle(dequePlaylist.begin(), dequePlaylist.end(), g);

            // Convert the shuffled deque back to the vector
            shuffledPlaylist.assign(dequePlaylist.begin(), dequePlaylist.end());
            } catch (const std::exception& e) {
                // Handle the exception, e.g., log the error or perform cleanup
                // For example, you can use Android logging:
                __android_log_print(ANDROID_LOG_ERROR, "YourTag", "Error during playlist shuffling: %s", e.what());
            }

        }
    }





};

static SuperpoweredPlayer *superpoweredPlayer;

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeInit(JNIEnv *env, jobject instance, jint sampleRate, jint bufferSize) {
    superpoweredPlayer = new SuperpoweredPlayer(sampleRate, bufferSize);
}
extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativePlay(JNIEnv *env, jobject instance,jint index) {

    superpoweredPlayer->play(index);
}

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativePause(JNIEnv *env, jobject instance) {
    superpoweredPlayer->pause();
}

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeStop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->stop();
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeIsPlaying(JNIEnv *env, jobject instance) {
    return static_cast<jboolean>(superpoweredPlayer->isPlaying());
}

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeSetLoopPoints(JNIEnv *env, jobject instance, jint startMs, jint endMs) {
    superpoweredPlayer->setLoopPoints(startMs, endMs);
}

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeStartLoop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->startLoop();
}

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeStopLoop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->stopLoop();
}

extern "C" JNIEXPORT jint JNICALL
Java_com_superpowered_playerexample_PlayerListManager_getCurrentPosition(JNIEnv *env, jobject thiz) {
    return superpoweredPlayer->getPlayHeadCurrentPosition();
}
extern "C" JNIEXPORT jint JNICALL
Java_com_superpowered_playerexample_PlayerListManager_getDuration(JNIEnv *env, jobject thiz) {
    return  superpoweredPlayer->getTrackDuration();
}
extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_setPosition(JNIEnv *env, jobject thiz,
                                                               jint position) {
superpoweredPlayer->setPlayHeadPosition(position);
}

//shuffiling logic

extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeSetShuffle(JNIEnv *env, jobject instance, jboolean enable) {
    if (enable) {
        // Convert the vector to a deque for random access
        std::deque<std::string> dequePlaylist(playlist.begin(), playlist.end());
        superpoweredPlayer->shufflePlaylist(dequePlaylist);//shufflePlaylist(dequePlaylist);
        // Convert the deque back to the vector
        shuffledPlaylist.assign(dequePlaylist.begin(), dequePlaylist.end());
    } else {
        shuffledPlaylist = playlist;
    }
}



extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativeAddSongToPlaylist(JNIEnv *env, jobject instance, jstring filePath_) {
    const char *filePath = env->GetStringUTFChars(filePath_, 0);

    superpoweredPlayer->addToPlaylist(filePath);


    env->ReleaseStringUTFChars(filePath_, filePath);
}


// onBackground - Put audio processing to sleep if no audio is playing.
extern "C" JNIEXPORT void
Java_com_superpowered_playerexample_PlayerListManager_onBackground(JNIEnv *__unused env,
                                                              jobject __unused obj) {
    superpoweredPlayer->activityOnPause();
}

// onForeground - Resume audio processing.
extern "C" JNIEXPORT void
Java_com_superpowered_playerexample_PlayerListManager_onForeground(JNIEnv *__unused env,jobject __unused obj) {
    superpoweredPlayer->activityOnResume();
}

// Cleanup - Free resources.
extern "C" JNIEXPORT void
Java_com_superpowered_playerexample_PlayerListManager_Cleanup(JNIEnv *__unused env,jobject __unused obj) {
superpoweredPlayer->cleanResourses();
}


extern "C" JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_nativePlayNext(JNIEnv *env, jobject instance, jobjectArray playNextList) {
    // Convert jobjectArray to std::vector<std::string>
    std::vector<std::string> cppPlayNextList;
    jsize length = env->GetArrayLength(playNextList);
    for (jsize i = 0; i < length; ++i) {
        jstring str = (jstring)env->GetObjectArrayElement(playNextList, i);
        const char *filePath = env->GetStringUTFChars(str, 0);
        cppPlayNextList.push_back(filePath);
        env->ReleaseStringUTFChars(str, filePath);
        env->DeleteLocalRef(str);
    }

    // Call the playNext function in your SuperpoweredPlayer instance
    superpoweredPlayer->playNext(cppPlayNextList);
}