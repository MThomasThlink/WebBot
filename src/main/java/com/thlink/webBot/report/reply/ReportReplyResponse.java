package com.thlink.webBot.report.reply;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ReportReplyResponse {
    private Integer errorCode;
    private String message;
    private Date startDate, finishDate;
    private BufferedImage image;
    
    private List<ReducedConversations> conversations;
    
    public static String objToJSON (ReportReplyResponse pReportAnalitic)
    {
        Gson gson = new GsonBuilder(). setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pReportAnalitic);
      //System.out.println(resp);
        return resp;
    }
}
