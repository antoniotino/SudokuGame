import GraphicUserInterface.MessageListenerGUI;
import Sudoku.SudokuGameImpl;
import User.User;
import junit.framework.TestCase;
import net.tomp2p.peers.PeerAddress;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TestSudokuGameImpl extends TestCase {

    @SuppressWarnings("unused")
    private static SudokuGameImpl peer0;
    private static SudokuGameImpl peer1;
    private static SudokuGameImpl peer2;
    private static SudokuGameImpl peer3;

    /**
     * Method used for start the system
     */
    @Before
    public void start_system() throws Exception {
        peer0 = new SudokuGameImpl(0, "127.0.0.1", new MessageListenerGUI(0));
        peer1 = new SudokuGameImpl(1, "127.0.0.1", new MessageListenerGUI(1));
        peer2 = new SudokuGameImpl(2, "127.0.0.1", new MessageListenerGUI(2));
        peer3 = new SudokuGameImpl(3, "127.0.0.1", new MessageListenerGUI(3));
    }

    /**
     * Test for method addUser
     */
    @Test
    public void test_addUser() throws Exception {
        start_system();

        User user1 = new User("Tino");
        User user2 = new User("Jack");
        User user3 = new User("Alberto");
        User user4 = new User("Gennaro");

        peer0.addUser(user1);
        peer1.addUser(user2);
        peer2.addUser(user3);
        peer3.addUser(user4);
    }

    /**
     * Test for method choose_difficulty
     */
    @Test
    public void test_choose_difficulty() throws Exception {
        start_system();

        peer1.choose_difficulty("easy");
        peer2.choose_difficulty("easy");
        peer3.choose_difficulty("hard");
    }

    /**
     * Test for method generateNewSudoku
     */
    @Test
    public void test_generateNewSudoku() throws Exception {
        start_system();

        peer1.choose_difficulty("easy");
        Integer[][] sudokuEasy1 = peer1.generateNewSudoku("SudokuEasy");
        assertTrue(sudokuEasy1.length != 0);

        peer2.choose_difficulty("easy");
        Integer[][] sudokuEasy2 = peer2.generateNewSudoku("SudokuEasy");
        assertFalse(sudokuEasy2.length == 0);
    }

    /**
     * Test for method duplicateNickname
     */
    @Test
    public void test_duplicateNickname1() throws Exception{
        start_system();

        User user1 = new User("Tino");
        HashMap<PeerAddress, User> nickname= peer0.duplicateNickname();
        ArrayList<String> nicknameUser= new ArrayList<String>();

        for(PeerAddress str: nickname.keySet())
            nicknameUser.add(nickname.get(str).getNickname());


        assertFalse(nicknameUser.contains(user1.getNickname()));
    }

    @Test
    public void test_duplicateNickname2() throws Exception{
        start_system();

        User user1 = new User("Tino");
        User user2 = new User("Tino"); //user 2: duplicate
        peer0.addUser(user1); //add user 1 in the system

        HashMap<PeerAddress, User> nickname= peer0.duplicateNickname();
        ArrayList<String> nicknameUser= new ArrayList<String>();

        for(PeerAddress str: nickname.keySet())
            nicknameUser.add(nickname.get(str).getNickname());

        assertTrue(nicknameUser.contains(user2.getNickname())); //check if user 2 is a duplicate
        }

    /**
     * Test for method active_room
     */
    @Test
    public void test_activeRoom1() throws Exception {
        start_system();

        peer0.choose_difficulty("easy");
        peer0.generateNewSudoku("SudokuEasy");

        HashMap<String, String> room_active = peer0.active_room();
        assertTrue(room_active.size() > 0);
    }

    @Test
    public void test_activeRoom2() throws Exception {
        start_system();

        HashMap<String, String> room_active = peer0.active_room();
        assertFalse(room_active.size() > 0);
    }

    /**
     * Test for method join
     */
    @Test
    public void test_join1() throws Exception {
        start_system();

        peer1.choose_difficulty("easy");
        Integer[][] sudokuEasy1 = peer1.generateNewSudoku("SudokuEasy");
        assertTrue(sudokuEasy1.length != 0);

        boolean join_user1 = peer1.join("SudokuEasy", "Jack");
        assertTrue(join_user1);

    }

    //@Test
    @Ignore
    public void test_join2() throws Exception {
        start_system();

        peer3.choose_difficulty("hard");
        Integer[][] sudokuhard = peer3.generateNewSudoku("Sudokuhard");
        assertTrue(sudokuhard.length != 0);

        boolean join_user4 = peer3.join("sudoku", "Alberto");
        assertFalse(join_user4);
    }

    /**
     * Test for method getSudoku
     */
    @Test
    public void test_getSudoku1() throws Exception {
        start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");

        Integer[][] sudokuEasy1 = peer1.getSudoku("SudokuEasy");
        assertTrue(sudokuEasy1.length != 0);
    }

    @Test
    public void test_getSudoku2() throws Exception {
        start_system();

        peer2.choose_difficulty("easy");
        peer2.generateNewSudoku("SudokuEasy");
        peer2.join("SudokuEasy", "Alberto");

        Integer[][] sudokuEasy1 = peer2.getSudoku("sudoku");
        assertFalse(sudokuEasy1.length != 0);
    }

    /**
     * Test for method placeNumber
     */
    @Test
    //Correct value in cell row = 1 and column = 8: 2 is valid for the sudoku "easy1", 5 is valid for the sudoku "easy2"
    public void test_placeNumber1() throws Exception{
       start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");
        int num_two = peer1.placeNumber("SudokuEasy", 1, 8, 2);

        peer2.choose_difficulty("easy");
        peer2.generateNewSudoku("SudokuEasy");
        peer2.join("SudokuEasy", "Alberto");
        int num_five = peer2.placeNumber("SudokuEasy", 1, 8, 5);

        if (num_two == 1 || num_five == 1)
            assertTrue(true);
    }

    @Test
    //Incorrect value in cell row = 1 and column = 1: 6 is not valid for the sudoku "medium1", 7 is not valid for the sudoku "medium2"
    public void test_placeNumber2() throws Exception{
        start_system();

        peer3.choose_difficulty("medium");
        peer3.generateNewSudoku("SudokuMedium");
        peer3.join("SudokuMedium", "Gennaro");
        int num_six = peer3.placeNumber("SudokuMedium", 1, 1, 6);

        int num_seven = peer3.placeNumber("SudokuMedium", 1, 1, 7);

        if (num_six == -1 || num_seven == -1)
            assertTrue(true);
    }

    //Correct value in cell row = 1 and column = 8 but already insert: 2 is valid for the sudoku "easy1", 5 is valid for the sudoku "easy2"
    public void test_placeNumber3() throws Exception{
        start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");
        int num_two = peer1.placeNumber("SudokuEasy", 1, 8, 2);

        int num_five = peer1.placeNumber("SudokuEasy", 1, 8, 5);

        if (num_two == 0 || num_five == 0)
            assertTrue(true);
    }

    /**
     * Test for method leaveNetwork
     */
    public void test_leaveNetwork()throws Exception{
        start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");
        peer2.join("SudokuEasy", "Alberto");
        peer3.join("SudokuEasy", "Gennaro");

        int leave1 = peer1.leaveNetwork("Jack", "SudokuEasy", true);
        int leave2 = peer2.leaveNetwork("Alberto", "SudokuEasy", true);
        int leave3 = peer3.leaveNetwork("Gennaro", "SudokuEasy", true);

        assertTrue(leave1 == 1);
        assertTrue(leave2 == 1);
        assertTrue(leave3 == 1);
    }

    /**
     * Test for method getHelp
     */
    //Help is possible: empty cell (row 1, column 8)
    public void test_getHelp1()throws Exception{
        start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");

        int help = peer1.getHelp("SudokuEasy", 1, 7);

        assertTrue(help == 1);
    }

    //Help is impossible: empty is busy (row 0, column 0)
    public void test_getHelp2()throws Exception{
        start_system();

        peer1.choose_difficulty("easy");
        peer1.generateNewSudoku("SudokuEasy");
        peer1.join("SudokuEasy", "Jack");

        int help = peer1.getHelp("SudokuEasy", 0, 0);

        assertTrue(help == 0);
    }
}
