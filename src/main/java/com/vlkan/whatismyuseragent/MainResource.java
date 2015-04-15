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
import com.vlkan.whatismyuseragent.github.GitHubIssueConflict;
import com.vlkan.whatismyuseragent.github.GitHubIssueSubmitter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
        String remoteAddress = request.getRemoteAddr();
        Optional<String> userAgent = Optional.ofNullable(request.getHeader("User-Agent"));
        Optional<? extends DeviceProfile> deviceProfile = findDeviceProfile(userAgent);
        return new MainView(remoteAddress, userAgent, deviceProfile);
    }

    private Optional<DeviceProfile> findDeviceProfile(Optional<String> optUserAgent) {
        return optUserAgent.flatMap(userAgent -> {
            try { return Optional.of(deviceProfilerService.findDeviceProfile(userAgent)); }
            catch (IllegalUserAgentException error) { return Optional.empty(); }
        });
    }

    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public URL submitDeviceProfile(MultivaluedMap<String, String> params, @Context HttpServletRequest request) throws IOException {
        checkNotNull(params, "form parameters");
        checkNotNull(request, "request");
        String remoteAddress = request.getRemoteAddr();
        String userAgent = Optional.ofNullable(request.getHeader("User-Agent"))
                .orElseThrow(() -> new IllegalArgumentException("missing User-Agent"));
        ImmutableMap<String, String> attributes = ImmutableMap.<String, String>builder()
                .putAll(extractAttributes(params))
                .put("deviceType", params.getFirst("deviceType"))
                .put("remoteAddress", remoteAddress)
                .put("userAgent", userAgent)
                .build();
        String checksum = checksum(attributes);
        return submitGitHubIssue(checksum, attributes);
    }

    private ImmutableMap<String, String> extractAttributes(MultivaluedMap<String, String> params) {
        return validDeviceAttributes.stream()
                .filter(params::containsKey)
                .collect(toImmutableMap(Function.identity(),
                        (key) -> Optional.ofNullable(params.getFirst(key)).map(String::trim).get()));
    }

    private static String checksum(ImmutableMap<String, String> attributes) {
        Hasher hasher = Hashing.murmur3_32().newHasher();
        attributes.keySet().stream().sorted().forEach(key -> {
            Optional<String> value = Optional.ofNullable(attributes.get(key)).map(String::trim).map(String::toLowerCase);
            hasher.putString(value.toString(), Charset.defaultCharset());
        });
        return hasher.hash().toString();
    }

    private URL submitGitHubIssue(String checksum, ImmutableMap<String, String> attributes) throws IOException {
        log.trace("Submitting GitHub Issue: checksum={}, attributes={}", checksum, attributes);
        try { return GitHubIssueSubmitter.of(repositoryName, checksum, attributes).submit(); }
        catch (GitHubIssueConflict exception) { throw new WebApplicationException(Response.Status.CONFLICT); }
    }

}
