
package com.thlink.webBot.sender.Evolution.sendText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quoted {

    @SerializedName("key")
    @Expose
    private Key key;
    @SerializedName("message")
    @Expose
    private Message message;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
