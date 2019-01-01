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

import java.nio.file.FileSystems
import spock.lang.PendingFeature

class FileSystemIntegrationSpec extends SmbSpecification {
  def uri = URI.create("smb://${USER}:${PASSWORD}@${c.containerIpAddress}:${c.firstMappedPort}/user")

  def fileSystem = FileSystems.newFileSystem(uri, [:])

  def cleanup() {
    fileSystem.close()
  }

  @PendingFeature
  def "returns root directories"() {
    when:
    def roots = fileSystem.rootDirectories

    then:
    roots.size() == 1
    roots[0].toString() == '\\'
  }
}
