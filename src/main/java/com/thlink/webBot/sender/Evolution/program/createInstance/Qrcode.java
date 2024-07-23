
package com.thlink.webBot.sender.Evolution.program.createInstance;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Qrcode {

    @SerializedName("pairingCode")
    @Expose
    private Object pairingCode;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("base64")
    @Expose
    private String base64;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Object getPairingCode() {
        return pairingCode;
    }

    public void setPairingCode(Object pairingCode) {
        this.pairingCode = pairingCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
