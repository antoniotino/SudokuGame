package Starter;

import GraphicUserInterface.MessageListenerGUI;
import Sudoku.SudokuGameImpl;
import User.*;
import Terminal.MessageListenerTerminal;
import Terminal.TerminalGrafic;
import GraphicUserInterface.SudokuGUI;
import net.tomp2p.peers.PeerAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class StartGame {

    public static void main(String[] args) throws Exception {
        int peerID = Integer.parseInt(args[0]);
        Scanner scanner= new Scanner(System.in);
        String nickname="";
        System.out.printf("\n\n \t * * * * * * * * * * * * * * * * * * * * *\n");
        System.out.printf("\t *\tWelcome in SudokuGame \t\t *\n");
        System.out.printf("\t *\tCreated by Jack&Tino \t\t *\n");
        System.out.printf("\t * * * * * * * * * * * * * * * * * * * * *\n\n");

        int choose;
        do{
            System.out.printf("Choose the graphic quality:\t1) Terminal \t 2) GUI : ");
            choose = scanner.nextInt();
        }while(choose <1 || choose>2);

        if (choose == 1) {
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerTerminal(peerID));
            HashMap<PeerAddress, User> nicknameHash= peer.duplicateNickname();
            ArrayList<String> nicknameUser= new ArrayList<String>();
            for(PeerAddress str: nicknameHash.keySet())
                nicknameUser.add(nicknameHash.get(str).getNickname());
            User user;
            do{
                System.out.print("Insert your nickname: ");
                nickname = scanner.next();
                user = new User(nickname);
            }while(nicknameUser.contains(nickname));
            peer.addUser(user);
            TerminalGrafic terminalGrafic = new TerminalGrafic(peer, peerID, user);
            terminalGrafic.startTerminal();
        } else{
            SudokuGameImpl peer = new SudokuGameImpl(peerID, "127.0.0.1", new MessageListenerGUI(peerID));
            HashMap<PeerAddress, User> nicknameHash= peer.duplicateNickname();
            HashMap<String, Integer> score= peer.score_peer();
            ArrayList<String> nicknameUser= new ArrayList<String>();
            for(PeerAddress str: nicknameHash.keySet())
                nicknameUser.add(nicknameHash.get(str).getNickname());
            User user;
            do{
                System.out.print("Insert your nickname: ");
                nickname = scanner.next();
                user = new User(nickname);
            }while(nicknameUser.contains(nickname));
            peer.addUser(user);
            score.put(user.getNickname(), user.getScore());
            SudokuGUI sudokuGui = new SudokuGUI(peer, peerID, user);
            sudokuGui.createGraphicInterface();
        }
    }
}