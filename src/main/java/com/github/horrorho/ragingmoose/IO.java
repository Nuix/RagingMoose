/*
 * The MIT License
 *
 * Copyright 2017 Ayesha.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.ragingmoose;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author Ayesha
 */
@Immutable
@ParametersAreNonnullByDefault
final class IO {

    @Nonnull
    static ByteBuffer readFully(@WillNotClose ReadableByteChannel ch, ByteBuffer bb)
            throws EOFException, IOException {
        while (bb.hasRemaining()) {
            if (ch.read(bb) == -1) {
                throw new EOFException();
            }
        }
        return bb;
    }

    /**
     *
     * @param is
     * @param bb
     * @return
     * @throws EOFException
     * @throws IOException
     * @throws UnsupportedOperationException if this buffer is not backed by an accessible array
     */
    @Nonnull
    static ByteBuffer readFully(@WillNotClose InputStream is, ByteBuffer bb) throws EOFException, IOException {
        byte[] bs = bb.array();
        int off = bb.arrayOffset() + bb.position();
        int n = bb.arrayOffset() + bb.limit();
        while (off < n) {
            int read = is.read(bs, off, n - off);
            if (read == -1) {
                throw new EOFException();
            }
            off += read;
        }
        bb.position(bb.limit());
        return bb;
    }

    static void copy(@WillNotClose InputStream is, @WillNotClose OutputStream os, long n)
            throws EOFException, IOException {
        for (long i = 0; i < n; i++) {
            int b = is.read();
            if (b == -1) {
                throw new EOFException();
            }
            os.write(b);
        }
    }

    private IO() {
    }
}
