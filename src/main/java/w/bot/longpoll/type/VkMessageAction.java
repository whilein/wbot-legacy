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

package w.bot.longpoll.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
@JsonDeserialize(as = ImmutableVkMessageAction.class)
public interface VkMessageAction {

    /**
     * Тип действия. Возможные значения:
     *
     * <ul>
     * <li>chat_photo_update — обновлена фотография беседы;</li>
     * <li>chat_photo_remove — удалена фотография беседы;</li>
     * <li>chat_create — создана беседа;</li>
     * <li>chat_title_update — обновлено название беседы;</li>
     * <li>chat_invite_user — приглашен пользователь;</li>
     * <li>chat_kick_user — исключен пользователь;</li>
     * <li>chat_pin_message — закреплено сообщение;</li>
     * <li>chat_unpin_message — откреплено сообщение;</li>
     * <li>chat_invite_user_by_link — пользователь присоединился к беседе по ссылке.</li>
     * </ul>
     *
     * @return Тип действия
     */
    @NotNull String getType();

    /**
     * Идентификатор пользователя (если > 0) или email (если < 0), которого пригласили или исключили
     * (для служебных сообщений с type = chat_invite_user или chat_kick_user).
     * <p>
     * Идентификатор пользователя, который закрепил/открепил
     * сообщение для type = chat_pin_message или chat_unpin_message.
     *
     * @return Идентификатор пользователя
     */
    @Nullable Integer getMemberId();

    /**
     * Название беседы (для служебных сообщений с type = chat_create или chat_title_update).
     *
     * @return Название беседы
     */
    @Nullable String getText();

    /**
     * Email, который пригласили или исключили
     * (для служебных сообщений с type = chat_invite_user или chat_kick_user и отрицательным member_id).
     *
     * @return Email
     */
    @Nullable String getEmail();

    // todo photo

}
