package com.thlink.webBot.qualifier;

import lombok.Data;

@Data
public class Client {
    private String phoneNumer, pushName, userName;

    @Override 
    public String toString ()
    {
        return String.format("[%s] PushName: %s, User: %s.", phoneNumer, pushName, userName);
    }
}
