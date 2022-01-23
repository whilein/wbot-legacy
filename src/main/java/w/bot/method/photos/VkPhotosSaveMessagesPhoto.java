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

package w.bot.method.photos;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;
import w.bot.method.VkMethod;
import w.bot.type.Photo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author whilein
 */
public interface VkPhotosSaveMessagesPhoto extends VkMethod<Photo[]> {

    static @NotNull VkPhotosSaveMessagesPhoto create(final @NotNull VkBot bot) {
        return new Stub(bot);
    }

    @NotNull VkPhotosSaveMessagesPhoto hash(@NotNull String hash);

    @NotNull VkPhotosSaveMessagesPhoto photo(@NotNull String photo);

    @NotNull VkPhotosSaveMessagesPhoto server(int server);

    final class Stub extends AbstractVkMethod<Photo[]> implements VkPhotosSaveMessagesPhoto {

        @Accessors(fluent = true)
        @Setter
        String hash;

        @Accessors(fluent = true)
        @Setter
        String photo;

        @Accessors(fluent = true)
        @Setter
        int server;

        private Stub(final VkBot vkBot) {
            super(vkBot, "photos.saveMessagesPhoto", Photo[].class);
        }

        @Override
        protected void initQuery(final @NotNull StringBuilder out) {
            super.initQuery(out);

            out
                    .append("&hash=").append(hash)
                    .append("&photo=").append(URLEncoder.encode(photo, StandardCharsets.UTF_8))
                    .append("&server=").append(server);
        }
    }

}
