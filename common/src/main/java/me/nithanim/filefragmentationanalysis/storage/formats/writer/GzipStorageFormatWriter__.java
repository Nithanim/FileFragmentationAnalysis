package me.nithanim.filefragmentationanalysis.storage.formats.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import lombok.RequiredArgsConstructor;
import me.nithanim.filefragmentationanalysis.storage.Index;

/**
 * Wrapps another {@link StorageFormatWriter} and compresses the data as gzip
 * with dflate algorithm.
 */
public class GzipStorageFormatWriter__ implements StorageFormatWriter {
    private static final byte[] GZIP_HEADER = new byte[]{0x1f, (byte) 0x8b, Deflater.DEFLATED, 0, 0, 0, 0, 0, 0, 0};
    private final StorageFormatWriter wrapped;

    public GzipStorageFormatWriter__(StorageFormatWriter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void write(OutputStream out, Index index) throws IOException {
        DeflaterOutputStream deflater = new DeflaterOutputStream(out);
        CRC32 crc32 = new CRC32();

        out.write(GZIP_HEADER);
        CompressingOutputStream cos = new CompressingOutputStream(deflater, crc32);
        wrapped.write(cos, index);
        cos.flush(Deflater.FULL_FLUSH);

        writeIntLE(out, (int) crc32.getValue());
        writeIntLE(out, (int) cos.getTotalBytesRead());
    }

    private void writeIntLE(OutputStream o, int i) throws IOException {
        o.write(i);
        o.write(i >> 8);
        o.write(i >> 16);
        o.write(i >> 24);
    }

    @RequiredArgsConstructor
    private static class CompressingOutputStream extends OutputStream {
        private final DeflaterOutputStream deflater;
        private final CRC32 crc32;
        private long totalBytesRead;

        byte[] bufferRead = new byte[1024 * 8];
        int bufferReadPos;
        byte[] bufferWrite = new byte[bufferRead.length];

        @Override
        public void write(int b) throws IOException {
            if (bufferReadPos >= bufferRead.length) {
                flush();
            }
            bufferRead[bufferReadPos++] = (byte) b;
        }

        @Override
        public void flush() throws IOException {
            flush(Deflater.NO_FLUSH);
        }

        public void flush(int flushType) throws IOException {
            crc32.update(bufferRead, 0, bufferReadPos);
            deflater.write(bufferRead, 0, bufferReadPos);
            totalBytesRead += bufferReadPos;
            bufferReadPos = 0;
        }

        public long getTotalBytesRead() {
            return totalBytesRead;
        }
    }
}
