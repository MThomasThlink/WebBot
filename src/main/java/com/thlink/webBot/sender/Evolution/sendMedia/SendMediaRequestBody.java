package com.thlink.webBot.sender.Evolution.sendMedia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SendMediaRequestBody {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("mediaMessage")
    @Expose
    private MediaMessage mediaMessage;
    @SerializedName("options")
    @Expose
    private Options options;

    public static String objToJSON (SendMediaRequestBody pRT)
    {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public MediaMessage getMediaMessage() {
        return mediaMessage;
    }

    public void setMediaMessage(MediaMessage mediaMessage) {
        this.mediaMessage = mediaMessage;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
