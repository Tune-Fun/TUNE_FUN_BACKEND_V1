package com.tune_fun.v1.base.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.net.URI;

@TestConfiguration
@Profile("test")
@RequiredArgsConstructor
public class JiraConfig {

    @Value("${jira.uri}")
    private String jiraUri;

    @Value("${jira.username}")
    private String jiraUsername;

    @Value("${jira.password}")
    private String jiraPassword;

    @Bean
    public JiraRestClient jiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(URI.create(jiraUri), jiraUsername, jiraPassword);
    }

}
