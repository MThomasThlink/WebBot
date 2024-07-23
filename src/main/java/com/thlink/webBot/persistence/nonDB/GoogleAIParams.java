package com.thlink.webBot.persistence.nonDB;

public class GoogleAIParams 
{
    private String projectID;
    private String promptFileName;
    //private String credentials;
    private String apiKey;

    public GoogleAIParams() {
    }

    public GoogleAIParams(String projectID, String promptFileName, /*String credentials,*/ String apiKey) {
        this.projectID = projectID;
        this.promptFileName = promptFileName;
        //this.credentials = credentials;
        this.apiKey = apiKey;
    }
    
    public String getPromptFileName() {
        return promptFileName;
    }

    public void setPromptFileName(String promptFileName) {
        this.promptFileName = promptFileName;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    
}
