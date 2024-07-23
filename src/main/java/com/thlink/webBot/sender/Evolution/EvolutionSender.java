package com.thlink.webBot.sender.Evolution;

import com.thlink.webBot.ai.google.REST.JSON.ReqAnsw;
import com.thlink.webBot.ai.google.REST.JSON.ReqAnsw2;
import com.thlink.webBot.ai.google.REST.RestHelper;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.sender.Evolution.findContacts.FindContatsRequestBody;
import com.thlink.webBot.sender.Evolution.findContacts.FindContatsResponseBody;
import com.thlink.webBot.sender.Evolution.findContacts.Where;
import com.thlink.webBot.sender.Evolution.program.createInstance.CreateInstanceBody;
import com.thlink.webBot.sender.Evolution.program.createInstance.CreateInstanceResponseBody;
import com.thlink.webBot.sender.Evolution.program.fetchInstance.FetchInstanceResponse;
import com.thlink.webBot.sender.Evolution.program.setWebHook.SetWebHookRequestBody;
import com.thlink.webBot.sender.Evolution.program.setWebHook.SetWebHookResponseBody;
import com.thlink.webBot.sender.Evolution.sendMedia.MediaMessage;
import com.thlink.webBot.sender.Evolution.sendMedia.SendMediaRequestBody;
import com.thlink.webBot.sender.Evolution.sendText.Key;
import com.thlink.webBot.sender.Evolution.sendText.Mentions;
import com.thlink.webBot.sender.Evolution.sendText.Message;
import com.thlink.webBot.sender.Evolution.sendText.Options;
import com.thlink.webBot.sender.Evolution.sendText.Quoted;
import com.thlink.webBot.sender.Evolution.sendText.SendTextBody;
import com.thlink.webBot.sender.Evolution.sendText.TextMessage;
import com.thlink.webBot.sender.MsgSender;
import static com.thlink.webBot.webbot.manager.InstanceManager.LOCAL_CLIENT;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.log4j.Logger;


public class EvolutionSender implements MsgSender {

  //private ChatParams params;
    private static final Logger logger = Logger.getLogger(EvolutionSender.class);
    private RestHelper h;
    private final String APP_WEBHOOK_URL_MONO  = "http://localhost:8800/webhook";
    private final String APP_WEBHOOK_URL_MULTI = "http://localhost:8888/multiwebhook";
    private       String EVOL_BASE_URL = "http://{server}:{port}";
    private       String EVOL_SEND_TEXT_URL;
    private       String EVOL_SEND_MEDIA_URL;
    private       String EVOL_FETCH_MONO_INSTANCE_URL;
    private       String EVOL_CREATE_INSTANCE_URL;
    private       String EVOL_SET_WEBHOOK_URL;
    private       String EVOL_FIND_CONTACTS_URL;
    private       String EVOL_MANAGER_URL = "http://{hostName}:{port}/manager";
    
    private String server, instanceName, instanceApiKey, globalApiKey;
    private int port;
    
    @Override
    public boolean startUp (int pPort, String pServer, String pInstanceName, String pInstanceApiKey, String pGlobalApiKey) 
    {
        port = pPort;
        server = pServer;
        instanceName = pInstanceName;
        instanceApiKey  = pInstanceApiKey;
        globalApiKey = pGlobalApiKey;
        
        EVOL_BASE_URL = EVOL_BASE_URL.replace("{server}", server).replace("{port}", String.valueOf(port));
        EVOL_SEND_TEXT_URL  = EVOL_BASE_URL.concat("/message/sendText/");
        EVOL_SEND_MEDIA_URL = EVOL_BASE_URL.concat("/message/sendMedia/");
        EVOL_FETCH_MONO_INSTANCE_URL = EVOL_BASE_URL.concat("/instance/fetchInstances?instanceName=").concat(instanceName);
        EVOL_CREATE_INSTANCE_URL = EVOL_BASE_URL.concat("/instance/create");
        EVOL_SET_WEBHOOK_URL = EVOL_BASE_URL.concat("/webhook/set/").concat(instanceName);
        EVOL_FIND_CONTACTS_URL = EVOL_BASE_URL.concat("/chat/findContacts/").concat(instanceName);
      //logger.info("EVOL_FIND_CONTACTS_URL = " + EVOL_FIND_CONTACTS_URL);
        
        try
        {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostName = localHost.getHostName();    
            EVOL_MANAGER_URL = EVOL_MANAGER_URL.replace("{hostName}", hostName).replace("{port}", String.format("%d", port));
        } catch (Exception e)
        {
            logger.error("startUp ERROR: " + e.toString());
            return false;
        }
                
        h = new RestHelper();
        return true;
    }

