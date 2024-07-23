package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thlink.webBot.webbot.manager.InstanceManager;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class tbInstances implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type; 
    private String description, instanceName;
    private boolean active;
    private Boolean answerMyself, showDashBoard;
    
    //@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    //@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sinceDate;
    //@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date untilDate;
    private Integer tempoNovaConversaH;
    private Integer managerIntervalS, maxIdleTimeS, maxDurationS;
    private String evolApiKey, evolGlobalKey;
    
    @Column(length = 10000)
    private String qrCodeBase64;
    
    @ManyToOne
    @JoinColumn(name = "clientId")      //só aparece aqu (é o nome de campo)
    @JsonIgnore
    private tbCadClients cadClient;
    
    @OneToOne(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private tbAiParameters aiParameters;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbKeyWords> keyWords;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  //@JsonIgnore
    private List<tbLogChatManager> logChatManager;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  //@JsonIgnore
    private List<tbLogConversation> logConversation;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbPostConversationQuestions> postConvQuestions;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbAcceptedTelephones> accepted;
    
    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbBlockedTelephones> blocked;
    

    @JsonIgnore
    private transient String errorMsg;
    
    //Special Functions
    public boolean evaluateDates ()
    {
        if (getSinceDate() != null)
            if (Calendar.getInstance().getTimeInMillis() < getSinceDate().getTime())
                    return false;
        if (getUntilDate() != null)
            if (Calendar.getInstance().getTimeInMillis() > getUntilDate().getTime())
                return false;
        return true;
    }
    
    public String getTypeDescription ()
    {
        return switch (type) {
            case 1 -> "AI_CHAT";
            case 3 -> "SPY";
            case 4 -> "AI_SPY";
            default -> "Desconhecido";
        };
    }

    public boolean isAI  () { return type == InstanceManager.eOPER.AI_CHAT.value; }
    public boolean isSPY () { return type == InstanceManager.eOPER.SPY.value; }
    
    //
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof tbInstances)) {
            return false;
        }
        tbInstances other = (tbInstances) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.thlink.webBot.persistence.entities.tbInstances[ id=" + id + " ]";
    }
    
}
