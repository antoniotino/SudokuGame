package Starter;

import Sudoku.SudokuGameImpl;
import Terminal.MessageListenerTerminal;
import Terminal.TerminalGrafic;

import java.util.Scanner;
import java.util.logging.Logger;

public class StartGame {

    public static void main(String[] args) throws Exception {

        /* Commento temporaneo: verificare la fattibilità dell'idea */

        int peerID = 0;
        final Logger LOGGER = Logger.getLogger(SudokuGameImpl.class.getName());
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose the graphic quality: \t 1) Terminal \t 2) GUI \t: ");
        int choose = scanner.nextInt();
        if(choose == 1){
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerTerminal(peerID));
            LOGGER.info("Open terminal");
            TerminalGrafic terminal = new TerminalGrafic(peer, peerID);
            terminal.startTerminal();
            LOGGER.info("Close terminal");
        }else if(choose == 2)
            System.out.println("GUI");
        else
            System.out.println("Choose error!");
    }
}