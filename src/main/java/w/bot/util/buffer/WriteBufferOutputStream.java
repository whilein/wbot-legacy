/*
 *    Copyright 2022 Whilein
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package w.bot.util.buffer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class WriteBufferOutputStream extends OutputStream {

    WriteBuffer writeBuffer;

    public static @NotNull OutputStream wrap(final @NotNull WriteBuffer writeBuffer) {
        return new WriteBufferOutputStream(writeBuffer);
    }

    @Override
    public void write(final byte @NotNull [] b, final int off, final int len) {
        writeBuffer.write(b, off, len);
    }

    @Override
    public void write(final int b) {
        writeBuffer.write(b);
    }
}
