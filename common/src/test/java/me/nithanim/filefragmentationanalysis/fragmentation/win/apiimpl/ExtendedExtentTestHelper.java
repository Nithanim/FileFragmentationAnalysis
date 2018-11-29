package me.nithanim.filefragmentationanalysis.fragmentation.win.apiimpl;

import lombok.Value;
import me.nithanim.fragmentationstatistics.natives.windows.RetrievalPointersBuffer;

@Value
class ExtendedExtentTestHelper implements RetrievalPointersBuffer.Extent {
    long currVcn;
    long lcn;
    long nextVcn;
}
