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

package w.bot.method;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import w.bot.VkBot;
import w.config.ConfigObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractVkMethod<R> implements VkMethod<R> {

    private static final String API_URL = "https://api.vk.com/method/";

    VkBot vkBot;

    String name;
    Class<? extends R> type;

    protected void initQuery(final @NotNull StringBuilder out) {
        out.append("access_token=").append(vkBot.getToken()).append("&v=").append(vkBot.getVersion());
    }

    protected @Nullable String join(final int @Nullable [] integers) {
        return integers != null
                ? IntStream.of(integers).mapToObj(Integer::toString).collect(Collectors.joining(","))
                : null;
    }

    protected void appendEncoded(
            final @NotNull StringBuilder out,
            final @NotNull String fieldName,
            final @Nullable Object value
    ) {
        if (value == null) {
            return;
        }

        out.append('&').append(fieldName).append('=').append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
    }

    protected void append(
            final @NotNull StringBuilder out,
            final @NotNull String fieldName,
            final @Nullable Object value
    ) {
        if (value == null) {
            return;
        }

        out.append('&').append(fieldName).append('=').append(value);
    }

    private R processResponse(final ConfigObject resp) {
        return resp.findAs("response", type)
                .orElseThrow(() -> constructError(resp.getObject("error")));
    }

    private RuntimeException constructError(final @Nullable ConfigObject error) {
        return error != null
                ? new RuntimeException("[" + error.getInt("error_code") + "] " + error.getString("error_msg"))
                : new RuntimeException();
    }

    @Override
    public @NotNull CompletableFuture<R> execute() {
        val query = new StringBuilder();
        initQuery(query);

        val request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(query.toString()))
                .uri(URI.create(API_URL + name))
                .build();

        val configProvider = vkBot.getConfigProvider();

        return vkBot.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenApply(configProvider::parse)
                .thenApply(this::processResponse);
    }
}
