package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message 
{
    @SerializedName("conversation")
    @Expose
    private String conversation;
    @Expose
    private ExtendedTextMessage extendedTextMessage;
    @SerializedName("messageContextInfo")
    @Expose
    private MessageContextInfo messageContextInfo;

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public ExtendedTextMessage getExtendedTextMessage() {
        return extendedTextMessage;
    }

    public void setExtendedTextMessage(ExtendedTextMessage extendedTextMessage) {
        this.extendedTextMessage = extendedTextMessage;
    }

    public MessageContextInfo getMessageContextInfo() {
        return messageContextInfo;
    }

    public void setMessageContextInfo(MessageContextInfo messageContextInfo) {
        this.messageContextInfo = messageContextInfo;
    }
    
    
}
