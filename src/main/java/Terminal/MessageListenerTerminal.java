package Terminal;
import Interface.MessageListener;
public class MessageListenerTerminal implements MessageListener {

    private int peerID;

    public MessageListenerTerminal(int peerID){
        this.peerID = peerID;
    }

    public Object parseMessage(Object obj){
        System.out.printf("\n\n["+peerID+"] (Direct Message Received) "+obj+"\n\n");
        return "success";
    }
}