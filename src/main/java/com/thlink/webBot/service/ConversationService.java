package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.repository.ConversationRepository;
import static com.thlink.webBot.webbot.manager.InstanceManager.WHATSAPP_SUFIX;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {
    private static final Logger logger = Logger.getLogger(ConversationService.class);
    private final ConversationRepository repo;

    public ConversationService(ConversationRepository repo) {
        this.repo = repo;
    }

    public tbLogConversation findById (Long pId) 
    {
        return repo.findById(pId).orElse(null);
        
    }
    
    public tbLogConversation saveObj (tbLogConversation pObj) 
    {
        return repo.saveAndFlush(pObj);
        
    }
    public tbLogConversation getLastConversationWithThisPerson(String from) {
        return repo.getLastConversationWithThisPerson(from);
    }

    public tbLogConversation createLogConversation (ChatMessage message, tbInstances pInstance, boolean pIsContact) 
    {
      //logger.info("createLogConversation");
        tbLogConversation log = new tbLogConversation();
        log.setInstance(pInstance);
        log.setStartTime(Calendar.getInstance().getTime());
        
        log.setStatus(1);
        log.setPushName(message.getPushName());
        log.setUserNumber(message.getFrom().replace(WHATSAPP_SUFIX,  ""));
        log.setIsContact(pIsContact);
        
        log.setStartedByMe(message.isFromMe());
        //log = repo.saveAndFlush(log);
        
      //logger.info("createLogConversation(END)");
        return log;
    }
}
