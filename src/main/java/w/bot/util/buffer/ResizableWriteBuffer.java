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
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ResizableWriteBuffer extends AbstractWriteBuffer {

    private ResizableWriteBuffer(final byte[] array) {
        super(array);
    }

    public static @NotNull WriteBuffer create(final int length) {
        return new ResizableWriteBuffer(new byte[length]);
    }

    @Override
    protected void ensure(final int count) {
        if (position + count > array.length) {
            array = Arrays.copyOf(array, Math.max(position + count, array.length * 2));
        }
    }

    @Override
    public @NotNull WriteBuffer length(final int length) {
        position = length;

        return this;
    }

    @Override
    public int getSize() {
        return position;
    }

    @Override
    public String toString() {
        return new String(array, 0, position);
    }

    @Override
    public byte @NotNull [] toByteArray() {
        return Arrays.copyOf(array, position);
    }
}
