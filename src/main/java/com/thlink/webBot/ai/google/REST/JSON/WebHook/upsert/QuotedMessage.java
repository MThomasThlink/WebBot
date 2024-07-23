package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuotedMessage {
    @SerializedName("conversation")
    @Expose
    private String conversation;
}
