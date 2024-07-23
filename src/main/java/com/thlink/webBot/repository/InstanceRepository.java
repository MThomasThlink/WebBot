package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbInstances;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepository extends JpaRepository<tbInstances, Long> {
}
