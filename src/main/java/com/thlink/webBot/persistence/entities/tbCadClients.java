package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import lombok.Data;

@Data
@Entity
public class tbCadClients implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    
    private String name; 
    
    //@Lob
    //@Column(name = "active", columnDefinition = "VARBINARY(1)")
    private boolean active;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sinceDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date untilDate;
 
    @OneToMany(mappedBy = "cadClient", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbInstances> instances;
    
    @JsonIgnore
    private transient String errorMsg;
    
    public static String objToJSON (tbCadClients pCR)
    {
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").setPrettyPrinting().create();
            String resp = gson.toJson(pCR);
            return resp;
        } catch (Exception e)
        {
            System.out.println("objToJSON tbCadClients ERROR: " + e.toString());
            return "ERRO";
        }
    }
    public static String objToJSON (List<tbCadClients> pCR)
    {
        try
        {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").setPrettyPrinting().create();
            String resp = gson.toJson(pCR);
            return resp;
        } catch (Exception e)
        {
            System.out.println("objToJSON tbCadClients ERROR: " + e.toString());
            return "ERRO";
        }
    }
}
