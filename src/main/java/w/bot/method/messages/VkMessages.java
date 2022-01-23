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

package w.bot.method.messages;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;
import w.util.RandomUtils;

/**
 * @author whilein
 */
@UtilityClass
public class VkMessages {

    public @NotNull VkMessagesSend send(final @NotNull VkBot bot) {
        return new VkMessagesSendImpl(bot);
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static final class VkMessagesSendImpl
            extends AbstractVkMethod<Integer>
            implements VkMessagesSend {

        @Accessors(fluent = true)
        @Setter
        Integer peerId;

        @Accessors(fluent = true)
        @Setter
        String message;

        @Accessors(fluent = true)
        @Setter
        int[] forwardMessages;

        @Accessors(fluent = true)
        @Setter
        Integer replyTo;

        private VkMessagesSendImpl(final VkBot vkBot) {
            super(vkBot, "messages.send", Integer.class);
        }

        @Override
        protected void initQuery(final @NotNull StringBuilder out) {
            super.initQuery(out);

            out.append("&random_id=").append(RandomUtils.getInt());

            append(out, "peer_id", peerId);
            appendEncoded(out, "message", message);

            append(out, "forward_messages", join(forwardMessages));
            append(out, "reply_to", replyTo);
        }
    }

}
