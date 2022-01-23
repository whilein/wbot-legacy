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

package w.bot.method.execute;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;
import w.bot.method.VkMethod;

/**
 * @author whilein
 */
public interface VkExecute<T> extends VkMethod<T> {

    static <T> @NotNull VkExecute<T> create(final @NotNull VkBot bot, final @NotNull Class<T> as) {
        return new Stub<>(bot, as);
    }

    @NotNull VkExecute<T> code(@NotNull String code);

    @FieldDefaults(level = AccessLevel.PRIVATE)
    final class Stub<T>
            extends AbstractVkMethod<T>
            implements VkExecute<T> {

        @Accessors(fluent = true)
        @Setter
        String code;

        private Stub(final VkBot vkBot, final Class<T> as) {
            super(vkBot, "execute", as);
        }

        @Override
        protected void initQuery(final @NotNull StringBuilder out) {
            super.initQuery(out);

            appendEncoded(out, "code", code);
        }
    }

}
