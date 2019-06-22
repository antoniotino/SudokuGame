package Sudoku;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import java.util.Map;
import java.util.Iterator;
import java.util.Random;

public class Sudoku implements Serializable {

    private String name;
    private Integer[][] unsolved_sudoku;
    private Integer[][] solved_sudoku;
    private String final_choose;

    /**
     * Constructor
     *
     * @param name: name of the sudoku
     */
    public Sudoku(String name) {
        this.name = name;
        unsolved_sudoku = new Integer[9][9];
        solved_sudoku = new Integer[9][9];
    }

    /**
     * Method that reads the sudoku matrix inside the JSON file
     */
    public void generate_sudoku(String difficulty) throws IOException, ParseException {

        //generate a random number
        int type_choose = generateRandomNumber();

        //concat the random number with the difficulty
        final_choose = difficulty + type_choose;

        //generate sudoku
        readJSON("unsolvedSudoku.json", final_choose, unsolved_sudoku);

        //generate sudoku solution
        readJSON("solvedSudoku.json", final_choose, solved_sudoku);
    }

    private void readJSON(String file_name, String final_choose, Integer[][] matrix) throws IOException, ParseException{
        // parsing file JSON
        Object obj = new JSONParser().parse(new FileReader(file_name));

        // typecasting obj to JSONObject
        JSONObject json_obj = (JSONObject) obj;

        // getting matrix
        JSONArray json_array = (JSONArray) json_obj.get(final_choose);

        // iterating
        int row = 0;
        Iterator itr1 = json_array.iterator();
        while (itr1.hasNext()) {
            Iterator<Map.Entry> itr2 = ((Map) itr1.next()).entrySet().iterator();
            while (itr2.hasNext()) {
                Map.Entry pair = itr2.next();
                String[] value = String.valueOf(pair.getValue()).split(",");

                for (int column = 0; column < value.length; column++)
                    matrix[row][column] = Integer.parseInt(value[column]);
            }
            row++;
        }
    }

    private int generateRandomNumber() {
        Random rand = new Random();
        int r = rand.nextInt(2);
        return r += 1;
    }

    /**
     * Method that inserts an element into the matrix
     */
    public boolean insert_number(int elem, int row, int column){

        if(unsolved_sudoku[row][column] == 0) {
            unsolved_sudoku[row][column] = elem;
            return true;
        }

        return false;
    }

    /**
     * Getter methods
     */
    public String getNameSudoku() {
        return name;
    }

    public Integer[][] getMatrixUnsolved() {
        return unsolved_sudoku;
    }

    public Integer[][] getMatrixSolved() {
        return solved_sudoku;
    }
}