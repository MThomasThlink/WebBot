package com.thlink.webBot.webbot.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;


public class ChatMap implements Serializable
{
    private static final long serialVersionUID = 10000;
    private static final Logger logger = Logger.getLogger(ChatMap.class);
    private static final String CHAT_MAP_FILE = "./Config/map.mem";
    private static ConcurrentHashMap<String, ChatManager> ccHMChat = new ConcurrentHashMap<>();

    public ChatMap() 
    {
        ccHMChat = new ConcurrentHashMap<>();
    }

    
    public ChatManager getChat (String pNumber)
    {
        return ccHMChat.get(pNumber);
    }
    
    public ChatManager putChat (String pNumber, ChatManager pChat)
    {
        return ccHMChat.put(pNumber, pChat);
    }
    
    public static boolean saveChatMap (ChatMap pThis)
    {
        logger.info("Salvando mapa de conversas...");
        try
        {
            FileOutputStream fileOut = new FileOutputStream(CHAT_MAP_FILE);
            try (ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                objectOut.writeObject(pThis);
            }
            return true;
        } catch (Exception e)
        {
            logger.error("Erro ao salvar mapa de conversas. " + e.toString());
            return false;
        }
    }
    
    public static ChatMap loadChatMap ()
    {
        logger.info("Carregando mapa de conversas...");
        try 
        {
            FileInputStream fileIn = new FileInputStream(CHAT_MAP_FILE);
            ChatMap ccc;
            try (ObjectInputStream objectIn = new ObjectInputStream(fileIn)) 
            {
                Object obj = objectIn.readObject();
                ccc = (ChatMap)obj;
            }
            return ccc;
        } catch (Exception e) 
        {
            logger.error("Erro ao carregar mapa de conversas. " + e.toString());
            logger.info("Criando mapa vazio. " + e.toString());
            return new ChatMap(); 
        }
    }

    public void remove (String key) 
    {
        ccHMChat.remove(key);
    }

    public Integer getSize () 
    {
        if (ccHMChat != null)
            return ccHMChat.size();
        return 0;
    }

    public boolean isEmpty() 
    {
        return getSize() == 0;
    }

    public Iterable<Map.Entry<String, ChatManager>> entrySet() 
    {
        if (ccHMChat != null)
            return ccHMChat.entrySet();
        return null;
    }
}
