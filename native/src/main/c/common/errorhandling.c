#include "errorhandling.h"

jint throwNativeCallException(JNIEnv *env, const char *function, jlong returnCode, jlong errorCode)
{
  char *className = "me/nithanim/fragmentationstatistics/natives/NativeCallException";

  jclass exceptionClass = (*env)->FindClass(env, className);
  if (exceptionClass == NULL)
  {
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NoClassDefFoundError"), className);
    return 0;
  }

  jmethodID exceptionConstructor = (*env)->GetMethodID(env, exceptionClass, "<init>", "(Ljava/lang/String;JI)V");
  if (exceptionConstructor == NULL)
  {
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NoSuchMethodError"), className);
    return 0;
  }

  jstring functionNameJni = (*env)->NewStringUTF(env, function);
  jobject exception = (*env)->NewObject(env, exceptionClass, exceptionConstructor, functionNameJni, returnCode, errorCode);
  if (exception == NULL)
  {
    size_t strlen = (size_t) snprintf(NULL, 0, "Unable to construct exception %s", className);
    char *msg = (char*)malloc(strlen + 1);

    sprintf(msg, "Unable to construct exception %s", className);
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/Exception"), msg);
    free(msg);
    return 0;
  }

  return (*env)->Throw(env, exception);
}
