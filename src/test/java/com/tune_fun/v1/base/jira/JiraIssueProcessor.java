package com.tune_fun.v1.base.jira;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import lombok.RequiredArgsConstructor;
import org.junitpioneer.jupiter.IssueProcessor;
import org.junitpioneer.jupiter.IssueTestCase;
import org.junitpioneer.jupiter.IssueTestSuite;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JiraIssueProcessor implements IssueProcessor {

    private final JiraRestClient jiraRestClient;

    @Override
    public void processTestResults(List<IssueTestSuite> issueTestSuites) {
        IssueTestSuite issueTestSuite = issueTestSuites.get(0);
        issueTestSuite.issueId();

        List<IssueTestCase> tests = issueTestSuite.tests();
        IssueTestCase issueTestCase = tests.get(0);
        issueTestCase.testId();

        IssueRestClient issueClient = jiraRestClient.getIssueClient();
//        issueClient.transition(issueTestSuite.issueId(), 1);

    }
}
