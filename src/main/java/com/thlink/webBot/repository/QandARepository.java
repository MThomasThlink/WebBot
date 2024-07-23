package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbAiQuestionAndAnswers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QandARepository extends JpaRepository<tbAiQuestionAndAnswers, Long> {    
}
