package com.github.khangnt.youtubecrawler.model.youtube;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Khang NT on 10/30/17.
 * Email: khang.neon.1997@gmail.com
 */

public class WindowSettings {
    @SerializedName("build_id")
    private String buildId;
    @SerializedName("build_label")
    private String buildLabel;
    @SerializedName("client_name")
    private String clientName;
    @SerializedName("client_version")
    private String clientVersion;
    @SerializedName("variants_checksum")
    private String variantChecksum;

    @SuppressWarnings("unused")
    public WindowSettings() {
    }

    public String getBuildId() {
        return buildId;
    }

    public String getBuildLabel() {
        return buildLabel;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public String getVariantChecksum() {
        return variantChecksum;
    }

}
