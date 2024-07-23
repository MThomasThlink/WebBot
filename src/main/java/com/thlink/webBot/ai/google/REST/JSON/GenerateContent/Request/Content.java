
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("parts")
    @Expose
    private Parts parts;

    public Content(String role, Parts parts) {
        this.role = role;
        this.parts = parts;
    }

    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }

    @Override
    public String toString ()
    {
        return String.format("[%s] %s.", this.role, this.parts.getText());
    }
}
