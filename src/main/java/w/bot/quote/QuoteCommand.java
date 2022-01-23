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

package w.bot.quote;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.bot.command.AbstractCommand;
import w.bot.command.CommandContext;

import java.util.Optional;

/**
 * @author whilein
 */
public final class QuoteCommand extends AbstractCommand {

    public QuoteCommand() {
        super("цитата", "quote");
    }

    @Override
    public void execute(
            final @NotNull CommandContext ctx
    ) {
        val quoteOf = ctx.getForwardMessage();

        if (quoteOf == null || quoteOf.getText().isEmpty() || !quoteOf.getFromId().isUser()) {
            ctx.sendMessage("❌ Вы должны переслать текстовое сообщение пользователя, чтобы сделать цитату");
            return;
        }

        val bot = ctx.getBot();

        bot.usersGet().userIds(quoteOf.getFromId().asInt()).fields("photo_max").make()
                .map(users -> users[0])
                .map(user -> {
                    val quoteGenerator = SimpleQuoteGenerator.create()
                            .text(quoteOf.getText())
                            .profileFirstName(user.getFirstName())
                            .profileLastName(user.getLastName())
                            .title("Цитаты ноунеймов");

                    Optional.ofNullable(user.getPhotoMax())
                            .map(photo -> bot.getPhotoDownloader().download(photo).call())
                            .ifPresent(quoteGenerator::profileImage);

                    return quoteGenerator.generate();
                })
                .compose(quote -> bot.getMessagesPhotoUploader().uploadPhoto(quote)
                        .compose(photo -> bot.messagesSend()
                                .attachment("photo" + photo.getOwnerId() + "_" + photo.getId())
                                .peerId(ctx.getSource().getId())
                                .make()))
                .callAsync();
    }
}
