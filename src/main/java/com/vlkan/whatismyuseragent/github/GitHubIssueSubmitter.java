package com.vlkan.whatismyuseragent.github;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

@Slf4j
public class GitHubIssueSubmitter {

    private final GHRepository repository;

    private final String checksum;

    private final ImmutableMap<String, String> attributes;

    private GitHubIssueSubmitter(GHRepository repository, String checksum, ImmutableMap<String, String> attributes) {
        this.repository = checkNotNull(repository, "repository");
        this.checksum = checkNotNull(checksum, "checksum");
        this.attributes = checkNotNull(attributes, "attributes");
    }

    public static GitHubIssueSubmitter of(
            String repositoryName, String checksum, ImmutableMap<String, String> attributes)
            throws IOException {
        GitHub connection = GitHub.connect();
        GHRepository repository = connection.getRepository(repositoryName);
        return new GitHubIssueSubmitter(repository, checksum, attributes);
    }

    public URL submit() throws GitHubIssueConflict, IOException {
        if (issueExists()) {
            log.trace("GitHub issue conflict: {}", checksum);
            throw new GitHubIssueConflict(repository.getFullName(), checksum);
        }
        URL url = createIssue();
        log.trace("Created GitHub issue: {}", url);
        return url;
    }

    private boolean issueExists() {
        return Arrays.stream(GHIssueState.values())
                .map(state -> {
                    try {
                        return repository
                                .getIssues(state).stream()
                                .anyMatch(issue -> issue.getTitle().contains(checksum));
                    } catch (IOException exception) {
                        log.error("failed to retrieve GitHub issues of state " + state, exception);
                        return false;
                    }
                }).findFirst().orElse(false);
    }

    private URL createIssue() throws IOException {
        return repository
                .createIssue(String.format("Device Profile Submission [%s]", checksum))
                .label("device-profile")
                .body(attributes.entrySet().stream()
                        .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                        .collect(joining("\n")))
                .create()
                .getUrl();
    }

}
