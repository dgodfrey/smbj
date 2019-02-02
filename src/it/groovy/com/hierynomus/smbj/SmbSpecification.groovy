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
package com.hierynomus.smbj

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import spock.lang.Shared
import spock.lang.Specification

class SmbSpecification extends Specification {
  @Shared
  @ClassRule
  TemporaryFolder folder = new TemporaryFolder();

  @Shared
  GenericContainer c;

  void setupSpec() {
    folder.getRoot().setWritable(true, false);

    c = new GenericContainer<>("smbj/smbj-itest:latest")
      .waitingFor(Wait.forLogMessage('.*nmbd entered RUNNING state.*', 1))
      .withFileSystemBind(folder.root.absolutePath, '/opt/samba/user')
      .withExposedPorts(445);

    c.start()
  }

  def cleanup() {
    def root = folder.root.toPath()

    Files.walkFileTree(root, new Deleter(root));
  }

  def cleanupSpec() {
    c.stop()
  }

  static class Deleter extends SimpleFileVisitor<Path> {

    private Path root

    Deleter(Path root) {
      this.root = root
    }

    @Override
    FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return super.visitFile(file, attrs)
    }

    @Override
    FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      if (dir != root)
        Files.delete(dir);
      return super.postVisitDirectory(dir, exc)
    }
  }
}
