SELECT    CAST(CV.id as int), CV.pushName, CV.userNumber, CV.qtdIterations, 
          CM.clientText, CM.manualText, CM.aiText, 
          CM.clientMoment, CM.manualMoment, CM.aiMoment 
FROM      tbLogConversationMessage CM 
INNER     JOIN tbLogConversation   CV ON CM.idConversation = CV.id 
INNER     JOIN tbInstances         I  ON CV.idInstance = I.id 
/*WHERE     I.id = :pInstanceId AND I.type = :pInstanceType  
 AND CM.clientMoment >= :pStartDate  AND CM.clientMoment <= :pFinishDate GROUP    BY CV.id, CV.pushName, CV.userNumber, CV.qtdIterations, CM.clientText, CM.manualText, CM.aiText, CM.clientMoment, CM.manualMoment, CM.aiMoment */
ORDER    BY CV.id 
