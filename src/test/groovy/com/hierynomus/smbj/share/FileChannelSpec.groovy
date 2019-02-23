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
package com.hierynomus.smbj.share

import java.nio.ByteBuffer
import spock.lang.Specification

abstract class FileChannelSpec extends Specification {
  static class WithOpenReadableChannel extends FileChannelSpec {

    def file = Stub(File);
    def channel = FileChannel.newFileChannel(file);

    def "is open"() {
      expect:
      channel.isOpen()

    }

    def "can close"() {
      when:
      channel.close()

      then:
      !channel.isOpen()
    }

    def "reads into bytebuffer"() {
      given:
      channel.position(55);
      def bb = ByteBuffer.allocate(100);
      bb.position(3);
      file.read(_, 55, 3, 97) >> { a ->
        a[3] = 1; a[4] = 2; a[5] = 3;
        return 3;
      }

      when:
      def read = channel.read(bb);

      then:
      read == 3;
      bb.position() == 6
      channel.position() == 58
    }
  }
}
