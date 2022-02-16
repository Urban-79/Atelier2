package com.company;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final ArrayList<Player> players = new ArrayList<>();
    private int is_head = 0;
    //private final Random rand = new Random();

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

    private int findWinner() throws InterruptedException {
        Character choix1 = players.get(0).get_Choice();
        Character choix2 = players.get(1).get_Choice();

        int winnerId = 0;
        System.out.println("WIN DU J1");
        if(choix1.equals("P")){
            System.out.println("Le player 1 a joué pierre");
        }else if(choix1.equals("F")){
            System.out.println("Le player 1 a joué feuille");
        }else if(choix1.equals("C")){
            System.out.println("Le player 1 a joué ciseaux");
        }

        if(choix2.equals("P")){
            System.out.println("Le player 2 a joué pierre");
        }else if(choix2.equals("F")){
            System.out.println("Le player 2 a joué feuille");
        }else if(choix2.equals("C")){
            System.out.println("Le player 2 a joué ciseaux");
        }

        if(choix1.equals("P") && choix2.equals("C")){
            winnerId = 1;
        }else if (choix1.equals("F") && choix2.equals("P")){
            winnerId = 1;
        }else if (choix1.equals("C") && choix2.equals("F")){
            winnerId = 1;
        }else if(choix2.equals("P") && choix1.equals("C")){
            winnerId = 2;
        }else if (choix2.equals("F") && choix1.equals("P")){
            winnerId = 2;
        }else if (choix2.equals("C") && choix1.equals("F")) {
            winnerId = 2;
        }
        return winnerId;
    }

    public void RegisterPlayer(Socket sock) {
        int id = findPlayerId();
        Player player = new Player(this, id, sock);

        System.out.printf("- Player %d arrived\n", id);
        players.set(id-1, player);
        player.start();
    }
    public synchronized int waitpfc() throws InterruptedException {
        if(allPlayersReady()) {
            notifyAll();
            is_head = findWinner();
        }
        else {
            wait();
        }
        return is_head;
    }
    public void write(DataOutputStream writer) throws IOException {
        writer.writeInt(is_head);
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

