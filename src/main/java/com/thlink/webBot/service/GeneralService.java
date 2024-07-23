package com.thlink.webBot.service;

import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.Content;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerateContentBody;
import com.thlink.webBot.misc.google.GoogleHelper;
import static com.thlink.webBot.misc.google.GoogleHelper.MODEL;
import static com.thlink.webBot.misc.google.GoogleHelper.USER;
import com.thlink.webBot.persistence.entities.tbAcceptedTelephones;
import com.thlink.webBot.persistence.entities.tbAiParameters;
import com.thlink.webBot.persistence.entities.tbAiQuestionAndAnswers;
import com.thlink.webBot.persistence.entities.tbBlockedTelephones;
import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.persistence.entities.tbKeyWords;
import com.thlink.webBot.persistence.entities.tbLogConversation;
import com.thlink.webBot.persistence.entities.tbLogConversationMessage;
import com.thlink.webBot.persistence.entities.tbPostConversationQuestions;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.persistence.nonDB.ParamsHelper;
import com.thlink.webBot.repository.AcceptedNumbersRepository;
import com.thlink.webBot.repository.AiParametersRepository;
import com.thlink.webBot.repository.BlockedNumbersRepository;
import com.thlink.webBot.repository.ChatManagerRepository;
import com.thlink.webBot.repository.ClientsRepository;
import com.thlink.webBot.repository.ConversationMessageRepository;
import com.thlink.webBot.repository.ConversationRepository;
import com.thlink.webBot.repository.InstanceRepository;
import com.thlink.webBot.repository.KeyWordsRepository;
import com.thlink.webBot.repository.PostConversationQuestionsRepository;
import com.thlink.webBot.repository.QandARepository;
import com.thlink.webBot.webbot.manager.InstanceManager;
import com.thlink.webBot.webbot.manager.InstanceManager.eOPER;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.SECOND;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GeneralService 
{
    private static final Logger logger = Logger.getLogger(GeneralService.class);
    
    private final ClientsRepository clientRepo;
    private final InstanceRepository instanceRepo;
    private final AiParametersRepository aiParametersRepo;
    private final ChatManagerRepository chatManagerRepo;
    private final ConversationRepository conversationRepo;
    private final ConversationMessageRepository conversationMessageRepo;
    private final BlockedNumbersRepository blockedNumbersRepo;
    private final AcceptedNumbersRepository acceptedNumbersRepo;
    private final QandARepository qaRepo;
    private final PostConversationQuestionsRepository postQRepo;
    private final KeyWordsRepository keyRepo;
    
    private final Random random = new Random();
    private List<String> steps;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public GeneralService (ClientsRepository pClientRepo, InstanceRepository pInstRepo, 
            AiParametersRepository pAiParametersRepo, ChatManagerRepository pChatManagerRepo, 
            ConversationRepository pConversationRepo, ConversationMessageRepository pConversationMessageRepo, 
            QandARepository pQARepo,
            BlockedNumbersRepository pBlockedNumbersRepo, AcceptedNumbersRepository pAcceptedNumbersRepo, 
            PostConversationQuestionsRepository pPostQRepo, KeyWordsRepository pKeyRepo) 
    {
        this.clientRepo = pClientRepo;
        this.instanceRepo = pInstRepo;
        this.aiParametersRepo = pAiParametersRepo;
        this.chatManagerRepo = pChatManagerRepo;
        this.conversationRepo= pConversationRepo;
        this.conversationMessageRepo = pConversationMessageRepo;
        this.qaRepo = pQARepo;
        this.blockedNumbersRepo = pBlockedNumbersRepo;
        this.acceptedNumbersRepo = pAcceptedNumbersRepo;
        this.postQRepo = pPostQRepo;
        this.keyRepo = pKeyRepo;
    }
    
    public List<tbLogConversation> listConverstionsFromInstanceAndDates (Long pIdInstance, Date pStartDate, Date pFinishDate) 
    {
        List<tbLogConversation> x = 
        entityManager.createQuery("SELECT   C FROM tbLogConversation C " +
                                  "JOIN     C.instance I " + 
                                  "WHERE    I.id = :pInstance AND C.startTime > :pStartDate AND C.finishTime < :pFinishDate " +
                                  "ORDER    BY C.id ")
                     .setParameter("pInstance", pIdInstance)
                     .setParameter("pStartDate" , pStartDate)
                     .setParameter("pFinishDate", pFinishDate)
                     .getResultList();
        
        return x;
    }

    private boolean deleteAll ()
    {
        try
        {
            logger.info("Deleting all...");
            
            /*logger.info("\tDeleting blockedNumbers ...");
            blockedNumbersRepo.deleteAll();
            logger.info("\tDeleting acceptedNumbers ...");
            acceptedNumbersRepo.deleteAll();
            //logger.info("\tDeleting keywords ...");
            //keyRepo.deleteAll();
            logger.info("\tDeleting conversationMessage...");
            conversationMessageRepo.deleteAll();
            logger.info("\tDeleting conversation...");
            conversationRepo.deleteAll();
            logger.info("\tDeleting chatManager...");
            chatManagerRepo.deleteAll();
            logger.info("\tDeleting aiParameters ...");
            aiParametersRepo.deleteAll();*/
            logger.info("\tDeleting instance...");
            instanceRepo.deleteAll();
            logger.info("\tDeleting client...");
            clientRepo.deleteAll();
            logger.info("Deleted!!\n");
            return true;
        } catch (Exception e)
        {
            logger.error("deleteAll ERROR: " + e.toString());
            return false;
        }
    }
    
    private tbCadClients populateClient (String pName)
    {
        logger.info("Salvando cliente");
        tbCadClients client = new tbCadClients();
        client.setActive(true);
        client.setName(pName);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 0);
        client.setSinceDate(c.getTime());
        c.add(Calendar.YEAR, +1);
        client.setUntilDate(c.getTime());
        client = clientRepo.saveAndFlush(client);
        return client;
    }
    
    private tbInstances populateInstance (tbCadClients client, String pInstanceName)
    {
        logger.info("Salvando instância");
        tbInstances instance = new tbInstances();
        instance.setActive(true);
        instance.setDescription(pInstanceName);
      //instance.setEvolApiKey("o1onuea7p8xnfjobl1xbi");
        instance.setEvolGlobalKey("B6D711FCDE4D4FD5936544120E713976");
        instance.setType(1);
        instance.setTempoNovaConversaH(24);
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.YEAR, 0);
        instance.setSinceDate(c2.getTime());
        c2.add(Calendar.YEAR, 1);
        instance.setUntilDate(c2.getTime());
        instance.setCadClient(client);
        instance.setInstanceName(pInstanceName);
        instance.setManagerIntervalS(10);
        instance.setMaxIdleTimeS(60);
        instance.setMaxDurationS(10 * 60);
        instance.setAnswerMyself(Boolean.TRUE);
        instance.setShowDashBoard(Boolean.FALSE);

        instance = instanceRepo.saveAndFlush(instance);
        return instance;
    }
            
    private tbAiParameters populateAiParameters (tbInstances instance, String prompt)
    {
        logger.info("Salvando parâmetros");
        tbAiParameters aiParams = new tbAiParameters();
        aiParams.setName("google");
        aiParams.setApiKey("AIzaSyBycQebz50nrktuhr_vpWdekOI7bdiAmJM");
        aiParams.setModel("gemini-1.5-flash");
        aiParams.setProjectId("ThLinkAI");
        //String prompt = params.getAiEngine().getGoogleAIParams().getPromptFileName();
        if (prompt == null || prompt.isEmpty())
        {
            steps.add("Sem Arquivo de configuração.");
            logger.error(steps.get(steps.size()-1));
            return null;
        }
        File f = new File(prompt);
        if (!f.exists())
        {
            steps.add("Arquivo de configuração não existe.");
            logger.error(steps.get(steps.size()-1));
            return null;
        }
        GoogleHelper gh = new GoogleHelper();
        GenerateContentBody initialContent = gh.loadQuestionsFromFile(prompt);
        if (initialContent == null || initialContent.getContents().isEmpty())
        {
            logger.error("Sem dados");
        }
        aiParams.setInstructions(initialContent.getSystemInstruction().getParts().getText());
        aiParams.setInstance(instance);
        List<tbAiQuestionAndAnswers> questions = new ArrayList<>();
        

        logger.info("Lendo questões");
        tbAiQuestionAndAnswers qa = new tbAiQuestionAndAnswers();
        for (int i = 0; i < initialContent.getContents().size(); i++)
        {
          //logger.info("Questão " + i);
            Content content = initialContent.getContents().get(i);
            if (content.getRole().equals(USER))   //par
            {
                qa = new tbAiQuestionAndAnswers();
                qa.setAiParameters(aiParams);
                qa.setQuestion(content.getParts().getText());
            }
            else if (content.getRole().equals(MODEL))   //pa
            {
                qa.setAnswer(content.getParts().getText());
                if (qa.getQuestion() != null)
                {
                    questions.add(qa);
                  //logger.info("Added: " + i);
                }
                else
                    System.out.println("Ignorando ....");
            }
            else
                logger.info("Ouro role: " + content.getRole());
        }
        logger.info("questions.size " + questions.size());
        aiParams.setQuestions(questions);
        
        aiParams = aiParametersRepo.saveAndFlush(aiParams);
        return aiParams;
    }
    
    private int populateListTelephones (boolean pAccepted, tbInstances instance, int pQtd)
    {
        int qtd = 0;
        for (int i = 0; i < pQtd; i++)
        {
            if (!pAccepted)
            {
                tbBlockedTelephones b = new tbBlockedTelephones();
                b.setNumber(generateRandomUserNumber());
                b.setInstance(instance);
                b = blockedNumbersRepo.saveAndFlush(b);
                if (b != null)
                    qtd++;
                else
                    break;
            }
            else
            {
                tbAcceptedTelephones b = new tbAcceptedTelephones();
                b.setNumber(generateRandomUserNumber());
                b.setInstance(instance);
                b = acceptedNumbersRepo.saveAndFlush(b);
                if (b != null)
                    qtd++;
                else
                    break;
            }
        }
        return qtd;
    }
    
    
    private List<tbLogConversation> populateConversations (tbInstances instance, InstanceManager.eOPER pType, 
                                                     int pNumConversationToGenerate, int pMinDelayS, int pMaxDelayS) 
    {
        logger.info("\tpopulateConversations");
        int conversationLengthMin = 60;
        List<tbLogConversation> listConv = new ArrayList<>();
        tbLogConversation conv = null;
        Calendar c = Calendar.getInstance();
        String userNumber = generateRandomUserNumber();
        //c.add(Calendar.DAY_OF_YEAR, -20);
                
        for (int i = 1; i <= pNumConversationToGenerate; i++)
        {
            logger.info("\t\tConversation " + i);
            
            
            int numMessages = (int)(Math.random()*10)+1; 
            conv = new tbLogConversation();
            conv.setIsContact(Boolean.TRUE);
            conv.setPushName("Client " + i);
            conv.setUserName("Client da Silva " + i);
            conv.setUserNumber(userNumber);
            conv.setQtdIterations(numMessages);
            conv.setInstance(instance);
            conv.setStartTime(c.getTime());
            c.add(Calendar.MINUTE, conversationLengthMin); //conversa durou 1h
            conv.setFinishTime(c.getTime());
            c.add(Calendar.MINUTE, -conversationLengthMin); //back to startTime
            conv.setScoreM1(5d);
            conv.setScoreM2(6d);
            conv.setScoreM3(7d);
            conv.setStartedByMe(false);
            conv.setScoreM1(5d);
            conv.setStatus(4);

            List<tbLogConversationMessage> list = new ArrayList<>();
            for (int j = 1; j <= numMessages; j++)
            {
                tbLogConversationMessage m1 = new tbLogConversationMessage();
                m1.setConversation(conv);
                m1.setUserNumber(userNumber);
                m1.setClientMoment(c.getTime());
                m1.setClientText(String.format("client %d-%d", conv.getId(), j));
                int delaySecs = getNormalDistributedInt(pMinDelayS, pMaxDelayS);
              //System.out.println("delaySecs = " + delaySecs);
                c.add(SECOND, delaySecs);
                if (pType.equals(eOPER.SPY))
                {
                    m1.setManualMoment(c.getTime());
                    m1.setManualText(String.format("manual %d-%d", conv.getId(), j));
                }
                else
                {
                    m1.setAiMoment(c.getTime());
                    m1.setAiText(String.format("AI %d-%d", conv.getId(), j));
                }
                list.add(m1);
                //if (saveObj(m1) == null)
                //    return null;
                //c.add(MINUTE, 5);    //soma mais um tempo para próxima pergunta.
                c.add(Calendar.MINUTE, -20);    //próxima conversa sobrepoe-se com a primeira

            }
            conv.setLogConversationMessage(list);
            conv = conversationRepo.saveAndFlush(conv);
            listConv.add(conv);
        }
        return listConv;
    }
    
    private List<tbPostConversationQuestions> populatePostQuestions (tbInstances instance) 
    {
        List<tbPostConversationQuestions> list = new ArrayList<>();
        tbPostConversationQuestions q;
                
        q = new tbPostConversationQuestions();
        q.setInstance(instance);
        q.setQuestion("Qual o nome o cliente informou durante a conversa?");
        q.setType(null);
        q.setWeight(null);
        q = postQRepo.saveAndFlush(q);
        list.add(q);
        
        q = new tbPostConversationQuestions();
        q.setInstance(instance);
        q.setQuestion("Durante esta conversa foi agendada uma visita? Se sim, para quando?");
        q.setType(null);
        q.setWeight(null);
        q = postQRepo.saveAndFlush(q);
        list.add(q);
        
        q = new tbPostConversationQuestions();
        q.setInstance(instance);
        q.setQuestion("De 0 a 10, como você avalia o potencial desta conversa (só o número)?");
        q.setType(2);
        q.setWeight(1);
        q = postQRepo.saveAndFlush(q);
        list.add(q);
        
        q = new tbPostConversationQuestions();
        q.setInstance(instance);
        q.setQuestion("O cliente informou o nome durante a conversa (sim ou não)?");
        q.setType(1);
        q.setWeight(5);
        q = postQRepo.saveAndFlush(q);
        list.add(q);
                        
        q = new tbPostConversationQuestions();
        q.setInstance(instance);
        q.setQuestion("O cliente agendou visita durante a conversa (sim ou não)?");
        q.setType(1);
        q.setWeight(5);
        q = postQRepo.saveAndFlush(q);
        list.add(q);
        return list;
    }

    private List<tbKeyWords> populateKeyWords (tbInstances pInstance)
    {
        List<tbKeyWords> list = new ArrayList<>();
        list.add(keyRepo.saveAndFlush(new tbKeyWords("agendar", 5, pInstance)));
        list.add(keyRepo.saveAndFlush(new tbKeyWords("visita", 5, pInstance)));
        list.add(keyRepo.saveAndFlush(new tbKeyWords("marcar", 5, pInstance)));
        list.add(keyRepo.saveAndFlush(new tbKeyWords("desistir", -1, pInstance)));
        list.add(keyRepo.saveAndFlush(new tbKeyWords("cancelar", -2, pInstance)));
        list.add(keyRepo.saveAndFlush(new tbKeyWords("confirmar", 3, pInstance)));
        return list;
    }
    
    @Transactional
    public List<String> populateDB (boolean pDeleteFirst, eOPER pType, int pNumConversations, Integer minDelayS, Integer maxDelayS)
    {
        logger.info("populateDB");
        steps = new ArrayList<>();
        ChatParams params = ParamsHelper.getFileParams();   //pode ser!
        if (params == null)
        {
            steps.add("Erro na recuperação do arquivo de parãmetros.");
            logger.error(steps.get(steps.size()-1));
            return steps;
        }
        if (pDeleteFirst)
        {
            steps.add("Delete all...");
            if (!deleteAll())
            {
                steps.add("Erro ao deletar dados.");
                logger.error(steps.get(steps.size()-1));
                return steps;
            }
        }
        tbCadClients client;
        if ( (client = populateClient("Auto Cliente")) == null )
        {
            steps.add("Erro ao salvar cliente.");
            logger.error(steps.get(steps.size()-1));
            return steps;
        }
        steps.add("Cliente salvo. nome = " + client.getName());

        tbInstances instance;
        if ( (instance = populateInstance(client, "box")) == null )
        {
            steps.add("Erro ao salvar instância.");
            logger.error(steps.get(steps.size()-1));
            return steps;
        }
        steps.add("Instância salva. nome = " + instance.getInstanceName());

        tbAiParameters aiParams;
        if ( (aiParams = populateAiParameters(instance, params.getAiEngine().getGoogleAIParams().getPromptFileName())) == null)
        {
            steps.add("Erro ao salvar parâmetros da AI.");
            logger.error(steps.get(steps.size()-1));
            return steps;
        }
        steps.add("Parâmetros da AI salvos com sucesso. Modelo = " + aiParams.getModel());

        List<tbAiQuestionAndAnswers> questions = qaRepo.saveAll(aiParams.getQuestions());
        if (questions == null)
            logger.error("Erro ao salvar perguntas e respostas.");
        else
            steps.add("Parâmetros da AI (com perguntas e respostas) salvos com sucesso. questions = " + questions.size());

        logger.info("Salvando perguntas e respostas.");
        
        
        logger.info("Salvando conversações & mensagens.");
        steps.add(String.format("Salvando %d conversações ", pNumConversations));
        if (minDelayS == null)
            minDelayS = 60;     //1minuto
        if (maxDelayS == null)
            maxDelayS = 60*3;    //3 minutos
        
        steps.add("MinDelayS = " + minDelayS);
        steps.add("MaxDelayS = " + maxDelayS);
        
        List<tbLogConversation> convs = populateConversations(instance, pType, pNumConversations, minDelayS, maxDelayS);
        if (convs == null)
        {
            steps.add("Erro ao salvar conversação.");
            logger.error(steps.get(steps.size()-1));
            return steps;
        }
        int qtdMsg = 0;
        for (tbLogConversation conv : convs)
        {
            steps.add(String.format("\tSalvando %d mensagens", conv.getLogConversationMessage().size()));
            qtdMsg += conv.getLogConversationMessage().size();
        }
        if (pNumConversations > 0)
            steps.add(String.format("%d conversações e %d mensagens salvas", pNumConversations, qtdMsg));

        logger.info("Pos-conversation questions: ");
        List<tbPostConversationQuestions> postQuestions = populatePostQuestions(instance);
        if (postQuestions != null && !postQuestions.isEmpty())
            steps.add(String.format("%d post-Questions salvas", postQuestions.size()));

        steps.add("Conversao salva. ");
        
        int qtdBlocked = populateListTelephones(false, instance, 100);
        steps.add("Telefones bloqueados: " + qtdBlocked);
        
        int qtdAccepted = populateListTelephones(true, instance, 10);
        steps.add("Telefones aceitos: " + qtdAccepted);

        List<tbKeyWords> listKeyWords = populateKeyWords(instance);
        if (listKeyWords == null)
            steps.add("Erro ao inserir keywords ");
        else
            steps.add("Keywords inseridas com sucesso: " + listKeyWords.size());
        return steps;
    }
    
    private String generateRandomUserNumber() 
    {
        int area = (int)(Math.random()*100);
        int g41 = (int)(Math.random()*10000);
        int g42 = (int)(Math.random()*10000);
        String number = String.format("%s%02d%04d%04d", "55", area, g41, g42);
        return number;
    }

    public Integer getNormalDistributedInt (int min, int max) 
    {
        // Calculate mean and standard deviation based on min and max
        double mean = (min + max) / 2.0;
        double stdDev = (max - min) / 6.0; // Assuming 99.7% values within min and max
        
        // Generate a normally distributed value
        double gaussian = random.nextGaussian() * stdDev + mean;
        
        // Clamp the value within the min and max bounds and return as an integer
        int result = (int) Math.round(gaussian);
        if (result < min) {
            result = min;
        } else if (result > max) {
            result = max;
        }
        return result;
    }

    
}
