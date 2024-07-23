package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbLogConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ConversationRepository extends JpaRepository<tbLogConversation, Long> {
    @Query("SELECT c FROM tbLogConversation c WHERE c.userNumber = :pUserNumber ORDER BY c.id DESC")
    tbLogConversation getLastConversationWithThisPerson (@Param("pUserNumber") String pUserNumber);
}