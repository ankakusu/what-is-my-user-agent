package com.vlkan.whatismyuseragent.github;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Value
@Immutable
@EqualsAndHashCode(callSuper = false)
public class GitHubIssueConflict extends Exception {

    private final String repositoryName;

    private final String checksum;

    public GitHubIssueConflict(String repositoryName, String checksum) {
        super(String.format("failed to create issue with checksum %s for repository %s", checksum, repositoryName));
        this.repositoryName = checkNotNull(repositoryName, "repository name");
        this.checksum = checkNotNull(checksum, "checksum");
    }

}
