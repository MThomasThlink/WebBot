package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemInstruction 
{
    @SerializedName("parts")
    @Expose
    private Parts parts;

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }

    
}
