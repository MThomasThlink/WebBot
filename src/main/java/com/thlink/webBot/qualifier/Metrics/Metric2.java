package com.thlink.webBot.qualifier.Metrics;

import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.qualifier.ConversationModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;


//Métrica que avalia a conversa baseado no tempo e na quantidade de palavras que o usuário digitou.
public class Metric2 
{
    private static final Logger logger = Logger.getLogger(Metric2.class);

    public Double evalMetric2 (ConversationModel pModel) 
    {
        logger.info("\tQualificator.evalMetric2");
        int numWords = 0;
        List<Double> listResults = new ArrayList<>();
        try
        {
            {
                if (pModel.getConversation().getStartTime() != null && pModel.getConversation().getFinishTime() != null && (numWords = getNumberWordsInConversation(pModel, pModel.getConversation().getId())) > 0)
                {
                    long elapsedS = (pModel.getConversation().getFinishTime().getTime() - pModel.getConversation().getStartTime().getTime()) / 1000;
                    listResults.add((double)(numWords*10) / elapsedS);
                }
            }
            
            if (listResults.isEmpty())
                return 0D;
            
            logger.info("\tQualificator.evalMetric2: " + meanResults(listResults));
            return meanResults(listResults);
            
        } catch (Exception e)
        {
            logger.error("\tQualificator.evalMetric2 ERROR: " + e.toString());
            return Double.valueOf(0);
        }
    }
    
    private Double meanResults(List<Double> listResults) 
    {
        Double acc = 0D;
        for (Double d : listResults)
            acc += d;
        
        return acc / listResults.size();
    }
    
    private int getNumberWordsInConversation (ConversationModel pModel, Long id) 
    {
        StringBuilder sb = new StringBuilder();
        for (tbLogConversationMessage message : pModel.getMessages())
        {
            if (Objects.equals(message.getId(), id))
            {
                sb.append(message.getClientText());
            }
        }
        if (sb.toString().isEmpty())
            return 0;
        String[] words = sb.toString().split(" ");
        return words.length;       
    }
}
