package edu.bsu.css22.topboat;

import edu.bsu.css22.topboat.models.Board;
import edu.bsu.css22.topboat.models.FireEvent;
import edu.bsu.css22.topboat.models.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocalPlayer extends Player {
    private ConnectMultiplayerGame.GameServer gameServer;

    public LocalPlayer(ConnectMultiplayerGame.GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void takeTurn() {
        try {
            FireEvent fireEvent = fireEvents.take();
            ArrayList<Board.Tile> hits = new ArrayList<>();
            ArrayList<Board.Tile> misses = new ArrayList<>();
            for(int[] affectedTile : fireEvent.getWeapon().getAffectedTiles()) {
                int x = affectedTile[1] + fireEvent.getTarget().x;
                int y = affectedTile[0] + fireEvent.getTarget().y;

                Board.Tile target = fireEvent.getTarget().getBoard().getTile(x, y);
                if (target == null || target.hasBeenHit()) {
                    continue;
                } else {
                    if(target.hit()) {
                        hits.add(target);
                    } else {
                        misses.add(target);
                    }
                }
            }
            if(hits.size() > 0) {
                String hitMessage = "You hit at " + buildStringFromTiles(hits);
                Log.gameLog().addMessage(new Log.Message(hitMessage, Log.Message.Type.SUCCESS));
            }
            if(misses.size() > 0) {
                String missMessage = "You missed at " + buildStringFromTiles(misses);
                Log.gameLog().addMessage(new Log.Message(missMessage, Log.Message.Type.ERROR));
            }

            sendShots(hits, misses);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendShots(ArrayList<Board.Tile> hits, ArrayList<Board.Tile> misses) {
        hits.addAll(misses);
        JSONArray tilesHit = new JSONArray();
        for(Board.Tile tile : hits) {
            String tileJson = String.format("{" +
                    "\"x\": %d" +
                    "\"y\": %d"
                    + "}", tile.x, tile.y);
            tilesHit.put(new JSONObject(tileJson));
        }
        gameServer.sendData(tilesHit.toString());
    }
}
