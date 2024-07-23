package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbAiParameters;
import com.thlink.webBot.repository.AiParametersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AiParametersService {
    private final Logger logger = LoggerFactory.getLogger(AiParametersService.class); 
    private final AiParametersRepository repo;
     
    @PersistenceContext
    private EntityManager entityManager;
    
    public AiParametersService (AiParametersRepository pRepo) 
    {
         this.repo = pRepo; 
    }    
    public List<tbAiParameters> getAllEntities() {
        logger.info("AiParametersService.getAllEntities: " + repo.findAll());
        for (tbAiParameters params : repo.findAll())
        {
            logger.info("Params name: " + params.getName());
        }
       return repo.findAll();
    }
    public tbAiParameters saveObj (tbAiParameters pObj) {
        return repo.save(pObj);
    }
    public tbAiParameters getAiParametersFromInstance (Long pIdInstance) 
    {
        tbAiParameters x = (tbAiParameters)
        entityManager.createQuery("SELECT   C FROM tbAiParameters C " +
                                  "JOIN     C.instance I " + 
                                  "WHERE    I.id = :pInstance " +
                                  "ORDER    BY C.id ")
                     .setParameter("pInstance", pIdInstance)
                     .getSingleResult();
        
        return x;
    }

}
