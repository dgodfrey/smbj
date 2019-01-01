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
    new SmbFsPath(null, true).toString() == '\\'
    new SmbFsPath(null, true, 'file.txt').toString() == '\\file.txt'
    new SmbFsPath(null, true, 'dir').toString() == '\\dir'
    new SmbFsPath(null, true, 'dir', 'dir2').toString() == '\\dir\\dir2'

    new SmbFsPath(null, false).toString() == ''
    new SmbFsPath(null, false, 'file.txt').toString() == 'file.txt'
    new SmbFsPath(null, false, 'dir').toString() == 'dir'
    new SmbFsPath(null, false, 'dir', 'dir2').toString() == 'dir\\dir2'
  }

  def 'returns root'() {
    expect:
    new SmbFsPath(fileSystem, true, 'dir', 'dir2').root.toString() == '\\'

    new SmbFsPath(fileSystem, false, 'dir', 'dir2').root == null
  }
}
