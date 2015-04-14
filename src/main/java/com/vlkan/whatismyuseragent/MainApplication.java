package com.vlkan.whatismyuseragent;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.vlkan.whatismyuseragent.devicemap.DeviceAttributes;
import com.vlkan.whatismyuseragent.devicemap.DeviceProfilerService;
import com.vlkan.whatismyuseragent.devicemap.apache.ApacheDeviceAttributes;
import com.vlkan.whatismyuseragent.devicemap.apache.ApacheDeviceProfilerService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

public final class MainApplication extends Application<MainConfiguration> {

    private final static Logger log = LoggerFactory.getLogger(MainApplication.class);

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        checkNotNull(bootstrap, "bootstrap");
        log.info("Initializing configuration...");
        enableVariableSubstitutionWithEnvironmentVariables(bootstrap);
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new ViewBundle<MainConfiguration>() {
            @Override
            public ImmutableMap<String, ImmutableMap<String, String>> getViewConfiguration(MainConfiguration configuration) {
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    private static void enableVariableSubstitutionWithEnvironmentVariables(Bootstrap<MainConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)));
    }

    @Override
    public void run(MainConfiguration configuration, Environment environment) throws Exception {
        log.info("Starting the application...");
        checkNotNull(configuration, "main configuration");
        checkNotNull(environment, "environment");
        environment.jersey().register(
                new MainResource(
                        configuration.getRepositoryName(),
                        ApacheDeviceAttributes.VALID_ATTRIBUTES,
                        new ApacheDeviceProfilerService()));
    }

    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

}
