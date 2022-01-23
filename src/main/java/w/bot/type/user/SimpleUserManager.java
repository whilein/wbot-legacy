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

package w.bot.type.user;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import w.bot.id.Id;
import w.bot.id.ImmutableId;
import w.bot.type.user.name.UserName;
import w.bot.type.user.name.UserNameCache;

import java.util.concurrent.CompletableFuture;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleUserManager implements UserManager {

    UserNameCache userNameCache;
    Int2ObjectMap<User> knownUsers;

    @Override
    public @NotNull User getUser(final int userId) {
        return knownUsers.computeIfAbsent(userId, __ -> new UserImpl(userNameCache, ImmutableId.create(userId)));
    }

    public static @NotNull UserManager create(final @NotNull UserNameCache userNameCache) {
        return new SimpleUserManager(userNameCache, new Int2ObjectOpenHashMap<>());
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class UserImpl implements User {

        UserNameCache userNameCache;

        Id id;

        @Override
        public String toString() {
            return "User[id=" + id + "]";
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || obj instanceof User user && user.getId().equals(id);
        }

        @Override
        public boolean isUser() {
            return true;
        }

        @Override
        public boolean isChat() {
            return false;
        }

        @Override
        public @NotNull CompletableFuture<@NotNull UserName> getName() {
            return null;
        }
    }

}
