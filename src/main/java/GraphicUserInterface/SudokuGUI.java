package GraphicUserInterface;

import Sudoku.SudokuGameImpl;
import User.User;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
public class SudokuGUI {
    private JFrame frame = new JFrame("Sudoku Game");
    private JTextField textField[][] = new JTextField[9][9];
    private GridPanel gridPanel = new GridPanel(new GridLayout(9,9,1,1));
    private User user;
    private SudokuGameImpl peer;
    private int peerID;
    private String join_game;
    private boolean join;
    Integer[][] sudoku = new Integer[9][9];
    int hour=0;
    int minute=0;
    int second=0;
    int millisecond=0;
    // constructor for blank SudokuTable. adds empty text fields to gridpanel
    public SudokuGUI(SudokuGameImpl peer, int peerID, User user) {
        this.peer=peer;
        this.peerID=peerID;
        this.user=user;
        for(int row=0; row < 9 ; row++){
            for(int column=0; column < 9 ; column++){
                textField[row][column]= new JTextField();
                textField[row][column].setBackground(Color.WHITE);
                textField[row][column].setForeground(Color.BLACK);
                textField[row][column].setHorizontalAlignment(0);
                textField[row][column].addActionListener(e -> ((JTextField) e.getSource()).setBackground(new Color(229,247,255)));
                textField[row][column].addFocusListener(new FocusListener() {

                    public void focusLost(FocusEvent e) {
                        ((JTextField) e.getSource()).setBackground(Color.WHITE);
                    }

                    public void focusGained(FocusEvent e) { }
                });
                gridPanel.add(textField[row][column]);
            }
        }

    }

