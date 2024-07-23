package com.thlink.webBot.persistence.nonDB;

public class EvolutionParams 
{
    private String instanceName, apiKey, createInstanceKey, primaryNumber, server;
    private Integer port;

    public EvolutionParams() {
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPrimaryNumber() {
        return primaryNumber;
    }

    public void setPrimaryNumber(String primaryNumber) {
        this.primaryNumber = primaryNumber;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getCreateInstanceKey() {
        return createInstanceKey;
    }

    public void setCreateInstanceKey(String createInstanceKey) {
        this.createInstanceKey = createInstanceKey;
    }

    
}
