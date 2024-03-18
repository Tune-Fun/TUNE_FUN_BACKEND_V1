package com.tune_fun.v1.external.firebase;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class FirebaseMto {

    @Getter
    @NoArgsConstructor
    public static class Base {
        private String title;
        private String body;
        private Map<String, String> data;

        public Base(String title, String body, Map<String, String> data) {
            this.title = title;
            this.body = body;
            this.data = data;
            if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be null or empty");
            if (body == null || body.isBlank()) throw new IllegalArgumentException("Body cannot be null or empty");
        }
    }

    @Getter
    public final static class ByTopic extends Base {
        private final String topic;

        public ByTopic(String topic, String title, String body, Map<String, String> data) {
            super(title, body, data);
            if (topic == null || topic.isBlank()) throw new IllegalArgumentException("Topic cannot be null or empty");
            this.topic = topic;
        }
    }

    @Getter
    public final static class ByToken extends Base {
        private final String token;

        public ByToken(String token, String title, String body, Map<String, String> data) {
            super(title, body, data);
            if (token == null || token.isBlank()) throw new IllegalArgumentException("Token cannot be null or empty");
            this.token = token;
        }
    }

    @Getter
    public final static class ByTokens extends Base {
        private final List<String> tokens;

        public ByTokens(List<String> tokens, String title, String body, Map<String, String> data) {
            super(title, body, data);
            if (tokens == null || tokens.isEmpty())
                throw new IllegalArgumentException("Tokens cannot be null or empty");
            if (tokens.size() > 500) throw new IllegalArgumentException("Tokens cannot exceed 500");
            this.tokens = tokens;
        }
    }

}
