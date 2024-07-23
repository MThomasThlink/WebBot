package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.repository.ClientsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service 
public class ClientService 
{
    private static final Logger logger = Logger.getLogger(ClientService.class);
    private final ClientsRepository repo;
     
    @PersistenceContext
    private EntityManager entityManager;
    
    public ClientService (ClientsRepository pRepo) 
    {
         this.repo = pRepo; 
    }
    
    
    @Transactional
    public int deleteViaEMWithWhere (String pWhereClause) {
        return entityManager.createQuery("DELETE FROM tbCadClients e " + pWhereClause)
                     //.setParameter("someField", someFieldValue)
                     .executeUpdate();
    }
    
    @Transactional
    public int deleteViaRepo (String pName) {
        return repo.deleteByName(pName);
    }
    
    @Transactional
    public int activateViaEM (Long pIdClient, boolean pActive) {
        return entityManager.createQuery("UPDATE tbCadClients e " + 
                                         "SET    e.active = :pActive " + 
                                         "WHERE  e.id = :pIdClient ")
                     .setParameter("pActive", pActive)
                     .setParameter("pIdClient", pIdClient)
                     .executeUpdate();
    }
    
    
    public List<tbCadClients> getAllEntities () {
      /*List<tbCadClients> l = repo.findAll();
        logger.info("ClientService.getAllEntities: ");
        for (tbCadClients cli : l)
        {
            logger.info("Cli name: " + cli.getName());
        }*/
       return repo.findAll();
    }
    public List<tbCadClients> getActives () 
    {
        List<tbCadClients> result = new ArrayList<>();
        List<tbCadClients> l = getAllEntities();
        logger.info("ClientService.getActives: ");
        Date now = Calendar.getInstance().getTime();
        logger.info("l.size = " + l.size());
        
        for (tbCadClients cli : l)
        {
            logger.info("Cli name: " + cli.getName());
            logger.info("Compare with since: " + now.compareTo(cli.getSinceDate()));
            logger.info("Compare with until: " + now.compareTo(cli.getUntilDate()));
            
            if (cli.isActive())
            {
                if (cli.getSinceDate() == null || now.compareTo(cli.getSinceDate()) > 0)
                {
                    if (cli.getUntilDate() == null || now.compareTo(cli.getUntilDate()) < 0)
                    {
                        logger.info("ADD Cli: ");
                        result.add(cli);
                    }
                }
            }
        }
        logger.info("returning size = " + result.size());
       return result;
    }
    
    public tbCadClients saveObj (tbCadClients pObj) {
        return repo.save(pObj);
    }
}
