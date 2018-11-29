#include <wtypes.h>
#include <WinIoCtl.h>

#include "me_nithanim_fragmentationstatistics_natives_windows_WinapiNative.h"
#include "extent.h"
#include "../common/errorhandling.h"

JNIEXPORT jint JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_WinapiNative_fillRetrievalPointers(
    JNIEnv *env, jobject obj, jlong handle, jlong inputBufferAddr, jlong outputBufferAddr, jint nAllocatedExtents)
{
  DWORD bytesReturned;
  BOOL r = DeviceIoControl(
      (HANDLE)handle,
      FSCTL_GET_RETRIEVAL_POINTERS,
      (void *)inputBufferAddr,
      sizeof(STARTING_VCN_INPUT_BUFFER),
      (void *)outputBufferAddr,
      getRetrievalPointersBufferRealSize((size_t)nAllocatedExtents),
      &bytesReturned,
      NULL);

  if (r != 0)
  {
    return NOERROR;
  }
  else
  {
    return GetLastError();
  }
}

JNIEXPORT jlong JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_WinapiNative_createFile(JNIEnv *env, jobject obj, jstring path)
{
  const char *pathNative = (*env)->GetStringUTFChars(env, path, 0);
  HANDLE h = CreateFile(
      pathNative,
      FILE_GENERIC_READ,
      FILE_SHARE_READ,
      NULL,
      OPEN_EXISTING,
      FILE_ATTRIBUTE_NORMAL,
      NULL);

  (*env)->ReleaseStringUTFChars(env, path, pathNative);

  if (h == INVALID_HANDLE_VALUE)
  {
    throwNativeCallException(env, "CreateFile", (jlong)h, GetLastError());
  }

  return (jlong)h;
}

JNIEXPORT jboolean JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_WinapiNative_closeHandle(JNIEnv *env, jobject obj, jlong handle)
{
  BOOL r = CloseHandle((HANDLE)handle);
  if (r == 0)
  {
    throwNativeCallException(env, "CloseHandle", r, GetLastError());
  }
  return r;
}

JNIEXPORT jobject JNICALL Java_me_nithanim_fragmentationstatistics_natives_windows_WinapiNative_getFileSystemInformation(
    JNIEnv *env, jobject obj, jstring path)
{
  const char *filePath = (*env)->GetStringUTFChars(env, path, 0);

  char volumePathName[20];
  char filesystemName[20];

  BOOL fRes;

  fRes = GetVolumePathName(
      filePath,
      (LPSTR)&volumePathName,
      (DWORD)sizeof(volumePathName));

  (*env)->ReleaseStringUTFChars(env, path, filePath);

  if (!fRes)
  {
    throwNativeCallException(env, "GetVolumePathName", fRes, GetLastError());
    return NULL;
  }

  fRes = GetVolumeInformation(
      volumePathName,
      NULL,
      0,
      NULL,
      NULL,
      NULL,
      (LPSTR)&filesystemName,
      (DWORD)sizeof(filesystemName));

  if (!fRes)
  {
    throwNativeCallException(env, "GetVolumeInformation", fRes, GetLastError());
    return NULL;
  }

  DWORD sectorsPerCluster;
  DWORD bytesPerSector;
  DWORD numberOfFreeClusters;
  DWORD totalNumberOfClusters;
  fRes = GetDiskFreeSpace(
      volumePathName,
      &sectorsPerCluster,
      &bytesPerSector,
      &numberOfFreeClusters,
      &totalNumberOfClusters);

  if (!fRes)
  {
    throwNativeCallException(env, "GetDiskFreeSpace", fRes, GetLastError());
    return NULL;
  }

  jstring filesystemNameJni = (*env)->NewStringUTF(env, filesystemName);
  const char *fsiClassName = "me/nithanim/fragmentationstatistics/natives/windows/FileSystemInformation";
  jclass fsiClass = (*env)->FindClass(env, fsiClassName);
  if (fsiClass == NULL)
  {
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NoClassDefFoundError"), fsiClassName);
    return NULL;
  }
  jmethodID fsiConstructor = (*env)->GetMethodID(env, fsiClass, "<init>", "(Ljava/lang/String;II)V");
  if (fsiConstructor == NULL)
  {
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NoSuchMethodError"), "Cannot find constructor!");
    return NULL;
  }
  jobject fsi = (*env)->NewObject(env, fsiClass, fsiConstructor, filesystemNameJni, sectorsPerCluster, bytesPerSector);
  return fsi;
}
