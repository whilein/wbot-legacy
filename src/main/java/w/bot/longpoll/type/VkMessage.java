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

package w.bot.longpoll.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import w.bot.id.Id;

import java.util.List;

/**
 * @author whilein
 */
@JsonDeserialize(as = ImmutableVkMessage.class)
public interface VkMessage {

    @NotNull String getText();

    long getDate();

    @NotNull Id getFromId();

    @NotNull Id getPeerId();

    int getId();

    int getConversationMessageId();

    boolean isOut();

    @Nullable VkMessageAction getAction();

    @Nullable VkMessage getReplyMessage();

    @NotNull List<@NotNull VkMessage> getForwardMessages();

    // todo attachments

}
