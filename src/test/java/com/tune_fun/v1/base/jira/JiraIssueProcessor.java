package com.tune_fun.v1.base.jira;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import io.atlassian.util.concurrent.Promise;
import lombok.RequiredArgsConstructor;
import org.junitpioneer.jupiter.IssueProcessor;
import org.junitpioneer.jupiter.IssueTestSuite;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;

@Component
@Profile("test")
@RequiredArgsConstructor
public class JiraIssueProcessor implements IssueProcessor {

    private final JiraRestClient jiraRestClient;

    @Override
    public void processTestResults(List<IssueTestSuite> issueTestSuites) {
        IssueRestClient issueClient = jiraRestClient.getIssueClient();
        issueTestSuites.forEach(i -> {
            if (!i.tests().stream().allMatch(t -> t.result().equals(SUCCESSFUL))) return;
            Promise<Issue> issuePromise = issueClient.getIssue(i.issueId());

            if (issuePromise.isDone())
                issueClient.transition(
                        issuePromise.claim(),
                        getCompletedTransitionInput(issueClient, issuePromise.claim())
                );
        });

    }

    private TransitionInput getCompletedTransitionInput(IssueRestClient issueClient, Issue issue) {
        Promise<Iterable<Transition>> transitions = issueClient.getTransitions(issue);
        Transition resolvedTransaction = null;

        if (transitions.isDone())
            resolvedTransaction = getTransitionByName(transitions.claim(), "Done");
        assert resolvedTransaction != null;

        Collection<FieldInput> fieldInputs = List.of(
                new FieldInput("resolution", ComplexIssueInputFieldValue.with("name", "Fixed"))
        );
        Comment comment = Comment.valueOf("Resolving issue using Backend Jira Rest Client");

        return new TransitionInput(resolvedTransaction.getId(), fieldInputs, comment);
    }

    private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions)
            if (transition.getName().equals(transitionName)) return transition;
        return null;
    }
}
