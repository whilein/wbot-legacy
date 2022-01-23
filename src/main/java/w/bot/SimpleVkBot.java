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

package w.bot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.longpoll.VkBotLongPoll;
import w.bot.longpoll.event.VkEvent;
import w.bot.method.groups.VkGroups;
import w.bot.method.groups.VkGroupsGetLongPollServer;
import w.bot.method.messages.VkMessages;
import w.bot.method.messages.VkMessagesSend;
import w.config.ConfigProvider;
import w.config.CustomJacksonConfigProvider;
import w.eventbus.EventBus;
import w.eventbus.SimpleEventBus;
import w.eventbus.SubscribeNamespace;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleVkBot implements VkBot, SubscribeNamespace {

    @Getter
    String token;

    @Getter
    ConfigProvider configProvider;

    @Getter
    HttpClient httpClient;

    EventBus<SimpleVkBot> bus;

    @Getter
    @NonFinal
    VkBotLongPoll longPoll;

    public static @NotNull VkBot create(final @NotNull VkBotConfig config) {
        val http = config.getHttp();

        val executor = http.getThreads() > 0
                ? Executors.newFixedThreadPool(http.getThreads())
                : Executors.newCachedThreadPool();

        val objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        val vkBot = new SimpleVkBot(
                config.getAuth().getToken(),
                CustomJacksonConfigProvider.create(objectMapper),
                HttpClient.newBuilder()
                        .executor(executor)
                        .build(),
                SimpleEventBus.create()
        );

        vkBot.longPoll = SimpleVkBotLongPoll.create(vkBot, config.getLongPoll()); // todo

        return vkBot;
    }

    @Override
    public void registerListener(final @NotNull Object object) {
        bus.register(this, object);
    }

    @Override
    public void registerListener(final @NotNull Class<?> type) {
        bus.register(this, type);
    }

    @Override
    public void unregisterListener(final @NotNull Object object) {
        bus.unregisterAll(object);
    }

    @Override
    public void unregisterListener(final @NotNull Class<?> type) {
        bus.unregisterAll(type);
    }

    @Override
    public void dispatch(final @NotNull VkEvent event) {
        bus.dispatch(event);
    }

    @Override
    public @NotNull String getVersion() {
        return "5.131";
    }

    @Override
    public @NotNull VkMessagesSend messagesSend() {
        return VkMessages.send(this);
    }

    @Override
    public @NotNull VkGroupsGetLongPollServer groupsGetLongPollServer() {
        return VkGroups.getLongPollServer(this);
    }
}
