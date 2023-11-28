cmake_minimum_required(VERSION 3.4.1)

# convert SDK path to forward slashes on Windows
file(TO_CMAKE_PATH ${PATH_TO_SUPERPOWERED} PATH_TO_SUPERPOWERED)

include_directories(src/main/cpp)
include_directories(${PATH_TO_SUPERPOWERED})
include_directories(${SUPERPOWERED_PLAYER_LIB_DIR}/)

# compile player example to a native library
add_library(
        PlayerExample
        SHARED
        src/main/cpp/PlayerExample.cpp
        ${PATH_TO_SUPERPOWERED}/OpenSource/SuperpoweredAndroidAudioIO.cpp
)

#[[
# Add the SuperpoweredPlayer library
add_library(superpoweredplayer
        SHARED
        src/main/cpp/PlayerListManager.cpp
        ${PATH_TO_SUPERPOWERED}/OpenSource/SuperpoweredAndroidAudioIO.cpp
        )
]]

add_library(audio_engin SHARED

        src/main/cpp/AudioEngin.cpp
        ${PATH_TO_SUPERPOWERED}/OpenSource/SuperpoweredAndroidAudioIO.cpp
        )

# link the native library against the following libraries
target_link_libraries(
        PlayerExample
        log
        android
        OpenSLES
        ${PATH_TO_SUPERPOWERED}/libSuperpoweredAndroid${ANDROID_ABI}.a
)
# link the native library against the following libraries
target_link_libraries(
        audio_engin
        log
        android
        OpenSLES
        ${PATH_TO_SUPERPOWERED}/libSuperpoweredAndroid${ANDROID_ABI}.a
)
