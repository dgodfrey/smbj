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

import spock.lang.Specification

class SmbFsPathSpec extends Specification {

  def fileSystem = new SmbFileSystem(null)

  def 'calculates path'() {
    expect:
    abs().toString() == '\\'
    abs('file.txt').toString() == '\\file.txt'
    abs('dir').toString() == '\\dir'
    abs('dir', 'dir2').toString() == '\\dir\\dir2'

    rel().toString() == ''
    rel('file.txt').toString() == 'file.txt'
    rel('dir').toString() == 'dir'
    rel('dir', 'dir2').toString() == 'dir\\dir2'
  }

  def 'returns root'() {
    expect:
    abs('dir', 'dir2').root.toString() == '\\'

    rel('dir', 'dir2').root == null
  }

  def 'returns filename'() {
    expect:
    abs().fileName == null
    abs('dir', 'dir2').fileName.toString() == 'dir2'

    rel('dir', 'file.txt').fileName.toString() == 'file.txt'
  }

  def 'returns parent'() {
    expect:
    abs().parent == null
    abs("dir").parent.toString() == '\\'
    abs("dir", "dir2").parent.toString() == '\\dir'

    rel("dir").parent == null
    rel("dir", "dir2").parent.toString() == 'dir'
  }

  def 'returns name count'() {
    expect:
    abs().nameCount == 0
    abs("dir").nameCount == 1
    abs("dir", "dir2").nameCount == 2

    rel("dir").nameCount == 1
    rel("dir", "dir2").nameCount == 2
  }

  def 'returns name'() {
    expect:
    abs("dir").getName(0).toString() == 'dir'
    abs("dir", "dir2").getName(0).toString() == 'dir'
    abs("dir", "dir2").getName(1).toString() == 'dir2'

    rel("dir").getName(0).toString() == 'dir'
    rel("dir", "dir2").getName(0).toString() == 'dir'
    rel("dir", "dir2").getName(1).toString() == 'dir2'
  }

  def 'returns subpath'() {
    when:
    def abs = abs("dir", "dir2", 'dir3', 'dir4')
    def rel = rel("dir", "dir2", 'dir3', 'dir4')

    then:
    abs.subpath(1, 2).toString() == 'dir2'
    abs.subpath(1, 4).toString() == 'dir2\\dir3\\dir4'

    rel.subpath(1, 2).toString() == 'dir2'
    rel.subpath(1, 4).toString() == 'dir2\\dir3\\dir4'
  }

  private SmbFsPath abs(String... segments) {
    new SmbFsPath(fileSystem, true, segments)
  }

  private SmbFsPath rel(String... segments) {
    new SmbFsPath(fileSystem, false, segments)
  }
}
