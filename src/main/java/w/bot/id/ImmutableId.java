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

package w.bot.id;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImmutableId implements Id {

    int id;

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj instanceof Id id && id.asInt() == this.id;
    }

    public static @NotNull Id create(final int id) {
        return new ImmutableId(id);
    }

    @Override
    public int asInt() {
        return id;
    }

    @Override
    public boolean isUser() {
        return !isChat() && !isGroup();
    }

    @Override
    public boolean isChat() {
        return id > CHAT_OFFSET;
    }

    @Override
    public boolean isGroup() {
        return id < 0;
    }
}
