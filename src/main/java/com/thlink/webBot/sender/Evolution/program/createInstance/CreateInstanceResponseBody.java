package com.thlink.webBot.sender.Evolution.program.createInstance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CreateInstanceResponseBody {

    @SerializedName("instance")
    @Expose
    private Instance instance;
    @SerializedName("hash")
    @Expose
    private Hash hash;
    @SerializedName("webhook")
    @Expose
    private Webhook webhook;
    @SerializedName("websocket")
    @Expose
    private Websocket websocket;
    @SerializedName("rabbitmq")
    @Expose
    private Rabbitmq rabbitmq;
    @SerializedName("sqs")
    @Expose
    private Sqs sqs;
    @SerializedName("typebot")
    @Expose
    private Typebot typebot;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("qrcode")
    @Expose
    private Qrcode qrcode;

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public void setWebhook(Webhook webhook) {
        this.webhook = webhook;
    }

    public Websocket getWebsocket() {
        return websocket;
    }

    public void setWebsocket(Websocket websocket) {
        this.websocket = websocket;
    }

    public Rabbitmq getRabbitmq() {
        return rabbitmq;
    }

    public void setRabbitmq(Rabbitmq rabbitmq) {
        this.rabbitmq = rabbitmq;
    }

    public Sqs getSqs() {
        return sqs;
    }

    public void setSqs(Sqs sqs) {
        this.sqs = sqs;
    }

    public Typebot getTypebot() {
        return typebot;
    }

    public void setTypebot(Typebot typebot) {
        this.typebot = typebot;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Qrcode getQrcode() {
        return qrcode;
    }

    public void setQrcode(Qrcode qrcode) {
        this.qrcode = qrcode;
    }

    public static CreateInstanceResponseBody jsonToObj(String pJSON) 
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            CreateInstanceResponseBody p = gson.fromJson(pJSON, CreateInstanceResponseBody.class);
            return p;
        } catch (Exception e)
        {
            System.out.printf("jsonToObj CreateInstanceResponseBody ERROR: %s. \n", e.toString());
            return null;
        }
    }
}
