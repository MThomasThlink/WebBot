package com.thlink.webBot.sender.Evolution.sendText;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mentions {

    @SerializedName("everyone")
    @Expose
    private Boolean everyone;
    @SerializedName("mentioned")
    @Expose
    private List<String> mentioned;

    public Boolean getEveryone() {
        return everyone;
    }

    public void setEveryone(Boolean everyone) {
        this.everyone = everyone;
    }

    public List<String> getMentioned() {
        return mentioned;
    }

    public void setMentioned(List<String> mentioned) {
        this.mentioned = mentioned;
    }

}
