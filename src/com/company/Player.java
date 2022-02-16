package com.company;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class Player extends Thread {
    private final Socket _sock;
    private final int _id;
    private final Game _game;
    private String _choice;
    private int _score = 0;

    public Player(Game game, int id, Socket sock){
        _game = game;
        _id = id;
        _sock = sock;
    }
    public boolean isReady() {
        return _choice!=null;
    }
    public int getScore() {
        return _score;
    }
    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(_sock.getInputStream());
            DataOutputStream writer = new DataOutputStream(_sock.getOutputStream());

            writer.writeInt(_id);
            writer.flush();
            for (; ; ) {
                if (_id == _game.waitpfc()) {
                    _score++;
                }
                _choice = null;
                _game.write(writer);
                }
        }
        catch(InterruptedException | IOException e){
            _game.onLeave(_id);
        }
    }
}

