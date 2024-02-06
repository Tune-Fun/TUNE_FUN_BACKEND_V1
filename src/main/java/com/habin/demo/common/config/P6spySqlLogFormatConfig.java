package com.habin.demo.common.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Slf4j
@Configuration
public class P6spySqlLogFormatConfig implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spySqlLogFormatConfig.class.getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));
    }

    private String formatSql(String category, String sql) {
        if (sql != null && !sql.trim().isEmpty() && Category.STATEMENT.getName().equals(category)) {
            sql = SQLPrefix.startsWith(sql) ?
                    FormatStyle.DDL.getFormatter().format(sql) : FormatStyle.BASIC.getFormatter().format(sql);
        }
        return sql;
    }

    @Getter
    @AllArgsConstructor
    private enum SQLPrefix {
        CREATE("create"), ALTER("alter"), COMMENT("comment");

        private final String prefix;

        static Boolean startsWith(String sql) {
            for (SQLPrefix prefix : SQLPrefix.values())
                if (sql.trim().toLowerCase(Locale.ROOT).startsWith(prefix.getPrefix())) return true;
            return false;
        }
    }


}
