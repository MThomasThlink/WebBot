package com.thlink.webBot.webbot.manager;


import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerateContentBody;
import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import lombok.Data;

@Data
public class MacroParams 
{
    private int evolutionPort;
    private String evolutionServer;
    private tbCadClients client;
    private tbInstances instance;
    private ChatMessage message;
    private GenerateContentBody generateBody;

    public MacroParams() {
    }

    public MacroParams(int evolutionPort, String evolutionServer, tbCadClients client, tbInstances instance) {
        this.evolutionPort = evolutionPort;
        this.evolutionServer = evolutionServer;
        this.client = client;
        this.instance = instance;
    }

    
}
