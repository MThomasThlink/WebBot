package com.thlink.webBot.qualifier.Metrics;

import com.thlink.webBot.persistence.entities.tbKeyWords;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.qualifier.ConversationModel;
import java.util.List;
import org.apache.log4j.Logger;


//Esta métrica se basea na frequencia com que palavras chave aparecem na mensagens
public class Metric1 
{
    private static final Logger logger = Logger.getLogger(Metric1.class);
    
    public Double evalMetric1 (ConversationModel pModel) 
    {
        logger.info("\tQualificator.evalMetric1");
        try
        {
            String allTx = concatMessages(pModel);
            List<tbKeyWords> keyWords = pModel.getInstance().getKeyWords();
            int total = 0;
            for (tbKeyWords word : keyWords)
            {
                if (word.getNumericValue() != 0)
                    total += countOccurrences(allTx, word.getWord()) * word.getNumericValue();
            }
            logger.info("\tQualificator.evalMetric1: " + total);
            return Double.valueOf(total);
        } catch (Exception e)
        {
            return Double.valueOf(0);
        }
    }

    private String concatMessages (ConversationModel pModel) 
    {
        StringBuilder sb = new StringBuilder();
        for (tbLogConversationMessage message : pModel.getMessages())
        {
            if (message.getAfterChat() == null || !message.getAfterChat())
                sb.append(message.getClientText().concat(". "));
                //sb.append(message.getTxText().concat(""));
        }
        return sb.toString();
    }    
    
    /*private List<tbKeyWords> getMockListKeyWords() 
    {
        List<tbKeyWords> list = new ArrayList<>();
        list.add(new tbKeyWords("preço", 2));
        list.add(new tbKeyWords("horários", 1));
        list.add(new tbKeyWords("visita", 1));
        list.add(new tbKeyWords("agenda", 2));
        return list;
    }*/
    
    public static int countOccurrences (String str, String subStr) 
    {
        if (subStr == null || subStr.isEmpty()) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(subStr, idx)) != -1) {
            count++;
            idx += subStr.length();
        }
        return count;
    }
    
}
