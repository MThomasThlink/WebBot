
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parts {

    @SerializedName("text")
    @Expose
    private String text;

    public Parts(String text) {
        this.text = text;
    }

    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
