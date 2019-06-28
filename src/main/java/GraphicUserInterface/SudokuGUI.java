package GraphicUserInterface;

import Sudoku.SudokuGameImpl;
import User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class SudokuGUI {
    private JFrame frame = new JFrame("Sudoku Game");
    private JTextField textField[][] = new JTextField[9][9];
    private GridPanel gridPanel = new GridPanel(new GridLayout(9, 9, 1, 1));
    private User user;
    private SudokuGameImpl peer;
    private int peerID;
    private String join_game;
    private boolean join;
    Integer[][] sudoku = new Integer[9][9];
    private List listMessages = new List();
    int hour = 0;
    int minute = 0;
    int second = 0;
    int millisecond = 0;
    int count = 3;
    Thread t;
    Integer value;
    private JTextField focusedTextBox;
    private int max;
    private JScrollPane listScroller;
    private HashMap<String, Integer> userScore= new HashMap<String, Integer>();

    // constructor for blank SudokuTable. adds empty text fields to gridpanel
    public SudokuGUI(SudokuGameImpl peer, int peerID, User user) {
        this.peer = peer;
        this.peerID = peerID;
        this.user = user;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                textField[row][column] = new JTextField();
                textField[row][column].setBackground(Color.WHITE);
                textField[row][column].setForeground(Color.BLACK);
                textField[row][column].setHorizontalAlignment(0);
                textField[row][column].setEnabled(false);
                textField[row][column].addActionListener(e -> ((JTextField) e.getSource()).setBackground(new Color(229, 247, 255)));
                gridPanel.add(textField[row][column]);
            }
        }
    }

    public void createGraphicInterface() {
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.okButtonText", "Agree");
        UIManager.put("OptionPane.yesButtonText", "Yes");

        TimerTask scoreTask = new TimerTask() {
            @Override
            public void run() {
                    userScore=peer.score_peer();
            }
        };
        java.util.Timer timerScore = new java.util.Timer();
        timerScore.schedule(scoreTask, 500, 500);

        JPanel mainPanel = new JPanel(new GridBagLayout()); //create main panel
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        //JPanel rightPanel = new JPanel(new GridBagLayout());
        JPanel infoPanel = new JPanel((new GridBagLayout()));
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.anchor = GridBagConstraints.EAST;

        JLabel labelInfo = new JLabel("PeerID:" + peerID + " Nickname: " + user.getNickname() + "\n Score: " + user.getScore(), JLabel.RIGHT);
        labelInfo.setFont(new Font("Helvetica", Font.BOLD, 15));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        infoPanel.add(labelInfo, gridBagConstraints);

        JLabel labelTime = new JLabel("00:00:00", JLabel.RIGHT);
        labelTime.setFont(new Font("Helvetica", Font.BOLD, 36));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        infoPanel.add(labelTime, gridBagConstraints);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JButton buttonCreate = new JButton("Create a new Sudoku");
        buttonCreate.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonCreate.setOpaque(true);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonCreate);
        buttonCreate.addActionListener(e -> {
            String _game_name="";
            HashMap<String, String> roomExisting = peer.active_room();
            ArrayList<String> roomName= new ArrayList<String>();
            for(String str: roomExisting.keySet()) {
                roomName.add(str);
            }
            do {
                String game_name = JOptionPane.showInputDialog(
                        frame,
                        "Insert the name of new Sudoku:",
                        "Create Sudoku",
                        JOptionPane.PLAIN_MESSAGE);
                _game_name= game_name;
            }while(roomName.contains(_game_name));
                if (_game_name != null && _game_name.length() > 0) {
                    Object[] options = {"EASY", "MEDIUM", "HARD"};
                    String difficulty = "";
                    int choose = JOptionPane.showOptionDialog(null, "Choose your difficulty and click OK to continue", "Choose a difficulty",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.YES_NO_CANCEL_OPTION,
                            null, options, options[0]);
                    if (choose == 0) {
                        difficulty = "easy";
                    } else if (choose == 1) {
                        difficulty = "medium";
                    } else if (choose == 2) {
                        difficulty = "hard";
                    } else {
                        System.out.println("Error into choose!");
                    }
                    peer.choose_difficulty(difficulty);
                    Integer[][] matrix = peer.generateNewSudoku(_game_name);
                    if (matrix.length == 0)
                        JOptionPane.showMessageDialog(null, "There is already a room with this name", "Error", JOptionPane.ERROR_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "Created a room with this name", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "There is already a room with this name", "Error", JOptionPane.ERROR_MESSAGE);
                }
        });

        JButton buttonView = new JButton("Sudoku actually active");
        JButton buttonJoin = new JButton("Join in a game");
        buttonView.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonView.setOpaque(true);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonView);
        buttonView.addActionListener(e -> {
            HashMap<String, String> room_active = peer.active_room();
            if (room_active.size() > 0) {
                ArrayList<Object> possibleValues = new ArrayList<Object>();
                for (String str : room_active.keySet()) {
                    possibleValues.add(str);
                }
                String selectedValue = (String) JOptionPane.showInputDialog(null,
                        "Choose one existing sudoku", "Existing Rooms",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        possibleValues.toArray(), possibleValues.get(0));
                if (selectedValue != null) {
                    join_game = selectedValue;
                    join = peer.join(join_game, user.getNickname());
                    if (join) {
                        JOptionPane.showMessageDialog(null, "Successfully join", "Join Complete", JOptionPane.INFORMATION_MESSAGE);
                        buttonView.setEnabled(false);
                        buttonJoin.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error in join to game", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "There are no active sudoku", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        buttonJoin.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonJoin.setOpaque(true);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonJoin);
        buttonJoin.addActionListener(e -> {
            join_game = JOptionPane.showInputDialog(
                    frame,
                    "Insert a name of game where you would join",
                    "Join in a game",
                    JOptionPane.PLAIN_MESSAGE);
            join = peer.join(join_game, user.getNickname());
            if (join) {
                JOptionPane.showMessageDialog(null, "Successfully join", "Join Complete", JOptionPane.INFORMATION_MESSAGE);
                buttonView.setEnabled(false);
                buttonJoin.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Error in join to game", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        TimerTask tasknew = new TimerTask() {
            @Override
            public void run() {
                listMessages.removeAll();
                for (Object message : peer.getMessages()) {
                    listMessages.add(message.toString());
                }
            }
        };
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(tasknew, 3000, 3000);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        listScroller = new JScrollPane(listMessages);

        JButton buttonSudoku = new JButton("Get a Sudoku");
        buttonSudoku.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonSudoku.setOpaque(true);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonSudoku);
        buttonSudoku.addActionListener(e -> {
            if (join_game == null || !join) {
                JOptionPane.showMessageDialog(frame, "You need to join a game", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Thread t = new Thread(() -> {
                    for (; ; ) {
                        try {
                            Thread.sleep(1);
                            if (millisecond > 1000) {
                                millisecond = 0;
                                second++;
                            }
                            if (second > 60) {
                                second = 0;
                                minute++;
                            }
                            if (minute > 60) {
                                minute = 0;
                                hour++;
                            }
                            millisecond++;
                            if (second < 10 && minute < 10 && hour < 10)
                                labelTime.setText("0" + hour + ":0" + minute + ":0" + second);
                            else if (second < 10 && minute < 10 && hour > 10)
                                labelTime.setText(hour + ":0" + minute + ":0" + second);
                            else if (second < 10 && minute > 10 && hour < 10)
                                labelTime.setText("0" + hour + ":" + minute + ":0" + second);
                            else if (second > 0 && minute < 10 && hour < 10)
                                labelTime.setText("0" + hour + ":0" + minute + ":" + second);
                            else
                                labelTime.setText(hour + ":" + minute + ":" + second);
                            labelInfo.setText("PeerID: " + peerID + "  Nickname: " + user.getNickname() + "  Score: " + user.getScore() + "\n Game: " + join_game);
                        } catch (Exception exception) {

                        }
                    }
                });
                t.start();
                sudoku = peer.getSudoku(join_game);
                printSudoku(sudoku, join_game, textField, userScore);
                buttonSudoku.setEnabled(false);
            }
        });

        JButton buttonLeave = new JButton("Leave Sudoku");
        buttonLeave.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonLeave.setOpaque(true);
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonLeave);
        buttonLeave.addActionListener(actionEvent -> {
            int value = JOptionPane.showConfirmDialog(null, "Are you sure to leave the network?", "Leave Sudoku", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
            if (value == JOptionPane.OK_OPTION) {
                peer.leaveNetwork(user.getNickname(), join_game, join);
                System.exit(0);
            }
        });

        JButton buttonHelp = new JButton("Get Help x" + count);
        buttonHelp.setFont(new Font("Helvetica", Font.BOLD, 15));
        buttonHelp.setOpaque(true);
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        buttonPanel.add(buttonHelp);
        buttonHelp.addActionListener(actionEvent -> {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    if (focusedTextBox == textField[row][column] && count > 0 && focusedTextBox.getText().equals("")) {
                        peer.getHelp(join_game, row, column);
                        printSudoku(peer.getSudoku(join_game), join_game, textField, userScore);
                        count--;
                        buttonHelp.setText("Get Help x" + count);
                    }
                }
            }
            if (count == 0)
                buttonHelp.setEnabled(false);
        });

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                textField[row][column].addFocusListener(new FocusListener() {
                    public void focusLost(FocusEvent e) {
                        //printSudoku(peer.getSudoku(join_game), join_game, textField, userScore);
                        if (e.getSource() instanceof JTextField) {
                            //HashMap<String, Integer> userScore=peer.score_peer();
                            focusedTextBox = (JTextField) e.getSource();
                        }
                    }

                    public void focusGained(FocusEvent e) {
                        //HashMap<String, Integer> userScore=peer.score_peer();
                        printSudoku(peer.getSudoku(join_game), join_game, textField, userScore);
                    }
                });
                int finalRow = row;
                int finalColumn = column;
                textField[row][column].addActionListener(actionEvent -> {
                    value = peer.placeNumber(join_game, finalRow, finalColumn, Integer.parseInt(((JTextField) actionEvent.getSource()).getText()));
                    if (value == -1) {
                        JOptionPane.showMessageDialog(frame, "Number not valid for cell");
                    } else if (value == 1) {
                        JOptionPane.showMessageDialog(frame, "Number valid for cell");
                        printSudoku(peer.getSudoku(join_game), join_game, textField, userScore);
                    } else if (value == 2) {
                        printSudoku(peer.getSudoku(join_game), join_game, textField, userScore);
                        peer.leaveNetwork(user.getNickname(), join_game, join);
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Number already in cell");
                    }
                });
            }
        }

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 3;
        mainPanel.add(infoPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        mainPanel.add(buttonPanel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(listScroller, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.insets = new Insets(0, 300, 0, 300);
        mainPanel.add(gridPanel, gridBagConstraints);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1360, 768);
        frame.setResizable(true);
        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setVisible(true);
    }

    /* Nested class for the grid panel used in the GUI */
    public class GridPanel extends JPanel {

        GridPanel(GridLayout layout) {
            super(layout);
        }

        //draw lines for 3x3 quadrants
        public void paintComponent(Graphics g) {
            g.fillRect(getWidth() / 3 - 1, 0, 3, getHeight());
            g.fillRect(2 * getWidth() / 3 - 1, 0, 3, getHeight());
            g.fillRect(0, getHeight() / 3 - 1, getWidth(), 3);
            g.fillRect(0, 2 * getHeight() / 3 - 2, getWidth(), 3);
        }
    }

    public void printSudoku(Integer[][] sudoku, String join_game, JTextField textField[][], HashMap<String, Integer> userScore) {
        int count=0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                textField[row][column].setEnabled(true);
                textField[row][column].setText("" + (sudoku[row][column] != 0 ? sudoku[row][column] : ""));
                if(sudoku[row][column]==0) count++;
                textField[row][column].setBackground(sudoku[row][column] != 0 ? Color.LIGHT_GRAY : Color.WHITE);
                textField[row][column].setEditable(sudoku[row][column] != 0 ? false : true);
                textField[row][column].setFocusable(sudoku[row][column] != 0 ? false : true);
                textField[row][column].setForeground(Color.BLACK);
            }
        }
        if(count==0) {
            JOptionPane.showConfirmDialog(null, getVictory(userScore), "End Game", JOptionPane.DEFAULT_OPTION);
            peer.leaveNetwork(user.getNickname(), join_game, join);
            System.exit(0);

        }
        listScroller.scrollRectToVisible(new Rectangle(0, listScroller.getHeight() - 2, 1, 1));
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