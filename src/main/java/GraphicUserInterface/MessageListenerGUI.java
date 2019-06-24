package GraphicUserInterface;

import Interface.MessageListener;

import javax.swing.*;
import java.util.logging.Logger;

public class MessageListenerGUI implements MessageListener{

    Logger l = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private int peerID;
    JLabel labelMessages;
    public MessageListenerGUI(int peerID){
        this.peerID = peerID;
    }

    public Object parseMessage(Object obj){
        l.info("["+peerID+"] (Direct Message Received) "+obj+"\n\n");
        return "success";
    }
}