
package com.thlink.webBot.sender.Evolution.program.createInstance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CreateInstanceBody {

    @SerializedName("instanceName")
    @Expose
    private String instanceName;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("qrcode")
    @Expose
    private Boolean qrcode;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getQrcode() {
        return qrcode;
    }

    public void setQrcode(Boolean qrcode) {
        this.qrcode = qrcode;
    }

    public static String objToJSON (CreateInstanceBody pRT)
    {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().create();
        String resp = gson.toJson(pRT);
      //System.out.println(resp);
        return resp;
    }
}
