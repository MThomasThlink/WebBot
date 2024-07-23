package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;
import lombok.Data;

@Data
@Entity
public class tbLogChatManager implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;
    private Integer qtdContacts;
    private Integer reason;
    
    @ManyToOne
    @JoinColumn(name = "instanceId")
    @JsonIgnore
    private tbInstances instance;
    
}
