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
package com.hierynomus.fs;

import java.io.File;
import java.net.URI;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

public class SmbFsPath implements Path {

    private static final char SEPARATOR = '\\';

    private final SmbFileSystem fileSystem;
    private final boolean absolute;
    private final String[] segments;

    SmbFsPath(SmbFileSystem fileSystem, boolean absolute, String... segments) {
        this.fileSystem = fileSystem;
        this.absolute = absolute;
        this.segments = segments;
    }

    @Override
    public SmbFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public boolean isAbsolute() {
        return absolute;
    }

    @Override
    public Path getRoot() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path getFileName() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path getParent() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public int getNameCount() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path getName(int index) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean startsWith(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean startsWith(String other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean endsWith(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean endsWith(String other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path normalize() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path resolve(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path resolve(String other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path resolveSibling(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path resolveSibling(String other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path relativize(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public URI toUri() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path toAbsolutePath() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path toRealPath(LinkOption... options) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public File toFile() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Iterator<Path> iterator() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public int compareTo(Path other) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        if (isAbsolute())
            b.append(SEPARATOR);

        for (int i = 0; i < segments.length; i++) {
            if (i > 0)
                b.append(SEPARATOR);

            String each = segments[i];
            b.append(each);
        }

        return b.toString();
    }
}
