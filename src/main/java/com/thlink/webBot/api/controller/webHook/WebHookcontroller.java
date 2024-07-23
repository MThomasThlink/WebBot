package com.thlink.webBot.api.controller.webHook;

import com.thlink.webBot.ai.google.REST.JSON.WebHook.upsert.MessageUpsertWebHookBody;
import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.webbot.manager.GlobalManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/multiwebhook")
public class WebHookcontroller {
    Logger logger = LoggerFactory.getLogger(WebHookcontroller.class);
 
    private final GlobalManager globalManager;

    public WebHookcontroller(GlobalManager pGlobalManager) {
        this.globalManager = pGlobalManager;
    }
    
    //@PostMapping(value="/multiwebhook/messages-upsert")
    @PostMapping("/{instanceName}/{command}")
    public ResponseEntity<String> multiWebHook (HttpServletRequest request, 
                                                @PathVariable String instanceName,
                                                @PathVariable String command,
                                                @RequestBody MessageUpsertWebHookBody myPojo)
    {   
        logger.info("multiWebHook from " + instanceName);
        try 
        {
            if (myPojo == null)
            {
                logger.error("multiWebHook myPojo == null ");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            if (!command.equals("messages-upsert"))
            {
                logger.error("!messages-upsert: " + command);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            //String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            ChatMessage message = MessageUpsertWebHookBody.convertIncomingMessage(myPojo);
            if (message == null)
            {
                
                logger.error("Erro na conversão da mensagem MessageUpsertWebHookBody: \n");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            ChatMessage reply;
            if (globalManager != null)
            {
                reply = globalManager.dispatchUserMessage(message);
                if (reply != null)
                {
                    logger.info("multiWebHook reply != null");
                  //logger.info("reply.toString() = " + reply.toString());
                    
                }
                else
                    logger.error("multiWebHook reply == null");
            }
            else
                logger.error("No manager to call");
            
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception as needed
            logger.error("multiWebHook ERROR: " + e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
         finally
         {
             
         }
    }
}
