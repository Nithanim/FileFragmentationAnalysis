#include <wtypes.h>
#include <stdio.h>
#include <WinIoCtl.h>

#include "extent.h"
#include "me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative.h"

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_getStructSize(JNIEnv *env, jclass class, jint nExtents)
{
    return (jint) getRetrievalPointersBufferRealSize(nExtents);
}

RETRIEVAL_POINTERS_BUFFER *getPointers(JNIEnv *env, jobject obj)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    return (RETRIEVAL_POINTERS_BUFFER *)(void *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative__1allocate(JNIEnv *env, jobject obj, jint nExtents)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    RETRIEVAL_POINTERS_BUFFER *addr = (RETRIEVAL_POINTERS_BUFFER *)malloc(getRetrievalPointersBufferRealSize(nExtents));
    (*env)->SetLongField(env, obj, fieldId, (jlong)(void *)addr);
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_getExtentCount(JNIEnv *env, jobject obj)
{
    RETRIEVAL_POINTERS_BUFFER *b = getPointers(env, obj);
    return (jint)b->ExtentCount;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_getStartingVcn(JNIEnv *env, jobject obj)
{
    RETRIEVAL_POINTERS_BUFFER *b = getPointers(env, obj);
    return (jlong)b->StartingVcn.QuadPart;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_getExtentAddr(JNIEnv *env, jobject obj, jint idx)
{
    RETRIEVAL_POINTERS_BUFFER *b = getPointers(env, obj);
    jlong base = (jlong)(intptr_t)&b->Extents;
    return base + (jlong)sizeof(EXTENT) * idx;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_RetrievalPointersBufferNative_close(JNIEnv *env, jobject obj) {
    free(getPointers(env, obj));
}
