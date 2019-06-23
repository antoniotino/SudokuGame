# SudokuGame

## About us
This project was developed for the "Architetture Distribuite per il Cloud" course. <br>
University of Salerno

#### Team 
* Team name: Jack&Tino
* Team leader: Giacomo Astarita
    * GitHub: giacomoastarita
* Components of the team: Antonio Tino
    * GitHub: antoniotino

## Technologies/Library
* Java SE (version 8)
* Apache Maven
* JSON
* TomP2P
* Docker
* Other technologies will be added soon...

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
* boolean leaveNetwork(String _nickname, String _game_name)
    * This method allows a peer to leave the network
* choose_difficulty(String difficulty)
    * This method allows you to set the difficulty of sudoku
* HashMap<String, String> active_room()
    * This method allows to view the active rooms (matches in progress)  
* Other features will be added soon...

## Solution 
Project under construction

## Test case 
Project under construction

## Compile & Execute in local environment
* You could use:
    * Eclipse, Intellij or any other IDE you like
    * Terminal (PowerShell/CMD on Windows or BASH on Linux-based system)
* Other information will be added soon...

## Build in a Docker container
Project under construction
