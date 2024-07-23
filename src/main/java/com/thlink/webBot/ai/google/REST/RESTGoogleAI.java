package com.thlink.webBot.ai.google.REST;

import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.Content;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerateContentBody;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerationConfig;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.Parts;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.SafetySettings;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.SystemInstruction;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response.Candidate;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response.ResponseContent;
import com.thlink.webBot.ai.google.REST.JSON.ReqAnsw;
import static com.thlink.webBot.misc.google.GoogleHelper.MODEL;
import static com.thlink.webBot.misc.google.GoogleHelper.USER;
import com.thlink.webBot.persistence.entities.tbAiParameters;
import com.thlink.webBot.persistence.entities.tbAiQuestionAndAnswers;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.webbot.manager.MacroParams;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class RESTGoogleAI implements AIInterface
{
    private static final Logger logger = Logger.getLogger(RESTGoogleAI.class);

    private static final String GOOGLE_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/{AI_MODEL}:generateContent?key=";
    private static String GOOGLE_URL;
    public static final String GOOGLE_QUESTIONS = "./GoogleQuestions.csv";
    private GenerateContentBody initialContent;
    private RestHelper h;

    @Override
    public boolean startAI (MacroParams pMacroParams) 
    {
        try
        {
          //logger.info("RESTGoogleAI.start");
            h = new RestHelper();
            //params = pAiParms.getParams();

            //Obtém initialContent da base de dados
            //initialContent = PersAI.getAiInitialContent(pAiParms.getInstance().getIdAi());
            initialContent = getAiInitialContent(pMacroParams.getInstance().getAiParameters());
            String strBody = GenerateContentBody.objToJSON(initialContent);
          //System.out.println("strBody = \n" + strBody);
            
            GOOGLE_URL = GOOGLE_BASE_URL.replace("{AI_MODEL}", pMacroParams.getInstance().getAiParameters().getModel()).concat(pMacroParams.getInstance().getAiParameters().getApiKey());
            ReqAnsw r = h.sendPost2(GOOGLE_URL, strBody);
            if (r.getResponseCode() == 200)
            {
              //System.out.println("GenerateContentBody Deu certo: \n" + r.getResponse());
              //logger.info("GenerateContentBody OK!");
                return true;
            }
            logger.error("GenerateContentBody ERRO: " + r.getResponseCode());
            logger.error("GenerateContentBody response: " + r.getResponse());
            //System.out.println(r.getResponse());
            return false;
        } catch (Exception e)
        {
            logger.error("startAI ERROR: " + e.toString());
            return false;
        }
    }

    @Override
    public String getResponseAI (String inputText) 
    {
        logger.info("getResponseAI: " + inputText);
        Content question = new Content("USER", new Parts(inputText));
        initialContent.getContents().add(question);
        String strBody = GenerateContentBody.objToJSON(initialContent);
      //System.out.println("strBody INI: \n" + strBody);
      //initialContent.getContents().remove(initialContent.getContents().size()-1); //remove the last one.
        ReqAnsw r = h.sendPost2(GOOGLE_URL, strBody);
        if (r.getResponseCode() == 200)
        {
          //System.out.println(r.getResponse());
            ResponseContent rc = ResponseContent.jsonToObj(r.getResponse());
            if (rc != null)
            {
                if (rc.getCandidates() != null && !rc.getCandidates().isEmpty())
                {
                    com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Response.Content answerContent = rc.getCandidates().get(0).getContent();
                    Content toAddRequest = new Content("MODEL", new Parts(answerContent.getParts().get(0).getText()));
                    initialContent.getContents().add(toAddRequest);
                    for (Candidate cand : rc.getCandidates())
                    {
                        String response = cand.getContent().getParts().get(0).getText();
                        logger.info("AI  resposta: " + response);
                        return response.replaceAll("\\n+$", "");
                    }
                }
            }
            return "Erro na resposta (sem candidatos) ";
        }
        else
            return "Erro na resposta: " + r.getResponseCode();
    }
    
    public static GenerateContentBody getAiInitialContent (tbAiParameters pAiParams) 
    {
        GenerateContentBody body = new GenerateContentBody();
        
        SystemInstruction systemInstruction = new SystemInstruction();
        systemInstruction.setParts(new Parts(pAiParams.getInstructions()));
        body.setSystemInstruction(systemInstruction);
        
        GenerationConfig generationConfig = new GenerationConfig();
        generationConfig.setMaxOutputTokens(200);
        generationConfig.setTemperature(0.2);
        generationConfig.setTopK(40);
        generationConfig.setTopP(0.8);

        SafetySettings safetySettings = new SafetySettings();
        safetySettings.setCategory("HARM_CATEGORY_SEXUALLY_EXPLICIT");
        safetySettings.setThreshold("BLOCK_LOW_AND_ABOVE");

        body.setGenerationConfig(generationConfig);
        body.setSafetySettings(safetySettings);
       
        //XXX
        List<tbAiQuestionAndAnswers> qas = pAiParams.getQuestions();
         if (qas == null)
            return body;
         List<Content> contents = new ArrayList<>();
         for (tbAiQuestionAndAnswers qa : qas)
         {
             if (qa.getQuestion() != null && !qa.getQuestion().startsWith("#"))
             {
                Content cq = new Content(USER, new Parts(qa.getQuestion()));
                Content ca = new Content(MODEL, new Parts(qa.getAnswer()));
                contents.add(cq);
                contents.add(ca);
             }
             /*else
                logger.info("Ignorando QA. Q = " + qa.getQuestion());*/
         }        
        body.setContents(contents);
        return body;
    }
    
    
}
