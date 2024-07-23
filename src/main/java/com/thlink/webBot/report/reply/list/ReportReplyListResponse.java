package com.thlink.webBot.report.reply.list;

import com.thlink.webBot.persistence.entities.tbLogConversation;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ReportReplyListResponse 
{
    private Integer errorCode;
    private String message;
    private Date startDate, finishDate;
    private ReportReplyStats stats;
    private List<tbLogConversation> conversations;
    
}
