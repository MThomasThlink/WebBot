
package com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseContent {

    @SerializedName("candidates")
    @Expose
    private List<Candidate> candidates;
    @SerializedName("usageMetadata")
    @Expose
    private UsageMetadata usageMetadata;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public UsageMetadata getUsageMetadata() {
        return usageMetadata;
    }

    public void setUsageMetadata(UsageMetadata usageMetadata) {
        this.usageMetadata = usageMetadata;
    }

    public static ResponseContent jsonToObj (String pJSON)
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            ResponseContent p = gson.fromJson(pJSON, ResponseContent.class);
            return p;
        } catch (Exception e)
        {
            System.out.printf("jsonToObj ERROR: %s. \n", e.toString());
            return null;
        }
    }
}
