package edu.bsu.css22.topboat.models;

import edu.bsu.css22.topboat.Player;

public class Stats {
    private Player[] players = new Player[2];

    private int[] misses = new int[2];
    private int[] hits = new int[2];
    private boolean[] result = new boolean[2];

    public Player getPlayer(int i) {
        return this.players[i];
    }

    public int getMisses(int i) {
        return this.misses[i];
    }

    public int getHits(int i) {
        return this.hits[i];
    }

    public boolean getResult(int i) {
        return result[i];
    }

    public void setPlayers(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    public void addHit(int player) {
        this.hits[player] += 1;
    }

    public void addMiss(int player) {
        this.misses[player] += 1;
    }

    public void setResult(boolean player1Result, boolean player2Result) {
        result[0] = player1Result;
        result[1] = player2Result;
    }

}
