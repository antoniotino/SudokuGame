package Sudoku;

import Interface.SudokuGame;
import Interface.MessageListener;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

import java.net.InetAddress;
import java.util.logging.Logger;


public class SudokuGameImpl implements SudokuGame {

    private final static Logger LOGGER = Logger.getLogger(SudokuGameImpl.class.getName());

    final private Peer peer;
    final private PeerDHT _dht;
    final private int DEFAULT_MASTER_PORT = 4000;

    private String difficulty;

    public SudokuGameImpl(int _id, String _master_peer, final MessageListener _listener) throws Exception {

        peer = new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT + _id).start();
        LOGGER.info("Created a peer... Start listening to incoming connections");

        _dht = new PeerBuilderDHT(peer).start();
        LOGGER.info("Initial DHT address = " + this.peer.peerAddress());

        FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
        fb.awaitUninterruptibly();
        LOGGER.info("Bootstrapping...");

        if (fb.isSuccess()) {
            LOGGER.info("Bootstrapping successful");
            peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        } else {
            LOGGER.severe("Bootstrapping failed: error in master peer");
            throw new Exception("Bootstrapping failed: error in master peer");
        }

        peer.objectDataReply(new ObjectDataReply() {

            public Object reply(PeerAddress sender, Object request) throws Exception {
                return _listener.parseMessage(request);
            }
        });
    }

    public Integer[][] generateNewSudoku(String _game_name) {
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(_game_name)).start();
            futureGet.awaitUninterruptibly();

            if (futureGet.isSuccess() && futureGet.isEmpty()) {
				SudokuChallenge sudokuChallenge = new SudokuChallenge(_game_name, difficulty);
                _dht.put(Number160.createHash(_game_name)).data(new Data(sudokuChallenge)).start().awaitUninterruptibly();
                return sudokuChallenge.getSudoku().getMatrix();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

   /* public boolean join(String _game_name, String _nickname) {
        return false;
    }

    public Integer[][] getSudoku(String _game_name) {
        return new Integer[][];
    }

    public Integer placeNumber(String _game_name, int _i, int _j, int _number) {
        return null;
    }*/

    /* Metodo temporaneo: da rivedere */
    public void leaveNetwork() {
        _dht.peer().announceShutdown().start().awaitUninterruptibly();

    }

    public  void choose_difficulty(String difficulty){
        this.difficulty = difficulty;
    }
}