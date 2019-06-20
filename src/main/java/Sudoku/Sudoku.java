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
    private Integer[][] sudoku;

    /**
     * Constructor
     * @param name: name of the sudoku
     */
    public Sudoku(String name) {
        this.name = name;
        sudoku = new Integer[9][9];
    }

    /**
     * Method that reads the sudoku matrix inside the JSON file
     * @return sudoku matrix
     */
    public void generate_sudoku(String difficulty) throws IOException, ParseException {

        // parsing file "sudokuUnsolved.json"
        Object obj = new JSONParser().parse(new FileReader("sudokuUnsolved.json"));

        // typecasting obj to JSONObject
        JSONObject json_obj = (JSONObject) obj;

        //generate a random number
        Random rand = new Random();
        int type_choose = rand.nextInt(2);
        type_choose += 1;

        //concat the random number with the difficulty
        String final_choose = difficulty + type_choose;

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
                    sudoku[row][column] = Integer.parseInt(value[column]);
            }
            row++;
        }
    }

    /**
     * Getter methods
     */
    public String getNameSudoku() {
        return name;
    }

    public Integer[][] getMatrix() {
        return sudoku;
    }
}