package com.thlink.webBot.sender.Evolution.program.setWebHook;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Webhook {

    @SerializedName("instanceName")
    @Expose
    private String instanceName;
    @SerializedName("webhook")
    @Expose
    private Webhook__1 webhook;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Webhook__1 getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook__1 webhook) {
        this.webhook = webhook;
    }

}
