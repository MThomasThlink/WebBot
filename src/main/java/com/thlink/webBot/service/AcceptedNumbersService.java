package com.thlink.webBot.service;

import com.thlink.webBot.repository.AcceptedNumbersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AcceptedNumbersService {
    private final Logger logger = LoggerFactory.getLogger(AiParametersService.class); 
    private final AcceptedNumbersRepository repo;

    public AcceptedNumbersService(AcceptedNumbersRepository repo) {
        this.repo = repo;
    }
    
}
