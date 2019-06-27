package Starter;

import GraphicUserInterface.MessageListenerGUI;
import Sudoku.SudokuGameImpl;
import User.*;
import Terminal.MessageListenerTerminal;
import Terminal.TerminalGrafic;
import GraphicUserInterface.SudokuGUI;
import java.util.Scanner;
public class StartGame {

    public static void main(String[] args) throws Exception {
        int peerID = Integer.parseInt(args[0]);
        Scanner scanner= new Scanner(System.in);

        System.out.printf("\n\n \t * * * * * * * * * * * * * * * * * * * * *\n");
        System.out.printf("\t *\tWelcome in SudokuGame \t\t *\n");
        System.out.printf("\t *\tCreated by Jack&Tino \t\t *\n");
        System.out.printf("\t * * * * * * * * * * * * * * * * * * * * *\n\n");

        System.out.printf("Insert your nickname: ");
        String nickname = scanner.nextLine();
        User user = new User(nickname);

        System.out.printf("Choose the graphic quality:\t1) Terminal \t 2) GUI : ");
        int choose = scanner.nextInt();
        while(choose <1 || choose>2){
            System.out.printf("Choose the graphic quality:\t1) Terminal \t 2) GUI : ");
            choose = scanner.nextInt();
        }
        if (choose == 1) {
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerTerminal(peerID));
            peer.addUser(user);
            TerminalGrafic terminalGrafic = new TerminalGrafic(peer, peerID, user);
            terminalGrafic.startTerminal();
        } else{
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerGUI(peerID));
            peer.addUser(user);
            SudokuGUI sudokuGui = new SudokuGUI(peer, peerID, user);
            sudokuGui.createGraphicInterface();
        }
    }
}