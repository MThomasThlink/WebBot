package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbKeyWords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyWordsRepository extends JpaRepository<tbKeyWords, Long>
{
    
}
