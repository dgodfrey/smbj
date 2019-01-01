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

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Collections;
import java.util.Set;

class SmbFileSystem extends FileSystem {

    private final SmbFileSystemProvider provider;
    private final SmbFsPath root;

    private boolean open;

    SmbFileSystem(SmbFileSystemProvider provider) {
        this.provider = provider;
        this.root = new SmbFsPath(this, true);
    }

    @Override
    public SmbFileSystemProvider provider() {
        return provider;
    }

    @Override
    public void close() {
        open = false;
        provider.removeFileSystem(this);
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean isReadOnly() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public String getSeparator() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    Path getRootDirectory() {
        return root;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return Collections.singleton(getRootDirectory());
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Path getPath(String first, String... more) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public WatchService newWatchService() {
        // Todo: implement
        throw new UnsupportedOperationException("todo");
    }
}
