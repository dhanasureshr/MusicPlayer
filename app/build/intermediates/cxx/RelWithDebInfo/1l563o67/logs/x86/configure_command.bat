@echo off
"C:\\Users\\dhana suresh\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HF:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=24" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=C:\\Users\\dhana suresh\\AppData\\Local\\Android\\Sdk\\ndk\\25.1.8937393" ^
  "-DCMAKE_ANDROID_NDK=C:\\Users\\dhana suresh\\AppData\\Local\\Android\\Sdk\\ndk\\25.1.8937393" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\Users\\dhana suresh\\AppData\\Local\\Android\\Sdk\\ndk\\25.1.8937393\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\Users\\dhana suresh\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_C_FLAGS=-O3 -fsigned-char" ^
  "-DCMAKE_CXX_FLAGS=-fsigned-char -IF:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app\\..\\..\\..\\Superpowered" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=F:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app\\build\\intermediates\\cxx\\RelWithDebInfo\\1l563o67\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=F:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app\\build\\intermediates\\cxx\\RelWithDebInfo\\1l563o67\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=RelWithDebInfo" ^
  "-BF:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app\\.cxx\\RelWithDebInfo\\1l563o67\\x86" ^
  -GNinja ^
  "-DANDROID_PLATFORM=android-24" ^
  "-DANDROID_TOOLCHAIN=clang" ^
  "-DANDROID_ARM_NEON=TRUE" ^
  "-DANDROID_STL=c++_static" ^
  "-DPATH_TO_SUPERPOWERED:STRING=F:\\audioapi\\SuperpoweredSDK\\SuperpoweredSDK\\Examples_Android\\SuperpoweredPlayer\\app\\..\\..\\..\\Superpowered"
