package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.persistence.entities.tbKeyWords;
import com.thlink.webBot.repository.KeyWordsRepository;
import org.apache.log4j.Logger;


public class KeyWordsService  {
    private static final Logger logger = Logger.getLogger(KeyWordsService.class);
    
    private final KeyWordsRepository repo;

    public KeyWordsService(KeyWordsRepository repo) {
        this.repo = repo;
    }
    
    public tbKeyWords saveObj (tbKeyWords pObj) {
        return repo.save(pObj);
    }

}
