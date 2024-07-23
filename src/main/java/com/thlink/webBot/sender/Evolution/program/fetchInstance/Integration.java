package com.thlink.webBot.sender.Evolution.program.fetchInstance;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Integration {

    @SerializedName("webhook_wa_business")
    @Expose
    private String webhookWaBusiness;

    public String getWebhookWaBusiness() {
        return webhookWaBusiness;
    }

    public void setWebhookWaBusiness(String webhookWaBusiness) {
        this.webhookWaBusiness = webhookWaBusiness;
    }

}
