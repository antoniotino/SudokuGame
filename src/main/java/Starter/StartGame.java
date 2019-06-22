package Starter;

import Sudoku.SudokuGameImpl;
import User.User;
import Terminal.MessageListenerTerminal;
import Terminal.TerminalGrafic;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.Scanner;

public class StartGame {

    public static void main(String[] args) throws Exception {
        int peerID = Integer.parseInt(args[0]);

        TextIO textIO = TextIoFactory.getTextIO();
        TextTerminal terminal = textIO.getTextTerminal();

        terminal.printf("\n\n \t * * * * * * * * * * * * * * * * * * * * *\n");
        terminal.printf("\t *\tWelcome in SudokuGame \t\t *\n");
        terminal.printf("\t *\tCreated by Jack&Tino \t\t *\n");
        terminal.printf("\t * * * * * * * * * * * * * * * * * * * * *\n\n");

        terminal.printf("Insert your nickname: ");
        String nickname = textIO.newStringInputReader().read();
        User user = new User(nickname);

        terminal.printf("Choose the graphic quality:\t1) Terminal \t 2) GUI : ");
        int choose = textIO.newIntInputReader().withMaxVal(2).withMinVal(1).read();
        if(choose == 1){
            terminal.resetLine();
            terminal.abort();
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerTerminal(peerID));
            peer.addUser(user);
            TerminalGrafic terminalGrafic = new TerminalGrafic(peer, peerID, user);
            terminalGrafic.startTerminal();
        }else{
            terminal.abort();
            System.out.println("GUI");
        }
    }
}