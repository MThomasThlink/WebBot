package com.thlink.webBot.sender.Evolution.findContacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class FindContatsRequestBody {

    @SerializedName("where")
    @Expose
    private Where where;

    public static String objToJSON (FindContatsRequestBody pRT)
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }
    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

}
