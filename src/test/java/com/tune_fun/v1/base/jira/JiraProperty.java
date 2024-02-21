package com.tune_fun.v1.base.jira;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationProperties("jira")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JiraProperty {

    private String uri;
    private String username;
    private String password;

    public URI getJiraUri() {
        return URI.create(uri);
    }

}
