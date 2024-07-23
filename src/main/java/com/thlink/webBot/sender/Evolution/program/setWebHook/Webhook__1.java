package com.thlink.webBot.sender.Evolution.program.setWebHook;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Webhook__1 {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("webhook_by_events")
    @Expose
    private Boolean webhookByEvents;
    @SerializedName("webhook_base64")
    @Expose
    private Boolean webhookBase64;
    @SerializedName("events")
    @Expose
    private List<String> events;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getWebhookByEvents() {
        return webhookByEvents;
    }

    public void setWebhookByEvents(Boolean webhookByEvents) {
        this.webhookByEvents = webhookByEvents;
    }

    public Boolean getWebhookBase64() {
        return webhookBase64;
    }

    public void setWebhookBase64(Boolean webhookBase64) {
        this.webhookBase64 = webhookBase64;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
