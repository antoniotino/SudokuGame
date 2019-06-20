package Terminal;
import Interface.MessageListener;

import java.util.logging.Logger;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class MessageListenerTerminal implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(MessageListenerTerminal.class.getName());
    private int peerID;

    public MessageListenerTerminal(int peerID){
        this.peerID = peerID;
    }

    /* da rivedere */
    public Object parseMessage(Object obj){
       /* TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();
        terminal.printf("\n["+peerID+"] (Direct Message Received) "+obj+"\n\n");
        LOGGER.info("["+peerID+"] (Direct Message Received) " + obj);*/
        System.out.println("MessageListenerTerminal");
        return "success";
    }
}