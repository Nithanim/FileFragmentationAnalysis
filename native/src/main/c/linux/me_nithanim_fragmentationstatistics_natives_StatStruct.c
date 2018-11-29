#include <stdlib.h>
#include <stdio.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <linux/fs.h>
#include <linux/hdreg.h>
#include <assert.h>
#include <string.h>
#include <errno.h>

#include "me_nithanim_fragmentationstatistics_natives_linux_StatStruct.h"

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_alloc
(JNIEnv *env, jobject obj) {
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    struct stat* addr = (struct stat*) malloc(sizeof (struct stat));
    (*env)->SetLongField(env, obj, fieldId, (jlong) addr);
}

struct stat* getStat(JNIEnv *env, jobject obj) {
    jclass class = (*env)->GetObjectClass(env, obj);
    jfieldID fieldId = (*env)->GetFieldID(env, class, "addr", "J");
    return (struct stat*) (*env)->GetLongField(env, obj, fieldId);
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getDev
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_dev;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getInodeNumber
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_ino;
}

JNIEXPORT jshort JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getMode
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jshort) stat->st_mode;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getNumberHardlinks
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jint) stat->st_nlink;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getUid
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jint) stat->st_uid;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getGid
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jint) stat->st_gid;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getRdev
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_rdev;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getSize
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_size;
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getBlksize
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jint) stat->st_blksize;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getBlocks
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_blocks;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getATime
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_atime;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getMTime
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_mtime;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_getCTime
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    return (jlong) stat->st_ctime;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_StatStruct_close
(JNIEnv *env, jobject obj) {
    struct stat* stat = getStat(env, obj);
    free(stat);
}
