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

import org.jetbrains.annotations.NotNull;
import w.bot.longpoll.VkBotLongPoll;
import w.bot.longpoll.event.VkEvent;
import w.bot.method.groups.VkGroupsGetLongPollServer;
import w.bot.method.messages.VkMessagesSend;
import w.config.ConfigProvider;

import java.net.http.HttpClient;

/**
 * @author whilein
 */
public interface VkBot {

    void registerListener(@NotNull Object object);

    void registerListener(@NotNull Class<?> type);

    void unregisterListener(@NotNull Object object);

    void unregisterListener(@NotNull Class<?> type);

    void dispatch(@NotNull VkEvent event);

    @NotNull ConfigProvider getConfigProvider();

    @NotNull String getToken();

    @NotNull String getVersion();

    @NotNull HttpClient getHttpClient();

    @NotNull VkBotLongPoll getLongPoll();

    @NotNull VkMessagesSend messagesSend();

    @NotNull VkGroupsGetLongPollServer groupsGetLongPollServer();

}
