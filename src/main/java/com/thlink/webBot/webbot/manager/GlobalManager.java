package com.thlink.webBot.webbot.manager;

//Esta classe gerencia o conjunto de Managers (um por instância) e valida tanto o cliente quanto as instâncias, regularmenet.

import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.nonDB.AIEngine;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.persistence.nonDB.EvolutionParams;
import com.thlink.webBot.persistence.nonDB.GoogleAIParams;
import com.thlink.webBot.persistence.nonDB.Messsaging;
import com.thlink.webBot.persistence.nonDB.ParamsDBHelper;
import com.thlink.webBot.sender.Evolution.EvolutionSender;
import com.thlink.webBot.service.AiParametersService;
import com.thlink.webBot.service.ChatManagerService;
import com.thlink.webBot.service.ClientService;
import com.thlink.webBot.service.ConversationMessageService;
import com.thlink.webBot.service.ConversationService;
import com.thlink.webBot.service.GeneralService;
import com.thlink.webBot.service.InstanceService;
import static com.thlink.webBot.webbot.manager.GlobalManager.eEVOL_STATE.CONNECTED;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
@EnableScheduling
public class GlobalManager 
{
    private static final Logger logger = LoggerFactory.getLogger(GlobalManager.class); 
    public enum eEVOL_STATE { NOT_CONNECTED, CONNECTED }
    private final ConcurrentHashMap<String, InstanceManager> ccHMManagers;
    
    @Autowired
    private final GeneralService generalSrv;
    private final ClientService clientSrv;
    private final InstanceService instanceSrv;
    private final AiParametersService aiParamsSrv;
    private final ChatManagerService chatManagerSrv;
    private final ConversationService convSrv;
    private final ConversationMessageService msgSrv;
    
    private ChatParams params;
    private final EvolutionSender testEvolution = new EvolutionSender();
    private eEVOL_STATE evolState;
    public GlobalManager (GeneralService pGeneralSrv,   ClientService pClientSrv, 
                          InstanceService pInstanceSrv, AiParametersService pAiParamsSrv, 
                          ChatManagerService pChatManagerSrv,
                          ConversationService pConversationSrv, ConversationMessageService pMsgSrv)
    {
        logger.info("GlobalManager(@PostConstruct) recebendo services no construtor.");
        generalSrv = pGeneralSrv;
        clientSrv = pClientSrv;
        instanceSrv = pInstanceSrv;
        aiParamsSrv = pAiParamsSrv;
        chatManagerSrv = pChatManagerSrv;
        convSrv = pConversationSrv;
        msgSrv = pMsgSrv;
        
        ccHMManagers = new ConcurrentHashMap<>();
    }
    
    @PostConstruct
    //O evento de inicialização já não é tão relevante, pois os dados sendo alterados no banco devem ser considerados.
    public boolean startGlobalManager ()
    {
        logger.info("startManagerMulti(@PostConstruct)");
        try
        {
            evolState = eEVOL_STATE.NOT_CONNECTED;
            return true;
        } catch (Exception e)
        {
            logger.error("startManagerMulti ERROR: " + e.toString());
            return false;
        }
    }

    @PreDestroy
    public void cleanup() {
        logger.info("@PreDestroy");
        // Cleanup logic
    }
    
