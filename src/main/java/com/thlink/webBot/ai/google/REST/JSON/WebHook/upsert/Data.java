package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data 
{
    @SerializedName("key")
    @Expose
    private Key key;
    @SerializedName("pushName")
    @Expose
    private String pushName;
    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("messageType")
    @Expose
    private String messageType;
    @SerializedName("messageTimestamp")
    @Expose
    private Integer messageTimestamp;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("source")
    @Expose
    private String source;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Integer messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    
}
