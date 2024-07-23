package com.thlink.webBot.ai.google.REST;

import com.thlink.webBot.webbot.manager.MacroParams;


public interface AIInterface 
{
    boolean startAI(MacroParams pAiParms);
    //boolean start(ChatParams pParams, boolean pTeste);
    String getResponseAI (String inputText);
}
