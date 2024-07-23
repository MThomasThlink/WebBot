package com.thlink.webBot.repository;

import com.thlink.webBot.persistence.entities.tbCadClients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientsRepository extends JpaRepository<tbCadClients, Long> 
{
    @Modifying
    @Query("DELETE FROM tbCadClients e WHERE e.name = :pName")
    int deleteByName(@Param("pName") String pName);
    
}
