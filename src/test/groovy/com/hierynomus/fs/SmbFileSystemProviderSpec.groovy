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
package com.hierynomus.fs

import java.nio.file.FileSystemAlreadyExistsException
import java.nio.file.FileSystemNotFoundException
import spock.lang.Specification

abstract class SmbFileSystemProviderSpec extends Specification {

  def uri = URI.create('smb://user:password@server/share')
  def provider = new SmbFileSystemProvider()

  static class WithNoFileSystems extends SmbFileSystemProviderSpec {

    def "should throw exception if FileSystem not created"() {
      when:
      provider.getFileSystem(uri)

      then:
      thrown FileSystemNotFoundException
    }

    def "should throw exception if server not specified"() {
      when:
      provider.newFileSystem(URI.create("smb://user:pw@/share"), [:])

      then:
      thrown InvalidShareException
    }

    def "should throw exception if share not specified"() {
      when:
      provider.newFileSystem(URI.create("smb://user:pw@server/"), [:])

      then:
      thrown InvalidShareException
    }

    def "should throw exception if no user not specified"() {
      when:
      provider.newFileSystem(URI.create("smb://server/share"), [:])

      then:
      thrown InvalidShareException
    }

    def "creates a FileSystem"() {
      when:
      def fs = provider.newFileSystem(uri, [:])

      then:
      fs instanceof SmbFileSystem
    }
  }

  static class WithFileSystem extends SmbFileSystemProviderSpec {

    def fileSystem = provider.newFileSystem(uri, [:])

    def "should throw exception if fileSystem already created"() {
      when:
      provider.newFileSystem(uri, [:])

      then:
      thrown FileSystemAlreadyExistsException
    }

    def "can create filesystem for different host"() {
      when:
      def other = provider.newFileSystem(URI.create('smb://user:password@server2/share'), [:])

      then:
      other != fileSystem
    }

    def "can create filesystem for different user"() {
      when:
      def other = provider.newFileSystem(URI.create('smb://user2:password@server/share'), [:])

      then:
      other != fileSystem
    }

    def "can create filesystem for different share"() {
      when:
      def other = provider.newFileSystem(URI.create('smb://user:password@server/share2'), [:])

      then:
      other != fileSystem
    }

    def "should return same FileSystem if fileSystem already created"() {
      when:
      def current = provider.getFileSystem(uri)

      then:
      fileSystem == current
    }

    def "should return same FileSystem if fileSystem already created except with different password"() {
      when:
      def current = provider.getFileSystem(URI.create('smb://user:XXXXX@server/share'))

      then:
      fileSystem == current
    }

    def "should remove FileSystem"() {
      when:
      provider.removeFileSystem(fileSystem)
      provider.getFileSystem(uri)

      then:
      thrown FileSystemNotFoundException

      when:
      def current = provider.newFileSystem(uri, [:])

      then:
      fileSystem != current
    }
  }
}
