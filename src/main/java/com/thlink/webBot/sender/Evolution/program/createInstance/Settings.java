
package com.thlink.webBot.sender.Evolution.program.createInstance;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Settings {

    @SerializedName("reject_call")
    @Expose
    private Boolean rejectCall;
    @SerializedName("msg_call")
    @Expose
    private String msgCall;
    @SerializedName("groups_ignore")
    @Expose
    private Boolean groupsIgnore;
    @SerializedName("always_online")
    @Expose
    private Boolean alwaysOnline;
    @SerializedName("read_messages")
    @Expose
    private Boolean readMessages;
    @SerializedName("read_status")
    @Expose
    private Boolean readStatus;
    @SerializedName("sync_full_history")
    @Expose
    private Boolean syncFullHistory;

    public Boolean getRejectCall() {
        return rejectCall;
    }

    public void setRejectCall(Boolean rejectCall) {
        this.rejectCall = rejectCall;
    }

    public String getMsgCall() {
        return msgCall;
    }

    public void setMsgCall(String msgCall) {
        this.msgCall = msgCall;
    }

    public Boolean getGroupsIgnore() {
        return groupsIgnore;
    }

    public void setGroupsIgnore(Boolean groupsIgnore) {
        this.groupsIgnore = groupsIgnore;
    }

    public Boolean getAlwaysOnline() {
        return alwaysOnline;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }

    public Boolean getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(Boolean readMessages) {
        this.readMessages = readMessages;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public Boolean getSyncFullHistory() {
        return syncFullHistory;
    }

    public void setSyncFullHistory(Boolean syncFullHistory) {
        this.syncFullHistory = syncFullHistory;
    }

}
