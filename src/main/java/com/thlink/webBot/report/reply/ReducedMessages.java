package com.thlink.webBot.report.reply;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.Data;

@Data
public class ReducedMessages {
    private String clientText, responseText;
    private String tipo;
    private Date clientMoment, replyMoment;
    @JsonIgnore 
    private Long delaySeconds;
    private String delay;

    public void setDelaySeconds(Long delaySeconds) {
        this.delaySeconds = delaySeconds;
        if (delaySeconds < 60)
            delay = String.format("%d seconds.", delaySeconds);
        else if (delaySeconds < 3600)
            delay = String.format("%5.2f minutes.", ((double)delaySeconds) / 60);
        else
            delay = String.format("%5.2f hours.", ((double)delaySeconds) / 3600);
        
    }
}
