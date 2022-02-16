package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    private final static int PORT = 0x2BAE;

    public static void main(String[] args) {
        Game game = new Game();

        try {
            ServerSocket sock_listen = new ServerSocket(PORT);

            System.out.printf("Listening on port %d\n", PORT);
            for (; ; ) {
                game.RegisterPlayer(sock_listen.accept());
            }
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}