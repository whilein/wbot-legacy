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
import w.bot.VkBot;
import w.bot.type.Photo;
import w.bot.util.buffer.ResizableWriteBuffer;
import w.bot.util.buffer.WriteBuffer;
import w.bot.util.buffer.WriteBufferOutputStream;
import w.flow.Flow;
import w.util.randomstring.RandomStringGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author whilein
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagesPhotoUploader implements PhotoUploader {

    private static final ThreadLocal<WriteBuffer> WRITE_BUFFER = ThreadLocal.withInitial(
            () -> ResizableWriteBuffer.create(8192));

    private static final RandomStringGenerator BOUNDARY_GENERATOR = RandomStringGenerator.builder()
            .addLetters()
            .addNumbers()
            .build();


    VkBot bot;

    public static @NotNull PhotoUploader create(final VkBot bot) {
        return new MessagesPhotoUploader(bot);
    }

    private String _uploadPhoto(final String uploadUrl, final BufferedImage image)
            throws IOException, InterruptedException {
        val boundary = BOUNDARY_GENERATOR.nextString(128);

        val buffer = WRITE_BUFFER.get();
        buffer.length(0);

        buffer.write("--").write(boundary).write("""
                \r
                Content-Disposition: form-data; name="file"; filename="image.png"\r
                Content-Type: image/png\r
                \r
                """);

        ImageIO.write(image, "png", WriteBufferOutputStream.wrap(buffer));

        buffer.write("\r\n--").write(boundary).write("--");

        val request = HttpRequest.newBuilder(URI.create(uploadUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(buffer.toByteArray()))
                .build();

        return bot.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    @Override
    public @NotNull Flow<@NotNull Photo> uploadPhoto(final @NotNull BufferedImage image) {
        return bot.photosGetMessagesUploadServer().make()
                .map(result -> _uploadPhoto(result.getUploadUrl(), image))
                .map(bot.getConfigProvider()::parse)
                .compose(result -> bot.photosSaveMessagesPhoto()
                        .server(result.getInt("server"))
                        .hash(result.getString("hash", ""))
                        .photo(result.getString("photo", ""))
                        .make())
                .map(result -> result[0]);
    }
}
