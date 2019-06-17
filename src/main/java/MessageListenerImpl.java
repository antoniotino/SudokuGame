import java.util.logging.Logger;

public class MessageListenerImpl implements MessageListener{

    private final static Logger LOGGER = Logger.getLogger(MessageListenerImpl.class.getName());

    private int peerid;

    public MessageListenerImpl(int peerid){
        this.peerid = peerid;
    }

    public Object parseMessage(Object obj) {
        System.out.println("["+peerid+"] (Direct Message Received) " + obj);
        LOGGER.info("["+peerid+"] (Direct Message Received) " + obj);
        return "success";
    }
}