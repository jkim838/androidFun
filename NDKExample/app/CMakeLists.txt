cmake_minimum_required(VERSION 3.4.1)

#ANDROID LIBRARY TO LINK C++ TO NDK?
add_library(app-glue STATIC ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)
target_include_directories(app-glue PUBLIC ${ANDROID_NDK}/sources/android/native_app_glue)

#ANDROID NDK LIBRARY
find_library(log-lib log)

#MY LIBRARY
#add_library(LIBRARY_NAME SHARED FILE_LOCATION)
add_library(native-lib SHARED src/main/cpp/native-lib.cpp)
include_directories(src/main/cpp/) #FOR HEADERS

target_link_libraries(native-lib app-glue ${log-lib})

#IMPORTING PREBUILT LIBRARIES
# 1. LIBRARY NAME 2. DECLARE AS SHARED LIBRARY 3. SET FLAG AS 'IMPORTED'
#add_library(imported-lib SHARED IMPORTED)
# 1. LIBRARY NAME 2. PROPERTIES 3. PATH
#set_target_properties(imported-lib PROPERTIES imported-lib/src/${ANDROID_ABI}/libimported-lib.so)
#include_directories(imported-lib/include/) #TO INCLUDE HEADER FILES
#target_link_libraries(native-lib imported-lib app-glue ${log-lib})
