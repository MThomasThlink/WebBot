package com.thlink.webBot.sender.Evolution.sendText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextMessage {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
