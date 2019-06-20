package Interface;

public interface SudokuGame {

    /* Default Feature */

    /**
     * Creates new games.
     *
     * @param _game_name a String, the sudoku game name.
     * @return sudoku (matrix)
     */
    Integer[][] generateNewSudoku(String _game_name);

    /**
     * Joins in a game.
     *
     * @param _game_name a String, the sudoku game name.
     * @param _nickname  a String, the name of the user.
     * @return true if the join success, false otherwise.
     */
//    boolean join(String _game_name, String _nickname);

    /**
     * Gets the Sudoku matrix game, with only the number placed by the user.
     * @param _game_name a String, the sudoku game name.
     * @return the integer matrix of the sudoku game.
     */
//    Integer[][] getSudoku(String _game_name);

    /**
     * Places a new solution number in the game.
     *
     * @param _game_name a String, the sudoku game name.
     * @param _i         the position on the row.
     * @param _j         the position on the column.
     * @param _number    the solution number.
     * @return the integer score of the placed number.
     */
 //   Integer placeNumber(String _game_name, int _i, int _j, int _number);

    /* New Feature */

    /**
     * Allows a peer to leave the network
     */
    void leaveNetwork();

    /**
     * Allows you to set the difficulty of sudoku
     */
    void choose_difficulty(String difficulty);
}