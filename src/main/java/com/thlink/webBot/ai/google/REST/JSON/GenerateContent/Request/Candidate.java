
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Candidate {

    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("finishReason")
    @Expose
    private String finishReason;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("safetyRatings")
    @Expose
    private List<SafetyRating> safetyRatings;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<SafetyRating> getSafetyRatings() {
        return safetyRatings;
    }

    public void setSafetyRatings(List<SafetyRating> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }

    @Override
    public String toString ()
    {
        return String.format("[%s] %s.", content.getRole(), content.getParts());
    }
}
