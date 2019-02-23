/*
 * Copyright (C)2016 - SMBJ Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hierynomus.smbj.share;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.concurrent.locks.ReentrantLock;

public class FileChannel implements SeekableByteChannel {
    private volatile boolean open = true;
    private final File file;

    private long position;

    private final ReentrantLock positionLock = new ReentrantLock();

    private FileChannel(File file) {
        this.file = file;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        ensureOpen();
        positionLock.lock();
        try {
            int read = file.read(dst.array(), position, dst.position(), dst.remaining());
            dst.position(dst.position() + read);
            if (read > 0)
                position += read;
            return read;
        } finally {
            positionLock.unlock();
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        ensureOpen();
        ensureWritable();
        return 0;
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) {
        position = newPosition;
        return this;
    }

    @Override
    public long size() {
        return file.getFileInformation().getStandardInformation().getEndOfFile();
    }

    @Override
    public SeekableByteChannel truncate(long size) {
        ensureWritable();
        return this;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }

    private void ensureOpen() throws ClosedChannelException {
        if (!isOpen())
            throw new ClosedChannelException();
    }

    private void ensureWritable() {
        throw new NonWritableChannelException();
    }

    public static FileChannel newFileChannel(File file) {
        return new FileChannel(file);
    }
}
