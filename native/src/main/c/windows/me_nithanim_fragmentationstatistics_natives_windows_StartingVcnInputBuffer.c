#include <wtypes.h>

#include "extent.h"
#include "me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative.h"

STARTING_VCN_INPUT_BUFFER *getVcnInputBuffer(JNIEnv *env, jobject obj)
{
  jclass class = (*env)->GetObjectClass(env, obj);
  jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
  return (STARTING_VCN_INPUT_BUFFER *)(void *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative__1allocate(JNIEnv *env, jobject obj)
{
  jclass class = (*env)->GetObjectClass(env, obj);
  jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
  STARTING_VCN_INPUT_BUFFER *addr = (STARTING_VCN_INPUT_BUFFER *)malloc(sizeof(STARTING_VCN_INPUT_BUFFER));
  (*env)->SetLongField(env, obj, fieldId, (jlong)(void *)addr);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative_getStartingVcn(JNIEnv *env, jobject obj)
{
  STARTING_VCN_INPUT_BUFFER *b = getVcnInputBuffer(env, obj);
  return (jint)b->StartingVcn.QuadPart;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative_setStartingVcn(JNIEnv *env, jobject obj, jlong startingVcn)
{
  STARTING_VCN_INPUT_BUFFER *b = getVcnInputBuffer(env, obj);
  LARGE_INTEGER li;
  li.QuadPart = startingVcn;
  b->StartingVcn = li;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative_getStructSize(JNIEnv *env, jobject obj)
{
  return sizeof(STARTING_VCN_INPUT_BUFFER);
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_StartingVcnInputBufferNative_close(JNIEnv *env, jobject obj)
{
  free(getVcnInputBuffer(env, obj));
}
