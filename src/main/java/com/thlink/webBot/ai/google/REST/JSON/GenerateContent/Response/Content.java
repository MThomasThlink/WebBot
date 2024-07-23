
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("parts")
    @Expose
    private List<Part> parts;
    @SerializedName("role")
    @Expose
    private String role;

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString ()
    {
      //return String.format("[%s] %s.", this.role, this.parts);
      //return String.format("%s.", this.parts.);
        return "qwert";
    }
}
