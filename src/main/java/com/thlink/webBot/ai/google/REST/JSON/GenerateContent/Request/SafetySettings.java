
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafetySettings {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("threshold")
    @Expose
    private String threshold;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

}
