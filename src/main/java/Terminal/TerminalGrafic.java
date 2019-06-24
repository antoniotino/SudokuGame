package Terminal;

import Sudoku.SudokuGameImpl;

import User.User;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TerminalGrafic {

    private User user;
    private SudokuGameImpl peer;
    private int peerID;
    private String join_game;
    private boolean join;
    private boolean victory;

    public TerminalGrafic(SudokuGameImpl peer, int peerID, User user) {
        this.peer = peer;
        this.peerID = peerID;
        this.user = user;
    }

    public void startTerminal() throws InterruptedException {
        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        while (true) {
            infoUser(user, peerID, terminal);
            printMenu(terminal);

            int option = textIO.newIntInputReader().withMaxVal(6).withMinVal(1).read("\nOption");
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
                    Integer [][] matrix = peer.generateNewSudoku(_game_name);
                    if(matrix.length == 0)
                        terminal.printf("\nThere is already a sudoku with this name");
                    else
                        terminal.printf("\nCreated a sudoku with this name");
                    break;
                case 2:
                    HashMap<String, String> room_active = peer.active_room();
                    terminal.printf("\n\nRooms actually active");
                    if(room_active.size() == 0)
                        terminal.printf("\nThere are no active rooms\n\n");
                    else
                        for(String str: room_active.keySet())
                            terminal.printf("\n"+str+"(Difficulty: "+room_active.get(str)+")");
                    terminal.printf("\n");
                    break;
                case 3:
                    join_game = textIO.newStringInputReader().read("Name's game: ");
                    join = peer.join(join_game, user.getNickname());
                    if(join)
                        terminal.printf("\nSuccessfully join \n");
                    else
                        terminal.printf("\nError in join to game \n");
                    break;
                case 4:
                    if(join_game == null || !join)
                        terminal.printf("\nYou need to join a game\n");
                    else
                        printSudoku(peer.getSudoku(join_game), join_game, terminal);
                    break;
                case 5:
                    if(join_game == null)
                        terminal.printf("\nYou need to join a game\n");
                    else{
                       int ele = textIO.newIntInputReader().withMaxVal(9).withMinVal(1).read("Insert a number: ");
                       int row = textIO.newIntInputReader().withMaxVal(9).withMinVal(1).read("Row: ");
                       row -= 1;
                       int column = textIO.newIntInputReader().withMaxVal(9).withMinVal(1).read("Column: ");
                       column -=1;
                       int result = peer.placeNumber(join_game, row, column, ele);
                       if(result == 1)
                           terminal.printf("\n\nNumber correct! \n\n");
                       else if(result == 0)
                           terminal.printf("\n\nNumber already insert!\n\n");
                       else if(result ==2){
                           terminal.printf("\n\nEnd Game...Now exit!\n\n");
                           TimeUnit.SECONDS.sleep(10);
                           peer.leaveNetwork(user.getNickname(), join_game, join);
                           System.exit(0);
                       }else
                           terminal.printf("\n\nIncorrect number! \n\n");
                    }
                    break;
                case 6:
                    terminal.printf("\nAre you sure to leave the network?\n");
                    boolean exit = textIO.newBooleanInputReader().withDefaultValue(false).read("exit?");
                    if (exit) {
                        if(peer.leaveNetwork(user.getNickname(), join_game, join))
                            System.exit(0);
                        terminal.printf("\nError in leaving the network!\n");
                    }
                    break;
                default: break;
            }
        }
    }

    private void infoUser(User user, int peerID, TextTerminal terminal) {
        terminal.printf("\n\n[PeerID: " + peerID + "] Nickname: " + user.getNickname() + " - Score: " + user.getScore() + "\n\n");
    }

    private void printMenu(TextTerminal terminal) {
        terminal.printf("\n1 - Create a new sudoku\n");
        terminal.printf("\n2 - Show active sudoku \n");
        terminal.printf("\n3 - Join in a game\n");
        terminal.printf("\n4 - Get sudoku\n");
        terminal.printf("\n5 - Place a number \n");
        terminal.printf("\n6 - EXIT\n");
    }

    private void printSudoku(Integer[][] sudoku, String _game_name, TextTerminal terminal) {
        int fin = 0;
        terminal.printf("\n\nRoom: "+_game_name);
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