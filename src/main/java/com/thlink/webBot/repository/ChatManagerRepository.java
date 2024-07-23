package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbLogChatManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatManagerRepository extends JpaRepository<tbLogChatManager, Long> {
    
}
