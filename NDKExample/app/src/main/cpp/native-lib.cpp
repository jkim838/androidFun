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
    __android_log_print(ANDROID_LOG_DEBUG, "COOKIE", "C++ COOKIES INT %d", cookies);
    char buffer[32];
    sprintf(buffer, "%d", cookies);
    return env->NewStringUTF(buffer);
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_jkim838_ndkexample_MainActivity_bakeCookie(JNIEnv *env, jclass clazz) {
    // TODO: implement bakeCookie()
    cookies++;
    __android_log_print(ANDROID_LOG_DEBUG, "COOKIE", "COOKIE BAKED: %d", cookies);
    return cookies;
}