package Terminal;

import Sudoku.SudokuGameImpl;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class TerminalGrafic {

    private SudokuGameImpl peer;
    private int peerID;

    public TerminalGrafic(SudokuGameImpl peer, int peerID) {
        this.peer = peer;
        this.peerID = peerID;
    }

    public void startTerminal() {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        terminal.printf("\nStaring peer id: %d\n", peerID);
        while (true) {
            printMenu(terminal);
            int option = textIO.newIntInputReader().withMaxVal(5).withMinVal(1).read("Option");
            switch (option){
                case 1:
                    peer.SayHallo("Antonio");
                    break;
                case 2:
                    System.out.println("OP2");
                    break;
                case 3:
                    System.out.println("OP3");
                    break;
                case 4:
                    System.out.println("OP4");
                    break;
                case 5:
                    terminal.printf("\nAre you sure to leave the network?\n");
                    boolean exit = textIO.newBooleanInputReader().withDefaultValue(false).read("exit?");
                    if(exit) {
                        peer.leaveNetwork();
                        System.exit(0);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void printMenu(TextTerminal terminal) {
        terminal.printf("\n1 - SAY HELLO\n");
        terminal.printf("\n2 - OP1\n");
        terminal.printf("\n3 - OP2\n");
        terminal.printf("\n4 - OP3\n");
        terminal.printf("\n5 - EXIT\n");
    }
}