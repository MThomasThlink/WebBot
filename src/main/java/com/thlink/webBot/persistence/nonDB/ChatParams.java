package com.thlink.webBot.persistence.nonDB;

import jakarta.xml.bind.annotation.XmlRootElement;
//import org.springframework.boot.web.server.WebServer;

@XmlRootElement
public class ChatParams 
{
    private transient String version;
    private Messsaging messaging;
    private AIEngine aiEngine;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Messsaging getMessaging() {
        return messaging;
    }

    public void setMessaging(Messsaging messaging) {
        this.messaging = messaging;
    }

    public AIEngine getAiEngine() {
        return aiEngine;
    }

    public void setAiEngine(AIEngine aiEngine) {
        this.aiEngine = aiEngine;
    }

            
}
