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
import w.bot.command.CommandManager;
import w.bot.longpoll.VkBotLongPoll;
import w.bot.longpoll.event.VkEvent;
import w.bot.method.execute.VkExecute;
import w.bot.method.groups.VkGroupsGetLongPollServer;
import w.bot.method.messages.VkMessagesSend;
import w.bot.method.photos.VkPhotosGetMessagesUploadServer;
import w.bot.method.photos.VkPhotosSaveMessagesPhoto;
import w.bot.method.users.VkUsersGet;
import w.bot.photo.PhotoDownloader;
import w.bot.photo.PhotoUploader;
import w.bot.type.chat.BotChatManager;
import w.bot.type.user.BotUserManager;
import w.bot.type.user.name.UserNameCache;
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

    @NotNull PhotoUploader getMessagesPhotoUploader();

    @NotNull PhotoDownloader getPhotoDownloader();

    @NotNull UserNameCache getUserNameCache();

    @NotNull BotUserManager getUserManager();

    @NotNull BotChatManager getChatManager();

    @NotNull CommandManager getCommandManager();

    @NotNull String getToken();

    @NotNull String getVersion();

    @NotNull HttpClient getHttpClient();

    @NotNull VkBotLongPoll getLongPoll();

    <T> @NotNull VkExecute<T> execute(@NotNull Class<T> as);

    @NotNull VkMessagesSend messagesSend();

    @NotNull VkGroupsGetLongPollServer groupsGetLongPollServer();

    @NotNull VkPhotosGetMessagesUploadServer photosGetMessagesUploadServer();

    @NotNull VkPhotosSaveMessagesPhoto photosSaveMessagesPhoto();

    @NotNull VkUsersGet usersGet();

}
