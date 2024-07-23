package com.thlink.webBot.service;

import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.repository.InstanceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InstanceService {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InstanceService.class);

    private final InstanceRepository repo;
     
    public InstanceService (InstanceRepository pRepo) 
    {
         this.repo = pRepo; 
    }    
    public List<tbInstances> getAllEntities() {
        logger.info("InstanceService.getAllEntities: " + repo.findAll());
        for (tbInstances inst : repo.findAll())
        {
            logger.info("Inst name: " + inst.getDescription());
        }
       return repo.findAll();
    }
    
    public tbInstances getById (Long pId)
    {
        return repo.findById(pId).get();
    }
    public tbInstances saveObj (tbInstances pObj) {
        return repo.saveAndFlush(pObj);
    }

}
