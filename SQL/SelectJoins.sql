select	--C.*  
		--I.*
		--P.*,
		--K.*
		LCM.*
from	tbCadClients C
LEFT	JOIN tbInstances I ON C.id = I.clientId
LEFT	JOIN tbAiParameters P ON P.id = I.aiParamsId
LEFT	JOIN tbKeyWords K ON I.id = K.instanceId
LEFT	JOIN tbLogchatManager LCM ON LCM.instanceId = I.id

