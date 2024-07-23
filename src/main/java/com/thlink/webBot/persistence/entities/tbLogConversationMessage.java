package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import static com.thlink.webBot.webbot.manager.InstanceManager.WHATSAPP_SUFIX;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import javax.persistence.Temporal;
import lombok.Data;

@Data
@Entity
public class tbLogConversationMessage implements Serializable
{

    public tbLogConversationMessage() {
    }
    
    public tbLogConversationMessage (tbLogConversation pConversation, ChatMessage pMsg) 
    {
        this.conversation = pConversation;
        this.clientText = pMsg.getClientMessage();
        this.clientMoment = pMsg.getClientMoment();
        this.aiText = pMsg.getAiMessage();
        this.aiMoment = pMsg.getAiMoment();
        
        this.manualText = pMsg.getManualMessage();
        this.manualMoment = pMsg.getManualMoment();
        this.afterChat = pMsg.isAfterChat();
        this.userNumber = pMsg.getFrom().replace(WHATSAPP_SUFIX, "");
    }
    
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    private String userNumber;
    
    @JsonIgnore
    @Column(length = 2000)
    private String clientText;
    @JsonIgnore
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date clientMoment;
    
    @JsonIgnore
    @Column(length = 2000)
    private String aiText;
    @JsonIgnore
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date aiMoment;

    @JsonIgnore
    @Column(length = 2000)
    private String manualText;
    
    @JsonIgnore
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date manualMoment;
    @JsonIgnore
    private Boolean afterChat;
    
    private transient Long delayS;
    private transient String strDelay;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    @JsonIgnore
    private tbLogConversation conversation;


    public Long getDelayS () 
    {
        if (this.clientMoment != null)
        {
            if (manualMoment != null)
                return Duration.between(this.clientMoment.toInstant(), this.aiMoment.toInstant()).toSeconds();
            else
                return this.delayS = Duration.between(this.clientMoment.toInstant(), this.aiMoment.toInstant()).toSeconds();
        }        
        return null;
    }
    
     public String getStrDelay ()
     {
         String localDelay;
         if (this.delayS < 60)
             localDelay = String.format("%d segundos", delayS);
         else if (this.delayS < 3600)
             localDelay = String.format("%4.2f minutos", ((double)delayS) / 60);
         else if (this.delayS < 86400)
             localDelay = String.format("%4.2f horas", ((double)delayS) / 3600);
         else
             localDelay = String.format("%4.2f dias", ((double)delayS) / 86400);
         
         return localDelay;
     }

    
}
