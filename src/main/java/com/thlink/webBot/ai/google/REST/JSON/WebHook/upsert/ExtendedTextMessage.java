package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtendedTextMessage {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("contextInfo")
    @Expose
    private ContextInfo contextInfo;
    @SerializedName("inviteLinkGroupTypeV2")
    @Expose
    private String inviteLinkGroupTypeV2;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ContextInfo getContextInfo() {
        return contextInfo;
    }

    public void setContextInfo(ContextInfo contextInfo) {
        this.contextInfo = contextInfo;
    }

    public String getInviteLinkGroupTypeV2() {
        return inviteLinkGroupTypeV2;
    }

    public void setInviteLinkGroupTypeV2(String inviteLinkGroupTypeV2) {
        this.inviteLinkGroupTypeV2 = inviteLinkGroupTypeV2;
    }
    
    
}
