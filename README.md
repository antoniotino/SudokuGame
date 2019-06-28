# SudokuGame

## About us
This project was developed for the "Architetture Distribuite per il Cloud" course. <br>
University of Salerno

##### Referents of the course
* Prof. Alberto Negro (Foundamentals and Algorithms for Distributed Systems)
* Prof. Gennaro Cordasco and Ph.D. Carmine Spagnuolo (Peer-to-Peer Networks) 

##### Team 
* Team name: Jack&Tino
* Team leader: Giacomo Astarita
    * GitHub: giacomoastarita
* Components of the team: Antonio Tino
    * GitHub: antoniotino

## Technologies/Library
* Java SE (version 8)
* Apache Maven
* JSON
* JUnit
* TomP2P
* Docker

## Problem Statement
Design and development of the Sudoku Game on a P2P network. <br>
Each user can place a number of the sudoku game, if it is not already placed takes 1 point, if it is already placed and it is rights takes 0 point, in other case receive -1 point. The games is based on 9 x 9 matrix. All users that play to a game are automatically informed when a user increment its score, and when the game is finished. The system allows the users to generate (automatically) a new Sudoku challange identified by a name, join in a challenge using a nickname, get the integer matrix describing the Sudoku challenge, and place a solution number

## Default Features
* Integer[][] generateNewSudoku(String _game_name)
    * This method allows to generate a new sudoku game
* boolean join(String _game_name, String _nickname)
    * This method allows a user to join in a game
* Integer[][] getSudoku(String _game_name)
    * This method allows to get the sudoku game with only the number placed by the user
* Integer placeNumber(String _game_name, int _i, int _j, int _number);
    * This method allows to place a new solution number in the game
## New Features
* Integer leaveNetwork(String _nickname, String _game_name)
    * This method allows a peer to leave the network
* choose_difficulty(String difficulty)
    * This method allows you to set the difficulty of sudoku
* HashMap<String, String> active_room()
    * This method allows to view the active rooms (matches in progress) 
*Integer getHelp(String _game_name, int row, int column)
    * This method allows you to help the user (max 3)     
    
## Other Methods    
* public void addUser(User user)
    * This method allows adding the user to the system (Sudoku game)
* private void victoryMsg(SudokuChallenge sudokuChallenge)
    * This method calculates the winner / winners and generates a message to alert others  
* private void sendMessage(String message, SudokuChallenge sudokuChallenge)
    * This method allows you to send a message to other peers
    
## Graphical User Interface
* Terminal Interface <br>
![Img](https://github.com/antoniotino/SudokuGame/blob/master/img/TerminalInterface.png)

* GUI <br>
![Img](https://github.com/antoniotino/SudokuGame/blob/master/img/GUI.png)
   
## Solution 
Project under construction

## Test
#### JUnit Test
Framework used: JUnit 4 <br>
Class: TestSudokuGameImpl.java <br>
Path: \src\test\java <br> <br>
The methods tested are all those of the SudokuGameImpl class which allows you to join the system and play with other users.They are:
<br> <br> **generateNewSudoku** <br>
There is only one test for this method:
1. *test_generateNewSudoku()* which generates two sudokus with the same name and this operation is not possible.


**duplicateNickname** <br>   
For this method there are two tests:
1. *test_duplicateNickname1()* in which only one user is entered whose nickname is not present in the system.
2. *test_duplicateNickname2()* in which two users are inserted: The second user would like to use the same nickname as the first and this operation is not possible.

**join** <br>
For this method there are two tests:
1. *test_join1()* in which a user joins an existing sudoku.
2. *test_join2()* in which a user tries to join a sudoku that does not exist.

**getSudoku** <br>
For this method there are two tests:
1. *test_getSudoku1()* in which the print of an existing sudoku is tested.
2. *test_getSudoku2()* in which the print of a non-existent sudoku is tested.

**placeNumber** <br>
For this method there are three tests:
1. *test_placeNumber1()* in which puts a correct number.
2. *test_placeNumber2()* in which puts a inccorrect number.
3. *test_placeNumber3()* in which it puts a correct number but cell is already busy.

**addUser** <br>
There is only one test for this method:
1. *test_addUser()* in which users are added to the system

**leaveNetwork** <br>
There is only one test for this method:
1. *test_leaveNetwork()* in which users leave the system.

**choose_difficulty** <br>
There is only one test for this method:
1. *test_choose_difficulty()* in which the difficulties of three sudokus are set

**active_room** <br>
For this method there are two tests:
1. *test_activeRoom1()* where active sudokus are shown (in this case one is shown because it is created before the operation)
2. *test_activeRoom2()* where active sudokus are shown (in this case no sudokus are shown as there are none)

**getHelp** <br>
For this method there are two tests:
1. *test_getHelp1()* where help is required for an empty cell.
2. *test_getHelp2()* where help is required for an already busy cell.

Number of peers: 4 <br>
Tests passed: 17 of 17 test
 
#### Test  without JUnit 
Class: TestGame.java <br>
Path: \src\test\java <br>
The methods tested are all those of the SudokuGameImpl class which allows you to join the system and play with other users.They are:
* generateNewSudoku
* duplicateNickname
* join
* getSudoku
* placeNumber
* addUser
* leaveNetwork
* choose_difficulty
* active_room
* getHelp

Number of peers: 4

## Compile & Execute in local environment
* You could use:
    * Eclipse, Intellij or any other IDE you like
        * Steps:
            * ...
    * Terminal (PowerShell/CMD on Windows or BASH on Linux-based system)
        * Commands:
            * ...
* Other information will be added soon...

## Build in a Docker container
Project under construction