    //Buscar os clientes e manter no CCHH somente as instâncias ativas.
    @Scheduled(fixedRate = 5000) // runs every 5 seconds
    public void refreshClients () 
    {
        //Verifica evolution
        if (!testEvolution.waitForEvolution(8080, "localhost"))
        {
            logger.error("Evolution not available.");
            evolState = eEVOL_STATE.NOT_CONNECTED;
            return;
        }
        evolState = eEVOL_STATE.CONNECTED;
        
        //logger.info("myTask @Scheduled(fixedRate)");
        var listClients = clientSrv.getAllEntities();
      //logger.info("Qtd: " + listClients.size());
        for (tbCadClients client : listClients)
        {
            if (validateClient(client))
            {
                for (tbInstances inst : client.getInstances())
                {
                    if (ccHMManagers.get(inst.getInstanceName()) != null)   //instância já existe em ccHH
                    {
                        //não precisa fazer nada
                    }
                    else    //criar
                    {
                        logger.info("Criando ChatManagerMulti para cliente " + client.getName() + " instancia " + inst.getInstanceName());
                        InstanceManager manager = new InstanceManager(generalSrv, clientSrv, instanceSrv, aiParamsSrv, chatManagerSrv, convSrv, msgSrv);
                        
                        //Inserir dados em ChatParams
                        params = new ParamsDBHelper().load();      //only one!
                        AIEngine aiEngine = new AIEngine();
                        aiEngine.setEngine(AIEngine.eAiEngines.GOOGLE);
                        GoogleAIParams googleAIParams = new GoogleAIParams();
                        googleAIParams.setApiKey(inst.getAiParameters().getApiKey());
                        googleAIParams.setProjectID(inst.getAiParameters().getProjectId());
                        aiEngine.setGoogleAIParams(googleAIParams);
                      //googleAIParams.setCredentials(inst.getAiParameters().getCredentials());
                        params.setAiEngine(aiEngine);

                        Messsaging messaging = new Messsaging();
                        {
                            EvolutionParams ep = new EvolutionParams();
                            ep.setInstanceName(inst.getInstanceName());
                            ep.setApiKey(inst.getEvolApiKey());
                            messaging.setEvolutionParams(ep);
                        }
                        params.setMessaging(messaging);
                        MacroParams mp = new MacroParams(8080, "localhost", client, inst);
                        logger.info("Calling manager.startMultiManager()!!!");
                        if (manager.startMultiManager(mp))
                        {
                            logger.info("Manager criado com sucesso.");
                            ccHMManagers.put(inst.getInstanceName(), manager);
                            dumpManagerCuncurrentHM();
                        }
                    }
                }
            }
            else
            {
                //Verifica se o client tem instâncias ativas. Se tiver, devem ser retirados
                for (tbInstances inst : client.getInstances())
                {
                    InstanceManager dyingManager = ccHMManagers.get(inst.getInstanceName());
                    if (dyingManager != null)
                    {
                        //Antes de remover, finalizar!
                        dyingManager.stopMultiManager(0);
                        ccHMManagers.remove(inst.getInstanceName());
                        dumpManagerCuncurrentHM();
                    }
                }
            }
        }
        
    }
    
    private boolean validateClient (tbCadClients cli) 
    {
        Date now = Calendar.getInstance().getTime();
        if (cli.isActive())
        {
            if (cli.getSinceDate() == null || now.compareTo(cli.getSinceDate()) > 0)
            {
                if (cli.getUntilDate() == null || now.compareTo(cli.getUntilDate()) < 0)
                {
                  //logger.info("Cli válido: " + cli.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private void dumpManagerCuncurrentHM () 
    {
        StringBuilder sb = new StringBuilder();
        if (!ccHMManagers.isEmpty())
        {
            for (Entry<String, InstanceManager> entry : ccHMManagers.entrySet())
            {
                sb.append("\tInstance Name: ").append(entry.getKey()).append("\n");
            }
        }
        else
            sb.append("Vazio.");
        logger.info("dump de ccHH: \n" + sb.toString());
    }
    
    //O Manager já foi criado (baseado nos clientes e instâncias ativas no banco). 
    public ChatMessage dispatchUserMessage (ChatMessage message) 
    {
        if (evolState.equals(CONNECTED))
        {
            InstanceManager localMultiManager = ccHMManagers.get(message.getInstanceName());
            if (localMultiManager == null)
            {
                logger.error("Mensagem recebida para instância que não tem manager: " + message.getInstanceName());
                return null;            
            }
            ChatMessage reply = localMultiManager.respondUserMessage(message); 
            return reply;
        }
        else
        {
            logger.error("Mensagem IGNORADA por Evolution não está respondendo.");
            return null;
        }
        
        
    }
    
    public String getValue ()
    {
        return "dslfkadjsflasdkfjasdlfkahsd";
    }

    public String getEvolState() {
        return evolState.toString();
    }
    
    public String getQtdInstanceManagers () {
        int s;
        if (ccHMManagers != null)
            s = ccHMManagers.size();
        else s = -1;
        return String.format("%d", s);
    }

    public String getIMDetails ()
    {
        StringBuilder sb = new StringBuilder();
        
        for (InstanceManager im : ccHMManagers.values())
        {
            sb.append(im.getInstanceDetais()).append("\n");
        }
      //logger.info("getIMDetails: " + sb.toString());
        return sb.toString();
    }
}
