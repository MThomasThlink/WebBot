
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsageMetadata {

    @SerializedName("promptTokenCount")
    @Expose
    private Integer promptTokenCount;
    @SerializedName("candidatesTokenCount")
    @Expose
    private Integer candidatesTokenCount;
    @SerializedName("totalTokenCount")
    @Expose
    private Integer totalTokenCount;

    public Integer getPromptTokenCount() {
        return promptTokenCount;
    }

    public void setPromptTokenCount(Integer promptTokenCount) {
        this.promptTokenCount = promptTokenCount;
    }

    public Integer getCandidatesTokenCount() {
        return candidatesTokenCount;
    }

    public void setCandidatesTokenCount(Integer candidatesTokenCount) {
        this.candidatesTokenCount = candidatesTokenCount;
    }

    public Integer getTotalTokenCount() {
        return totalTokenCount;
    }

    public void setTotalTokenCount(Integer totalTokenCount) {
        this.totalTokenCount = totalTokenCount;
    }

}
