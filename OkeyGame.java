import java.util.*;
public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        int tileIndex = 0;
        for (int i = 0; i < 15; i++) {
            players[0].addTile(tiles[tileIndex++]);
        }
        for (int m = 1; m < 4; m++) {
            for (int i = 0; i < 14; i++) {
                players[m].addTile(tiles[tileIndex++]);
            }
        }
        for (int i = 0; i < (112 - 57); i++) {
            tiles[i] = tiles[tileIndex++];
        }
        for (int i = 57; i < 112; i++) {
            tiles[i] = null;
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        if (lastDiscardedTile != null) {
            players[currentPlayerIndex].addTile(lastDiscardedTile);
            return lastDiscardedTile.toString();
        }
        return null;
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        if (tiles != null) {
            players[currentPlayerIndex].addTile(tiles[0]);
            String pickedTile = tiles[0].toString();
            for (int i = 0; i < tiles.length - 1; i++) {
                tiles[i] = tiles[i + 1];
            }
            tiles[tiles.length - 1] = null;
            return pickedTile;
        }
        return null;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        // Creates an ArrayList and adds all tiles to this ArrayList
        ArrayList<Tile> arrayListOfTiles=new ArrayList<>();
        for(int i=0;i<tiles.length;i++){
            arrayListOfTiles.add(tiles[i]);
        }

        // Shuffles ArrayList with Collections.shuffle() method
        Collections.shuffle(arrayListOfTiles);

        // Changes tiles array according to arrayListOfTiles
        for(int i=0;i<arrayListOfTiles.size();i++){
            tiles[i]=arrayListOfTiles.get(i);
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return players[currentPlayerIndex].isWinningHand();
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() 
    {
        Player player = players[currentPlayerIndex];
        if (lastDiscardedTile != null) 
        {
            for (Tile tile : player.getTiles()) 
            {
                if (tile != null) 
                {
                    if (lastDiscardedTile.canFormChainWith(tile)) 
                    {
                        player.addTile(lastDiscardedTile);
                        lastDiscardedTile = null;
                        System.out.println(player.getName() + " got the last discarded tile");
                        return;
                    }
                }
            }
        }
        String pickedTile = getTopTile();
        System.out.println(player.getName() + " got a tile from the pile");
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() 
    {
        Player player = players[currentPlayerIndex];
        Tile[] playerTiles = player.getTiles();
        int toBeDiscaredTileIndex = -1; //to shut the compiler and act as a refrence for future
        for(int i = 0; i < playerTiles.length; i++) //search for a duplicate
        {
            if (playerTiles[i] != null) 
            {
                for(int j = i + 1; j < playerTiles.length; j++)
                {
                    if (playerTiles[j] != null) 
                    {
                        if (playerTiles[i].getValue() == playerTiles[j].getValue() && playerTiles[i].getColor() == playerTiles[j].getColor()) 
                        {
                            toBeDiscaredTileIndex = j;
                            break; 
                        }
                    }
                }
            }
            if (toBeDiscaredTileIndex != -1) //break out of the loop if we find a duplicate
            {
                break;
            }
        }

        int leastUsefulTileindex = -1;
        int lowestChainCount = 100;
        int i = 0;

        if (toBeDiscaredTileIndex == -1) // remove the tile that has the least chains
        {
            for (Tile tile : playerTiles) 
            {
                if (tile != null) 
                {
                    int chainCount = 0;
                    for (Tile tile2 : playerTiles) 
                    {
                        if (tile2 != null && tile != tile2) 
                        {
                            if (tile.canFormChainWith(tile2)) 
                            {   
                                chainCount++;
                            }
                        }
                    }
                    if (chainCount < lowestChainCount) 
                    {
                        lowestChainCount = chainCount;
                        leastUsefulTileindex = i;
                    }
                }
                i++;
            }
        }

        discardTile(toBeDiscaredTileIndex);
        System.out.println(player.getName() + " discarded tile: " + lastDiscardedTile);
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Player currentPlayer = players[currentPlayerIndex];
        lastDiscardedTile = currentPlayer.playerTiles[tileIndex];
        currentPlayer.getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