    public void createGraphicInterface(){

        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Agree");
        UIManager.put("OptionPane.yesButtonText", "Yes");
        JPanel mainPanel = new JPanel(new GridBagLayout()); //create main panel
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JPanel rightPanel = new JPanel(new GridBagLayout());

        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;

        JLabel labelInfo= new JLabel("PeerID:"+peerID + " Nickname: "+ user.getNickname()+ "\n Score: "+user.getScore(), JLabel.RIGHT);
        labelInfo.setFont(new Font("Helvetica", Font.BOLD, 15));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        rightPanel.add(labelInfo, gridBagConstraints);

        JLabel labelTime= new JLabel("00:00:00", JLabel.RIGHT);
        labelTime.setFont(new Font("Helvetica", Font.BOLD, 36));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 100;
        rightPanel.add(labelTime, gridBagConstraints);

        JButton buttonCreate = new JButton("Create a new Sudoku");
        buttonCreate.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonCreate.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonCreate, gridBagConstraints);
        buttonCreate.addActionListener(e -> {
            String game_name = JOptionPane.showInputDialog(
                    frame,
                    "Insert the name of new Sudoku:",
                    "Create Sudoku",
                    JOptionPane.PLAIN_MESSAGE);

            String _game_name= game_name;
            if(_game_name!=null && _game_name.length()>0){
                Object[] options = { "EASY", "MEDIUM", "HARD" };
                String difficulty = "";
                int choose= JOptionPane.showOptionDialog(null, "Choose your difficulty and click OK to continue", "Choose a difficulty",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_CANCEL_OPTION,
                        null, options, options[0]);
                if(choose == 0) {
                    difficulty = "easy";
                } else if(choose == 1) {
                    difficulty = "medium";
                } else if (choose == 2) {
                    difficulty ="hard";
                } else {
                    System.out.println("Error into choose!");
                }
                peer.choose_difficulty(difficulty);
                Integer [][] matrix = peer.generateNewSudoku(_game_name);
                if(matrix.length == 0)
                    JOptionPane.showMessageDialog(null, "There is already a room with this name", "Error", JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Created a room with this name", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        JButton buttonView = new JButton("Sudoku actually active");
        buttonView.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonView.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonView, gridBagConstraints);
        buttonView.addActionListener(e -> {
            HashMap<String, String> room_active = peer.active_room();
            if(room_active.size()==0){
                JOptionPane.showMessageDialog(null, "There are no active sudoku", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {

                ArrayList<Object> possibleValues= new ArrayList<Object>();
                for(String str : room_active.keySet()) {
                    possibleValues.add(str);
                }
                String selectedValue = (String)JOptionPane.showInputDialog(null,
                        "Choose one existing sudoku", "Existing Rooms",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        possibleValues.toArray(), possibleValues.get(0));
                if(selectedValue!= null){
                    join_game=selectedValue;
                    join = peer.join(join_game, user.getNickname());
                    if(join){
                        JOptionPane.showMessageDialog(null, "Successfully join", "Join Complete", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Error in join to game", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton buttonJoin = new JButton("Join in a game");
        buttonJoin.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonJoin.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonJoin, gridBagConstraints);
        buttonJoin.addActionListener(e -> {
            join_game = JOptionPane.showInputDialog(
                    frame,
                    "Insert a name of game where you would join",
                    "Join in a game",
                    JOptionPane.PLAIN_MESSAGE);
            join = peer.join(join_game, user.getNickname());

                if(join){
                    JOptionPane.showMessageDialog(null, "Successfully join", "Join Complete", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Error in join to game", "Error", JOptionPane.ERROR_MESSAGE);
                }
        });

        JButton buttonSudoku = new JButton("Get a Sudoku");
        buttonSudoku.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonSudoku.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonSudoku, gridBagConstraints);
        buttonSudoku.addActionListener(e -> {

            if(join_game==null || !join){
                JOptionPane.showMessageDialog(frame, "You need to join a game", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Thread t= new Thread(() -> {
                    for(;;)
                    {
                        try{
                            Thread.sleep(1);
                            if(millisecond>1000){
                                millisecond=0;
                                second++;
                            }
                            if(second>60){
                                second=0;
                                minute++;
                            }
                            if(minute>60){
                                minute=0;
                                hour++;
                            }
                            millisecond++;
                            if(second <10 && minute <10 && hour <10)
                                labelTime.setText("0"+hour+":0"+minute+":0"+second);
                            else if(second <10 && minute <10 && hour >10)
                                labelTime.setText(hour+":0"+minute+":0"+second);
                            else if(second <10 && minute >10 && hour <10)
                                labelTime.setText("0"+hour+":"+minute+":0"+second);
                            else if(second >0 && minute <10 && hour <10)
                                labelTime.setText("0"+hour+":0"+minute+":"+second);
                            else
                                labelTime.setText(hour+":"+minute+":"+second);

                            labelInfo.setText("PeerID: "+peerID+"  Nickname: "+user.getNickname()+"  Score: "+user.getScore()+"\n Game: "+join_game);
                        }
                        catch(Exception exception)
                        {

                        }
                    }
                });
                t.start();
                sudoku=peer.getSudoku(join_game);
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        textField[row][column].setText(""+ (sudoku[row][column] !=0 ? sudoku[row][column] : ""));
                        textField[row][column].setBackground(sudoku[row][column] !=0 ? Color.LIGHT_GRAY: Color.WHITE);
                        textField[row][column].setEditable(sudoku[row][column] !=0 ? false: true);
                        textField[row][column].setFocusable(sudoku[row][column] !=0 ? false: true);
                        textField[row][column].setForeground(Color.BLACK);
                        int finalRow = row;
                        int finalColumn = column;
                        textField[row][column].getDocument().addDocumentListener(new DocumentListener() {
                            @Override
                            public void insertUpdate(DocumentEvent documentEvent) {
                                warn(documentEvent);
                            }

                            @Override
                            public void removeUpdate(DocumentEvent documentEvent) {
                                warn(documentEvent);
                            }

                            @Override
                            public void changedUpdate(DocumentEvent documentEvent) {
                                warn(documentEvent);
                            }

                            public void warn(DocumentEvent documentEvent) {
                                Integer number;
                                String value;
                                DocumentEvent.EventType type = documentEvent.getType();
                                if (type.equals(DocumentEvent.EventType.CHANGE)) {
                                    if (Pattern.matches("\\d{1}", textField[finalRow][finalColumn].getText()) == true) {
                                        value = textField[finalRow][finalColumn].getText();
                                    } else {
                                        value = "0";
                                        JOptionPane.showMessageDialog(null, "String not valid", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else if (type.equals(DocumentEvent.EventType.INSERT)) {
                                    if (Pattern.matches("\\d{1}", textField[finalRow][finalColumn].getText()) == true) {
                                        value = textField[finalRow][finalColumn].getText();
                                    } else {
                                        value = "0";
                                        JOptionPane.showMessageDialog(null, "String not valid", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else {
                                    value = "0";
                                }
                                if (value == "0") {
                                    number = 0;
                                } else {
                                    number = peer.placeNumber(join_game, finalRow, finalColumn, Integer.parseInt(textField[finalRow][finalColumn].getText()));
                                }
                                if (number == 0) {
                                    textField[finalRow][finalColumn].setText("");
                                } else if (number == -1) {
                                    JOptionPane.showMessageDialog(null, "Number not valid for this cell, your score -1", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Number valid for this cell, your score +1", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    textField[finalRow][finalColumn].setEditable(false);
                                }
                            }
                        });
                    }
                }
                buttonSudoku.setEnabled(false);
            }
        });

        JButton buttonLeave = new JButton("Leave from Sudoku Game");
        buttonLeave.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonLeave.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonLeave, gridBagConstraints);
        buttonLeave.addActionListener(actionEvent -> {
            int value= JOptionPane.showConfirmDialog(null, "Are you sure to leave the network?", "Leave Sudoku", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
            if(value==JOptionPane.OK_OPTION) {
                peer.leaveNetwork(user.getNickname(), join_game, join);
                System.exit(0);
            }
        });

        JButton buttonHelp = new JButton("Get Help");
        buttonHelp.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonHelp.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipady = 20;
        rightPanel.add(buttonHelp, gridBagConstraints);
        buttonHelp.addActionListener(actionEvent -> {
            int value= JOptionPane.showConfirmDialog(null, "Are you sure to leave the network?", "Leave Sudoku", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
            if(value==JOptionPane.OK_OPTION) {
                peer.leaveNetwork(user.getNickname(), join_game, join);
                System.exit(0);
            }
        });

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        mainPanel.add(rightPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        mainPanel.add(gridPanel, gridBagConstraints);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100,600);
        frame.setResizable(true);
        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(300,300));
        frame.setVisible(true);
    }

    /* Nested class for the grid panel used in the GUI */
    public class GridPanel extends JPanel{

        GridPanel(GridLayout layout){
            super(layout);
        }

        //draw lines for 3x3 quardrants
        public void paintComponent(Graphics g){
            g.fillRect(getWidth()/3 - 1,0,3,getHeight());
            g.fillRect(2*getWidth()/3 - 1,0,3,getHeight());
            g.fillRect(0,getHeight()/3 - 1,getWidth(),3);
            g.fillRect(0,2*getHeight()/3 - 2,getWidth(),3);
        }
    }

}