package com.thlink.webBot.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.Data;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "TBKEYWORDS")
public class tbKeyWords implements Serializable
{
    public tbKeyWords() {
    }

    public tbKeyWords(String word, int numericValue, tbInstances instance) {
        this.word = word;
        this.numericValue = numericValue;
        this.instance = instance;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String word;
    private int numericValue;

    @ManyToOne
    @JoinColumn(name = "instanceId")      //só aparece aqui (é o nome de campo)
    @JsonIgnore
    private tbInstances instance;
    
}
