package com.thlink.webBot.api.controller.inspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

@Data
public class HelloResponse {
    private String servidor;
    private String mensagem;
    private Long tempoMs;
    private Boolean conexaoBanco, https;
    private String dataHora;
    private String versao;
    private Double nichts;

    public HelloResponse (String pServidor)
    {
        this.servidor = pServidor;
        this.https = false;
    }
    public HelloResponse (String pServidor, boolean pHTTPs)
    {
        this.servidor = pServidor;
        this.https = pHTTPs;
    }
            
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getTempoMs() {
        return tempoMs;
    }

    public void setTempoMs(Long tempoMs) {
        this.tempoMs = tempoMs;
    }

    public Boolean getConexaoBanco() {
        return conexaoBanco;
    }

    public void setConexaoBanco(Boolean conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    
    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }
    
    
    public static String helloToJSON (HelloResponse pMsg)
    {
        try
        {
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            String resp = gson.toJson(pMsg);
            return resp;   
        } catch (Exception e)
        {
            System.out.println("helloToJSON ERROR: " + e.toString());
            return null;
        }
    }

    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }
    
    
    /*public static HelloResponse jsonToHello (String pJSON)
    {
        try
            {
                Gson gson = new GsonBuilder().serializeNulls().create();
                HelloResponse p = gson.fromJson(pJSON, HelloResponse.class);
                return p;
            } catch (Exception e)
            {
                System.out.printf("jsonToHello ERROR: %s. \n", e.toString());
                return null;
            }
    }*/

}
