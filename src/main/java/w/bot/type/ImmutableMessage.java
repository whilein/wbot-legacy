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

package w.bot.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import w.bot.id.Id;

import java.util.List;

/**
 * @author whilein
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableMessage implements Message {

    int id;

    String text;

    long date;

    MessageAction action;

    @JsonProperty("conversation_message_id")
    int conversationMessageId;

    @JsonProperty("from_id")
    Id fromId;

    @JsonProperty("peer_id")
    Id peerId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @JsonProperty("out")
    boolean out;

    @JsonProperty("reply_message")
    Message replyMessage;

    @JsonProperty("fwd_messages")
    List<Message> forwardMessages;

}