#include <stdlib.h>
#include <stdio.h>
#include "jni.h"

#ifndef ERRORHANDLING
#define ERRORHANDLING

#ifdef __cplusplus
extern "C" {
#endif

jint throwNativeCallException(JNIEnv *env, const char *function, jlong returnCode, jlong errorCode);

#ifdef __cplusplus
}
#endif

#endif