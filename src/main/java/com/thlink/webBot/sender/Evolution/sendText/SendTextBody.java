
package com.thlink.webBot.sender.Evolution.sendText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SendTextBody {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("textMessage")
    @Expose
    private TextMessage textMessage;
    @SerializedName("options")
    @Expose
    private Options options;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static String objToJSON (SendTextBody pRT)
    {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }
}
