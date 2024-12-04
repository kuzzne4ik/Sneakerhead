#include <jni.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_sneakershop_SneakersAdapter_updateLastClickTime(JNIEnv *env, jobject thiz,
                                                                 jlong currentTime) {
    return (jlong)currentTime;
}