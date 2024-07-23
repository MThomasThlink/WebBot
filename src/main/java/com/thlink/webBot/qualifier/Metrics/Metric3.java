package com.thlink.webBot.qualifier.Metrics;

import com.thlink.webBot.persistence.entities.tbPostConversationQuestions;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.qualifier.ConversationModel;
import static com.thlink.webBot.webbot.manager.InstanceManager.APP_HEADER;
import static com.thlink.webBot.webbot.manager.InstanceManager.APP_ID;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;


public class Metric3 
{
    private static final Logger logger = Logger.getLogger(Metric3.class);
    public Double evalMetric3 (ConversationModel pModel) 
    {
        logger.info("\tQualificator.evalMetric3");
        if (pModel.getConversation() == null)
        {
            logger.error("Sem conversa. Não fazer as pós-perguntas.");
            return -1D;
        }
        List<tbPostConversationQuestions> questions = pModel.getInstance().getPostConvQuestions();
        if (questions == null || questions.isEmpty())
        {
            logger.error("Erro na obtenção das perguntas idBot " + pModel.getIdConversation());
            return null;
        }
        double posProcResult = 0;
        for (tbPostConversationQuestions question: questions)
        {
            ChatMessage questionMessage = new ChatMessage();
            questionMessage.setIdConversation(pModel.getIdConversation());
            questionMessage.setFromMe(Boolean.TRUE);
            questionMessage.setFrom("5548991026166");
            //questionMessage.setPushName(chat.getPushName());
            questionMessage.setClientMessage(APP_HEADER.concat(question.getQuestion()));
            questionMessage.setAfterChat(Boolean.TRUE);
            questionMessage.setClientMoment(Calendar.getInstance().getTime());
            questionMessage = pModel.getChat().processMessage(questionMessage);  //faz a pergunta (e já salva no banco)
            if (questionMessage != null)
            {
                posProcResult += processPosQuestion(question ,questionMessage);  //analisa a resposta
            }
            else
                logger.error("Erro no envio da pergunta pósChat.");
        }
        return posProcResult;
        //PersAI.updateConversation(chat.getIdConversation(), null, null, null, null, qual3);
    }    
    
    private static double processPosQuestion (tbPostConversationQuestions question, ChatMessage questionMessage) 
    {
        int result = 0;
        try
        {
            String reply = questionMessage.getAiMessage().replace(APP_ID, "").replace(":", "").replace(".", "").trim();
            if (question.getType() != null)
            {
                switch (question.getType()) 
                {
                    case 1: //yes/no
                        if (booleanizeResponse(reply.toLowerCase()))
                        {
                            result += question.getWeight();
                          //logger.info("\t1: result = " + result);
                        }
                        break;

                    case 2: //numerico
                        try
                        {
                            Integer value = Integer.valueOf(reply);
                            result += (value * question.getWeight());
                          //logger.info("\t2: result = " + result);
                        } catch (Exception e)
                        {
                            logger.error("\tValor esperado era numérico: " + reply);
                        }
                        break;
                }       
            }
            return result;
        } catch (Exception e)
        {
            logger.error("Erro na avaliação da resposta.");
            return 0;
        }
    }
            
    private static boolean booleanizeResponse (String pText)
    {
        List<String> positives = Arrays.asList("ok", "true", "sim");
        for (String positive : positives)
            if (pText.contains(positive))
                return true;
        return false;
    }
}
