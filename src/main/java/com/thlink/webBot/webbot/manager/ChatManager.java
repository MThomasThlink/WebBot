package com.thlink.webBot.webbot.manager;

import com.thlink.webBot.ai.google.REST.AIInterface;
import com.thlink.webBot.ai.google.REST.RESTGoogleAI;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.sender.Evolution.EvolutionSender;
import com.thlink.webBot.sender.MsgSender;
import com.thlink.webBot.service.ConversationMessageService;
import com.thlink.webBot.service.ConversationService;
import com.thlink.webBot.service.InstanceService;
import static com.thlink.webBot.webbot.manager.ChatManager.CHAT_STATE.STARTED;
import static com.thlink.webBot.webbot.manager.InstanceManager.eOPER.AI_CHAT;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatManager implements Serializable
{
    private Long idConversation;
    private String userName = null, pushName;
    private int idleTimeS, silenceTimeS;
    private long conversationStartTime, lastMessageTime;
    //private CustomLog logger;
    private static final Logger logger = LoggerFactory.getLogger(ChatManager.class); 

    private String from;
    private CHAT_STATE status;
    private static AIInterface ai;
    private static ChatParams params;
    private boolean busy;
    private MsgSender sender;
    private List<tbLogConversationMessage> messages;
    private tbInstances instance;
    private tbLogConversation conv;
    
    //                      0       1       2           3         4                5    
    public enum CHAT_STATE { VOID, STARTED, RUNNING, FINISHED, EXCEED_DURATION, EXCEED_IDLE };
    public List<String> fragWhoAmI = getWhoIamTalkingoToFragment();
    
    @Autowired
    private final InstanceService instanceService;
    private final ConversationService convService;
    private final ConversationMessageService msgService;

    public ChatManager (InstanceService pInstanceService, ConversationService pConvService, ConversationMessageService pMsgService) {
        this.instanceService = pInstanceService;
        this.convService = pConvService;
        this.msgService = pMsgService;
    }
   
    //Functions
    public ChatMessage startChat (MacroParams pACP)
    {
        logger.info("startChat");
        try
        {
            this.busy = true;
            if (pACP == null)
            {
                return new ChatMessage(-1, "Parâmetros nulos.");
            }
            
            if (pACP.getMessage() == null)
            {
                return new ChatMessage(-2, "Mensagem nula.");
            }
          //logger.info("startChat validade MacroParams");
            instance = pACP.getInstance();
            conversationStartTime = Calendar.getInstance().getTimeInMillis();
            ChatMessage message = pACP.getMessage();
            //String fileName = String.format("./Log/Clientes/%s.log", message.getFrom());
            //logger = new CustomLog(fileName, INFO);
            //logger.inicializa();
            logger.info("startConversation with: " + message.getFrom());
            logger.info("Visitor: " + message.getPushName());
            pushName = message.getPushName();
          //logger.info("startChat verify Instance mode");
            if (instance.getType() == AI_CHAT.value /*|| instance.getType() == AI_SPY.value*/)
            {
                logger.info("startChat mode: AI_CHAT");
                ai = new RESTGoogleAI();
                if (!ai.startAI(pACP))
                {
                    logger.error("Erro em startAI. ");
                    return new ChatMessage(-3, "Erro de inicialização da AI Google.");
                }
                logger.info("startChat ai.started OK!");
            }
            
            //switch (pACP.getParams().getMessaging().getSender()) 
            {
                //case EVOLUTION:
                    sender = new EvolutionSender();
                    if (!sender.startUp(pACP.getEvolutionPort(), pACP.getEvolutionServer(), instance.getInstanceName(), instance.getEvolApiKey(), instance.getEvolGlobalKey()))
                    {
                        return new ChatMessage(-5, "Erro na preparação do serviço de mensageria.");
                    }
                    //break;
                //default:
                //    return new ChatMessage(-4, "Servidor de mensagem desconhecido.");
            }
            from = message.getFrom();
            //params = pACP.getParams();
            status = CHAT_STATE.STARTED;
            setLastMessageTime(Calendar.getInstance().getTimeInMillis());
            
            //Se estiver em modo SPY, reconstrói a conversa a partir da última
            
            if (instance.isSPY())
            {
                //conv = getLastConversationWithThisPerson(message.getFrom());
                conv = convService.getLastConversationWithThisPerson(message.getFrom());
                if (conv != null)
                {
                    //Verificar se a última conversa foi a mais de x horas
                    if (differenceInHours(conv.getStartTime()) > instance.getTempoNovaConversaH())
                    {
                        conv = convService.createLogConversation(message, instance, sender.isContact(message.getFrom()));
                        instance.getLogConversation().add(conv);
                    }
                    else
                    {
                        //Aproveita esta conversa
                    }
                }
                else    //Primeira vez deste cliente
                {
                    conv = convService.createLogConversation(message, instance, sender.isContact(message.getFrom()));
                    logger.info("1. Size = " + instance.getLogConversation().size());
                    instance.getLogConversation().add(conv);
                    logger.info("2. Size = " + instance.getLogConversation().size());
                }
            }
            else    //AI sempre começa aqui.
            {
                conv = convService.createLogConversation(message, instance, sender.isContact(message.getFrom()));
                conv = convService.saveObj(conv);
              //logger.info("Conversation saved and flushed!");
                //instance.getLogConversation().add(conv);
                //logger.info("4. Size = " + instance.getLogConversation().size());
            }
          //logger.info("instanceService was not saved at this point.");
            //instance = instanceService.saveObj(instance);
            idConversation = conv.getId();
            messages = new ArrayList<>();
            return message;
        } catch (Exception e)
        {
            logger.error("startChat ERROR: " + e.toString());
            return null;
        }
        finally
        {
            this.busy = false;
        }
    }
    
    private long differenceInHours (Date pDate)
    {
        Date now = Calendar.getInstance().getTime();
        long seconds = now.toInstant().toEpochMilli() - pDate.toInstant().getEpochSecond();
        return seconds / (60*60*24);
    }

    public ChatMessage processMessage (ChatMessage message) 
    {
      //logger.info("processMessage()");
        if (status.equals(STARTED) || message.isAfterChat())
        {   
            try
            {
                this.busy = true;
                setLastMessageTime(Calendar.getInstance().getTimeInMillis());
                if (instance.isAI() /*|| instance.isAISPY()*/)
                {
                    if (message.getClientMessage() != null)
                    {
                        String reply = ai.getResponseAI(message.getClientMessage());
                        if (reply != null && !reply.isEmpty())
                        {
                          //logger.info("reply != null && !reply.isEmpty()");
                            message.setAiMessage(reply);
                            message.setAiMoment(Calendar.getInstance().getTime());
                            if (!message.isAfterChat())
                                message = appFilter(message);
                            if (instance.isAI())
                            {
                                if (!message.isAfterChat())
                                {
                                  //logger.info("processMessage.sender.sendTextMessage()");
                                    if (sender.sendTextMessage(message) )
                                    {
                                        logger.info("Envio OK!");
                                        message.setIdConversation(idConversation);
                                        tbLogConversationMessage tbMsg = new tbLogConversationMessage(conv, message);
                                      //logger.info("msgService.saveObj(conv, message); ");
                                        tbMsg = msgService.saveObj(tbMsg);     //@processMessage() AQUI ESTÀ DANDO O ERRO!!+/
                                      //logger.info("msgService.saveObj(conv, message) OK! ");
                                        messages.add(tbMsg);  //acrescentada quanto se tem a REPOSTA do AI
                                        detectClientName();
                                    }
                                    else
                                        logger.error("Erro de envio.");
                                }// não precisa enviar respostas de afterChat! mas precisa gravar
                                else
                                {
                                    logger.info("Acrescenta mensagem afterChat");
                                    message.setIdConversation(idConversation);
                                    tbLogConversationMessage tbMsg = new tbLogConversationMessage(conv, message);
                                    messages.add(tbMsg);  //acrescentada quanto se tem a REPOSTA do AI
                                    tbMsg = msgService.saveObj(tbMsg);
                                  //logger.info("Mensagem de afterchat salva");                                
                                }
                            }
                            else
                            {
                                message.setIdConversation(idConversation);
                                tbLogConversationMessage tbMsg = new tbLogConversationMessage(conv, message);
                                tbMsg = msgService.saveObj(tbMsg);
                                messages.add(tbMsg);  //acrescentada quanto se tem a REPOSTA do AI
                                //PersConversationMessage.saveLogConversationMessage(message);     //@processMessage()
                                msgService.saveObj(conv, message);     //@processMessage()
                            }
                        }
                        else
                        {
                            message.setErrorCode(-4);
                            message.setErrorMsg("Erro na resposta da AI");
                        }
                    }
                    else
                    {
                        //salvar a resposta manual.
                        message.setIdConversation(idConversation);
                        tbLogConversationMessage tbMsg = new tbLogConversationMessage(conv, message);
                        tbMsg = msgService.saveObj(tbMsg);
                        messages.add(tbMsg);  //acrescentada quanto se tem a REPOSTA do AI
                        if (msgService.updateLogConversationMessage(message) == null)   //não conseguiu atualizar 
                            logger.error("Erro na atualização conversa! ");
                    }
                }
                else if (instance.isSPY())
                {
                    message.setIdConversation(idConversation);
                    tbLogConversationMessage tbMsg = new tbLogConversationMessage(conv, message);
                    tbMsg = msgService.saveObj(tbMsg);
                    messages.add(tbMsg);  //acrescentada quanto se tem a REPOSTA do AI

                    if (!message.isFromMe())
                        msgService.saveObj(conv, message);     //@processMessage()
                        //PersConversationMessage.saveLogConversationMessage(message);
                    else
                        if (msgService.updateLogConversationMessageSPY(message) == null)
                        {
                            logger.error("Erro na atualização da resposta [SPY].");
                        }
                }
              //logger.info("Fim da função ");
                return message;
            } catch (Exception e)
            {
                logger.error("processMessage ERROR: " + e.toString());
                return null;
            }
            finally
            {
                this.busy = false;
                detectConversationEnd(message);
            }
        }
        else
        {
            logger.error("processMessage não executado! status = " + status);
            return null;
        }
    }
    
    public boolean sendTextMessage (ChatMessage message) 
    {
        if (sender != null)
            return sender.sendTextMessage(message);
        return false;
    }

    public ChatMessage composeMessage (String pToSend)
    {
        ChatMessage localChat = new ChatMessage();
        localChat.setFrom(from);
        localChat.setInstanceName(instance.getInstanceName());
        localChat.setAiMessage(pToSend);
        
        return localChat;
    }
 
    private ChatMessage appFilter (ChatMessage message) 
    {
        StringBuilder matchingLines = new StringBuilder();
        String[] lines = message.getAiMessage().split("\\r?\\n");
        for (String line : lines) 
        {
            if (!line.startsWith(InstanceManager.APP_ID)) 
            {
                if (matchingLines.length() > 0) 
                {
                    matchingLines.append("\n"); // Add a newline before each line except the first
                }
                matchingLines.append(line);
            }
            else
                System.out.println("Ignoring " + line);
        }
        
        message.setAiMessage(matchingLines.toString());
        return message;
    }
    private boolean detectFragment (String pText, List<String> fragments)
    {
        for (String fragment : fragments)
        {
            if (pText.contains(fragment))
                return true;
        }
        return false;
    }
    private void detectConversationEnd (ChatMessage message) 
    {
        if (message == null)
            return;
        if (message.getClientMessage() == null)
            return;
        switch (message.getClientMessage().toLowerCase()) 
        {
            case "obrigado":
            case "obrigado.":
            case "fim":
                setStatus(CHAT_STATE.FINISHED);  //ChatManager vai encerrar a operação
                logger.info("detectConversationEnd!!!");
                break;
        }
        
        if (message.getAiMessage() == null)
            return;
        switch (message.getAiMessage().toLowerCase())
        {
            case "Se precisar de mais alguma coisa, pode me chamar":
                setStatus(CHAT_STATE.FINISHED);  //ChatManager vai encerrar a operação
                break;
        }
            
        if (message.getAiMessage().toLowerCase().contains("Até breve!")) 
        {
            setStatus(CHAT_STATE.FINISHED);  //ChatManager vai encerrar a operação
        }
    }
    
    public void finishConversation (String reason)
    {
        logger.info("finishConversation:");
        if (reason != null && !reason.isEmpty())
        {
            logger.info(reason + "\n");
            //macroSendMutiple(new GeneralSendContent(from, reason));
        }
        //currCap = "0";
    }
    private void detectClientName () 
    {
        if (userName != null)     //não precisa mais procurar
            return;
        if (messages == null || messages.isEmpty())
            return;
        tbLogConversationMessage lastMessage = messages.get(messages.size()-1);
        if (messages.size() <= 1)
            return;
        tbLogConversationMessage last2Message = messages.get(messages.size()-2);
        if (detectFragment(last2Message.getAiText(), fragWhoAmI))
        {
            String potencialName = lastMessage.getClientText();
            String potencialNameLC = potencialName.toLowerCase();
            if (potencialNameLC.contains("não") || potencialNameLC.contains("nao") || potencialNameLC.contains("negativo") || potencialNameLC.contains("porque"))
                return;
            userName = potencialName;
            logger.info("clientName: " + userName);
        }
    }    
    private List<String> getWhoIamTalkingoToFragment ()
    {
        List<String> list = new ArrayList<>();
        list.add("Posso saber com quem estou falando");
        list.add("Posso saber seu nome");
        list.add("Posso saber com quem estou falando");
        list.add("Posso saber como se chama");
        
        return list;
    }
    
    //Props

    public tbLogConversation getConv() {
        return conv;
    }

    public int getIdleTimeS() {
        return idleTimeS;
    }

    public void setIdleTimeS(int idleTimeS) {
        this.idleTimeS = idleTimeS;
    }

    public int getSilenceTimeS() {
        return silenceTimeS;
    }

    public void setSilenceTimeS(int silenceTimeS) {
        this.silenceTimeS = silenceTimeS;
    }

    public long getConversationStartTime() {
        return conversationStartTime;
    }

    public void setConversationStartTime(long conversationStartTime) {
        this.conversationStartTime = conversationStartTime;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public CHAT_STATE getStatus() {
        return status;
    }

    public void setStatus(CHAT_STATE status) {
        this.status = status;
    }

    public static AIInterface getAi() {
        return ai;
    }

    public static void setAi(AIInterface ai) {
        ChatManager.ai = ai;
    }

    public static ChatParams getParams() {
        return params;
    }

    public static void setParams(ChatParams params) {
        ChatManager.params = params;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Long getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(Long idConversation) {
        this.idConversation = idConversation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPushName() {
        return pushName;
    }

    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    public void setInstance(tbInstances instance) {
        this.instance = instance;
    }

    public List<tbLogConversationMessage> getMessages() {
        return messages;
    }

  
    public int getQtdMessages() 
    {
        if (messages != null)
            return messages.size();
        else
            return 0;
    }

    public tbInstances getInstance() {
        return instance;
    }

    
}
