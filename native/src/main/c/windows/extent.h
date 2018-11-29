#include <wtypes.h>

#ifndef EXTENT_STRUCT
#define EXTENT_STRUCT

typedef struct EXTENT {
    LARGE_INTEGER NextVcn;
    LARGE_INTEGER Lcn;
} EXTENT;

size_t getRetrievalPointersBufferRealSize(size_t nExtents);

#endif
