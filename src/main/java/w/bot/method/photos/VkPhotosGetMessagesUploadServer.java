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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import w.bot.VkBot;
import w.bot.method.AbstractVkMethod;
import w.bot.method.VkMethod;

/**
 * @author whilein
 */
public interface VkPhotosGetMessagesUploadServer extends VkMethod<VkPhotosGetMessagesUploadServer.Result> {

    static @NotNull VkPhotosGetMessagesUploadServer create(final @NotNull VkBot bot) {
        return new Stub(bot);
    }

    interface Result {

        @NotNull String getUploadUrl();

        int getAlbumId();

        @Value
        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
        class Stub implements Result {

            @JsonProperty("upload_url")
            String uploadUrl;

            @JsonProperty("album_id")
            int albumId;

        }

    }

    final class Stub extends AbstractVkMethod<Result> implements VkPhotosGetMessagesUploadServer {

        private Stub(final VkBot vkBot) {
            super(vkBot, "photos.getMessagesUploadServer", Result.Stub.class);
        }

    }

}
