package com.thlink.webBot.sender;

import com.thlink.webBot.persistence.nonDB.ChatMessage;
import com.thlink.webBot.persistence.nonDB.ChatParams;
import com.thlink.webBot.sender.Evolution.findContacts.FindContatsResponseBody;
import com.thlink.webBot.sender.Evolution.findContacts.Where;
import java.util.List;

/**
 *
 * @author MThomas
 */
public interface MsgSender {
    boolean startUp (int pEvolutionPort, String pEvolutionServer, String pInstanceName, String pInstanceApiKey, String pGlobalApiKey);
    boolean sendTextMessage (ChatMessage message);
    boolean sendMediaMessage (ChatMessage message, List<String> pFiles);
    List<FindContatsResponseBody> findAllContacts ();
    boolean isContact (String pId);
    List<FindContatsResponseBody> findContactsWhere (Where pWhere);
    
}
