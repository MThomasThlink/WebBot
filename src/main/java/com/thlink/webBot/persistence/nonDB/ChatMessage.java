package com.thlink.webBot.persistence.nonDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import lombok.Data;

@Data
public class ChatMessage {
    private int id;
    private String instanceName;
    private Long idConversation;
    private String pushName, from;
    private boolean fromMe, afterChat;
    
    private Integer errorCode;
    private String errorMsg;
    
    private String clientMessage, aiMessage, manualMessage;
    private Date clientMoment, aiMoment, manualMoment;

    private String reservedField;
            
    public ChatMessage() {
    }
    
    public ChatMessage (Integer codErro, String errorMsg) {
        this.errorCode = codErro;
        this.errorMsg = errorMsg;
    }

    public static String objToJSON (ChatMessage pRT)
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }    
}
