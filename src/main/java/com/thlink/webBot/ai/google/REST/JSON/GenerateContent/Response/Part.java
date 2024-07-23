
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Part {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /*@Override
    public String toString ()
    {
        return String.format("%s.", text);
    }*/
}
