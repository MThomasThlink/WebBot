package com.thlink.webBot.persistence.nonDB;

public class Messsaging 
{
    public enum MSG_SENDER { EVOLUTION, META };
    
    private MSG_SENDER sender;
    private EvolutionParams evolutionParams;

    public Messsaging() {
    }

    public Messsaging(MSG_SENDER sender) {
        this.sender = sender;
    }
    
    public MSG_SENDER getSender() {
        return sender;
    }

    public void setSender(MSG_SENDER sender) {
        this.sender = sender;
    }

    public EvolutionParams getEvolutionParams() {
        return evolutionParams;
    }

    public void setEvolutionParams(EvolutionParams evolutionParams) {
        this.evolutionParams = evolutionParams;
    }
}
