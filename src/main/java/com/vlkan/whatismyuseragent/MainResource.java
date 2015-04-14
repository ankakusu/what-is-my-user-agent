package com.vlkan.whatismyuseragent;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.vlkan.whatismyuseragent.devicemap.DeviceAttributes;
import com.vlkan.whatismyuseragent.devicemap.DeviceProfile;
import com.vlkan.whatismyuseragent.devicemap.DeviceProfilerService;
import com.vlkan.whatismyuseragent.devicemap.IllegalUserAgentException;
import com.vlkan.whatismyuseragent.devicemap.apache.ApacheDeviceAttributes;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static com.vlkan.whatismyuseragent.util.GuavaCollectors.toImmutableMap;
import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Slf4j
@Path("/")
public final class MainResource {

    private final String repositoryName;

    private final  ImmutableSet<String> validDeviceAttributes;

    private final DeviceProfilerService<? extends DeviceAttributes> deviceProfilerService;

    public MainResource(
            String repositoryName,
            ImmutableSet<String> validDeviceAttributes,
            DeviceProfilerService<? extends DeviceAttributes> deviceProfilerService) {
        this.repositoryName = checkNotNull(repositoryName, "repository name");
        this.validDeviceAttributes = checkNotNull(validDeviceAttributes, "valid device attributes");
        this.deviceProfilerService = checkNotNull(deviceProfilerService, "device profiler service");
    }

    @GET
    @Timed
    public MainView getMain(@Context HttpServletRequest request) {
        checkNotNull(request, "request");
        Optional<String> userAgent = Optional.ofNullable(request.getHeader("User-Agent"));
        Optional<? extends DeviceProfile> deviceProfile = findDeviceProfile(userAgent);
        return new MainView(userAgent, deviceProfile);
    }

    private Optional<DeviceProfile> findDeviceProfile(Optional<String> optUserAgent) {
        return optUserAgent.flatMap(userAgent -> {
            try { return Optional.of(deviceProfilerService.findDeviceProfile(userAgent)); }
            catch (IllegalUserAgentException error) { return Optional.empty(); }
        });
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void submitDeviceProfile(MultivaluedMap<String, String> params, @Context HttpServletRequest request) throws IOException {
        checkNotNull(params, "form parameters");
        checkNotNull(request, "request");
        String userAgent = Optional.ofNullable(request.getHeader("User-Agent"))
                .orElseThrow(() -> new IllegalArgumentException("missing User-Agent"));
        ImmutableMap<String, String> attributes = ImmutableMap.<String, String>builder()
                .putAll(extractApacheAttributes(params))
                .put("deviceType", params.getFirst("deviceType"))
                .put("userAgent", userAgent)
                .build();
        String checksum = checksum(attributes);
        submitGitHubIssue(checksum, attributes);
    }

    private static ImmutableMap<String, String> extractApacheAttributes(MultivaluedMap<String, String> params) {
        return ApacheDeviceAttributes.VALID_ATTRIBUTES.stream()
                .collect(toImmutableMap(Function.identity(),
                        (key) -> Optional.ofNullable(params.getFirst(key)).map(String::trim).get()));
    }

    private static String checksum(ImmutableMap<String, String> attributes) {
        Hasher hasher = Hashing.sha256().newHasher();
        attributes.keySet().stream().sorted().forEach(key -> {
            Optional<String> value = Optional.ofNullable(attributes.get(key)).map(String::trim).map(String::toLowerCase);
            hasher.putString(value.toString(), Charset.defaultCharset());
        });
        return hasher.hash().toString();
    }

    private void submitGitHubIssue(String checksum, ImmutableMap<String, String> attributes) throws IOException {
        log.trace("Submitting GitHub Issue: checksum={}, attributes={}", checksum, attributes);
        GitHub github = GitHub.connect();
        GHRepository repository = github.getRepository(repositoryName);
        if (gitHubIssueExists(repository, checksum)) log.trace("GitHub issue already exists!");
        else createGitHubIssue(repository, checksum, attributes);
    }

    private static boolean gitHubIssueExists(GHRepository repository, String checksum) {
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

    private static void createGitHubIssue(GHRepository repository, String checksum, ImmutableMap<String, String> attributes) throws IOException {
        repository
                .createIssue(String.format("Device Profile Submission [%s]", checksum))
                .label("device-profile")
                .create();
    }

}
