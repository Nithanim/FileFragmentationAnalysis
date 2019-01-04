#include <stdlib.h>
#include <sys/statvfs.h>

#include "me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative.h"

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_alloc(JNIEnv *env, jobject obj)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    struct statvfs *addr = (struct statvfs *)malloc(sizeof(struct statvfs));
    (*env)->SetLongField(env, obj, fieldId, (jlong)addr);
}

struct statvfs *getStruct(JNIEnv *env, jobject obj)
{
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    return (struct statvfs *)(*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getPreferredBlockSize(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_bsize;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getMinimumBlockSize(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_frsize;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getTotalNumberOfBlocks(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_blocks;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getNumberOfFreeBlocks(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_bfree;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getNumberOfFreeBlocksUnprivileged(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_bavail;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getNumberOfInodes(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_files;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getNumberOfFreeInodes(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_ffree;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getNumberOfFreeInodesUnprivileged(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_favail;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getFileSystemId(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_fsid;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getMountFlags(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_flag;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_getMaxFileNameLength(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    return (jlong)str->f_namemax;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStructNative_close(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    free(str);
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatVfsStruct_close(JNIEnv *env, jobject obj)
{
    struct statvfs *str = getStruct(env, obj);
    free(str);
}
