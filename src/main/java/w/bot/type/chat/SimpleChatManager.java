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

package w.bot.type.chat;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import w.bot.id.Id;
import w.bot.id.ImmutableId;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleChatManager implements ChatManager {

    Int2ObjectMap<Chat> knownChats;

    @Override
    public @NotNull Chat getChat(final int chatId) {
        return knownChats.computeIfAbsent(chatId, __ -> new ChatImpl(ImmutableId.create(chatId)));
    }

    public static @NotNull ChatManager create() {
        return new SimpleChatManager(new Int2ObjectOpenHashMap<>());
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ChatImpl implements Chat {
        Id id;

        @Override
        public String toString() {
            return "Chat[id=" + id + "]";
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof Chat chat && chat.getId().equals(id);
        }

        @Override
        public boolean isUser() {
            return false;
        }

        @Override
        public boolean isChat() {
            return true;
        }
    }

}
