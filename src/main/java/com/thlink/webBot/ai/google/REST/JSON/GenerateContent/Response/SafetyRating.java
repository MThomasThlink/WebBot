
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafetyRating {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("probability")
    @Expose
    private String probability;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

}
