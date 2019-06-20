package Terminal;

import Sudoku.SudokuGameImpl;

import User.User;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class TerminalGrafic {

    private User user;
    private SudokuGameImpl peer;
    private int peerID;

    public TerminalGrafic(SudokuGameImpl peer, int peerID, User user) {
        this.peer = peer;
        this.peerID = peerID;
        this.user = user;
    }

    public void startTerminal() {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        while (true) {
            infoUser(user, peerID, terminal);
            printMenu(terminal);

            int option = textIO.newIntInputReader().withMaxVal(5).withMinVal(1).read("\nOption");
            switch (option) {
                case 1:
                    terminal.printf("Insert the name's game: ");
                    String _game_name = textIO.newStringInputReader().read();

                    terminal.printf("\nChoose the sudoku's difficulty\n1) Easy \t 2) Medium \t 3)Hard \t: ");
                    int choose = textIO.newIntInputReader().withMaxVal(3).withMinVal(1).read();

                    String difficulty = "";
                    switch (choose) {
                        case 1:
                            difficulty += "easy";
                            break;
                        case 2:
                            difficulty += "medium";
                            break;
                        case 3:
                            difficulty += "hard";
                            break;
                        default:
                            terminal.printf("\n Error into choose!\n");
                            break;
                    }
                    peer.choose_difficulty(difficulty);
                    printSudoku(peer.generateNewSudoku(_game_name), terminal);
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
                    if (exit) {
                        peer.leaveNetwork();
                        System.exit(0);
                    }
                    break;
                default:
                    terminal.printf("\n Error into choose!\n");
                    break;
            }
        }
    }

    private void infoUser(User user, int peerID, TextTerminal terminal) {
        terminal.printf("\n\n[PeerID: " + peerID + "] Nickname: " + user.getNickname() + " - Score: " + user.getScore() + "\n");
    }

    private void printMenu(TextTerminal terminal) {
        terminal.printf("\n1 - Create a new sudoku\n");
        terminal.printf("\n2 - Join in a game\n");
        terminal.printf("\n3 - OP3\n");
        terminal.printf("\n4 - OP4\n");
        terminal.printf("\n5 - EXIT\n");
    }

    private void printSudoku(Integer[][] sudoku, TextTerminal terminal) {
        int fin = 0;
        terminal.println("\n\n---------------------------");
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (column % 3 == 0) {
                    if (column == 0) {
                        if (sudoku[row][column] == 0)
                            terminal.printf(" | . ");
                        else
                            terminal.printf(" | " + sudoku[row][column] + " ");
                    } else {
                        if (sudoku[row][column] == 0)
                            terminal.printf("| . ");
                        else
                            terminal.printf("| " + sudoku[row][column] + " ");
                    }
                } else if (column == sudoku.length - 1) {
                    if (sudoku[row][column] == 0)
                        terminal.printf(". |\n");
                    else
                        terminal.printf(sudoku[row][column] + " |\n");
                } else {
                    if (sudoku[row][column] == 0)
                        terminal.printf(". ");
                    else
                        terminal.printf(sudoku[row][column] + " ");
                }
            }
            fin++;
            if (fin % 3 == 0)
                terminal.println("---------------------------");
        }
    }
}