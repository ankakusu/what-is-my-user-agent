package com.vlkan.whatismyuseragent;

import com.google.common.collect.ImmutableList;
import com.vlkan.whatismyuseragent.devicemap.DeviceProfile;
import com.vlkan.whatismyuseragent.devicemap.DeviceType;
import com.vlkan.whatismyuseragent.devicemap.attribute.DeviceOs;
import com.vlkan.whatismyuseragent.devicemap.attribute.InputDevice;
import com.vlkan.whatismyuseragent.devicemap.attribute.MobileBrowser;
import io.dropwizard.views.View;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Arrays;
import java.util.Optional;

import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public final class MainView extends View {

    public final static String TEMPLATE_NAME = "main.mustache";

    private final String remoteAddress;

    private final Optional<String> userAgent;

    private final Optional<? extends DeviceProfile> deviceProfile;

    private final ImmutableList<DeviceType> deviceTypes = ImmutableList.copyOf(DeviceType.values());

    private final ImmutableList<DeviceOs> deviceOses = ImmutableList.copyOf(DeviceOs.values());

    private final ImmutableList<MobileBrowser> mobileBrowsers = ImmutableList.copyOf(MobileBrowser.values());

    private final ImmutableList<InputDevice> inputDevices = ImmutableList.copyOf(InputDevice.values());

    public MainView(String remoteAddress, Optional<String> userAgent, Optional<? extends DeviceProfile> deviceProfile) {
        super(TEMPLATE_NAME);
        this.remoteAddress = checkNotNull(remoteAddress, "remote address");
        this.userAgent = checkNotNull(userAgent, "User-Agent");
        this.deviceProfile = checkNotNull(deviceProfile, "device profile");
    }

}
