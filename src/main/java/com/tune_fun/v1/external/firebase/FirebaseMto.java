package com.tune_fun.v1.external.firebase;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FirebaseMto {

    @Getter
    @NoArgsConstructor
    public abstract static class Base {
        private String title;
        private String body;

        public Base(String title, String body) {
            this.title = title;
            this.body = body;
            if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be null or empty");
            if (body == null || body.isBlank()) throw new IllegalArgumentException("Body cannot be null or empty");
        }
    }

    @Getter
    public final static class ByTopic extends Base {
        private final String topic;

        public ByTopic(String topic, String title, String body) {
            super(title, body);
            if (topic == null || topic.isBlank()) throw new IllegalArgumentException("Topic cannot be null or empty");
            this.topic = topic;
        }
    }

    @Getter
    public final static class ByToken extends Base {
        private final String token;

        public ByToken(String token, String title, String body) {
            super(title, body);
            if (token == null || token.isBlank()) throw new IllegalArgumentException("Token cannot be null or empty");
            this.token = token;
        }
    }

    @Getter
    public final static class ByTokens extends Base {
        private final List<String> tokens;

        public ByTokens(List<String> tokens, String title, String body) {
            super(title, body);
            if (tokens == null || tokens.isEmpty())
                throw new IllegalArgumentException("Tokens cannot be null or empty");
            if (tokens.size() > 500) throw new IllegalArgumentException("Tokens cannot exceed 500");
            this.tokens = tokens;
        }
    }

}
