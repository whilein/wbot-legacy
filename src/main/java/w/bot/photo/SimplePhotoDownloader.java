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

package w.bot.photo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import w.flow.Flow;
import w.flow.Flows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimplePhotoDownloader implements PhotoDownloader {

    HttpClient client;

    public static @NotNull PhotoDownloader create(final @NotNull HttpClient client) {
        return new SimplePhotoDownloader(client);
    }

    @Override
    public @NotNull Flow<@NotNull BufferedImage> download(final @NotNull String url) {
        val request = HttpRequest.newBuilder(URI.create(url))
                .build();

        return Flows.ofSupplier(() -> client.send(request, HttpResponse.BodyHandlers.ofInputStream()))
                .map(response -> {
                    try (val inputStream = response.body()) {
                        return ImageIO.read(inputStream);
                    }
                });
    }
}
