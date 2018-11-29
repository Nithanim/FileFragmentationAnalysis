#include "extent.h"

size_t getRetrievalPointersBufferRealSize(size_t nExtents) {
  return sizeof(RETRIEVAL_POINTERS_BUFFER) + sizeof(EXTENT) * (nExtents - 1); //one extent element is already in the struct...
}
