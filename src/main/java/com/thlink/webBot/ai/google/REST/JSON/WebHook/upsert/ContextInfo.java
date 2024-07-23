package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContextInfo {
    @SerializedName("stanzaId")
    @Expose
    private String stanzaId;
    @SerializedName("participant")
    @Expose
    private String participant;
    @SerializedName("quotedMessage")
    @Expose
    private QuotedMessage quotedMessage;
    @SerializedName("remoteJid")
    @Expose
    private String remoteJid;

    public String getStanzaId() {
        return stanzaId;
    }

    public void setStanzaId(String stanzaId) {
        this.stanzaId = stanzaId;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public QuotedMessage getQuotedMessage() {
        return quotedMessage;
    }

    public void setQuotedMessage(QuotedMessage quotedMessage) {
        this.quotedMessage = quotedMessage;
    }

    public String getRemoteJid() {
        return remoteJid;
    }

    public void setRemoteJid(String remoteJid) {
        this.remoteJid = remoteJid;
    }
    
    
}
