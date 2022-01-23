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

import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.command.api.AbstractCommand;
import w.bot.type.MessageSource;
import w.bot.type.user.User;

/**
 * @author whilein
 */
public final class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("тест", "test");
    }

    @Override
    public void execute(
            final @NotNull VkBot bot,
            final @NotNull MessageSource source,
            final @NotNull User sender,
            final @NotNull String @NotNull [] args
    ) {
        bot.messagesSend()
                .peerId(source.getId())
                .message("Источник сообщения - " + source + "\nОтправитель - " + sender)
                .make().callAsync();
    }

}
