package com.thlink.webBot.ai.google.REST.JSON;

import com.thlink.webBot.sender.Evolution.findContacts.FindContatsResponseBody;
import java.util.List;

public class ReqAnsw2 
{
    private Integer responseCode;
    private String erroMessage;
    private List<FindContatsResponseBody> listContacts;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<FindContatsResponseBody> getListContacts() {
        return listContacts;
    }

    public void setListContacts(List<FindContatsResponseBody> listContacts) {
        this.listContacts = listContacts;
    }

    public String getErroMessage() {
        return erroMessage;
    }

    public void setErroMessage(String erroMessage) {
        this.erroMessage = erroMessage;
    }

   
}

