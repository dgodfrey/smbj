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
    path(true).toString() == '\\'
    path(true, 'file.txt').toString() == '\\file.txt'
    path(true, 'dir').toString() == '\\dir'
    path(true, 'dir', 'dir2').toString() == '\\dir\\dir2'

    path(false).toString() == ''
    path(false, 'file.txt').toString() == 'file.txt'
    path(false, 'dir').toString() == 'dir'
    path(false, 'dir', 'dir2').toString() == 'dir\\dir2'
  }

  def 'returns root'() {
    expect:
    path(true, 'dir', 'dir2').root.toString() == '\\'

    path(false, 'dir', 'dir2').root == null
  }

  def 'returns filename'() {
    expect:
    path(true).fileName == null
    path(true, 'dir', 'dir2').fileName.toString() == 'dir2'
    path(false, 'dir', 'file.txt').fileName.toString() == 'file.txt'
  }

  def 'returns parent'() {
    expect:
    path(true).parent == null
    path(true, "dir").parent.toString() == '\\'
    path(true, "dir", "dir2").parent.toString() == '\\dir'

    path(false, "dir").parent == null
    path(false, "dir", "dir2").parent.toString() == 'dir'
  }

  def 'returns name count'() {
    expect:
    path(true).nameCount == 0
    path(true, "dir").nameCount == 1
    path(true, "dir", "dir2").nameCount == 2

    path(false, "dir").nameCount == 1
    path(false, "dir", "dir2").nameCount == 2
  }

  def 'returns name'() {
    expect:
    path(true, "dir").getName(0).toString() == 'dir'
    path(true, "dir", "dir2").getName(0).toString() == 'dir'
    path(true, "dir", "dir2").getName(1).toString() == 'dir2'

    path(false, "dir").getName(0).toString() == 'dir'
    path(false, "dir", "dir2").getName(0).toString() == 'dir'
    path(false, "dir", "dir2").getName(1).toString() == 'dir2'
  }

  def 'returns subpath'() {
    when:
    def abs = path(true, "dir", "dir2", 'dir3', 'dir4')
    def rel = path(false, "dir", "dir2", 'dir3', 'dir4')

    then:
    abs.subpath(1, 2).toString() == 'dir2'
    abs.subpath(1, 4).toString() == 'dir2\\dir3\\dir4'

    rel.subpath(1, 2).toString() == 'dir2'
    rel.subpath(1, 4).toString() == 'dir2\\dir3\\dir4'
  }

  private SmbFsPath path(boolean absolute, String... segments) {
    new SmbFsPath(fileSystem, absolute, segments)
  }
}
