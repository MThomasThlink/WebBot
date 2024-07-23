package com.thlink.webBot.webbot.manager;

import static com.thlink.webBot.ai.google.REST.RestHelper.ENCODING;
import com.thlink.webBot.persistence.entities.tbAcceptedTelephones;
import com.thlink.webBot.persistence.entities.tbBlockedTelephones;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbLogChatManager;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.qualifier.ConversationModel;
import com.thlink.webBot.qualifier.QualificationResult;
import com.thlink.webBot.qualifier.Qualificator;
import com.thlink.webBot.sender.Evolution.EvolutionSender;
import com.thlink.webBot.sender.Evolution.program.createInstance.CreateInstanceResponseBody;
import com.thlink.webBot.sender.Evolution.program.fetchInstance.FetchInstanceResponse;
import com.thlink.webBot.sender.Evolution.program.setWebHook.SetWebHookResponseBody;
import com.thlink.webBot.service.AiParametersService;
import com.thlink.webBot.service.ChatManagerService;
import com.thlink.webBot.service.ClientService;
import com.thlink.webBot.service.ConversationMessageService;
import com.thlink.webBot.service.ConversationService;
import com.thlink.webBot.service.GeneralService;
import com.thlink.webBot.service.InstanceService;
import com.thlink.webBot.webbot.manager.ChatManager.CHAT_STATE;
import static com.thlink.webBot.webbot.manager.InstanceManager.eINSTANCE_STATES.OPEN;
import java.io.File;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceManager {
    
    private static final Logger logger = LoggerFactory.getLogger(InstanceManager.class); 
    
    @Autowired
  //private final GeneralService generalSrv;
  //private final ClientService clientSrv;
    private final InstanceService instanceSrv;
    //private final AiParametersService aiParamsSrv;
    private final ChatManagerService chatManagerSrv;
    private final ConversationService convSrv;
    private final ConversationMessageService msgSrv;

    
    public enum eOPER { 
                AI_CHAT(1), DUMB_BOT(2), SPY (3)/*, AI_SPY (4)*/;
                public final int value;
                private eOPER (int value)  { this.value = value; }
    }
    public enum eINSTANCE_STATES { STOPED, STARTED, CREATED, CONFIGURED, CLOSED, OPEN }
    
    public static final String LOCAL_CLIENT = "LOCAL_CLIENT";
    public static final String WHATSAPP_SUFIX = "@s.whatsapp.net";
    public static final String APP_ID = "APP";
    public static final String APP_HEADER = APP_ID.concat(" aqui: ");

    private tbLogChatManager logChatManager = new tbLogChatManager();   //deve ser atualizado no final
    private int numberMessagesSinceStart = 0, numberChatsSinceStart = 0;
  //private ChatParams params;
    private static ChatMap chatMap;
    private MacroParams macroParams;
    private tbInstances instance;
    private Timer chatTimer;
    private eINSTANCE_STATES instanceState;
    
    public InstanceManager (GeneralService pGeneralSrv,   ClientService pClientSrv, 
                             InstanceService pInstanceSrv, AiParametersService pAiParamsSrv, 
                             ChatManagerService pChatManagerSrv,
                             ConversationService pConversationSrv, ConversationMessageService pMsgSrv)
    {
        logger.info("ChatManagerMulti recebendo services no construtor.");
        //generalSrv = pGeneralSrv;
        //clientSrv = pClientSrv;
        instanceSrv = pInstanceSrv;
        //aiParamsSrv = pAiParamsSrv;
        chatManagerSrv = pChatManagerSrv;
        convSrv = pConversationSrv;
        msgSrv = pMsgSrv;
    }
    
    public boolean startMultiManager (MacroParams pMP)
    {
        logger.info("InstanceManager startMultiManager()");
        macroParams = pMP;
      //params = pMP.getParams();
        instance = pMP.getInstance();
        logChatManager = new tbLogChatManager();
        logChatManager.setInstance(instance);
        logChatManager.setStartDate(Calendar.getInstance().getTime());
        
        chatManagerSrv.saveObj(logChatManager);
        instanceState = eINSTANCE_STATES.STARTED;

        chatTimer = new Timer();    //XXX desativado por enquanto
        chatTimer.schedule(new InstanceManager.MonitorChats(), 0l, (long)(instance.getManagerIntervalS() * 1000));
           
        chatMap = ChatMap.loadChatMap();
        if (chatMap == null)
        {
            logger.error("chatMap == null");
            return false;
        }
      /*A validação da instância é importante mas não deve ser síncrona (pois impediria que o GM criasse outros IMs.
        Approach: criar um timer que quando executado cria a instance, configura o WH e espera que esteja conectado. 
        Quando estiver, seta um flag de estado - condição verificada em cada dispatchMessage. O timer também garantirá que a instância
        não foi desconectada. */
      
        return true;
    }
    
    public boolean stopMultiManager (int pReason)
    {
        logger.info("ChatManagerMulti stopMultiManager()");
        logChatManager.setEndDate(Calendar.getInstance().getTime());
        logChatManager.setQtdContacts(numberChatsSinceStart);
        logChatManager.setReason(pReason);
        logChatManager = chatManagerSrv.saveObj(logChatManager);
        if (logChatManager == null)
            logger.error("ChatManagerMulti erro ao salvar.");
        logger.info("ChatManagerMulti stopMultiManager(END)");
        instanceState = eINSTANCE_STATES.STOPED;
        return true;
    }

    public ChatMessage respondUserMessage (ChatMessage message) 
    {
        logger.info("respondUserMessage.");
        try
        {
            if (!instanceState.equals(OPEN))
            {
                logger.info("respondUserMessage: estado inválido: " + instanceState.toString());
                return null;
            }
            if (!validateSource(message))
            {
                logger.error("Bloqueado por validateSource()s.");
                return null;
            }
          //logger.info("respondUserMessage past validateSource.");
            if (message.isFromMe())
            {
                if (!instance.getAnswerMyself())
                {
                    logger.error("Ignorar mensagens próprias.");
                    return null;
                }
            }
          //logger.info("chatMap.getChat?");
            ChatManager localChat = chatMap.getChat(message.getFrom());
            if (localChat == null)
            {
              //logger.info("chatMap.getChat == null");
                logger.info("Iniciando conversação com " + message.getFrom());
                localChat = new ChatManager(instanceSrv, convSrv, msgSrv);
                macroParams.setMessage(message);
                ChatMessage initialChat = localChat.startChat(macroParams);
                if ( initialChat == null)
                {
                    logger.error("Erro em localChat. startChat nulo");
                    return null;
                }
                if (initialChat.getErrorCode() != null)
                {
                    logger.error("startChat.getErrorCode = " + initialChat.getErrorCode());
                    return null;
                }
                
                chatMap.putChat(message.getFrom(), localChat);
                numberChatsSinceStart++;
            }
            //else 
            //    logger.info("chatMap.getChat != null");
            ChatMessage reply = localChat.processMessage(message);
            numberMessagesSinceStart++;
            return reply;
        } catch (Exception e)
        {
            logger.error("respondUserMessage: ERROR: " + e.toString());
            return null;
        }
    }

    private boolean validateSource (ChatMessage message) 
    {
        if (message.getFrom() == null || message.getFrom().isEmpty() )
        {
            logger.error("validateSource: getFrom vazio.");
            return false;
        }
        String numero = message.getFrom().replace(WHATSAPP_SUFIX, "");
        if (numero.startsWith("55480")) //cliente de teste
            return true;
        if (instance.getBlocked() != null && !instance.getBlocked().isEmpty())
        {
            for (tbBlockedTelephones b : instance.getBlocked())
                if (b.getNumber().equals(numero))
                {
                    logger.error("validateSource: getFrom bloqueado: " + numero);
                    return false;
                }
        }
        
        if (instance.getAccepted() != null && !instance.getAccepted().isEmpty())
        {
            for (tbAcceptedTelephones a : instance.getAccepted())
                if (a.getNumber().equals(numero))
                {
                    logger.error("validateSource: getFrom não aceito: " + numero);
                    return false;
                }
        }
        return true;
    }
    
    public void finalizeChat (Map.Entry<String, ChatManager> set)
    {
        logger.info("ChatManagerMulti.finalizeChat ");
        ChatManager chatManager = set.getValue();
        chatMap.remove(set.getKey());
        switch (chatManager.getStatus()) 
        {
            case FINISHED:
                break;
            case EXCEED_IDLE:
                ChatMessage idleMessage = chatManager.composeMessage("Comunicação finalizada por duração excessiva. " + macroParams.getInstance().getMaxIdleTimeS() + "segs.");
                if (!chatManager.sendTextMessage(idleMessage))
                {
                    logger.error("Erro no envio da mensagem de finalização.");
                }
                logger.info(String.format("Remove '%s' due to %s.\n", set.getKey(), set.getValue().getStatus()));
                break;

            case EXCEED_DURATION:
                ChatMessage durationMessage = chatManager.composeMessage("Comunicação finalizada por duração excessiva. " + macroParams.getInstance().getMaxIdleTimeS() + "segs.");
                //idleMessage.set
                //respondUserMessage(idleMessage);
                if (!chatManager.sendTextMessage(durationMessage))
                {
                    logger.error("Erro no envio da mensagem de finalização.");
                }
                logger.info(String.format("Remove '%s' due to %s.\n", set.getKey(), set.getValue().getStatus()));
                break;
        }
        
        if (macroParams.getInstance().isAI())
        {
            logger.info("Agora  chama o qualificador.");
            Qualificator qual = new Qualificator();
            ConversationModel model = new ConversationModel();
            {   //Alimenta os campos do modelo.
              //model.setCodBot(params.getCodBot());
              //model.setParams(macroParams.getParams());
                Date now = Calendar.getInstance().getTime();
                model.setIdConversation(chatManager.getIdConversation());
                model.setChat(chatManager);
                com.thlink.webBot.qualifier.Client localClient = new com.thlink.webBot.qualifier.Client();
                localClient.setPhoneNumer(chatManager.getFrom()); 
                localClient.setPushName(chatManager.getPushName());
                localClient.setUserName(chatManager.getUserName());
                model.setClient(localClient);

                model.setInstance(macroParams.getInstance());
                
                tbLogConversation conversation = chatManager.getConv();
                conversation.setFinishTime(now);

                model.setConversation(conversation);
                model.setPostMessages(instance.getPostConvQuestions());
            }

            List<tbLogConversationMessage> messages = chatManager.getMessages();
            logger.info("messages size = " + messages.size());
            if (!messages.isEmpty())
            {
                model.setMessages(messages);
                QualificationResult result = qual.evaluateModel(model);
                logger.info("evaluateModel: " + result);
                tbLogConversation conversation = convSrv.findById(model.getConversation().getId());
                if (convSrv != null)
                {
                    conversation.setScoreM1(result.getScoreM1());
                    conversation.setScoreM2(result.getScoreM2());
                    conversation.setScoreM3(result.getScoreM3());
                    conversation.setQtdIterations(messages.size());
                    conversation.setFinishTime(model.getConversation().getFinishTime());
                    conversation.setUserName(model.getClient().getUserName());
                    logger.info("Salvando resultados: ");
                    if (convSrv.saveObj(conversation) != null)
                        logger.info("Fim da qualificação. ");
                    else
                        logger.error("ERRO ao salvar resultados!");
                }
                else
                {
                    logger.error("Conversação não encontrada. Dados não foram salvos");

                }
            }
            else
                logger.error("Sem mensagens");
        }
        else if (macroParams.getInstance().isSPY())
        {
          //List<tbLogConversationMessage> messages = getMessagesFromConversationId(aiChat.getIdConversation());
          //List<tbLogConversationMessage> messages = instance.getLogConversation()
          //PersConversation.updateConversation(aiChat.getIdConversation(), aiChat.getClientName(), now, aiChat.getStatus().ordinal(), messages.size(), null);
        }
    }
    private boolean checkConversationIdle (ChatManager pChat)
    {
        if (macroParams.getInstance().getMaxIdleTimeS() != null && macroParams.getInstance().getMaxIdleTimeS() > 0)
        {
            long durationS = (Calendar.getInstance().getTimeInMillis() - pChat.getLastMessageTime()) / 1000;
          //logger.info(String.format("\t[%s]durationS = %d", user.getFrom(), durationS));
            if (durationS > macroParams.getInstance().getMaxIdleTimeS())
            {
                logger.info("Bot exceeded conversation idle time!!");
                return true;
            }
        }
        return false;
    }
    private boolean checkConversationLength (ChatManager pChat)
    {
        if (macroParams.getInstance().getMaxDurationS()!= null && macroParams.getInstance().getMaxDurationS() > 0)
        {
            long durationS = (Calendar.getInstance().getTimeInMillis() - pChat.getConversationStartTime()) / 1000;
          //logger.info(String.format("\t[%s]durationS = %d", user.getFrom(), durationS));
            if (durationS > macroParams.getInstance().getMaxDurationS())
            {
                logger.info("Bot exceeded conversation max duration time!!");
                return true;
            }
        }
        return false;
    }

    public int getOnGoingChats ()
    {
        return chatMap.getSize();
    }
    
    public String getInstanceName ()
    {
        return instance.getInstanceName();
    }
    public String getInstanceState ()
    {
        return instanceState.toString();
    }
    public String getInstanceDetais ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(instance.getInstanceName()).append(" - Tipo: ").append(instance.getTypeDescription()).append(".\n");
        int onGoingChats = getOnGoingChats();
        if (onGoingChats > 0)
            sb.append("\tChats: ").append(this.getOnGoingChats()).append("\n");
        else
            sb.append("\tSem chats.");
        sb.append("\n");
        return sb.toString();
        
    }
    private class MonitorChats extends TimerTask 
    {
        private EvolutionSender sender = new EvolutionSender();
        int ctd = 0;
        
        public MonitorChats() 
        {
            if (!sender.startUp(macroParams.getEvolutionPort(), macroParams.getEvolutionServer(), instance.getInstanceName(), instance.getEvolApiKey(), instance.getEvolGlobalKey()))
            {
                logger.info("validateInstance ERRO 1");
                return;
            }
        }
        private boolean saveQrCode (String folderName, String fileName, String base64) 
        {
            logger.info("saveQrCode " + fileName);
            try
            {
                if (base64.startsWith("data:image/png;base64,")) 
                    base64 = base64.substring("data:image/png;base64,".length());
                else if (base64.startsWith("data:image/jpeg;base64,")) 
                    base64 = base64.substring("data:image/jpeg;base64,".length());

                byte[] img = Base64.getDecoder().decode(base64.getBytes(ENCODING));
                File folder = new File(folderName);
                if (!folder.exists())
                    folder.mkdirs();
                File qrFile = new File(folderName.concat("/").concat(fileName));
                FileUtils.writeByteArrayToFile(qrFile, img); 
                logger.info("saveQrCode OK!");
                return true;
            } catch (Exception e)
            {
                logger.error ("saveQrCode ERROR: " + e.toString());
                return false;
            }
        }
    
        public boolean validateInstance ()
        {
            StringBuilder sb = new StringBuilder();
            boolean result = false;
            try
            //switch (params.getMessaging().getSender()) 
            {
                //case EVOLUTION:
                    sb.append("Validando instância ").append(instance.getInstanceName());
                    
                    sb.append(" Buscando dados ...");
                    FetchInstanceResponse r = sender.fetchMUltiInstance(instance.getInstanceName());
                    
                    if (r == null)
                    {
                        sb.append("validateInstance não encontrou a instância. Criar! ");
                        CreateInstanceResponseBody request = sender.createMultiInstance(instance.getInstanceName());
                        if (request != null)
                        {
                            if (request.getInstance().getInstanceName().equals(instance.getInstanceName()))
                            {
                                instanceState = instanceState = eINSTANCE_STATES.CREATED;
                                sb.append("Salvar QrCode e ApiKey na instância");
                                instance.setQrCodeBase64(request.getQrcode().getBase64());  //não esqueça de salvar!
                                instance.setEvolApiKey(request.getHash().getApikey());
                                instance = instanceSrv.saveObj(instance);

                                sb.append("Instância criada. Programar webHooks. ");
                                SetWebHookResponseBody response = sender.setWebHookMulti(instance.getInstanceName());
                                if (response != null && response.getStatus() == null && response.getWebhook() != null)
                                {
                                    instanceState = eINSTANCE_STATES.CONFIGURED;
                                    sb.append("Instância criada. Use o QrCode para conectar o celular e avançar. ");
                                    result = true;
                                    r = sender.fetchMUltiInstance(instance.getInstanceName());
                                }

                                if (saveQrCode("./QrCode", "QrCode-".concat(instance.getInstanceName()).concat(".png"), 
                                               request.getQrcode().getBase64()))
                                    sb.append("QrCode salvo em arquivo." );
                                else
                                    sb.append("Erro ao salvar apiKey das configurações: ").append(instance.getInstanceName());
                            }
                            else
                                sb.append("Instância criada com nome errado: ").append(request.getInstance().getInstanceName());
                        }
                        else    //o problema é que pode ter criado (e com apiKey, que não temos mais).
                            sb.append("Sem resposta para o comando de criação de instância: ").append(instance.getInstanceName());
                    }
                    else
                    {
                        sb.append(" Encontrada: ").append(r.getInstance().getInstanceName());
                        if (instance.getEvolApiKey() == null || instance.getEvolApiKey().isEmpty())
                        {
                            sb.append("Instância existe mas ApiKey não é conhecida. Re-criar!"); //XXX
                            result = false;
                        }
                        else
                            result = true;
                    }
                    if (!result)
                    {
                        sb.append("Resultado NEGATIVO!");
                        return false;
                    }

                    if (r == null)
                    {
                        sb.append("Sem dados da instância");
                        return false;
                    }
                    if (!r.getInstance().getStatus().equals("open"))
                    {
                        instanceState = eINSTANCE_STATES.CLOSED;
                        sb.append(" Status: ").append(r.getInstance().getStatus());
                        return false;
                    }
                    sb.append(" Status: ").append(r.getInstance().getStatus());
                    instanceState = eINSTANCE_STATES.OPEN;
                    return true;

            } catch (Exception e)
            {
                sb.append("validateInstance ERROR: ").append(e.toString());
                return false;
            }
            finally
            {
                if (!result)
                    logger.error(sb.toString());
                /*else
                    logger.info(sb.toString());*/
            }
        }

        @Override
        public void run() 
        {
            try
            {
              //logger.info("MonitorChats.run: " + ctd);
                if (!instanceState.equals(OPEN) || ctd % 2 == 0)
                {
                    if (!validateInstance())
                    {
                        logger.error("!validateInstance()");
                        return;
                    }
                }
                if (macroParams.getInstance().isAI())
                {
                    if (chatMap == null || chatMap.isEmpty())
                    {
                      //logger.info("MonitorChats.chatMap.isEmpty()");
                        return;
                    }


                    for (Map.Entry<String, ChatManager> set : chatMap.entrySet())
                    {
                        ChatManager user = set.getValue();
                        if (user == null)   //should not happen!
                        {
                            logger.error("MonitorChats user == NULL");
                            continue;
                        }

                        if (user.isBusy())
                        {
                            logger.error("MonitorChats busy: " + user.getFrom());
                            continue;
                        }
                        switch (user.getStatus())
                        {
                            case FINISHED:
                            {
                                logger.info(String.format("Remove '%s' due to %s.\n", set.getKey(), set.getValue().getStatus()));
                                finalizeChat(set);
                                break;
                            }   
                            default:
                            {
                                if (checkConversationIdle(user))    //Verifica tempo de ociosidade
                                {
                                    user.setStatus(CHAT_STATE.EXCEED_IDLE);
                                    finalizeChat(set);
                                    break;
                                }
                                if (checkConversationLength(user))  //Verifica se conversa não excedeu a duração máxima
                                {
                                    user.setStatus(CHAT_STATE.EXCEED_DURATION);
                                    finalizeChat(set);
                                    break;
                                }
                            }   //default
                        }       //switch
                    }           //for
                }               //if Ai
            } catch (Exception e)//catch
            {
                logger.error("MonitorTask ERROR: " + e.toString());
            }
            finally
            {
                ctd++;
            }
        }                       //run
    }                   

}
