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

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class SmbSpecification extends Specification {
  @Shared
  GenericContainer c = new GenericContainer("smbj/smbj-itest:latest")
    .waitingFor(Wait.forLogMessage('.*nmbd entered RUNNING state.*', 1))
    .withExposedPorts(445);
}
