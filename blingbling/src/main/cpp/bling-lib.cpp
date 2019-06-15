#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_dinson_customview_manager_BlingNdkHelper_getFromC(JNIEnv *env, jclass type) {
    return env->NewStringUTF("this is from C");
}
