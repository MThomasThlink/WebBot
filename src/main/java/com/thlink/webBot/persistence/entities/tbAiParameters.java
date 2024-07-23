package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class tbAiParameters implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name, projectId, model, apiKey;
    
    @Column(length = 10000)
    private String instructions;
    
    @OneToMany(mappedBy = "aiParameters", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  //@JsonIgnore
    private List<tbAiQuestionAndAnswers> questions;
    
    @OneToOne
    @JoinColumn(name = "instanceId")      //só aparece aqu (é o nome de campo)
    @JsonIgnore
    private tbInstances instance;
    
}

