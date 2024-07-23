package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thlink.webBot.persistence.nonDB.DeviceListMetadata;


public class MessageContextInfo {
    @SerializedName("deviceListMetadata")
    @Expose
    private DeviceListMetadata deviceListMetadata;
    @SerializedName("deviceListMetadataVersion")
    @Expose
    private Integer deviceListMetadataVersion;

}
