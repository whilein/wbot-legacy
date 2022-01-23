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

package w.bot.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import w.bot.VkBot;
import w.bot.longpoll.event.VkMessageEvent;
import w.bot.quote.QuoteCommand;
import w.bot.type.Message;
import w.bot.type.MessageSource;
import w.bot.type.user.BotUser;
import w.eventbus.Subscribe;
import w.util.Iterables;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimpleCommandManager implements CommandManager {

    VkBot vkBot;

    Map<String, Command> commandMap;

    private static final String[] EMPTY_ARGUMENTS = new String[0];

    public static @NotNull CommandManager create(final @NotNull VkBot bot) {
        val commandManager = new SimpleCommandManager(bot, new HashMap<>());
        bot.registerListener(commandManager);

        commandManager.register(new QuoteCommand());

        return commandManager;
    }

    @Override
    public void register(final @NotNull Command command) {
        commandMap.put(command.getName().toLowerCase(), command);

        for (val alias : command.getAliases()) {
            commandMap.put(alias.toLowerCase(), command);
        }
    }

    @Subscribe
    private void handleMessage(
            final VkMessageEvent event
    ) {
        val message = event.getText();

        val fromId = event.getFromId();
        val peerId = event.getPeerId();

        if (!message.startsWith(".") || !fromId.isUser()) {
            return;
        }

        val commandContents = message.substring(1).split(" ");
        val commandName = commandContents[0].toLowerCase();
        val command = commandMap.get(commandName);

        if (command == null) {
            return;
        }

        val source = peerId.isChat()
                ? vkBot.getChatManager().getChat(peerId.asInt())
                : vkBot.getUserManager().getUser(peerId.asInt());

        val sender = vkBot.getUserManager().getUser(fromId.asInt());

        command.execute(new CommandContextImpl(vkBot, sender, source, event.getMessage(), commandContents));
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class CommandContextImpl implements CommandContext {
        @Getter
        VkBot bot;

        @Getter
        BotUser sender;

        @Getter
        MessageSource source;

        @Getter
        Message message;

        String[] contents;

        @Override
        public @Nullable Message getForwardMessage() {
            return Optional.ofNullable(message.getReplyMessage())
                    .or(() -> Iterables.getFirst(message.getForwardMessages()))
                    .orElse(null);
        }

        @Override
        public int getArgumentCount() {
            return contents.length - 1;
        }

        @Override
        public @NotNull String getArgument(final int index) {
            return contents[index + 1];
        }

        @Override
        public void sendMessage(final @NotNull String text) {
            bot.messagesSend().message(text).peerId(source.getId()).make().callAsync();
        }
    }
}
