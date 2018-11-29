#include <sys/types.h>
#include <errno.h>
#include <linux/fiemap.h>

#include "../common/errorhandling.h"
#include "me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative.h"

struct fiemap *getFiemap(JNIEnv *env, jobject obj)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    return (struct fiemap *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_alloc(JNIEnv *env, jobject obj, jint maxExtents)
{
    struct fiemap *addr = (struct fiemap *)malloc(sizeof(struct fiemap) + sizeof(struct fiemap_extent) * (size_t)maxExtents);
    addr->fm_extent_count = (__u32)maxExtents;
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    (*env)->SetLongField(env, obj, fieldId, (jlong)addr);
    return (jlong)addr;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_setStart(JNIEnv *env, jobject obj, jlong offset)
{
    getFiemap(env, obj)->fm_start = (__u64)offset;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_setLength(JNIEnv *env, jobject obj, jlong length)
{
    getFiemap(env, obj)->fm_length = (__u64)length;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_setFlags(JNIEnv *env, jobject obj, jint flags)
{
    getFiemap(env, obj)->fm_flags = (__u32)flags;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_getFlags(JNIEnv *env, jobject obj)
{
    return (jint)getFiemap(env, obj)->fm_flags;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_getMappedExtents(JNIEnv *env, jobject obj)
{
    return (jint)getFiemap(env, obj)->fm_mapped_extents;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_getExtentAddr(JNIEnv *env, jobject obj, jint i)
{
    return (jlong)&getFiemap(env, obj)->fm_extents[i];
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapStructNative_close(JNIEnv *env, jobject obj)
{
    free(getFiemap(env, obj));
}