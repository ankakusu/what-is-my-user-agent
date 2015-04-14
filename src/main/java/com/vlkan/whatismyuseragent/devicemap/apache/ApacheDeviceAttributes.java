package com.vlkan.whatismyuseragent.devicemap.apache;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.vlkan.whatismyuseragent.devicemap.DeviceAttributes;
import com.vlkan.whatismyuseragent.devicemap.attribute.DeviceOs;
import com.vlkan.whatismyuseragent.devicemap.attribute.DisplayDimensions;
import com.vlkan.whatismyuseragent.devicemap.attribute.InputDevice;
import com.vlkan.whatismyuseragent.devicemap.attribute.MobileBrowser;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static com.vlkan.whatismyuseragent.util.GuavaCollectors.toImmutableSet;
import static com.vlkan.whatismyuseragent.util.Preconditions.checkNotNull;

@Value
@Immutable
@EqualsAndHashCode(callSuper = true)
public class ApacheDeviceAttributes extends DeviceAttributes {

    public transient final static ImmutableSet<String> VALID_ATTRIBUTES =
                Arrays.stream(ApacheDeviceAttributes.class.getDeclaredFields())
                        .map(Field::getName).collect(toImmutableSet());

    private final Optional<String> vendor;
    private final Optional<String> model;
    private final Optional<DisplayDimensions> displayDimensions;
    private final MobileBrowser mobileBrowser;
    private final Optional<String> mobileBrowserVersion;
    private final DeviceOs deviceOs;
    private final Optional<String> deviceOsVersion;
    private final Optional<String> nokiaEdition;
    private final Optional<String> nokiaSeries;
    private final boolean dualOrientation;
    private final InputDevice inputDevice;
    private final boolean wirelessDevice;
    private final boolean tablet;
    private final boolean desktop;
    private final boolean bot;
    private final boolean ajaxSupportJavaScript;
    private final boolean ajaxSupportGetElementById;
    private final boolean ajaxSupportInnerHtml;
    private final boolean ajaxManipulateDom;
    private final boolean ajaxManipulateCss;
    private final boolean ajaxSupportEvents;
    private final boolean ajaxSupportEventListener;
    private final boolean xhtmlFormatAsCssProperty;
    private final boolean xhtmlFormatAsAttribute;
    private final Optional<String> marketingName;
    private final boolean imageInlining;
    private final Optional<String> from;

    public ApacheDeviceAttributes(ImmutableMap<String, String> attributes) {
        super(checkNotNull(attributes, "attributes"));
        Parser parser = new Parser(attributes);
        this.vendor = parser.getString("vendor");
        this.model = parser.getString("model");
        this.displayDimensions = parser.getDisplayDimensions("displayWidth", "displayHeight");
        this.mobileBrowser = parser.getMobileBrowser("mobile_browser");
        this.mobileBrowserVersion = parser.getString("mobile_browser_version");
        this.deviceOs = parser.getDeviceOs("device_os");
        this.deviceOsVersion = parser.getString("device_os_version");
        this.nokiaEdition = parser.getString("nokia_edition");
        this.nokiaSeries = parser.getString("nokia_series");
        this.dualOrientation = parser.getBoolean("dual_orientation");
        this.inputDevice = parser.getInputDevice("inputDevices");
        this.wirelessDevice = parser.getBoolean("is_wireless_device");
        this.tablet = parser.getBoolean("is_tablet");
        this.desktop = parser.getBoolean("is_desktop");
        this.bot = parser.getBoolean("is_bot");
        this.ajaxSupportJavaScript = parser.getBoolean("ajax_support_javascript");
        this.ajaxSupportGetElementById = parser.getBoolean("ajax_support_getelementbyid");
        this.ajaxSupportInnerHtml = parser.getBoolean("ajax_support_inner_html");
        this.ajaxManipulateDom = parser.getBoolean("ajax_manipulate_dom");
        this.ajaxManipulateCss = parser.getBoolean("ajax_manipulate_css");
        this.ajaxSupportEvents = parser.getBoolean("ajax_support_events");
        this.ajaxSupportEventListener = parser.getBoolean("ajax_support_event_listener");
        this.xhtmlFormatAsCssProperty = parser.getBoolean("xhtml_format_as_css_property");
        this.xhtmlFormatAsAttribute = parser.getBoolean("xhtml_format_as_attribute");
        this.marketingName = parser.getString("marketing_name");
        this.imageInlining = parser.getBoolean("image_inlining");
        this.from = parser.getString("from");
    }

}
