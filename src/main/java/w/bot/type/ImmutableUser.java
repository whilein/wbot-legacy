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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * @author whilein
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImmutableUser implements User {

    int id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    String deactivated;

    @JsonProperty("is_closed")
    boolean closed;

    @JsonProperty("can_access_closed")
    boolean canAccessClosed;

    @Accessors(fluent = true)
    @JsonProperty("has_photo")
    boolean hasPhoto;

    @JsonProperty("photo_50")
    String photo50;

    @JsonProperty("photo_100")
    String photo100;

    @JsonProperty("photo_200_orig")
    String originalPhoto200;

    @JsonProperty("photo_200")
    String photo200;

    @JsonProperty("photo_max")
    String photoMax;

    @JsonProperty("photo_max_org")
    String originalPhotoMax;

    @JsonProperty("photo_400_orig")
    String originalPhoto400;


}
