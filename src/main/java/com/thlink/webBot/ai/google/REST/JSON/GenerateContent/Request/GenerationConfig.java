package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerationConfig {

    @SerializedName("temperature")
    @Expose
    private Double temperature;
    @SerializedName("topP")
    @Expose
    private Double topP;
    @SerializedName("topK")
    @Expose
    private Integer topK;
    @SerializedName("maxOutputTokens")
    @Expose
    private Integer maxOutputTokens;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Integer getTopK() {
        return topK;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(Integer maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }

}
