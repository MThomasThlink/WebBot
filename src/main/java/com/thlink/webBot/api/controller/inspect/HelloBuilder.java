package com.thlink.webBot.api.controller.inspect;

import java.util.Calendar;

public class HelloBuilder 
{
    public HelloResponse answerHello (String pServer, String pVersion)
    {
        long tIni = Calendar.getInstance().getTimeInMillis();
        HelloResponse hr = new HelloResponse(pServer);
        hr.setVersao(pVersion);
        hr.setMensagem("OK");
        hr.setTempoMs(Calendar.getInstance().getTimeInMillis() - tIni);
        //String response = HelloResponse.helloToJSON(hr);
        return hr;
    }
}
