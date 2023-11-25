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
#include <jni.h>
#include <string>
#include <android/log.h>
#include <OpenSource/SuperpoweredAndroidAudioIO.h>
#define log_print __android_log_print
static SuperpoweredAndroidAudioIO *audioIO;
static Superpowered::AdvancedAudioPlayer *player;
static std::vector<std::string> playlist;
static std::vector<std::string> shuffledPlaylist;
std::mutex playerMutex;
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
        Superpowered::Initialize("ExampleLicenseKey-WillExpire-OnNextUpdate");
        /*
        // setting the temp folder for progressive downloads or HLS playback
        // not needed for local file playback
        const char *str = env->GetStringUTFChars(tempPath, 0);
        Superpowered::AdvancedAudioPlayer::setTempFolder(str);
        env->ReleaseStringUTFChars(tempPath, str);
        */
        // creating the player
        player = new Superpowered::AdvancedAudioPlayer((unsigned int) sampleRate, 0);

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
    }
    ~SuperpoweredPlayer() {
        // Cleanup and release resources
        cleanResourses();
    }

    void play(int index) {
        // Implement playback logic using SuperpoweredAdvancedAudioPlayer
        std::lock_guard<std::mutex> lock(playerMutex);

        if (player != nullptr && index >= 0 && index < shuffledPlaylist.size()) {
            player->open(shuffledPlaylist[index].c_str());

            player->play();
        }

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
        player->pause();
    }

    void stop() {
        // Implement stop logic
        player->seek(0);

    }

    bool isPlaying() {
        // Implement isPlaying logic
        if(player->isPlaying())
        {
            return true;
        }
        return false;
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

    void toggle_play()
    {
        player->togglePlayback();
        Superpowered::CPU::setSustainedPerformanceMode(player->isPlaying());
    }
    void shufflePlaylist(std::deque<std::string>& playlist) {
        std::random_device rd;
        std::mt19937 g(rd());
        std::shuffle(playlist.begin(), playlist.end(), g);
    }
// Add this function to update the native CPP vectors
    void addToPlaylist(const char* filePath) {
        // Add the file path to the regular playlist
        std::lock_guard<std::mutex> lock(playerMutex);
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

static SuperpoweredPlayer *superpoweredPlayer; // superpowerplayer class is initialized here

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
        if(filePath != nullptr){
            cppPlayNextList.push_back(filePath);
            env->ReleaseStringUTFChars(str, filePath);
        }
        env->DeleteLocalRef(str);
    }
    // Call the playNext function in your SuperpoweredPlayer instance
    superpoweredPlayer->playNext(cppPlayNextList);
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_superpowered_playerexample_PlayerListManager_onUserInterfaceUpdate(JNIEnv *env,jobject thiz) {
    switch (player->getLatestEvent()) {
        case Superpowered::AdvancedAudioPlayer::PlayerEvent_None:

        case Superpowered::AdvancedAudioPlayer::PlayerEvent_Opening:
            break; // do nothing
        case Superpowered::AdvancedAudioPlayer::PlayerEvent_Opened:
            break;//player->play(); break;
        case Superpowered::AdvancedAudioPlayer::PlayerEvent_OpenFailed: {
            int openError = player->getOpenErrorCode();
            log_print(ANDROID_LOG_ERROR, "PlayerExample", "Open error %i: %s", openError,
                      Superpowered::AdvancedAudioPlayer::statusCodeToString(openError));
        }
            break;
        case Superpowered::AdvancedAudioPlayer::PlayerEvent_ConnectionLost:
            break;
        case Superpowered::AdvancedAudioPlayer::PlayerEvent_ProgressiveDownloadFinished:
            break;
    }
    if (player->eofRecently()) {
        //this code is for looping the current track in a continues loop
        // this is just temprovory
        player->pause();
        player->setPosition(0, false, false);
        player->play();
    }
    return (jboolean) player->isPlaying();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_openfilefrom_1path(JNIEnv *env, jobject thiz,
                                                                         jstring path) {
    const char *str = env->GetStringUTFChars(path,0);
    player->open(str);
    env->ReleaseStringUTFChars(path,str);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_superpowered_playerexample_PlayerListManager_toggle_1playback_1test_1playandpause(
        JNIEnv *env, jobject thiz) {
    superpoweredPlayer->toggle_play();
}