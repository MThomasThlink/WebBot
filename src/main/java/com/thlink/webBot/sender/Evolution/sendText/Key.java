
package com.thlink.webBot.sender.Evolution.sendText;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Key {

    @SerializedName("remoteJid")
    @Expose
    private String remoteJid;
    @SerializedName("fromMe")
    @Expose
    private Boolean fromMe;
    @SerializedName("id")
    @Expose
    private String id;

    public String getRemoteJid() {
        return remoteJid;
    }

    public void setRemoteJid(String remoteJid) {
        this.remoteJid = remoteJid;
    }

    public Boolean getFromMe() {
        return fromMe;
    }

    public void setFromMe(Boolean fromMe) {
        this.fromMe = fromMe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
