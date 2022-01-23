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

package w.bot.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author whilein
 */
@JsonDeserialize(as = ImmutableUser.class)
public interface User {

    int getId();

    @NotNull String getFirstName();

    @NotNull String getLastName();

    @Nullable String getDeactivated();

    boolean isClosed();

    boolean isCanAccessClosed();

    boolean hasPhoto();

    @Nullable String getPhoto50();

    @Nullable String getPhoto100();

    @Nullable String getPhoto200();

    @Nullable String getOriginalPhoto200();

    @Nullable String getPhotoMax();

    @Nullable String getOriginalPhotoMax();

    @Nullable String getOriginalPhoto400();
}
