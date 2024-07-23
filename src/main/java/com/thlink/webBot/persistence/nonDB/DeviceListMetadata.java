package com.thlink.webBot.persistence.nonDB;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeviceListMetadata {
    @SerializedName("senderKeyHash")
    @Expose
    private String senderKeyHash;
    @SerializedName("senderTimestamp")
    @Expose
    private String senderTimestamp;
    @SerializedName("recipientKeyHash")
    @Expose
    private String recipientKeyHash;
    @SerializedName("recipientTimestamp")
    @Expose
    private String recipientTimestamp;
}
