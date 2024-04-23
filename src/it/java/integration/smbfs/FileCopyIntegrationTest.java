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
package integration.smbfs;

import com.hierynomus.smbfs.SmbFileSystem;
import com.hierynomus.smbfs.SmbFileSystemProvider;
import com.hierynomus.smbfs.SmbPath;
import com.hierynomus.smbj.testcontainers.SambaContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Files;
import java.nio.file.Path;

import static integration.smbfs.RandomData.randomString;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class FileCopyIntegrationTest {

    @Container
    private final SambaContainer samba = SambaContainer.INSTANCE;

    private SmbFileSystemProvider provider;

    @BeforeEach
    void setUp() {
        provider = new SmbFileSystemProvider();
    }

    @Test
    void copiesFileOnSameShare(@TempDir Path folder) throws Exception {

        String data = randomString(23);
        Transferable transferable = Transferable.of(data);
        samba.copyFileToContainer(transferable, "/opt/samba/user/source.txt");

        try (SmbFileSystem fileSystem = provider.newFileSystem(samba.userUri(), emptyMap())) {
            SmbPath source = fileSystem.getPath("source.txt");
            SmbPath target = fileSystem.getPath("target.txt");
            fileSystem.provider().copy(source, target);
        }

        Path localPath = folder.resolve("written.txt");
        samba.copyFileFromContainer("/opt/samba/user/target.txt", localPath.toString());

        String written = Files.readString(localPath);
        assertEquals(data, written);
    }

    @Test
    void copiesFileOnDifferentShares(@TempDir Path folder) throws Exception {

        String data = randomString(33);
        Transferable transferable = Transferable.of(data);
        samba.copyFileToContainer(transferable, "/opt/samba/share/source.txt");

        try (SmbFileSystem fileSystem = provider.newFileSystem(samba.publicUri(), emptyMap());
             SmbFileSystem targetFileSystem = provider.newFileSystem(samba.userUri(), emptyMap())) {

            SmbPath source = fileSystem.getPath("source.txt");
            SmbPath target = targetFileSystem.getPath("target.txt");

            fileSystem.provider().copy(source, target);
        }

        Path localPath = folder.resolve("written.txt");
        samba.copyFileFromContainer("/opt/samba/user/target.txt", localPath.toString());

        String written = Files.readString(localPath);
        assertEquals(data, written);
    }
}
