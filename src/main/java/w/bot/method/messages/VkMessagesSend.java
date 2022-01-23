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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import w.bot.VkBot;
import w.bot.id.Id;
import w.bot.id.ImmutableId;
import w.bot.method.AbstractVkMethod;
import w.bot.method.VkMethod;
import w.util.RandomUtils;

/**
 * @author whilein
 */
public interface VkMessagesSend extends VkMethod<Integer> {

    static @NotNull VkMessagesSend create(final @NotNull VkBot bot) {
        return new Stub(bot);
    }

    @NotNull VkMessagesSend peerId(@Nullable Integer peerId);

    @NotNull VkMessagesSend peerId(@Nullable Id peerId);

    @NotNull VkMessagesSend message(@Nullable String message);

    @NotNull VkMessagesSend forwardMessages(int @Nullable ... messageIds);

    @NotNull VkMessagesSend replyTo(@Nullable Integer messageId);

    @NotNull VkMessagesSend attachment(@Nullable String attachment);

    @FieldDefaults(level = AccessLevel.PRIVATE)
    final class Stub
            extends AbstractVkMethod<Integer>
            implements VkMessagesSend {

        Id peerId;

        @Accessors(fluent = true)
        @Setter
        String message;

        @Accessors(fluent = true)
        @Setter
        int[] forwardMessages;

        @Accessors(fluent = true)
        @Setter
        Integer replyTo;

        @Accessors(fluent = true)
        @Setter
        String attachment;

        private Stub(final VkBot vkBot) {
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

            append(out, "attachment", attachment);
        }

        @Override
        public @NotNull VkMessagesSend peerId(final @Nullable Integer peerId) {
            this.peerId = peerId == null ? null : ImmutableId.create(peerId);
            return this;
        }

        @Override
        public @NotNull VkMessagesSend peerId(final @Nullable Id peerId) {
            this.peerId = peerId;
            return this;
        }
    }


}
