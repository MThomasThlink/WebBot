package com.thlink.webBot.report.reply;

import java.util.Date;
import lombok.Data;

@Data
public class ReportReplyParameters {
    public Long idInstance;
    public String type, forma;

    public Date startDate, finishDate;
    public Boolean onlyFirst;
    public Integer qtdIntervals;
    public Integer maxRegs;
    
    public static String dumpParameters (ReportReplyParameters p) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Parâmetros de ReportReply: \n");
        sb.append("Obrigatórios: \n");
        sb.append(String.format("\tidInstancia = %s\n", p.idInstance));
        if (p.type != null)
        {
            sb.append(String.format("\ttipo = %s\n", p.type));
            if (p.type.equals("G"))
                sb.append(String.format("\tforma = %s\n", p.forma));
        }
        sb.append("Opcionais: \n");
        sb.append(String.format("\tstartDate = %s\n", p.startDate));
        sb.append(String.format("\tfinishDate = %s\n", p.finishDate));
        sb.append(String.format("\tonlyfirst = %s\n", p.onlyFirst));
        sb.append(String.format("\tmaxRegs = %s\n", p.maxRegs));
        return sb.toString();
    }
    
}
