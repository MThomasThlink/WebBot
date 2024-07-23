package com.thlink.webBot.persistence.nonDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ParamsHelper 
{
    private static final String CONFIG_FILE = "./Config/SQL_AIBOTParams.xml";
  //private static final String CONFIG_FILE = "./Config/DERBY_AIBOTParams.xml";
  //private static final String CONFIG_FILE = "./Config/SQLITE_AIBOTParams.xml";

    public static ChatParams getFileParams ()
    {
        return getFileParams(CONFIG_FILE);
    }
    public static ChatParams getFileParams (String pFile)
    {
        File fTest = new File(pFile);
        if (fTest.exists())
        {
            try
            {
                ObjectMapper mapper = new XmlMapper();
                InputStream inputStream = new FileInputStream(pFile);
                TypeReference<ChatParams> typeReference = new TypeReference<ChatParams>() {};
                ChatParams params = mapper.readValue(inputStream, typeReference);
                
                params.setVersion(new VersionHelper().getAIBotVersion());
                return params;
            } catch (Exception e)
            {
                System.out.println("getFileParams ERRO: " + e.toString());
                return null;
            }
        }
        else
        {
            //ChatParams params = getMockParams();
            //return saveFileParams(params);
            return null;
        }
    }
    
    public static ChatParams saveFileParams (ChatParams pParams)
    {
        return saveFileParams(pParams, CONFIG_FILE);
    }
    public static ChatParams saveFileParams (ChatParams pParams, String pFile)
    {
        try
        {
            ObjectMapper mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File(pFile), pParams);
            return pParams;
        } catch (Exception e)
        {
            System.out.println("ERRO: " + e.toString());
        }
        return null;
    }    
    
    static class VersionHelper 
    {
        public String getAIBotVersion () 
        {
            String v = this.getClass().getPackage().getSpecificationVersion();
            if (v != null)
                return v;
            else 
                return "AB.CD";
        }
    }
}
