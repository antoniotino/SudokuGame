package Terminal;
import Interface.MessageListener;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class MessageListenerTerminal implements MessageListener {

    private int peerID;

    public MessageListenerTerminal(int peerID){
        this.peerID = peerID;
    }

    public Object parseMessage(Object obj){
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();
        terminal.printf("\n\n["+peerID+"] (Direct Message Received) "+obj+"\n\n");
        return "success";
    }
}