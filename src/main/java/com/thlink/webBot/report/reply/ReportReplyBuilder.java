package com.thlink.webBot.report.reply;
/*
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.service.ClientService;
import com.thlink.webBot.service.GeneralService;
import com.thlink.webBot.service.InstanceService;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


public class ReportReplyBuilder {

    @Autowired
    private final GeneralService generalSrv;
    private final ClientService clientSrv;
    private final InstanceService instanceSrv;

    public ReportReplyBuilder(GeneralService generalSrv, ClientService clientSrv, InstanceService instanceSrv) {
        this.generalSrv = generalSrv;
        this.clientSrv = clientSrv;
        this.instanceSrv = instanceSrv;
    }
    
    private List<ReducedConversations> getListReducedConversation (tbInstances instance, Boolean onlyFirst, Date startDate, Date finishDate, Integer maxRegs) 
    {
        
    }
    
    public ReportReplyResponse buildReplyReport (ReportReplyParameters p) 
    {
        ReportReplyResponse repo = new ReportReplyResponse();
        //Recupera instância
        tbInstances instance = instanceSrv.getById(p.idInstance); 
        if (instance == null)
        {
            repo.setMessage("Instância inexistente.");
            return repo;
        }
        if (!instance.isActive())
        {
            repo.setMessage("Instância inativa.");
            return repo;
        }
        
        //Recupera a lista de mensagens
        
        List<ReducedConversations> redConvs =  getListReducedConversation(instance, p.onlyFirst, p.startDate, p.finishDate, p.maxRegs);
        if (p.type.equals("L"))
        {
            repo.setConversations(redConvs);
        }
        else if (p.type.equals("G"))
        {
            double[] values = calculateArray(instance, redConvs);
            ProbProcessor prob = new ProbProcessor("Histograma");
            prob.setValues(values);
            prob.setNumberOfBins(p.qtdIntervals);
            prob.go();
            double average = prob.calculateAverage(values);
            double mean = prob.calculateMean(values);
            BufferedImage img = null;
            if (p.forma.equals("1"))
                img = prob.getBufferedImage(); 
            else if (p.forma.equals("2"))
                img = prob.getBufferedImageAvgMean(average, mean); 
          //prob.saveBufferedImage(img, "C:\\WorkDir\\AIBOT\\HistoMean", "png");
            repo.setImage(img);        
        }
        return repo;
        
    }
    private static double[] calculateArray (tbInstances instance, List<ReducedConversations> pList)
    {
        List<Long> values = new ArrayList<>();
        int i = 0;
        //Calcula tempo em MINUTOS!
        for (ReducedConversations conv : pList)
        {
            for (ReducedMessages message : conv.getMessages())
            {
                if (message.getReplyMoment() != null && message.getClientMoment() != null)
                {
                    values.add(Duration.between(message.getClientMoment().toInstant(), message.getReplyMoment().toInstant()).toMinutes());
                  //System.out.println(values[i]);
                    i++;
                }
                else
                    System.out.println("SPY !!!");
            }
        }
        double[] newArray = new double[i];
        for (int j = 0; j < values.size(); j++) {
            newArray[j] = values.get(j).doubleValue();
        }

        return newArray;
    }
}
*/