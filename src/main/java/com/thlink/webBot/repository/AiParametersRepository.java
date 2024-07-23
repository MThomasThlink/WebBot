package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbAiParameters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiParametersRepository extends JpaRepository<tbAiParameters, Long> {
    
}
