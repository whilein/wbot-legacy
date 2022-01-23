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

package w.bot.method.users;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;
import w.bot.method.VkMethod;
import w.bot.type.User;
import w.bot.type.user.name.UserNameCase;

/**
 * @author whilein
 */
public interface VkUsersGet extends VkMethod<User[]> {

    static @NotNull VkUsersGet create(final @NotNull VkBot bot) {
        return new Stub(bot);
    }

    @NotNull VkUsersGet userIds(int @NotNull ... userIds);

    @NotNull VkUsersGet fields(@NotNull String @Nullable ... fields);

    @NotNull VkUsersGet nameCase(@Nullable UserNameCase nameCase);

    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    final class Stub
            extends AbstractVkMethod<User[]>
            implements VkUsersGet {

        @Accessors(fluent = true)
        int[] userIds;

        @Accessors(fluent = true)
        String[] fields;

        @Accessors(fluent = true)
        UserNameCase nameCase;


        private Stub(final VkBot vkBot) {
            super(vkBot, "users.get", User[].class);
        }

        @Override
        protected void initQuery(final @NotNull StringBuilder out) {
            super.initQuery(out);

            append(out, "user_ids", join(userIds));
            append(out, "fields", join(fields));
            append(out, "name_case", nameCase);
        }

    }


}
