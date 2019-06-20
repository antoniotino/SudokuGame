package Sudoku;

import net.tomp2p.peers.PeerAddress;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class SudokuChallenge implements Serializable {

    private Sudoku sudoku;
    private SolvedSudoku solvedSudoku;
    private String _game_name;

    private HashMap<PeerAddress, String> peers_on_game = new HashMap<PeerAddress, String>();
    private HashMap<PeerAddress, Integer> peers_score = new HashMap<PeerAddress, Integer>();

    public SudokuChallenge(String _game_name, String difficulty) throws IOException, ParseException {
        this._game_name = _game_name;
        sudoku = new Sudoku(_game_name);
        sudoku.generate_sudoku(difficulty);
    }

    /**
     * Getter Methods
     */
    public Sudoku getSudoku() {
        return sudoku;
    }
}
