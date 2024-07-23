package com.thlink.webBot.sender.Evolution.program.fetchInstance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thlink.webBot.sender.Evolution.program.createInstance.Instance;

@Generated("jsonschema2pojo")
public class FetchInstanceResponse {

    
    @SerializedName("instance")
    @Expose
    private Instance instance;

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public static FetchInstanceResponse jsonToObj(String pJSON) 
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            FetchInstanceResponse p = gson.fromJson(pJSON, FetchInstanceResponse.class);
            return p;
        } catch (Exception e)
        {
            System.out.printf("jsonToObj FetchInstanceResponse ERROR: %s. \n", e.toString());
            return null;
        }
    }

}
