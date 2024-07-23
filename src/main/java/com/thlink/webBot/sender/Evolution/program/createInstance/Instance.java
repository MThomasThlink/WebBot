
package com.thlink.webBot.sender.Evolution.program.createInstance;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Instance {

    @SerializedName("instanceName")
    @Expose
    private String instanceName;
    @SerializedName("instanceId")
    @Expose
    private String instanceId;
    @SerializedName("webhook_wa_business")
    @Expose
    private Object webhookWaBusiness;
    @SerializedName("access_token_wa_business")
    @Expose
    private String accessTokenWaBusiness;
    @SerializedName("status")
    @Expose
    private String status;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Object getWebhookWaBusiness() {
        return webhookWaBusiness;
    }

    public void setWebhookWaBusiness(Object webhookWaBusiness) {
        this.webhookWaBusiness = webhookWaBusiness;
    }

    public String getAccessTokenWaBusiness() {
        return accessTokenWaBusiness;
    }

    public void setAccessTokenWaBusiness(String accessTokenWaBusiness) {
        this.accessTokenWaBusiness = accessTokenWaBusiness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
