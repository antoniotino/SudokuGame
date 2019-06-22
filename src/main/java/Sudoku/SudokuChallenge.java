package Sudoku;

import User.User;
import net.tomp2p.peers.PeerAddress;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class SudokuChallenge implements Serializable {

    private Sudoku sudoku;
    private SolvedSudoku solvedSudoku;
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