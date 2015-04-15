package com.vlkan.whatismyuseragent.github;

import com.codahale.metrics.health.HealthCheck;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.kohsuke.github.GitHub;

@Value
@EqualsAndHashCode(callSuper = false)
public class GitHubHealthCheck extends HealthCheck {

    private final String repositoryName;

    @Override
    protected Result check() throws Exception {
        GitHub.connect().getRepository(repositoryName);
        return Result.healthy();
    }

}
