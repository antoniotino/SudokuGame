package GraphicUserInterface;

import Interface.MessageListener;

public class MessageListenerGUI implements MessageListener{

    private int peerID;

    public MessageListenerGUI(int peerID){
        this.peerID = peerID;
    }

    public Object parseMessage(Object obj){
        return "success";
    }
}