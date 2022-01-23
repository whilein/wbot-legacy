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

package w.bot.type.user.name;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleUserNameCache implements UserNameCache {

    private static final String GET_ALL = """
            var a=[],b=[],c=0,d=["nom","gen","dat","acc","ins","abl"];
            while(c!=6) {
            var e=API.users.get({"user_id":%s,"name_case":d[c]})[0];
            a.push(e.first_name);
            b.push(e.last_name);
            c=c+1;
            }
            return [a,b];
            """.replaceAll("[\r\n]", "").trim();

    AsyncLoadingCache<Integer, UserName> asyncCache;

    public static @NotNull UserNameCache create(final @NotNull VkBot bot) {
        return new SimpleUserNameCache(Caffeine.newBuilder()
                .expireAfterWrite(8, TimeUnit.HOURS)
                .buildAsync(userId -> loadUserName(bot, userId)));
    }

    private static UserName loadUserName(final VkBot bot, final int userId) {
        val result = bot.execute(String[][].class)
                .code(GET_ALL.formatted(userId))
                .make().call();

        return new UserNameImpl(result[0], result[1]);
    }

    @Override
    public @NotNull CompletableFuture<@NotNull UserName> getName(final int userId) {
        return asyncCache.get(userId);
    }

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class UserNameImpl implements UserName {

        String[] first;
        String[] last;

        @Override
        public @NotNull String getFull() {
            return getFull(UserNameCase.NOM);
        }

        @Override
        public @NotNull String getFull(final @NotNull UserNameCase nameCase) {
            return getFirst(nameCase) + " " + getLast(nameCase);
        }

        @Override
        public @NotNull String getFirst() {
            return getFirst(UserNameCase.NOM);
        }

        @Override
        public @NotNull String getFirst(final @NotNull UserNameCase nameCase) {
            return first[nameCase.ordinal()];
        }

        @Override
        public @NotNull String getLast() {
            return getLast(UserNameCase.NOM);
        }

        @Override
        public @NotNull String getLast(final @NotNull UserNameCase nameCase) {
            return last[nameCase.ordinal()];
        }
    }
}
