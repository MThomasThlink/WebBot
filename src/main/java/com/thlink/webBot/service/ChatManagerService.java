package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbLogChatManager;
import com.thlink.webBot.repository.ChatManagerRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ChatManagerService {
    private static final Logger logger = Logger.getLogger(ChatManagerService.class);
    private final ChatManagerRepository repo;
     
    public ChatManagerService (ChatManagerRepository pRepo) 
    {
         this.repo = pRepo; 
    }
    
    public tbLogChatManager saveObj (tbLogChatManager pObj) {
        return repo.save(pObj);
    }
    
}
