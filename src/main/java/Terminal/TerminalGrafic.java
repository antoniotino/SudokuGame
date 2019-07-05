package Terminal;

import Sudoku.SudokuGameImpl;

import User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TerminalGrafic {

    private User user;
    private SudokuGameImpl peer;
    private int peerID;
    private String join_game;
    private boolean join;
    private int count_help = 3;
    private HashMap<String, Integer> userScore= new HashMap<String,Integer>();

    public TerminalGrafic(SudokuGameImpl peer, int peerID, User user) {
        this.peer = peer;
        this.peerID = peerID;
        this.user = user;
    }

    public void startTerminal() throws InterruptedException {
        while (true) {
            infoUser(user, peerID);
            printMenu();
            TimerTask scoreTask = new TimerTask() {
                @Override
                public void run() {
                    userScore=peer.score_peer();
                }
            };
            java.util.Timer timerScore = new java.util.Timer();
            timerScore.schedule(scoreTask, 500, 500);
            Scanner scanner= new Scanner(System.in);
            System.out.println("\nOption");
            int option = scanner.nextInt();
            if(option >0 && option <8){
                switch (option) {
                    case 1:
                        HashMap<String, String> roomExisting = peer.active_room();
                        ArrayList<String> roomName= new ArrayList<String>();
                        String _game_name="";
                        for(String str: roomExisting.keySet()) {
                            roomName.add(str);
                        }
                        do {
                            System.out.println("Insert the name's game: ");
                            _game_name= scanner.next();
                        }while(roomName.contains(_game_name));
                        int choose;
                        do{
                            System.out.println("Choose the sudoku's difficulty\n1) Easy \t 2) Medium \t 3)Hard \t: ");
                            while(!scanner.hasNextInt()){
                                System.out.println("\n Error into choose!\nChoose the sudoku's difficulty\n1) Easy \t 2) Medium \t 3)Hard \t: ");
                                scanner.next();
                            }
                            choose= scanner.nextInt();
                        } while(choose < 1 || choose >3);
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
                        }
                        peer.choose_difficulty(difficulty);
                        Integer [][] matrix = peer.generateNewSudoku(_game_name);
                        if(matrix.length == 0)
                            System.out.printf("\nThere is already a sudoku with this name");
                        else
                            System.out.printf("\nCreated a sudoku with this name");
                        break;
                    case 2:
                        HashMap<String, String> room_active = peer.active_room();
                        System.out.printf("\n\nRooms actually active");
                        if(room_active.size() > 0) {
                            for (String str : room_active.keySet())
                                System.out.printf("\n" + str + "(Difficulty: " + room_active.get(str) + ")");
                        }
                        else {
                            System.out.printf("\nThere are no active rooms\n\n");
                        }
                        System.out.printf("\n");
                        break;
                    case 3:
                        System.out.println("Name's game: ");
                        join_game = scanner.next();
                        join = peer.join(join_game, user.getNickname());
                        if(join)
                            System.out.printf("\nSuccessfully join \n");
                        else
                            System.out.printf("\nError in join to game \n");
                        break;
                    case 4:
                        if(join_game == null || !join)
                            System.out.printf("\nYou need to join a game\n");
                        else
                            printSudoku(peer.getSudoku(join_game), join_game,userScore);
                        break;
                    case 5:
                        if(join_game == null)
                            System.out.printf("\nYou need to join a game\n");
                        else{
                            int ele;
                            do{
                                System.out.println("Insert number: ");
                                while(!scanner.hasNextInt()){
                                    System.out.println("That's not a number");
                                    scanner.next();
                                }
                                ele= scanner.nextInt();
                            } while(ele < 0);
                            int row;
                            do{
                                System.out.println("Row: ");
                                while(!scanner.hasNextInt()){
                                    System.out.println("Number row not correct \nRow: ");
                                    scanner.next();
                                }
                                row= scanner.nextInt();
                            } while(row < 1 || row >9);
                            row -= 1;
                            int column;
                            do{
                                System.out.println("Column: ");
                                while(!scanner.hasNextInt()){
                                    System.out.println("Number column not correct \nColumn: ");
                                    scanner.next();
                                }
                                column= scanner.nextInt();
                            } while(column < 1 || column >9);
                            column -=1;
                            int result = peer.placeNumber(join_game, row, column, ele);
                            if(result == 1)
                                System.out.printf("\n\nNumber correct! \n\n");
                            else if(result == 0)
                                System.out.printf("\n\nNumber already insert!\n\n");
                            else if(result ==2){
                                System.out.println(getVictory(userScore));
                                System.out.printf("\n\nEnd Game...Now exit!\n\n");
                                TimeUnit.SECONDS.sleep(10);
                                peer.leaveNetwork(user.getNickname(), join_game, join);
                                System.exit(0);
                            }else
                                System.out.printf("\n\nIncorrect number! \n\n");
                        }
                        break;
                    case 6:
                        if(join_game == null)
                            System.out.printf("\nYou need to join a game\n");
                        else{
                            if(count_help == 0)
                                System.out.printf("\n\nThe number of suggestions is over \n\n");
                            else{
                                int row;
                                do{
                                    System.out.println("Row: ");
                                    while(!scanner.hasNextInt()){
                                        System.out.println("Number row not correct \nRow: ");
                                        scanner.next();
                                    }
                                    row= scanner.nextInt();
                                } while(row < 1 || row >9);
                                row -= 1;
                                int column;
                                do{
                                    System.out.println("Column: ");
                                    while(!scanner.hasNextInt()){
                                        System.out.println("Number column not correct \nColumn: ");
                                        scanner.next();
                                    }
                                    column= scanner.nextInt();
                                } while(column < 1 || column >9);
                                column -=1;
                                if(peer.getHelp(join_game, row, column)==1){
                                    System.out.printf("\n\nok! \n\n");
                                    count_help--;
                                }else if( peer.getHelp(join_game, row, column)==2){
                                    System.out.println(getVictory(userScore));
                                    System.out.printf("\n\nEnd Game...Now exit!\n\n");
                                    TimeUnit.SECONDS.sleep(10);
                                    peer.leaveNetwork(user.getNickname(), join_game, join);
                                    System.exit(0);
                                }
                                else {
                                    System.out.printf("\n\nNumber already insert in this position! \n\n");
                                }
                            }
                        }
                        break;
                    case 7:
                        int exit;
                        do{
                        System.out.println("\nAre you sure to leave the network?\n 1) Yes \t2) No");
                        while(!scanner.hasNextInt()){
                            System.out.println("Choose not correct \nAre you sure to leave the network?\n 1) Yes \t2) No");
                            scanner.next();
                        }
                            exit= scanner.nextInt();
                    } while(exit < 1 || exit >2);
                        if (exit==1) {
                            if(peer.leaveNetwork(user.getNickname(), join_game, join)==1)
                                System.exit(0);
                            System.out.printf("\nError in leaving the network!\n");
                        }
                        break;
                    default: break;
                }
            }

        }
    }

    private void infoUser(User user, int peerID) {
        System.out.printf("\n\n[PeerID: " + peerID + "] Nickname: " + user.getNickname() + " - Score: " + user.getScore() + "\n\n");
    }

    private void printMenu() {
        System.out.printf("\n1 - Create a new sudoku\n");
        System.out.printf("\n2 - Show active sudoku \n");
        System.out.printf("\n3 - Join in a game\n");
        System.out.printf("\n4 - Get sudoku\n");
        System.out.printf("\n5 - Place a number \n");
        System.out.printf("\n6 - Help x"+count_help+" \n");
        System.out.printf("\n7 - EXIT\n");
    }

    private void printSudoku(Integer[][] sudoku, String _game_name, HashMap<String, Integer> userScore) throws InterruptedException {
        int fin = 0;
        int count= 0;
        System.out.printf("\n\nRoom: "+_game_name);
        System.out.println("\n    1   2   3   4   5   6   7   8   9    \n  ┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓");
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (column % 3 == 0) {
                    if (column == 0) {
                        if (sudoku[row][column] == 0){
                            System.out.print((row+1)+" ┃   │ ");
                            count++;
                        }
                        else {
                            System.out.print((row + 1) + " ┃ " + "\u001B[1m" + sudoku[row][column] + "\u001B[0m │ ");
                        }
                    } else {
                        if (sudoku[row][column] == 0){
                            System.out.print("  │ ");
                            count++;
                        }
                        else {
                            System.out.print("\u001B[1m" + sudoku[row][column] + "\u001B[0m │ ");
                        }
                    }
                }else if(column == 2 || column ==5) {
                    if (sudoku[row][column] == 0){
                        System.out.print("  ┃ ");
                        count++;
                    }
                    else {
                        System.out.print("\u001B[1m" + sudoku[row][column] + "\u001B[0m ┃ ");
                    }
                }
                else if (column == sudoku.length - 1) {
                    if (sudoku[row][column] == 0){
                        System.out.print("  ┃\n");
                        count++;
                    }
                    else {
                        System.out.print("\u001B[1m" + sudoku[row][column] + "\u001B[0m ┃\n");
                    }
                } else {
                    if (sudoku[row][column] == 0){
                        System.out.print("  │ ");
                        count++;
                    }
                    else {
                        System.out.print("\u001B[1m" + sudoku[row][column] + "\u001B[0m │ ");
                    }
                }
            }
            fin++;
            if (fin == 9) {
                System.out.println("  ┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛");
            }
            else if(fin == 3 || fin == 6){
                System.out.println("  ┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫");
            }
            else{
                System.out.println("  ┣───┼───┼───┃───┼───┼───┃───┼───┼───┫");
            }
        }
        if(count==0){
            System.out.println(getVictory(userScore));
            System.out.printf("\n\nEnd Game...Now exit!\n\n");
            TimeUnit.SECONDS.sleep(10);
            peer.leaveNetwork(user.getNickname(), join_game, join);
            System.exit(0);
        }
    }

    public String getVictory(HashMap<String, Integer> userScore){
        int max_score= user.getScore();
        String winner = user.getNickname();
        for(String str : userScore.keySet()){
            if(userScore.get(str) > max_score && !str.equals(winner)){
                max_score= userScore.get(str);
                winner= str;
            }
            if(userScore.get(str)==max_score && !str.equals(winner)){
                max_score= userScore.get(str);
                winner+=" "+(str);
            }
        }
        String message= "The user/s "+winner+" won the sudoku game with score of "+max_score;
        return message;
    }
}