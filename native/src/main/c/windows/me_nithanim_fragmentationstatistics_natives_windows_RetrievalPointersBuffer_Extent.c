#include <wtypes.h>
#include <stdio.h>
#include <WinIoCtl.h>

#include "extent.h"
#include "me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_ExtentNative.h"

EXTENT *getExtent(JNIEnv *env, jobject obj)
{
  jclass class = (*env)->GetObjectClass(env, obj);
  jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
  return (EXTENT *)(void *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_00024ExtentNative_getNextVcn(JNIEnv *env, jobject obj)
{
  EXTENT *b = getExtent(env, obj);
  return (jlong)b->NextVcn.QuadPart;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_00024ExtentNative_getLcn(JNIEnv *env, jobject obj)
{
  EXTENT *b = getExtent(env, obj);
  return (jlong)b->Lcn.QuadPart;
}
