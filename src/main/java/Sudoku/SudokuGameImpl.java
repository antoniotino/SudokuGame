package Sudoku;

import Interface.SudokuGame;
import Interface.MessageListener;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class SudokuGameImpl implements SudokuGame {

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;
    final private int ID = 1000;

    /**
     * A map that contains the active rooms
     * Key: game name and Value: difficulty
     */
    private HashMap<String, String> room_active = new HashMap<>();

    /**
     * A map containing the sudoku relative to game name
     * Key: game name and Value: Sudoku
     */
    private HashMap<String, SudokuChallenge> challenges = new HashMap<String, SudokuChallenge>();

    private ArrayList<Object> messages = new ArrayList<Object>();
    private String difficulty;
    private Integer[][] empty = new Integer[0][0];

    public SudokuGameImpl(int _id, String _master_peer, final MessageListener _listener) throws Exception {

        peer = new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT + _id).start();
        _dht = new PeerBuilderDHT(peer).start();

        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        if (fb.isSuccess())
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        else
            throw new Exception("Bootstrapping failed: error in master peer");

        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                messages.add(request);
                return _listener.parseMessage(request);
            }
        });
    }

    /* Default Feature */
    public Integer[][] generateNewSudoku(String _game_name) {

        if (room_active.containsKey(_game_name)) return empty;

        room_active.put(_game_name, difficulty);

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            if (futureGet.isSuccess()) {
                SudokuChallenge sudokuChallenge = new SudokuChallenge(_game_name, difficulty);
                _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
                return sudokuChallenge.getSudoku().getMatrix();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean join(String _game_name, String _nickname) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                if(futureGet.isEmpty()) return false;

                SudokuChallenge sudokuChallenge;
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                if (sudokuChallenge.addIntoGame(_dht.peer().peerAddress(), _nickname)){
                    _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
                    String message = _nickname + " join in " + _game_name;
                    sendMessage(message, sudokuChallenge);

                    challenges.put(_game_name, sudokuChallenge);
                    return true;
                }

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer[][] getSudoku(String _game_name){
        try{
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            if (futureGet.isSuccess()){

                //I take the sudoku relative to _game_name
                SudokuChallenge sudokuChallenge = challenges.get(_game_name);
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                return sudokuChallenge.getSudoku().getMatrix();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

  /*  public Integer placeNumber(String _game_name, int _i, int _j, int _number) {
        return null;
    }*/

    /* Other Methods */

    /**
     * Allows a peer to send a message to other peers.
     */
    private void sendMessage(String message, SudokuChallenge sudokuChallenge) {

        for (PeerAddress peerAddress : sudokuChallenge.getPeersInGame().keySet()) {
            if (_dht.peer().peerAddress() != peerAddress) {
                FutureDirect futureDirect = _dht.peer().sendDirect(peerAddress).object(message).start();
                futureDirect.awaitUninterruptibly();
            }
        }
    }

    /**
     * Getter Methods
     */
    public ArrayList<Object> getMessages() {
        return messages;
    }

    /* New Feature */

    /**
     * Allows a peer to leave the network
     */
    public void leaveNetwork(/*String _nickname, String _game_name*/) { //Implemtare invio msg
        _dht.peer().announceShutdown().start().awaitUninterruptibly();
/*        String message = _nickname + " join in " + _game_name;
        sendMessage(message, sudokuChallenge);*/

    }

    /**
     * Allows you to set the difficulty of sudoku
     */
    public void choose_difficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Allows to view the active rooms
     */

    public HashMap<String, String> active_room() { //Bug qui
        for(String str: room_active.keySet())
            System.out.println(str);

        return room_active;
    }
}