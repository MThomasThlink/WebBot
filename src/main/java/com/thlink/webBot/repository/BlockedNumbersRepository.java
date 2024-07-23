package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbBlockedTelephones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedNumbersRepository extends JpaRepository<tbBlockedTelephones, Long>{
}
