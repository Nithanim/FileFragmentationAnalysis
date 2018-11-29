
#include <sys/ioctl.h>
#include <sys/types.h>
#include <string.h>
#include <errno.h>
#include <linux/fiemap.h>

#include "../common/errorhandling.h"
#include "me_nithanim_fragmentationstatistics_natives_linux_FiemapExtentNative.h"

struct fiemap_extent *getFiemapExtent(JNIEnv *env, jobject obj)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    return (struct fiemap_extent *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapExtentNative_getLogical(JNIEnv *env, jobject obj)
{
    return (jlong)getFiemapExtent(env, obj)->fe_logical;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapExtentNative_getPhysical(JNIEnv *env, jobject obj)
{
    return (jlong)getFiemapExtent(env, obj)->fe_physical;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapExtentNative_getLength(JNIEnv *env, jobject obj)
{
    return (jlong)getFiemapExtent(env, obj)->fe_length;
}
JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_FiemapExtentNative_getFlags(JNIEnv *env, jobject obj)
{
    return (jint)getFiemapExtent(env, obj)->fe_flags;
}
