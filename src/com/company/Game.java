package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Player> players = new ArrayList<>();
    private boolean is_head = false;
    private final Random rand = new Random();

    private boolean allPlayersReady() {
        return players.stream().allMatch(p -> (p == null || p.isReady()));
    }

    private int findPlayerId() {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i) == null) {
                return i+1;
            }
        }
        players.add(null);
        return players.size();
    }
    public void RegisterPlayer(Socket sock) {
        int id = findPlayerId();
        Player player = new Player(this, id, sock);

        System.out.printf("- Player %d arrived\n", id);
        players.set(id-1, player);
        player.start();
    }
    public synchronized boolean waitHeadOrTail() throws InterruptedException {
        if(allPlayersReady()) {
            notifyAll();
            is_head = rand.nextInt(2) != 0;
            System.out.printf("All %d played, got %s\n", players.size(), is_head ? "HEAD" : "TAIL");
        }
        else {
            wait();
        }
        return is_head;
    }
    public void write(DataOutputStream writer) throws IOException {
        writer.writeBoolean(is_head);
        writer.writeInt(players.size());
        for (Player player : players) {
            writer.writeInt(player == null ? -1 : player.getScore());
        }
        writer.flush();
    }
    public void onLeave(int id) {
        System.out.printf("- Player %d left\n", id);
        players.set(id - 1, null);
    }
}

