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

package w.bot.longpoll;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.VkBotConfig;
import w.bot.longpoll.event.VkChatInviteUserEvent;
import w.bot.longpoll.event.VkChatKickUserEvent;
import w.bot.longpoll.event.VkMessageEvent;
import w.bot.longpoll.type.VkMessage;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author whilein
 */
@Getter
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleVkBotLongPoll implements VkBotLongPoll {

    private static final String URL = "%s?act=a_check&key=%s&ts=%s&wait=90";

    VkBot vkBot;

    int groupId;

    @NonFinal
    URI uri;

    @NonFinal
    String server, key, ts;

    public static @NotNull VkBotLongPoll create(final @NotNull VkBot bot,
                                                final @NotNull VkBotConfig.LongPoll config) {
        return new SimpleVkBotLongPoll(bot, config.getGroupId());
    }

    private void _updateServer() {
        val result = vkBot.groupsGetLongPollServer()
                .groupId(groupId)
                .execute()
                .join();

        server = result.getServer();
        key = result.getKey();
        ts = result.getTs();

        _updateUri();
    }

    private void _updateUri() {
        this.uri = URI.create(URL.formatted(
                server,
                key,
                ts
        ));
    }

    @Override
    public void start() {
        _updateServer();

        while (true) {
            try {
                val request = HttpRequest.newBuilder()
                        .uri(uri)
                        .build();

                val response = vkBot.getHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofByteArray());

                val json = vkBot.getConfigProvider().parse(response.body());
                val error = json.getInt("failed", 0);

                switch (error) {
                    case 0 -> {
                        ts = json.getString("ts");
                        _updateUri();

                        for (val update : json.getObjectList("updates")) {
                            val type = update.getString("type", "");

                            switch (type) {
                                case "message_new" -> {
                                    val message = update.findObject("object")
                                            .map(object -> object.getAs("message", VkMessage.class))
                                            .orElseThrow();
                                    val action = message.getAction();
                                    if (action != null) {
                                        switch (action.getType()) {
                                            case "chat_invite_user" -> {
                                                assert action.getMemberId() != null;
                                                vkBot.dispatch(new VkChatInviteUserEvent(vkBot, message,
                                                        action.getMemberId(), action.getEmail()));
                                            }
                                            case "chat_kick_user" -> {
                                                assert action.getMemberId() != null;
                                                vkBot.dispatch(new VkChatKickUserEvent(vkBot, message,
                                                        action.getMemberId(), action.getEmail()));
                                            }
                                        }
                                    } else {
                                        vkBot.dispatch(new VkMessageEvent(vkBot, message));
                                    }
                                }

                                // todo make another events
                            }
                        }
                    }
                    case 1 -> {
                        ts = String.valueOf(json.getInt("ts"));
                        log.debug("[LP] Получен failed 1: ts был обновлён на {}", ts);
                        _updateUri();
                    }
                    case 2, 3 -> {
                        log.debug("[LP] Получен failed {}: запрашиваем новый сервер и ключ", error);
                        _updateServer();
                    }
                }

            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
