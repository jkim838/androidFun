//
// Created by OKIM on 23/01/2020.
//

#include "native-lib.h"
#include <android_native_app_glue.h>
#include <jni.h>
#include <cstring>
#include <stdio.h>
#include <android/log.h>

int cookies = 0;
extern "C" JNIEXPORT jstring JNICALL
Java_com_jkim838_ndkexample_MainActivity_showCookie(JNIEnv *env, jclass instance)
{
    // Equivalent to printf
    __android_log_print(ANDROID_LOG_DEBUG, "COOKIE", "C++ COOKIES INT %d", cookies);
    // crate a buffer for characters
    char buffer[32];
    // cast integer to string and assign them to the buffer
    sprintf(buffer, "%d", cookies);
    // return as string
    return env->NewStringUTF(buffer);
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_jkim838_ndkexample_MainActivity_bakeCookie(JNIEnv *env, jclass clazz) {
    cookies++;
    __android_log_print(ANDROID_LOG_DEBUG, "COOKIE", "COOKIE BAKED: %d", cookies);
    return cookies;
}