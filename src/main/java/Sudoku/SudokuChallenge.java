package Sudoku;

import net.tomp2p.peers.PeerAddress;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class SudokuChallenge implements Serializable {

    private Sudoku sudoku;
    private String _game_name;

    /**
     * A map containing the peers connected to the sudoku
     * Key: PeerAddress and Value: _game_name
     */
    private HashMap<PeerAddress, String> peersInGame = new HashMap<PeerAddress, String>();

    public SudokuChallenge(String _game_name, String difficulty) throws IOException, ParseException {
        this._game_name = _game_name;
        sudoku = new Sudoku(_game_name);
        sudoku.generate_sudoku(difficulty);
    }

    /**
     *  The method adds a user into the game
     */
    public Boolean addIntoGame(PeerAddress peerAddress, String _game_name)
    {
        for(PeerAddress peer : peersInGame.keySet())
            if(peer.equals(peerAddress))
                return false;

        peersInGame.put(peerAddress, _game_name);
        return true;
    }

    /**
     * Method that checks if the number can be entered
     */
    public boolean checker_sudoku(int elem, int row, int column){
        Integer[][] solved = sudoku.getMatrixSolved();
        if(solved[row][column] == elem){
            sudoku.insert_number(elem, row, column);
            return true;
        }

        return false;
    }

    /**
     * Method that checks if the game is finished
     */
    public boolean end_game(){
        Integer[][] unsolved = sudoku.getMatrixUnsolved();
        Integer[][] solved = sudoku.getMatrixSolved();

        for(int rows=0; rows<9; rows++)
            for(int columns=0; columns<9; columns++)
                if(unsolved[rows][columns] != solved[rows][columns])
                    return false;

        return true;
    }

    /**
     * Method that checks if the number has already been entered
     */
    public boolean number_already_insert(int elem, int row, int column){
        Integer[][] unsolved = sudoku.getMatrixUnsolved();
        if(unsolved[row][column] == elem)
            return true;

        return false;
    }

    /**
     * Getter Methods
     */
    public Sudoku getSudoku() {
        return sudoku;
    }

    public String getGameName(){
        return _game_name;
    }

    public HashMap<PeerAddress, String> getPeersInGame(){
        return peersInGame;
    }
}