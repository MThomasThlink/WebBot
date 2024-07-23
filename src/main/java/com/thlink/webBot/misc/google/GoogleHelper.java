package com.thlink.webBot.misc.google;

import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.Content;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerateContentBody;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.GenerationConfig;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.Parts;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.SafetySettings;
import com.thlink.webBot.ai.google.REST.JSON.GenerateContent.Request.SystemInstruction;
import static com.thlink.webBot.ai.google.REST.RestHelper.ENCODING;
import com.thlink.webBot.service.GeneralService;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;


public class GoogleHelper 
{
    private static final Logger logger = Logger.getLogger(GoogleHelper.class);
    public static final String USER = "USER";
    public static final String MODEL = "MODEL";
    
    public GenerateContentBody loadQuestionsFromFile (String pFileName)
    {
        logger.info("loadQuestionsFromFile: " + pFileName);
        try
        {
            GenerateContentBody body = new GenerateContentBody();
          //FileReader filereader = new FileReader(pFileName); 
            List<Content> contents = new ArrayList<>();
            
            String content = new String(Files.readAllBytes(Paths.get(pFileName)), ENCODING);
            content = content.replace("\n", "").replace("\"", "");
            String[] values = content.split("[;\\r\\n]+");
            int ctdPrompts = 0;
            for (int i = 0; i < values.length; i+=2)
            {
                switch (i) 
                {
                    case 0:
                        //logger.info(String.format("*%s: %s.", values[i], values[i+1]));
                        break;
                    case 2:
                        //logger.info(String.format("+%s: %s.", values[i], values[i+1]));
                        SystemInstruction systemInstruction = new SystemInstruction();
                        systemInstruction.setParts(new Parts(values[i+1]));
                        body.setSystemInstruction(systemInstruction);
                        break;
                    default:
                        ctdPrompts++;
                        logger.info(String.format("\t[%d] %s: %s", ctdPrompts, values[i], values[i+1]));
                        contents.add(new Content(USER,  new Parts(values[i])));
                        contents.add(new Content(MODEL,  new Parts(values[i+1])));
                        
                        break;
                }
            }
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
            body.setContents(contents);        
            logger.info("loadQuestionsFromFile OK " + contents.size());
            return body;
        } catch (Exception e)
        {
            logger.error("loadQuestionsFromFile ERROR: " + e.toString());
            return null;
        }
    }
    
}
