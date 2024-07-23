package com.thlink.webBot.api.controller.inspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thlink.webBot.persistence.entities.tbAiParameters;
import com.thlink.webBot.persistence.entities.tbCadClients;
import com.thlink.webBot.persistence.entities.tbInstances;
import com.thlink.webBot.report.ReportBuilder;
import com.thlink.webBot.report.reply.ReportReplyParameters;
import com.thlink.webBot.report.reply.graph.ReportReplyGraphResponse;
import com.thlink.webBot.report.reply.list.ReportReplyListResponse;
import com.thlink.webBot.service.AiParametersService;
import com.thlink.webBot.service.ClientService;
import com.thlink.webBot.service.GeneralService;
import com.thlink.webBot.service.InstanceService;
import com.thlink.webBot.webbot.manager.InstanceManager;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class InspectController {
    Logger logger = LoggerFactory.getLogger(InspectController.class);
    
    @Autowired
    private final GeneralService generalSrv;
    private final ClientService clientSrv;
    private final InstanceService instanceSrv;
    private final AiParametersService aiParamsSrv;
    
    public InspectController(GeneralService pGeneralSrv, ClientService pCliSrv, 
                             InstanceService pInstSrv, AiParametersService pAiParamSrv) {
        this.generalSrv = pGeneralSrv;
        this.clientSrv = pCliSrv;
        this.instanceSrv = pInstSrv;
        this.aiParamsSrv = pAiParamSrv;
    }
    
    
    @RequestMapping(value="/hello2")
    public ResponseEntity<String> hello2 ()
    {   
        logger.info("hello2");
        HelloResponse r = new HelloBuilder().answerHello("WebBot", getVersao());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
         try {
            String json = objectMapper.writeValueAsString(r);
          //String json = HelloResponse.helloToJSON(r);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            
            return ResponseEntity.ok().headers(headers).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @RequestMapping(value="/metrics/reply/graph")
    public ResponseEntity<byte[]> createReplyGraphReport (HttpServletRequest request,
            @RequestParam(required = true,  defaultValue = "", value="instanceId") String strInstanceId,
            @RequestParam(required = false, defaultValue = "", value="onlyFirst")  String strOnlyFirst,
            @RequestParam(required = false, defaultValue = "", value="type")       String strType,
            @RequestParam(required = false, defaultValue = "", value="startDate")  String strStartDate,
            @RequestParam(required = false, defaultValue = "", value="finishDate") String strFinishDate)
    {
        logger.info("/metrics/reply/graph");
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Set appropriate media type

            ReportReplyGraphResponse response = new ReportReplyGraphResponse();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            ReportReplyParameters p = new ReportReplyParameters();
            if (strInstanceId.isEmpty())
            {
                response.setMessage("Especificar data de início e fim (startDate e finishDate");
                return new ResponseEntity<>(null, headers, HttpStatus.OK);
            }
            p.idInstance = Long.valueOf(strInstanceId);
            if (strType.isEmpty())
            {
                response.setMessage("Especificar tipo de gráfico (type [1, 2]");
                return new ResponseEntity<>(null, headers, HttpStatus.OK);
            }
            switch (strType) {
                case "1":
                case "2":
                    p.type = strType;
                    break;
                default:
                {
                    response.setMessage("Tipo de gráfico inválido (type [1, 2]");
                    return new ResponseEntity<>(null, headers, HttpStatus.OK);
                }
            }
            if (strOnlyFirst.isEmpty())
                p.onlyFirst = false;
            else
                p.onlyFirst = Boolean.valueOf(strOnlyFirst);
            if (strStartDate.isEmpty() | strFinishDate.isEmpty())
            {
                response.setMessage("Especificar data de início e fim (startDate e finishDate");
                return new ResponseEntity<>(null, headers, HttpStatus.OK);
            }
            p.startDate = sdf.parse(strStartDate);
            p.finishDate = sdf.parse(strFinishDate);
            logger.info("/metrics/reply/list params ok");
            logger.info(ReportReplyParameters.dumpParameters(p));
            
            response = new ReportBuilder(generalSrv).graphReportBuild(p);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(response.getImage(), "png", baos);
            byte[] imageData = baos.toByteArray();
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
            
        } catch (Exception e)
        {
            logger.error("createReplyGraphReport ERROR: " + e.toString());
            return null;
        }
    }
    
    @RequestMapping(value="/metrics/reply/list")
    public ResponseEntity<ReportReplyListResponse> createReplyListReport (HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "",  value="instanceId") String strInstanceId,
            @RequestParam(required = false, defaultValue = "", value="onlyFirst")  String strOnlyFirst,
            @RequestParam(required = false, defaultValue = "", value="startDate")  String strStartDate,
            @RequestParam(required = false, defaultValue = "", value="finishDate") String strFinishDate)
    {
        logger.info("/metrics/reply/list");
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            ReportReplyListResponse response = new ReportReplyListResponse();
            ReportReplyParameters p = new ReportReplyParameters();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            if (strInstanceId.isEmpty())
            {
                response.setMessage("Especificar data de início e fim (startDate e finishDate");
                return ResponseEntity.ok().headers(headers).body(response);
            }
            p.idInstance = Long.valueOf(strInstanceId);
            if (strOnlyFirst.isEmpty())
                p.onlyFirst = false;
            else
                p.onlyFirst = Boolean.valueOf(strOnlyFirst);
            if (strStartDate.isEmpty() | strFinishDate.isEmpty())
            {
                response.setMessage("Especificar data de início e fim (startDate e finishDate");
                return ResponseEntity.ok().headers(headers).body(response);
            }
            p.startDate = sdf.parse(strStartDate);
            p.finishDate = sdf.parse(strFinishDate);
            logger.info("/metrics/reply/list params ok");
            logger.info(ReportReplyParameters.dumpParameters(p));
            response = new ReportBuilder(generalSrv).listReportBuild(p);
            //json = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e)
        {
            logger.error("createReplyListReport ERROR: " + e.toString());
            return null;
        }
    }
    
    @RequestMapping(value="/entities/populateDB")
    public ResponseEntity<List<String>> populateDB (HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "",       value="type")      String strType,
            @RequestParam(required = true, defaultValue = "0",      value="qtdConvs")  String strNumConvs,
            @RequestParam(required = false, defaultValue = "",      value="minDelayS") String strMinDelayS,
            @RequestParam(required = false, defaultValue = "",      value="maxDelayS") String strMaxDelayS,
            @RequestParam(required = false, defaultValue = "false", value="delFirst")  String strDeleteFirst)
    {
        logger.info("populateDB type = " + strType);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            
        ResponseEntity<List<String>> answer;
        InstanceManager.eOPER oper;
        switch (strType) {
            case "1" -> oper = InstanceManager.eOPER.AI_CHAT;
            case "3" -> oper = InstanceManager.eOPER.SPY;
            default -> {
                return new ResponseEntity<>(Arrays.asList("Invalid type [1, 3]"), HttpStatus.NOT_FOUND);
            }               
        }
        Integer numConvs = Integer.valueOf(strNumConvs);
        if (numConvs < 0)
            return new ResponseEntity<>(Arrays.asList("Define qtdConvs"), HttpStatus.NOT_FOUND);               
        
        boolean deleteFirst = Boolean.parseBoolean(strDeleteFirst);
        Integer minDelayS = null;
        if (!strMinDelayS.isEmpty())
            minDelayS = Integer.valueOf(strMinDelayS);
        
        Integer maxDelayS = null;
        if (!strMaxDelayS.isEmpty())
            maxDelayS = Integer.valueOf(strMaxDelayS);
        
        List<String> steps = generalSrv.populateDB(deleteFirst, oper, numConvs, minDelayS, maxDelayS);
        if (steps != null && !steps.isEmpty())
            answer = new ResponseEntity<>(steps, HttpStatus.OK);
        else
            answer = new ResponseEntity<>(Arrays.asList("ERRO"), HttpStatus.NOT_FOUND);
        return answer;
    }
    
    //Clients
    @RequestMapping(value="/entities/clients/get")
    public ResponseEntity<List<tbCadClients>> getClients ()
    {
        logger.info("getClients");
        List<tbCadClients> result = clientSrv.getAllEntities();
        if (result != null && !result.isEmpty())
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.accepted().build();
    }
    //Deletar dados via entityManager (com parâmetros)
    @RequestMapping(value="/entities/clients/delEM")
    public ResponseEntity<String> delClientsViaEM (HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "", value="name") String strName)
    {
        logger.info("delClientsViaEM: " + strName);
        if (!strName.isEmpty())
        {    
            Integer aff = clientSrv.deleteViaEMWithWhere(String.format("WHERE e.name = '%s'", strName));
            logger.info("delClientsViaEM aff = " + aff);
            return ResponseEntity.ok(String.format("[%d] registros deletados.", aff));
        }
        return ResponseEntity.ok(String.format("Use o parâmetro name para definir o cliente a ser deletado."));
    }
    
    //Deletar dados via entityManager (com parâmetros)
    @RequestMapping(value="/entities/clients/delRepo")
    public ResponseEntity<String> delClientsViaRepo (HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "", value="name") String strName)
    {
        logger.info("delClientsViaRepo: " + strName);
        if (!strName.isEmpty())
        {    
            Integer aff = clientSrv.deleteViaRepo(strName);
            logger.info("delClientsViaRepo aff = " + aff);
            return ResponseEntity.ok(String.format("[%d] registros deletados.", aff));
        }
        return ResponseEntity.ok(String.format("Use o parâmetro name para definir o cliente a ser deletado."));
    }
    
    //Atualiza dados via entityManager
    @RequestMapping(value="/entities/clients/activate")
    public ResponseEntity<String> activateClients (HttpServletRequest request,
            @RequestParam(required = true, defaultValue = "", value="idClient") String pIdClient,
            @RequestParam(required = true, defaultValue = "", value="activate") String pActivate)
    {
        logger.info("activateClients ");
        if (!pIdClient.isEmpty() || !pActivate.isEmpty())
        {    
            Long idClient = Long.valueOf(pIdClient);
            Boolean active = Boolean.valueOf(pActivate);
            logger.info(String.format("activateClients: idClient = %d, active = %s", idClient, active));
            Integer aff = clientSrv.activateViaEM(idClient, active);
            logger.info("activateClients aff = " + aff);
            return ResponseEntity.ok(String.format("[%d] registros atualizado.", aff));
        }
        return ResponseEntity.ok(String.format("Use o parâmetro idClient e activate para definir o cliente a ser atualizado."));
    }
    
    @RequestMapping(value="/entities/instances")
    public ResponseEntity<List<tbInstances>> getInstances ()
    {
        logger.info("getInstances");
        List<tbInstances> result = instanceSrv.getAllEntities();
        if (result != null && !result.isEmpty())
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.accepted().build();
    }
    
    @RequestMapping(value="/entities/aiParameters")
    public ResponseEntity<List<tbAiParameters>> getAiParameters ()
    {
        logger.info("getAiParameters");
        List<tbAiParameters> result = aiParamsSrv.getAllEntities();
        if (result != null && !result.isEmpty())
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.accepted().build();
    }

    public String getVersao() 
    {
        String v = this.getClass().getPackage().getSpecificationVersion();
        if (v != null)
            return v;
        else 
            return "AB.CD";
    }
}
