package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationMessageRepository extends JpaRepository<tbLogConversationMessage, Long> {
}