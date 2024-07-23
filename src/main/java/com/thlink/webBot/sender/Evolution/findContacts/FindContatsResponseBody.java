package com.thlink.webBot.sender.Evolution.findContacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thlink.webBot.persistence.entities.tbContacts;
import static com.thlink.webBot.webbot.manager.InstanceManager.WHATSAPP_SUFIX;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class FindContatsResponseBody 
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pushName")
    @Expose
    private String pushName;
    @SerializedName("profilePictureUrl")
    @Expose
    private Object profilePictureUrl;
    @SerializedName("owner")
    @Expose
    private String owner;

    public static List<tbContacts> convertContacts (List<FindContatsResponseBody> pFindContacts)
    {
        List<tbContacts> list = new ArrayList<>();
        for (FindContatsResponseBody findContact : pFindContacts)
        {
            tbContacts dbContact = new tbContacts();
            dbContact.setId(findContact.getId());
            dbContact.setOwner(findContact.getOwner());
            if (findContact.getProfilePictureUrl() != null)
                dbContact.setProfilePictureUrl(findContact.getProfilePictureUrl().toString());
            dbContact.setPushName(findContact.getPushName());
            list.add(dbContact);
        }
        return list;
    }
    
    public static FindContatsResponseBody jsonToObj (String pJSON) 
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().create();
            FindContatsResponseBody p = gson.fromJson(pJSON, FindContatsResponseBody.class);
            p.setId(p.getId().replace(WHATSAPP_SUFIX, ""));
            return p;
        } catch (Exception e)
        {
            System.out.printf("jsonToObj FindContatsResponseBody ERROR: %s. \n", e.toString());
            return null;
        }
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    public Object getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(Object profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
}
