package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.repository.ConversationMessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConversationMessageService 
{
    private static final Logger logger = Logger.getLogger(ConversationMessageService.class);
    private final ConversationMessageRepository repo;

    @PersistenceContext
    private EntityManager entityManager;
    
    public ConversationMessageService (ConversationMessageRepository repo) {
        this.repo = repo;
    }

    public tbLogConversationMessage saveObj (tbLogConversationMessage log) 
    {
        return repo.save(log);   
    }
    
    public tbLogConversationMessage saveObj (tbLogConversation pConversation, ChatMessage message) 
    {
        tbLogConversationMessage log = new tbLogConversationMessage(pConversation, message);
        return repo.save(log);
        
    }

    public tbLogConversationMessage updateLogConversationMessage (ChatMessage message) 
    {
        return null;
    }

    public tbLogConversationMessage updateLogConversationMessageSPY (ChatMessage pMessage) 
    {
        tbLogConversationMessage mess = (tbLogConversationMessage)
        entityManager.createQuery("SELECT   C FROM tbLogConversationMessage C " +
                                  "JOIN     C.instance I " + 
                                  "WHERE    C.userNumber = :pUserName " +
                                  "ORDER    BY C.clientMoment DESC ")
                     .setParameter("pUserName", pMessage.getFrom())
                     .getSingleResult();
        if (mess == null)
            return null;
        if (pMessage.getAiMessage() != null)
        {
            mess.setAiMoment(Calendar.getInstance().getTime());
            mess.setAiText(pMessage.getAiMessage());
        }
        if (pMessage.getManualMessage() != null)
        {
            mess.setManualMoment(Calendar.getInstance().getTime());
            mess.setManualText(pMessage.getManualMessage());
        }
        return repo.saveAndFlush(mess);
        
    }
    
    
}
