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

  String uriString = 'smb://user:password@server/share'

  def factory = Mock(Factory)

  def uri = URI.create(uriString)
  def provider = new SmbFileSystemProvider(factory)

  static class WithNoFileSystems extends SmbFileSystemProviderSpec {

    def fileSystem = Mock(SmbFileSystem)

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
      given:
      factory.createFileSystem(provider) >> fileSystem

      when:
      def fs = provider.newFileSystem(uri, [:])

      then:
      fs == fileSystem
    }
  }

  static class WithFileSystem extends SmbFileSystemProviderSpec {

    def mockFileSystem1 = Mock(SmbFileSystem)
    def mockFileSystem2 = Mock(SmbFileSystem)

    SmbFileSystem fileSystem

    def setup() {
      factory.createFileSystem(provider) >>> [mockFileSystem1, mockFileSystem2]
      fileSystem = provider.newFileSystem(uri, [:])
    }

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
      other == mockFileSystem2
    }

    def "can create filesystem for different user"() {
      when:
      def other = provider.newFileSystem(URI.create('smb://user2:password@server/share'), [:])

      then:
      other == mockFileSystem2
    }

    def "can create filesystem for different share"() {
      when:
      def other = provider.newFileSystem(URI.create('smb://user:password@server/share2'), [:])

      then:
      other == mockFileSystem2
    }

    def "should return same FileSystem if fileSystem already created"() {
      when:
      def current = provider.getFileSystem(uri)

      then:
      current == mockFileSystem1
    }

    def "should return same FileSystem if fileSystem already created except with different password"() {
      when:
      def current = provider.getFileSystem(URI.create('smb://user:XXXXX@server/share'))

      then:
      current == mockFileSystem1
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
      current == mockFileSystem2
    }

    def 'should return path from URI'() {
      when:
      def path1 = provider.getPath(URI.create("${uriString}/dir/file.txt"))
      def path2 = provider.getPath(URI.create("${uriString}/file2.txt"))
      def path3 = provider.getPath(URI.create("smb://user:password@server/share2/file2.txt"))

      then:
      path1.fileSystem == mockFileSystem1
      path1.toString() == '\\dir\\file.txt'

      path2.fileSystem == mockFileSystem1
      path2.toString() == '\\file2.txt'

      path3.fileSystem == mockFileSystem2
      path3.toString() == '\\file2.txt'
    }
  }
}
