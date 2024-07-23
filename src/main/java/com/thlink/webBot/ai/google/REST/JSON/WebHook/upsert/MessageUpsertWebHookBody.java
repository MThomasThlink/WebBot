package com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import java.util.Calendar;

public class MessageUpsertWebHookBody {
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("instance")
    @Expose
    private String instance;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("server_url")
    @Expose
    private String serverUrl;
    @SerializedName("apikey")
    @Expose
    private String apikey;
    
    @Expose
    private String reservedField;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getReservedField() {
        return reservedField;
    }

    public void setReservedField(String reservedField) {
        this.reservedField = reservedField;
    }
    
    
    public static ChatMessage convertIncomingMessage (MessageUpsertWebHookBody justReceivedMessage) 
    {
        ChatMessage convMsg = new ChatMessage();
        convMsg.setFrom(justReceivedMessage.data.getKey().getRemoteJid());
        convMsg.setPushName(justReceivedMessage.data.getPushName());
        convMsg.setFromMe(justReceivedMessage.getData().getKey().getFromMe());
        if (justReceivedMessage.data.getKey().getFromMe() /*&& 
           !justReceivedMessage.data.getKey().getRemoteJid().startsWith("554891026166")*/)  //Esta mensagem veio do meu número e não é para mim: é uma resposta MANUAL
        {
            if (justReceivedMessage.data.getMessage().getExtendedTextMessage() != null)
                convMsg.setManualMessage(justReceivedMessage.data.getMessage().getExtendedTextMessage().getText());
            else
                convMsg.setManualMessage(justReceivedMessage.data.getMessage().getConversation());
            convMsg.setManualMoment(Calendar.getInstance().getTime());
        }
        else
        {
            if (justReceivedMessage.data.getMessage().getExtendedTextMessage() != null)
                convMsg.setClientMessage(justReceivedMessage.data.getMessage().getExtendedTextMessage().getText());
            else
                convMsg.setClientMessage(justReceivedMessage.data.getMessage().getConversation());
            convMsg.setClientMoment(Calendar.getInstance().getTime());
        }
        convMsg.setReservedField(justReceivedMessage.getReservedField());
        convMsg.setInstanceName(justReceivedMessage.getInstance());
      //System.out.println("convMsg.getRxMessage = " + justReceivedMessage.getInstance());
        return convMsg;
    }
}
