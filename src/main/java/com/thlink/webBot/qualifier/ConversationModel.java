package com.thlink.webBot.qualifier;

import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.persistence.entities.tbPostConversationQuestions;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.webbot.manager.ChatManager;
import java.util.List;
import lombok.Data;

@Data
public class ConversationModel {
    private Long idConversation;
    private Client client;
    private ChatManager chat;
    private ChatParams params;
    private tbInstances instance;
    private tbLogConversation conversation;
    private List<tbLogConversationMessage> messages;
    private List<tbPostConversationQuestions> postMessages;
    private Double score;

}
