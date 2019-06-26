import GraphicUserInterface.MessageListenerGUI;
import Sudoku.SudokuGameImpl;
import User.User;

import java.util.HashMap;
import java.util.logging.Logger;

public class TestGame {

    public static void main(String[] args) throws Exception {

        Logger logg = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        //4 peers
        SudokuGameImpl peer0 = new SudokuGameImpl(0, "127.0.0.1", new MessageListenerGUI(0));
        SudokuGameImpl peer1 = new SudokuGameImpl(1, "127.0.0.1", new MessageListenerGUI(1));
        SudokuGameImpl peer2 = new SudokuGameImpl(2, "127.0.0.1", new MessageListenerGUI(2));
        SudokuGameImpl peer3 = new SudokuGameImpl(3, "127.0.0.1", new MessageListenerGUI(3));

        //4 users
        User user1 = new User("Tino");
        User user2 = new User("Jack");
        User user3 = new User("Alberto");
        User user4 = new User("Gennaro");

        //The 4 peers are added to the system
        peer0.addUser(user1);
        peer1.addUser(user2);
        peer2.addUser(user3);
        peer3.addUser(user4);

        //3 peers of 4 choose the difficulty of the sudoku they are about to create
        peer1.choose_difficulty("easy");
        peer2.choose_difficulty("easy");
        peer3.choose_difficulty("hard");

        //Sudoku generation with difficulty easy: correct
        Integer[][] sudoku_easy = peer1.generateNewSudoku("SudokuEasy");
        if (sudoku_easy.length != 0)
            logg.info("Sudoku created correctly!");

        //Sudoku generation with easy difficulty: wrong because there is already a sudoku with that name and that difficulty
        Integer[][] sudoku_duplicate = peer2.generateNewSudoku("SudokuEasy");
        if (sudoku_duplicate.length == 0)
            logg.info("There is already a sudoku with this name!");

        //Sudoku generation with difficulty hard: correct
        Integer[][] sudoku_hard = peer3.generateNewSudoku("SudokuHard");
        if (sudoku_hard.length != 0)
            logg.info("Sudoku created correctly!");

        //Show active sudokus
        HashMap<String, String> room_active = peer3.active_room();
        if(room_active.size() > 0)
            logg.info("There are " + (room_active.size()+1)+" active sudokus!");

        //Join correct in sudoku SudokuEasy
        if (peer0.join("SudokuEasy", "Antonio"))
            logg.info("Successfully join!");
        else
            logg.info("Error in join to game!");

        //Join incorrect in a non-existent sudoku
        if (peer1.join("Sudoku", "Jack"))
            logg.info("Successfully join!");
        else
            logg.info("Error in join to game!");

        //Join correct in sudoku SudokuHard
        if (peer2.join("SudokuHard", "Alberto"))
            logg.info("Successfully join!");
        else
            logg.info("Error in join to game!");

        //print sudoku
        if (peer0.getSudoku("SudokuEasy").length != 0)
            logg.info("Print sudoku!");
        else
            logg.info("Error in print to sudoku!");

        //Help is possible: empty cell (row 1, column 8)
        if(peer0.getHelp("SudokuEasy", 1, 8))
            logg.info("getHelp correct");

        //Help is impossible: empty is busy (row 2, column 0)
        if(!peer2.getHelp("SudokuHard", 2, 0))
            logg.info("getHelp incorrect");

        //place number correct in cell row = 1 and column = 8
        //2 is valid for the sudoku "easy1", 5 is valid for the sudoku "easy2"
        if (peer1.placeNumber("SudokuEasy", 1, 8, 2) == 1)
            logg.info("Valid and correct value!");
        else if (peer1.placeNumber("SudokuEasy", 1, 8, 5) == 1)
            logg.info("Valid and correct value!");

        //place number incorrect in cell row = 1 and column = 2
        //6 is not valid for the sudoku "easy1", 7 is not valid for the sudoku "easy2"
        if (peer1.placeNumber("SudokuEasy", 1, 2, 6) == -1)
            logg.info("Value incorrect!");
        else if (peer1.placeNumber("SudokuEasy", 1, 2, 7) == -1)
            logg.info("Value incorrect!");

        //place number correct in cell row = 1 and column = 8  but already insert
        //2 is valid for the sudoku "easy1", 5 is valid for the sudoku "easy2"
        if (peer2.placeNumber("SudokuHard", 2, 0, 8) == 0)
            logg.info("Valid and correct value but already insert!");
        else if (peer2.placeNumber("SudokuHard", 2, 0, 4) == 0)
            logg.info("Valid and correct value but already insert!");

        //leave network
        if (!peer0.leaveNetwork("Antonio", "SudokuEasy", true))
            logg.info("Error in leaving the network!");

        if (!peer1.leaveNetwork("Jack", "SudokuEasy", false))
            logg.info("Error in leaving the network!");

        if(!peer2.leaveNetwork("Alberto", "SudokuHard", true))
            logg.info("Error in leaving the network!");

        if(!peer3.leaveNetwork("Gennaro", "SudokuHard", false))
            logg.info("Error in leaving the network!");

        System.exit(0);
    }
}
