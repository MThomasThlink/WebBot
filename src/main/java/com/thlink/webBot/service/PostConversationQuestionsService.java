package com.thlink.webBot.service;

import com.thlink.webBot.repository.PostConversationQuestionsRepository;

public class PostConversationQuestionsService {
     private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InstanceService.class);

    private final PostConversationQuestionsRepository repo;

    public PostConversationQuestionsService(PostConversationQuestionsRepository repo) {
        this.repo = repo;
    }
    
    
}
