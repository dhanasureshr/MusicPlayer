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



#define log_print __android_log_print

static SuperpoweredAndroidAudioIO *audioIO;
static Superpowered::AdvancedAudioPlayer *player;

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

public:

    SuperpoweredPlayer(int sampleRate, int bufferSize) {

        // Initialize SuperpoweredAdvancedAudioPlayer and other necessary setup
        // Initialize SuperpoweredAdvancedAudioPlayer and other necessary setup

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




    void play(const char *filePath) {
        // Implement playback logic using SuperpoweredAdvancedAudioPlayer
        // Example: SuperpoweredPlayer->open(filePath); SuperpoweredPlayer->play(true);
        player->togglePlayback();
        Superpowered::CPU::setSustainedPerformanceMode(player->isPlaying());// prevent dropouts

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





};

static SuperpoweredPlayer *superpoweredPlayer;

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativeInit(JNIEnv *env, jobject instance, jint sampleRate, jint bufferSize) {
    superpoweredPlayer = new SuperpoweredPlayer(sampleRate, bufferSize);
}
extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativePlay(JNIEnv *env, jobject instance, jstring filePath) {
    const char *file = env->GetStringUTFChars(filePath, 0);
    superpoweredPlayer->play(file);
    env->ReleaseStringUTFChars(filePath, file);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativePause(JNIEnv *env, jobject instance) {
    superpoweredPlayer->pause();
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativeStop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->stop();
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_PlayerListManager_nativeIsPlaying(JNIEnv *env, jobject instance) {
    return static_cast<jboolean>(superpoweredPlayer->isPlaying());
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativeSetLoopPoints(JNIEnv *env, jobject instance, jint startMs, jint endMs) {
    superpoweredPlayer->setLoopPoints(startMs, endMs);
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativeStartLoop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->startLoop();
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_nativeStopLoop(JNIEnv *env, jobject instance) {
    superpoweredPlayer->stopLoop();
}




extern "C" JNIEXPORT jint JNICALL
Java_com_example_PlayerListManager_getCurrentPosition(JNIEnv *env, jobject thiz) {
    return superpoweredPlayer->getPlayHeadCurrentPosition();
}
extern "C" JNIEXPORT jint JNICALL
Java_com_example_PlayerListManager_getDuration(JNIEnv *env, jobject thiz) {
    return  superpoweredPlayer->getTrackDuration();
}
extern "C" JNIEXPORT void JNICALL
Java_com_example_PlayerListManager_setPosition(JNIEnv *env, jobject thiz,
                                                               jint position) {

}


// onBackground - Put audio processing to sleep if no audio is playing.
extern "C" JNIEXPORT void
Java_com_example_PlayerListManager_nativeStartLoop_onBackground(JNIEnv *__unused env,
                                                              jobject __unused obj) {
    superpoweredPlayer->activityOnPause();
}

// onForeground - Resume audio processing.
extern "C" JNIEXPORT void
Java_com_example_PlayerListManager_nativeStartLoop_onForeground(JNIEnv *__unused env,
                                                              jobject __unused obj) {
    superpoweredPlayer->activityOnResume();
}

// Cleanup - Free resources.
extern "C" JNIEXPORT void
Java_com_example_PlayerListManager_Cleanup(JNIEnv *__unused env,
                                                         jobject __unused obj) {
superpoweredPlayer->cleanResourses();
}
