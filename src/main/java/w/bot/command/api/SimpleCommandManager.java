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

package w.bot.command.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.command.TestCommand;
import w.bot.longpoll.event.VkMessageEvent;
import w.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        commandManager.register(new TestCommand());

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

        val commandArguments = commandContents.length > 1
                ? Arrays.copyOfRange(commandContents, 1, commandContents.length)
                : EMPTY_ARGUMENTS;

        val source = peerId.isChat()
                ? vkBot.getChatManager().getChat(peerId.asInt())
                : vkBot.getUserManager().getUser(peerId.asInt());

        val sender = vkBot.getUserManager().getUser(fromId.asInt());

        command.execute(vkBot, source, sender, commandArguments);
    }
}
