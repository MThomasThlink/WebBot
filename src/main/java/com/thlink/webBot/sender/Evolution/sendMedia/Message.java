package com.thlink.webBot.sender.Evolution.sendMedia;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Message {

    @SerializedName("conversation")
    @Expose
    private String conversation;

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

}
