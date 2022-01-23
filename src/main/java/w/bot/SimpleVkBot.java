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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.command.CommandManager;
import w.bot.command.SimpleCommandManager;
import w.bot.id.Id;
import w.bot.id.ImmutableId;
import w.bot.longpoll.SimpleVkBotLongPoll;
import w.bot.longpoll.VkBotLongPoll;
import w.bot.longpoll.event.VkEvent;
import w.bot.method.execute.VkExecute;
import w.bot.method.groups.VkGroupsGetLongPollServer;
import w.bot.method.messages.VkMessagesSend;
import w.bot.method.photos.VkPhotosGetMessagesUploadServer;
import w.bot.method.photos.VkPhotosSaveMessagesPhoto;
import w.bot.method.users.VkUsersGet;
import w.bot.photo.MessagesPhotoUploader;
import w.bot.photo.PhotoDownloader;
import w.bot.photo.PhotoUploader;
import w.bot.photo.SimplePhotoDownloader;
import w.bot.type.chat.BotChatManager;
import w.bot.type.chat.SimpleBotChatManager;
import w.bot.type.user.BotUserManager;
import w.bot.type.user.SimpleBotUserManager;
import w.bot.type.user.name.SimpleUserNameCache;
import w.bot.type.user.name.UserNameCache;
import w.config.ConfigProvider;
import w.config.CustomJacksonConfigProvider;
import w.eventbus.EventBus;
import w.eventbus.SimpleEventBus;
import w.eventbus.SubscribeNamespace;

import java.io.IOException;
import java.net.http.HttpClient;

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
    PhotoUploader messagesPhotoUploader;

    @Getter
    @NonFinal
    PhotoDownloader photoDownloader;

    @Getter
    @NonFinal
    UserNameCache userNameCache;

    @Getter
    @NonFinal
    VkBotLongPoll longPoll;

    @Getter
    @NonFinal
    BotUserManager userManager;

    @Getter
    @NonFinal
    BotChatManager chatManager;

    @Getter
    @NonFinal
    CommandManager commandManager;

    public static @NotNull VkBot create(final @NotNull VkBotConfig config) {
        val objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        val module = new SimpleModule();

        module.addDeserializer(Id.class, new JsonDeserializer<>() {
            @Override
            public Id deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
                return ImmutableId.create(parser.getValueAsInt());
            }
        });

        objectMapper.registerModule(module);

        val vkBot = new SimpleVkBot(
                config.getAuth().getToken(),
                CustomJacksonConfigProvider.create(objectMapper),
                HttpClient.newBuilder()
                        .build(),
                SimpleEventBus.create()
        );

        vkBot.messagesPhotoUploader = MessagesPhotoUploader.create(vkBot);
        vkBot.photoDownloader = SimplePhotoDownloader.create(vkBot.httpClient);

        vkBot.userNameCache = SimpleUserNameCache.create(vkBot);
        vkBot.userManager = SimpleBotUserManager.create(vkBot.userNameCache);

        vkBot.chatManager = SimpleBotChatManager.create();

        vkBot.commandManager = SimpleCommandManager.create(vkBot);

        vkBot.longPoll = SimpleVkBotLongPoll.create(vkBot, config.getLongPoll());

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
    public <T> @NotNull VkExecute<T> execute(final @NotNull Class<T> as) {
        return VkExecute.create(this, as);
    }

    @Override
    public @NotNull VkMessagesSend messagesSend() {
        return VkMessagesSend.create(this);
    }

    @Override
    public @NotNull VkGroupsGetLongPollServer groupsGetLongPollServer() {
        return VkGroupsGetLongPollServer.create(this);
    }

    @Override
    public @NotNull VkPhotosGetMessagesUploadServer photosGetMessagesUploadServer() {
        return VkPhotosGetMessagesUploadServer.create(this);
    }

    @Override
    public @NotNull VkPhotosSaveMessagesPhoto photosSaveMessagesPhoto() {
        return VkPhotosSaveMessagesPhoto.create(this);
    }

    @Override
    public @NotNull VkUsersGet usersGet() {
        return VkUsersGet.create(this);
    }

}
