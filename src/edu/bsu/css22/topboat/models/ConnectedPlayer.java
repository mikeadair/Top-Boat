package edu.bsu.css22.topboat.models;

import edu.bsu.css22.topboat.ConnectMultiplayerGame;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConnectedPlayer extends Player {
    private ConnectMultiplayerGame.GameServer gameServer;

    public ConnectedPlayer(ConnectMultiplayerGame.GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void takeTurn() {
        String turnData = gameServer.nextData();
        JSONArray tilesHit = new JSONArray(turnData);
        for(Object tileObject : tilesHit) {
            JSONObject tileJson = (JSONObject) tileObject;
            int x = tileJson.getInt("x");
            int y = tileJson.getInt("y");
            Board.Tile tile = Board.playerBoard().getTile(x, y);
            tile.hit();
        }
    }
}
