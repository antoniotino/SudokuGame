package Sudoku;

import Interface.SudokuGame;
import Interface.MessageListener;
import User.User;

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

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class SudokuGameImpl implements SudokuGame {

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;

    /**
     * A map that contains users
     * Key: PeerAddress and Value: user
     */
    private HashMap<PeerAddress, User> usersInGame = new HashMap<PeerAddress, User>();


    /**
     * A map that contains the active rooms
     * Key: game name and Value: difficulty
     */
    private HashMap<String, String> room_active = new HashMap<String, String>();

    /**
     * A map that contains the score and nickname of peers
     * Key: user.getnickname() and Value: user.getScore()
     */
    private HashMap<String, Integer> userScore = new HashMap<String, Integer>();

    /**
     * A map that contains the sudoku relative to game name
     * Key: game name and Value: Sudoku
     */
    private HashMap<String, Integer[][]> challenges = new HashMap<String, Integer[][]>();

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

            FutureGet room = _dht.get(Number160.MAX_VALUE.createHash("room_active")).start();
            room.awaitUninterruptibly();

            if (futureGet.isSuccess() && room.isSuccess()) { //tmp
                SudokuChallenge sudokuChallenge = new SudokuChallenge(_game_name, difficulty);
                _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
                _dht.put(Number160.createHash("room_active")).data(new Data(room_active)).start().awaitUninterruptibly();
                return sudokuChallenge.getSudoku().getMatrixUnsolved();
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
                if (futureGet.isEmpty()) return false;

                SudokuChallenge sudokuChallenge;
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                if (sudokuChallenge.addIntoGame(_dht.peer().peerAddress(), _nickname)) {
                    _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
                    String message = _nickname + " join in " + _game_name;
                    sendMessage(message, sudokuChallenge);

                    challenges.put(_game_name, sudokuChallenge.getSudoku().getMatrixUnsolved());
                    return true;
                }

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer[][] getSudoku(String _game_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return empty;

                //I take the sudoku relative to _game_name
                SudokuChallenge sudokuChallenge;
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                return sudokuChallenge.getSudoku().getMatrixUnsolved();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return empty;
    }

    public Integer placeNumber(String _game_name, int _i, int _j, int _number) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            FutureGet score = _dht.get(Number160.createHash("userScore")).start();
            score.awaitUninterruptibly();

            SudokuChallenge sudokuChallenge;
            sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();
            if (futureGet.isSuccess() && !futureGet.isEmpty() && score.isSuccess()) {

                //Checks if the number can be entered
                if (sudokuChallenge.checker_sudoku(_number, _i, _j)) {
                    //update sudoku in local
                    challenges.put(_game_name, sudokuChallenge.getSudoku().getMatrixUnsolved());

                    //update sudoku in DHT
                    _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();

                    //Add +1 to user
                    for (PeerAddress peerAddress : usersInGame.keySet())
                        if (peerAddress.equals(peer.peerAddress())) {
                            User u = usersInGame.get(peerAddress);
                            u.increaseScore(1);
                            usersInGame.put(peerAddress, u);
                            sudokuChallenge.getPeerScore().put(u.getNickname(), u.getScore());
                            userScore.put(u.getNickname(), u.getScore());
                            //update score in DHT
                            _dht.put(Number160.createHash("userScore")).data(new Data(userScore)).start().awaitUninterruptibly();
                            String message = u.getNickname() + " add number " + _number + " in game: " + _game_name;
                            sendMessage(message, sudokuChallenge);
                        }
                    //Checks if the game is finished
                    if (sudokuChallenge.end_game()) {
                        return 2;
                    }
                    else {
                        return 1;
                    }
                } else if (sudokuChallenge.number_already_insert(_number, _i, _j)) { //Checks if the number has already been entered
                    //Add +0 to user
                    return 0;
                } else { //the number is wrong
                    //Remove -1 to user
                    for (PeerAddress peerAddress : usersInGame.keySet())
                        if (peerAddress.equals(peer.peerAddress())) {
                            User u = usersInGame.get(peerAddress);
                            u.decreaseScore(1);
                            usersInGame.put(peerAddress, u);
                            sudokuChallenge.getPeerScore().put(u.getNickname(), u.getScore());
                            userScore.put(u.getNickname(), u.getScore());
                            //update score in DHT
                            _dht.put(Number160.createHash("userScore")).data(new Data(userScore)).start().awaitUninterruptibly();
                        }
                    return -1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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
     * Add the user to HashMap
     */
    public void addUser(User user) throws IOException {
        usersInGame.put(peer.peerAddress(), user);

        //update in dht
        _dht.put(Number160.createHash("usersInGame")).data(new Data(usersInGame)).start().awaitUninterruptibly();
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
    public Integer leaveNetwork(String _nickname, String _game_name, boolean join) {

        if (join) {
            try {
                FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
                futureGet.awaitUninterruptibly();
                if (futureGet.isSuccess())
                    if (futureGet.isEmpty()) return 0;

                SudokuChallenge sudokuChallenge;
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                if (sudokuChallenge.removeIntoGame(_dht.peer().peerAddress(), _nickname)) {
                    String message = _nickname + " leave " + _game_name;
                    sendMessage(message, sudokuChallenge);

                    _dht.peer().announceShutdown().start().awaitUninterruptibly();

                    return 1;
                }

                return 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        _dht.peer().announceShutdown().start().awaitUninterruptibly();
        return 1;
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

    public HashMap<String, String> active_room() {

        try {
            FutureGet room = _dht.get(Number160.createHash("room_active")).start();
            room.awaitUninterruptibly();
            if(room.isEmpty()) return new HashMap<>();
            if (room.isSuccess()) {
                if(room.isEmpty())
                    return new HashMap<>();

                HashMap<String, String> r;
                r = (HashMap<String, String>) room.dataMap().values().iterator().next().object();

                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    /**
     * Allows to view the peers' score
     */

    public HashMap<String, Integer> score_peer() {

        try {
            FutureGet score = _dht.get(Number160.createHash("userScore")).start();
            score.awaitUninterruptibly();
            if(score.isEmpty()) return new HashMap<>();
            if (score.isSuccess()) {
                HashMap<String, Integer> r;
                r = (HashMap<String, Integer>) score.dataMap().values().iterator().next().object();
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * Allows to check if the nickname is already used
     */
    public HashMap<PeerAddress, User> duplicateNickname() {

        try {
            FutureGet nickname = _dht.get(Number160.createHash("usersInGame")).start();
            nickname.awaitUninterruptibly();
            if(nickname.isEmpty()) return new HashMap<>();
            if (nickname.isSuccess()) {
                HashMap<PeerAddress, User> r;
                r = (HashMap<PeerAddress, User>) nickname.dataMap().values().iterator().next().object();
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
    /**
     * Allows you to help the user (max 3)
     */
    public Integer getHelp(String _game_name, int row, int column) {

        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty()) return 0;

                //I take the sudoku relative to _game_name
                SudokuChallenge sudokuChallenge;
                sudokuChallenge = (SudokuChallenge) futureGet.dataMap().values().iterator().next().object();

                int help = sudokuChallenge.help(row, column);
                if (help == 0)
                    return 0;

                //-1 is removed from the user to balance the +1 of the method placeNumber
                for (PeerAddress peerAddress : usersInGame.keySet())
                    if (peerAddress.equals(peer.peerAddress())) {
                        User u = usersInGame.get(peerAddress);
                        u.decreaseScore(1);
                        usersInGame.put(peerAddress, u);
                        sudokuChallenge.getPeerScore().put(u.getNickname(), u.getScore());
                    }

                return placeNumber(_game_name, row, column, help);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}