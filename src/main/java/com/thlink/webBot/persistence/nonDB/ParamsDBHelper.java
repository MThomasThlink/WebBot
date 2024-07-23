package com.thlink.webBot.persistence.nonDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamsDBHelper 
{
    private static final Logger logger = LoggerFactory.getLogger(ParamsDBHelper.class); 
    public ChatParams load ()
    {
        logger.info("ParamsDBHelper.load");
        try
        {
            ChatParams p = new ChatParams();
            p.setAiEngine(new AIEngine());

            p.setVersion(getAIBotVersion());
            return p;
        } catch (Exception e)
        {
            logger.error("ParamsDBHelper.load ERROR: " + e.toString());
            return null;
        }
    }
    
    
    public String getAIBotVersion () 
    {
        String v = this.getClass().getPackage().getSpecificationVersion();
        if (v != null)
            return v;
        else 
            return "AB.CD";
    }
            
}
