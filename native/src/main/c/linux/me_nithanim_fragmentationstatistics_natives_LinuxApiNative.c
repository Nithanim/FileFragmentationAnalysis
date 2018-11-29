#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/statfs.h>
#include <linux/fs.h>
#include <string.h>
#include <errno.h>
#include <linux/fiemap.h>
#include "me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative.h"
#include "../common/errorhandling.h"

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_openFileForReading(JNIEnv *env, jobject obj, jstring file)
{
    const char *filename = (*env)->GetStringUTFChars(env, file, NULL);
    int r = open(filename, O_RDONLY);
    if (r < 0)
    {
        if (errno == EACCES)
        {
            jclass clazz = (*env)->FindClass(env, "me/nithanim/fragmentationstatistics/natives/PermissionDeniedException");
            (*env)->ThrowNew(env, clazz, "Insufficient permissions.");
        } else
        {
            throwNativeCallException(env, "open", r, errno);
        }
    }
    (*env)->ReleaseStringUTFChars(env, file, filename);
    return r;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_closeFile(JNIEnv *env, jobject obj, jint fd)
{
    int r = close(fd);
    if (r < 0)
    {
        throwNativeCallException(env, "close", r, errno);
    }
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_getBlocksize(JNIEnv *env, jobject obj, jint fd)
{
    jint blockSize;
    //There doesn't seem to be any documention for FIGETBSZ but it is used in conjunction with
    //fibmap to get the blocksize.
    int r = ioctl(fd, FIGETBSZ, &blockSize);
    if (r == -1)
    {
        throwNativeCallException(env, "ioctl", r, errno);
    }
    return blockSize;
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_getFilesystemType(JNIEnv *env, jobject obj, jstring p)
{
    const char *path = (*env)->GetStringUTFChars(env, p, NULL);

    struct statfs sfs;
    int r = statfs(path, &sfs);
    if (r == -1)
    {
        throwNativeCallException(env, "statfs", r, errno);
    }

    (*env)->ReleaseStringUTFChars(env, p, path);
    return (jlong)sfs.f_type;
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_fstat(JNIEnv *env, jobject obj, jint fd, jlong statAddr)
{
    int r = fstat((int)fd, (struct stat *)statAddr);
    if (r == -1)
    {
        throwNativeCallException(env, "fstat", r, errno);
    }
}

JNIEXPORT void JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_fillFiemap(JNIEnv *env, jobject obj, jint fd, jlong fsAddr)
{
    int r = ioctl(fd, FS_IOC_FIEMAP, (struct fiemap*) fsAddr);
    if (r < 0)
    {
        throwNativeCallException(env, "ioctl", r, errno);
    }
}

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxApiNative_fibmap(JNIEnv *env, jobject obj, jint fd, jint index)
{
    int block = index;
    int r = ioctl(fd, FIBMAP, &block);
    if (r < 0)
    {
        if (errno == EPERM)
        {
            jclass c = (*env)->FindClass(env, "me/nithanim/fragmentationstatistics/natives/linux/RootRequiredException");
            (*env)->ThrowNew(env, c, NULL);
        }
        else
        {
            throwNativeCallException(env, "ioctl", r, errno);
        }
    }
    return block;
}

//DOES NOT WORK BECAUSE IT NEEDS ROOT! WE CAN ONLY USE BLOCKS (CLUSTERS), NEVER SECTORS.
/*
JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_linux_LinuxapiNative_getSectorsize(JNIEnv *env, jobject obj, jint fd)
{
    jint sectorSize;
    
    int r = ioctl(fd, BLKSSZGET, &sectorSize);
    if (r <= 0)
    {
        throwNativeCallException(env, "icotl with BLKSSZGET", r, errno);
    }
    return sectorSize;
}
*/
