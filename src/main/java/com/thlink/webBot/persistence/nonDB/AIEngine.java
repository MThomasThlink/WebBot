package com.thlink.webBot.persistence.nonDB;

import static com.thlink.webBot.persistence.nonDB.AIEngine.eAiEngines.GOOGLE;

public class AIEngine 
{
    public enum eAiEngines { GOOGLE };
    
    private eAiEngines engine;
    private GoogleAIParams googleAIParams;

    public AIEngine() {
        engine = GOOGLE;
        googleAIParams = new GoogleAIParams();
    }

    public AIEngine(eAiEngines engine) {
        this.engine = engine;
    }

    
    public eAiEngines getEngine() {
        return engine;
    }

    public void setEngine(eAiEngines engine) {
        this.engine = engine;
    }

    public GoogleAIParams getGoogleAIParams() {
        return googleAIParams;
    }

    public void setGoogleAIParams(GoogleAIParams googleAIParams) {
        this.googleAIParams = googleAIParams;
    }
 
    
}
