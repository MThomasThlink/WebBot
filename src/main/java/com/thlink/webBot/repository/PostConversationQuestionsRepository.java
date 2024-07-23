package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbPostConversationQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostConversationQuestionsRepository extends JpaRepository<tbPostConversationQuestions, Long> {
}

