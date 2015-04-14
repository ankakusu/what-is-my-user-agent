package com.vlkan.whatismyuseragent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class MainConfiguration extends Configuration {

    private final String repositoryName;

    private final ImmutableMap<String, ImmutableMap<String, String>> viewRendererConfiguration;

    @JsonCreator
    public MainConfiguration(
            @JsonProperty("repositoryName") String repositoryName,
            @JsonProperty("viewRendererConfiguration") Map<String, Map<String, String>> viewRendererConfiguration) {
        this.repositoryName = checkNotNull(repositoryName, "repository name");
        this.viewRendererConfiguration = copyViewRendererConfiguration(viewRendererConfiguration);
    }

    private static ImmutableMap<String, ImmutableMap<String, String>> copyViewRendererConfiguration(
            Map<String, Map<String, String>> viewRendererConfiguration) {
        checkNotNull(viewRendererConfiguration, "view renderer configuration");
        ImmutableMap.Builder<String, ImmutableMap<String, String>> builder = ImmutableMap.builder();
        viewRendererConfiguration.entrySet().stream()
                .forEach(entry -> builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue())));
        return builder.build();
    }

}
