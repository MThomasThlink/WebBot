package com.thlink.webBot.service;

import com.thlink.webBot.repository.BlockedNumbersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BlockedNumbersService {
    private final Logger logger = LoggerFactory.getLogger(AiParametersService.class); 
    private final BlockedNumbersRepository repo;

    public BlockedNumbersService(BlockedNumbersRepository repo) {
        this.repo = repo;
    }
    
}