    @Override
    public boolean sendTextMessage (ChatMessage message) 
    {
      //logger.info("sendTextMessage()");
        if (message.getReservedField() != null && message.getReservedField().equals(LOCAL_CLIENT))
        {
            logger.info("By pass de sendTextMessage (LOCAL_CLIENT)!!!\n");
            return true;
        }
        
        try
        {
            SendTextBody send = new SendTextBody();
            send.setNumber(message.getFrom());
            TextMessage t = new TextMessage();
            t.setText(message.getAiMessage());
            send.setTextMessage(t);
            send.setOptions(getDefaultOptions());
                        
            String strBody = SendTextBody.objToJSON(send);
          //System.out.println("URL: " + EVOL_SEND_TEXT_URL.concat(message.getInstanceName()));
          /*System.out.println("HDs.: " + getDefaultParameters());
            System.out.println("strBody = \n" + strBody);*/
            ReqAnsw r = h.sendPost4(EVOL_SEND_TEXT_URL.concat(message.getInstanceName()), "POST", strBody, getDefaultParameters());
            if (r.getResponseCode() == 200 || r.getResponseCode() == 201)
            {
              //System.out.println("Deu certo: \n" + r.getResponse());
                return true;
            }
            logger.error("sendTextMessage " + r.getResponseCode());            
            return false;
        } catch (Exception e)
        {
            logger.error("sendTextMessage ERROR: " + e.toString());
            return false;
        }
    }
    
