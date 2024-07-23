package com.thlink.webBot.sender.Evolution.program.setWebHook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SetWebHookResponseBody {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("response")
    @Expose
    private Response response;
    
    @SerializedName("webhook")
    @Expose
    private Webhook webhook;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    

    public static SetWebHookResponseBody jsonToObj(String pJSON) 
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            SetWebHookResponseBody p = gson.fromJson(pJSON, SetWebHookResponseBody.class);
            return p;
        } catch (Exception e)
        {
            System.out.printf("jsonToObj SetWebHookResponseBody ERROR: %s. \n", e.toString());
            return null;
        }
    }
}
