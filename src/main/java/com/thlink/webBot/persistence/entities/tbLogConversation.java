package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import lombok.Data;

@Data
@Entity
public class tbLogConversation implements Serializable
{
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String userNumber, userName;
    private String pushName;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date finishTime;
    @JsonIgnore
    private Integer status, qtdIterations;
    @JsonIgnore
    private Double scoreM1, scoreM2, scoreM3;
    @JsonIgnore
    private Boolean isContact, startedByMe;
    
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbLogConversationMessage> logConversationMessage;
    
    @ManyToOne
    @JoinColumn(name = "instanceId")
    @JsonIgnore
    private tbInstances instance;
    
}