    public FetchInstanceResponse fetchMonoInstance ()
    {
        ReqAnsw r = h.sendPost4(EVOL_FETCH_MONO_INSTANCE_URL, "GET", "", getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
          //System.out.println("ERRO! \n" + r.getResponse());            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        FetchInstanceResponse fetchInstanceResponse = FetchInstanceResponse.jsonToObj(r.getResponse());
        return fetchInstanceResponse;
    }
    
    public FetchInstanceResponse fetchMUltiInstance (String pInstance)
    {
        EVOL_FETCH_MONO_INSTANCE_URL = EVOL_BASE_URL.concat("/instance/fetchInstances?instanceName=").concat(pInstance);
        ReqAnsw r = h.sendPost4(EVOL_FETCH_MONO_INSTANCE_URL, "GET", "", getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
            logger.error("fetchMUltiInstanceERRO! getResponseCode() = " + r.getResponseCode());
            logger.error(r.getResponse());
            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        FetchInstanceResponse fetchInstanceResponse = FetchInstanceResponse.jsonToObj(r.getResponse());
        return fetchInstanceResponse;
    }
    
    public CreateInstanceResponseBody createMonoInstance (String pName)
    {
        CreateInstanceBody request = new CreateInstanceBody();
        request.setInstanceName(pName);
        request.setQrcode(Boolean.TRUE);
        request.setToken(UUID.randomUUID().toString());
        String body = CreateInstanceBody.objToJSON(request);
      //System.out.println(body);
        ReqAnsw r = h.sendPost4(EVOL_CREATE_INSTANCE_URL, "POST", body, getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
            System.out.println("ERRO! \n" + r.getResponse());            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        CreateInstanceResponseBody createInstanceResponseBody = CreateInstanceResponseBody.jsonToObj(r.getResponse());
        return createInstanceResponseBody;
    }
    
    public CreateInstanceResponseBody createMultiInstance (String pName)
    {
        CreateInstanceBody request = new CreateInstanceBody();
        request.setInstanceName(pName);
        request.setQrcode(Boolean.TRUE);
        request.setToken(UUID.randomUUID().toString());
        String body = CreateInstanceBody.objToJSON(request);
      //System.out.println(body);
        ReqAnsw r = h.sendPost4(EVOL_CREATE_INSTANCE_URL, "POST", body, getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
            System.out.println("ERRO! \n" + r.getResponse());            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        CreateInstanceResponseBody createInstanceResponseBody = CreateInstanceResponseBody.jsonToObj(r.getResponse());
        return createInstanceResponseBody;
    }

    public SetWebHookResponseBody setWebHookMono (String pInstanceName)
    {
        SetWebHookRequestBody request = new SetWebHookRequestBody();
        request.setUrl(APP_WEBHOOK_URL_MONO);
        request.setWebhookBase64(Boolean.TRUE);
        request.setWebhookByEvents(Boolean.TRUE);
        request.setEvents(Arrays.asList("MESSAGES_UPSERT" /*, "SEND_MESSAGE"*/));

        String body = SetWebHookRequestBody.objToJSON(request);
      //System.out.println(body);
        EVOL_SET_WEBHOOK_URL = EVOL_BASE_URL.concat("/webhook/set/").concat(pInstanceName);
        ReqAnsw r = h.sendPost4(EVOL_SET_WEBHOOK_URL, "POST", body, getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
            System.out.println("ERRO! \n" + r.getResponse());            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        SetWebHookResponseBody setWebHookResponseBody = SetWebHookResponseBody.jsonToObj(r.getResponse());
        return setWebHookResponseBody;
    }
    
    public SetWebHookResponseBody setWebHookMulti (String pInstanceName)
    {
        SetWebHookRequestBody request = new SetWebHookRequestBody();
        String url = APP_WEBHOOK_URL_MULTI.concat("/").concat(pInstanceName);
        logger.info("setWebHookMulti @" + url);
        request.setUrl(url);
        request.setWebhookBase64(Boolean.TRUE);
        request.setWebhookByEvents(Boolean.TRUE);
        request.setEvents(Arrays.asList("MESSAGES_UPSERT", "SEND_MESSAGE"));

        String body = SetWebHookRequestBody.objToJSON(request);
      //System.out.println(body);
        EVOL_SET_WEBHOOK_URL = EVOL_BASE_URL.concat("/webhook/set/").concat(pInstanceName);
        ReqAnsw r = h.sendPost4(EVOL_SET_WEBHOOK_URL, "POST", body, getDefaultParametersForInstance());
        if (r.getResponseCode() != 200 && r.getResponseCode() != 201)
        {
            System.out.println("ERRO! \n" + r.getResponse());            
            return null;
        }
      //System.out.println("Deu certo: \n" + r.getResponse());
        SetWebHookResponseBody setWebHookResponseBody = SetWebHookResponseBody.jsonToObj(r.getResponse());
        return setWebHookResponseBody;
    }
    
    
    private HashMap<String, String> getDefaultParameters ()
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("apikey", instanceApiKey);
        return headers;
    }
    
    //Não tem apiKey
    private HashMap<String, String> getDefaultParametersForInstance ()
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("apikey", globalApiKey);
        return headers;
    }
    
    private Options getDefaultOptions () 
    {
        Options opt = new Options();
        opt.setDelay(0);
        opt.setPresence("composing");
        opt.setLinkPreview(Boolean.TRUE);
        Quoted quoted = new Quoted();
        Key key = new Key();
        key.setRemoteJid("string");
        key.setFromMe(Boolean.FALSE);
        key.setId("string");
        quoted.setKey(key);
        
        Message message = new Message();
        message.setConversation("string");
        quoted.setMessage(message);
        
        Mentions mentions = new Mentions();
        mentions.setEveryone(Boolean.TRUE);
        mentions.setMentioned(Arrays.asList("0"));
        
        return opt;
    }
    
    @Override
    public boolean sendMediaMessage (ChatMessage message, List<String> pFiles) 
    {
        //"image", ""
        Path path = Paths.get(pFiles.get(0));
        byte[] bytes;
        try 
        {
            bytes = Files.readAllBytes(path);
        } catch (IOException ex) 
        {
            
            return false;
        }
        String encoded = Base64.getEncoder().encodeToString(bytes); 
        SendMediaRequestBody smrb = new SendMediaRequestBody();
        smrb.setNumber(message.getFrom());
        MediaMessage mediaMessage = new MediaMessage();
        mediaMessage.setCaption(message.getAiMessage());
        if (pFiles.get(0).toLowerCase().endsWith(".png") || 
            pFiles.get(0).toLowerCase().endsWith(".jpg") || 
            pFiles.get(0).toLowerCase().endsWith(".jpeg") || 
            pFiles.get(0).toLowerCase().endsWith(".bmp"))
            mediaMessage.setMediatype("image");
        else if (pFiles.get(0).toLowerCase().endsWith(".pdf"))
            mediaMessage.setMediatype("document");
        else
            return false;
            
        mediaMessage.setMedia(encoded);
        mediaMessage.setFileName(pFiles.get(0));
        smrb.setMediaMessage(mediaMessage);
        com.thlink.webBot.sender.Evolution.sendMedia.Options options = new com.thlink.webBot.sender.Evolution.sendMedia.Options();
        options.setDelay(1200);
        options.setPresence("composing");
        smrb.setOptions(options);
        
        String strBody = SendMediaRequestBody.objToJSON(smrb);
      /*System.out.println("URL: " + EVOL_SEND_MEDIA_URL.concat(params. getMessaging().getEvolutionParams().getInstanceName()));
        System.out.println("HDs.: " + getDefaultParameters());
        System.out.println("strBody = \n" + strBody);*/
        ReqAnsw r = h.sendPost4(EVOL_SEND_MEDIA_URL.concat(instanceName), "POST", strBody, getDefaultParameters());
        if (r.getResponseCode() == 200 || r.getResponseCode() == 201)
        {
          //System.out.println("Deu certo: \n" + r.getResponse());
            return true;
        }
        System.out.println("ERRO! \n" + r.getResponse());            
        return false;

    }
    
    private HashMap<String, String> getDefaultParameters2 ()
    {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("apikey", instanceApiKey);
        return headers;
    }
    
    @Override
    public List<FindContatsResponseBody> findAllContacts ()
    {
        logger.info("findAllContacts()");
        Where w = new Where();
        w.setId(LOCAL_CLIENT);
        return findContactsWhere(new Where());
    }
    
    @Override
    public boolean isContact (String pId)
    {
        logger.info("isContact()");
        Where w = new Where();
        w.setId(pId);
        List<FindContatsResponseBody> list1 = findContactsWhere(w);
        if (list1 != null && list1.size() == 1)
            return true;
        return false;
    }
    @Override
    public List<FindContatsResponseBody> findContactsWhere (Where pWhere)
    {
      //logger.info("findContactsWhere() " + pWhere.getId());
        try
        {
            FindContatsRequestBody fcrb = new FindContatsRequestBody();
            fcrb.setWhere(pWhere);
            String strBody = FindContatsRequestBody.objToJSON(fcrb);
          //logger.info("EVOL_FIND_CONTACTS_URL = " + EVOL_FIND_CONTACTS_URL);
          //logger.info("strBody = " + strBody);
            ReqAnsw2 r = h.sendPostResponseZip(EVOL_FIND_CONTACTS_URL, "POST", strBody, getDefaultParameters2());
            if (r.getResponseCode() == 200 || r.getResponseCode() == 201)
            {
              //System.out.println("Deu certo: \n" + r.getResponse());
              //FindContatsResponseBody response = FindContatsResponseBody.jsonToObj(r.getResponse());
                return r.getListContacts();
            }
            logger.error("findContactsWhere NOK! r.getResponseCode() = " + r.getResponseCode());
            return null;
        } catch (Exception e)
        {
            logger.error("findAllContacts ERROR: " + e.toString());
            return null;
        }
        finally
        {
            
        }
    }
    
    public boolean waitForEvolution (int pPort, String pServer)
    {
        RestHelper localH = new RestHelper();
        localH.setTimeoutMs(5000);
        
        String baseURL = EVOL_BASE_URL.replace("{server}", pServer).replace("{port}", String.valueOf(pPort));
      //logger.info("Buscando Evolution ... @" + baseURL);
        ReqAnsw r = localH.sendPost4(baseURL, "GET", "", getDefaultParameters());
        if (r.getResponseCode() == 200 || r.getResponseCode() == 201)
        {
          //logger.info("waitForEvolution OK!");
            return true;
        }
        logger.info(String.format("Evolution não encontrada!"));
        return false;
    }
}
