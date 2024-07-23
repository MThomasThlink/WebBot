package com.thlink.webBot.sender.Evolution.sendMedia;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thlink.webBot.sender.Evolution.sendText.Mentions;
import com.thlink.webBot.sender.Evolution.sendText.Quoted;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Options {

    @SerializedName("delay")
    @Expose
    private Integer delay;
    @SerializedName("presence")
    @Expose
    private String presence;
    @SerializedName("linkPreview")
    @Expose
    private Boolean linkPreview;
    @SerializedName("quoted")
    @Expose
    private Quoted quoted;
    @SerializedName("mentions")
    @Expose
    private Mentions mentions;

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public Boolean getLinkPreview() {
        return linkPreview;
    }

    public void setLinkPreview(Boolean linkPreview) {
        this.linkPreview = linkPreview;
    }

    public Quoted getQuoted() {
        return quoted;
    }

    public void setQuoted(Quoted quoted) {
        this.quoted = quoted;
    }

    public Mentions getMentions() {
        return mentions;
    }

    public void setMentions(Mentions mentions) {
        this.mentions = mentions;
    }

}
