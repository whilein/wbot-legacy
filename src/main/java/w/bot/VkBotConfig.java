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

package w.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author whilein
 */
@Value
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class VkBotConfig {

    @JsonProperty(value = "long-poll", required = true)
    LongPoll longPoll;

    @JsonProperty(required = true)
    Auth auth;

    @JsonProperty(required = true)
    Http http;

    @Value
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @RequiredArgsConstructor
    public static class LongPoll {
        @JsonProperty(value = "group-id", required = true)
        int groupId;
    }

    @Value
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @RequiredArgsConstructor
    public static class Auth {
        @JsonProperty(required = true)
        String token;
    }

    @Value
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    @RequiredArgsConstructor
    public static class Http {
        @JsonProperty(required = true)
        int threads;


    }

}
