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

package w.bot.method.groups;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;

/**
 * @author whilein
 */
@UtilityClass
public class VkGroups {

    public @NotNull VkGroupsGetLongPollServer getLongPollServer(final @NotNull VkBot bot) {
        return new VkGroupsGetLongPollServerImpl(bot);
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static final class VkGroupsGetLongPollServerImpl
            extends AbstractVkMethod<VkGroupsGetLongPollServer.Result>
            implements VkGroupsGetLongPollServer {

        @Accessors(fluent = true)
        @Setter
        int groupId;

        private VkGroupsGetLongPollServerImpl(final VkBot vkBot) {
            super(vkBot, "groups.getLongPollServer", Result.class);
        }

        @Override
        protected void initQuery(final @NotNull StringBuilder out) {
            super.initQuery(out);

            out.append("&group_id=").append(groupId);
        }

        @Value
        @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
        @RequiredArgsConstructor
        private static class Result implements VkGroupsGetLongPollServer.Result {

            String key;
            String server;
            String ts;

        }

    }

}
