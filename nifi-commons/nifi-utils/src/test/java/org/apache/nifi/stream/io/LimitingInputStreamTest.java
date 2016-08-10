/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.stream.io;

import java.io.EOFException;
import java.io.IOException;

import junit.framework.TestCase;

public class LimitingInputStreamTest extends TestCase {
    private final static byte[] TEST_BUFFER = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    public void testReadLimitNotReached() throws IOException {
        LimitingInputStream is = new LimitingInputStream(new ByteArrayInputStream(TEST_BUFFER), 50, false);
        long bytesRead = StreamUtils.copy(is, new ByteArrayOutputStream());
        assertEquals(bytesRead, TEST_BUFFER.length);

        is = new LimitingInputStream(new ByteArrayInputStream(TEST_BUFFER), 50, true);
        bytesRead = StreamUtils.copy(is, new ByteArrayOutputStream());
        assertEquals(bytesRead, TEST_BUFFER.length);
    }

    public void testReadLimitExceeded() throws IOException {
        final LimitingInputStream is = new LimitingInputStream(new ByteArrayInputStream(TEST_BUFFER), 9);
        final long bytesRead = StreamUtils.copy(is, new ByteArrayOutputStream());
        assertEquals(bytesRead, 9);
    }

    public void testReadLimitExceededEof() throws IOException {
        final LimitingInputStream is = new LimitingInputStream(new ByteArrayInputStream(TEST_BUFFER), 9, true);
        try {
            StreamUtils.copy(is, new ByteArrayOutputStream());
            fail("Should not get here");
        } catch (final EOFException eof) {
        }
    }
}
