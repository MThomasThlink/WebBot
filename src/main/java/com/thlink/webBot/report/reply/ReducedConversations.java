package com.thlink.webBot.report.reply;

import java.util.List;
import lombok.Data;

@Data
public class ReducedConversations {
    private String pushName, userNumber;
    private List<ReducedMessages> messages;
}
