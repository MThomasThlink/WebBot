package com.thlink.webBot.report;

import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.report.reply.ProbProcessor;
import com.thlink.webBot.report.reply.ReportReplyParameters;
import com.thlink.webBot.report.reply.graph.ReportReplyGraphResponse;
import com.thlink.webBot.report.reply.list.ReportReplyListResponse;
import com.thlink.webBot.report.reply.list.ReportReplyStats;
import com.thlink.webBot.service.GeneralService;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class ReportBuilder
{

    private static final Logger logger = Logger.getLogger(GeneralService.class);
    private final GeneralService generalService;

    public ReportBuilder(GeneralService generalService) {
        this.generalService = generalService;
    }
 
    //Este relatório usa as conversas da instância no intervalo de datas fornecido
    public ReportReplyListResponse listReportBuild (ReportReplyParameters p) 
    {
        logger.info("ReportBuilder.listReportBuild");
        ReportReplyListResponse response = new ReportReplyListResponse();
        try
        {
            List<tbLogConversation> conversations = generalService.listConverstionsFromInstanceAndDates(p.idInstance, p.startDate, p.finishDate);
            if (conversations == null)
            {
                response.setMessage("Erro ao recuperar as mensagens");
                return response;
            }
            logger.info("conversations.size() = " + conversations.size());
            if (p.onlyFirst)
            {
                //Filter conversations, and keep only the first one on each
                conversations = keepOnlyFirstMessage(conversations);
            }
            ReportReplyStats stats = calculateStats(conversations);
            response.setStats(stats);
            response.setStartDate(p.startDate);
            response.setFinishDate(p.finishDate);
            response.setConversations(conversations);
            return response;
        } catch (Exception e)
        {
            logger.info("ReportBuilder.listReportBuild ERROR: " + e.toString());
            response.setMessage(e.toString());
            return response;
        }
        finally
        {
            
        }
        
    }
    
    private ReportReplyStats calculateStats (List<tbLogConversation> conversations) 
    {
         if (conversations == null || conversations.isEmpty())
             return null;
         
        List<Long> listDelaysS = new ArrayList<>();
        Double accumulatorS = 0d;
        Long smallest = Long.MAX_VALUE, highest = Long.MIN_VALUE;
        int ctdSamples = 0;
        double variance = 0.0;
        for (tbLogConversation conv : conversations)
        {
            if (conv == null)
                continue;
                    
            List<tbLogConversationMessage> msgs =  conv.getLogConversationMessage();
            for (tbLogConversationMessage msg : msgs)
            {
                if (msg.getClientMoment() != null)
                {
                    Long delayS;
                    if (msg.getAiMoment() != null)
                    {
                        delayS = (msg.getAiMoment().toInstant().toEpochMilli() - msg.getClientMoment().toInstant().toEpochMilli()) / 1000;
                    }
                    else if (msg.getManualMoment() != null)
                    {
                        delayS = (msg.getManualMoment().toInstant().toEpochMilli() - msg.getClientMoment().toInstant().toEpochMilli()) / 1000;
                    }
                    else
                        continue;
                    ctdSamples++;
                    listDelaysS.add(delayS);
                    msg.setDelayS(delayS);
                    if (delayS < smallest)
                        smallest = delayS;
                    else if (delayS > highest)
                        highest = delayS;
                    accumulatorS += delayS;
                    
                    //logger.info(String.format("delayS = %d", delayS));
                }
            }
        }

        ReportReplyStats stats = new ReportReplyStats();
        stats.setAverageS(accumulatorS / ctdSamples);
        stats.setAverageMin(Math.round((stats.getAverageS()/60)    * 100.0) / 100.0);
        stats.setAverageHour(Math.round((stats.getAverageS()/3600) * 100.0) / 100.0);
        stats.setSmallest(smallest);
        stats.setHighest(highest);

        //Com a média calculada, passar novamente para calcular o desvio.
        double[] doubleArray = new double[listDelaysS.size()];
        int i = 0;
        for (Long delayS : listDelaysS)
        {
            variance += Math.pow(delayS - stats.getAverageS(), 2);
            doubleArray[i++] = (double)delayS;
        }
        stats.setValues(doubleArray);
        variance /= ctdSamples;
        Double stdDev = Math.round((Math.sqrt(variance)) * 100.0) / 100.0;
        stats.setStdDev(stdDev);

        return stats;
    }
    
    public static double calculateStandardDeviation(double[] numbers) {
        // Step 1: Calculate the mean (average)
        double sum = 0.0;
        for (double num : numbers) {
            sum += num;
        }
        double mean = sum / numbers.length;

        // Step 2: Calculate the variance
        double variance = 0.0;
        for (double num : numbers) {
            variance += Math.pow(num - mean, 2);
        }
        variance /= numbers.length;

        // Step 3: Calculate the standard deviation (square root of variance)
        double standardDeviation = Math.sqrt(variance);

        return standardDeviation;
    }
    
    private List<tbLogConversation> keepOnlyFirstMessage (List<tbLogConversation> conversations) 
    {
        for (tbLogConversation conv : conversations)
        {
            List<tbLogConversationMessage> msgs =  conv.getLogConversationMessage();
            if (msgs != null && !msgs.isEmpty())
            {
                tbLogConversationMessage first = msgs.get(0);
                msgs.removeIf(item -> !item.equals(first));
            }

        }
        return conversations;
    }

    public ReportReplyGraphResponse graphReportBuild (ReportReplyParameters p) 
    {
        logger.info("ReportBuilder.graphReportBuild");
        ReportReplyGraphResponse response = new ReportReplyGraphResponse();
        try
        {
            List<tbLogConversation> conversations = generalService.listConverstionsFromInstanceAndDates(p.idInstance, p.startDate, p.finishDate);
            if (conversations == null)
            {
                response.setMessage("Erro ao recuperar as mensagens");
                return response;
            }
            logger.info("conversations.size() = " + conversations.size());
            if (p.onlyFirst)
            {
                //Filter conversations, and keep only the first one on each
                conversations = keepOnlyFirstMessage(conversations);
            }
            
            //Obter um array de dados para traçar o Histograma.
            logger.info("graphReportBuild calculateStats");
            ReportReplyStats stats = calculateStats(conversations);
            ProbProcessor prob = new ProbProcessor("Histograma");
            prob.setNumberOfBins(20);
            
            prob.setValues(stats.getValues());
            prob.go();
            BufferedImage img;
            if (p.type.equals("1"))
                img = prob.getBufferedImage(); 
            else
            {
                double average = prob.calculateAverage(stats.getValues());
                double mean = prob.calculateMean(stats.getValues());            
                img = prob.getBufferedImageAvgMean(average, mean); 
            }
            response.setImage(img);
            return response;
        } catch (Exception e)
        {
            logger.info("ReportBuilder.graphReportBuild ERROR: " + e.toString());
            response.setMessage(e.toString());
            return response;
        }
        finally
        {
            
        }
    }
}