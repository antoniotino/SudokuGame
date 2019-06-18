public class StartGame {

    public static void main(String[] args) throws Exception {
        System.out.println("Project under construction");

        int peerID = 0;
        SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerImpl(peerID));
        peer.SayHallo("Antonio");
    }
}