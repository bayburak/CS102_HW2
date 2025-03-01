public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * Removes the given tile and returns the removed tile, returns null if the given index is invalid.
     */
    public Tile getAndRemoveTile(int index) {
        Tile removedTile = null;

        //Checks if the index is valid
        if(index >= 0 && index <= 14){
            removedTile = playerTiles[index];

            playerTiles[index] = null;
            for(int i = index+1; i< playerTiles.length;i++){
                playerTiles[i-1] = playerTiles[i];
            }
            playerTiles[playerTiles.length-1] = null;
        }
        numberOfTiles--;
        return removedTile;
    }

    /*
     * Adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {

        //makes sure the number of tiles don't exeed 15
        if (numberOfTiles <= 14) {
            for(int i = 0; i < numberOfTiles+1; i++){
                Tile aTile = playerTiles[i];

                // This ensures that we can add tiles if there are no tiles in the list yet or the tile we want to add goes in the last possible index.
                if(aTile == null){
                    playerTiles[i] = t;
                }
                //Compares prioritizing number then  looks for color
                else if(t.compareTo(aTile) <= 0 ){
                    // Shifts the array to make room for the new tile
                    for(int k = numberOfTiles; k > i; k--){
                        playerTiles[k] = playerTiles[k-1];
                    }

                    playerTiles[i] = t;
                }
            }
            numberOfTiles++;
        }

    }

    /*
     * checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {
        int noOfChains = 0;
        Tile prevTile = null;
        int counter = 1;

        for(Tile currentTile : playerTiles){

            //Skips the first tile because there is no prevTile at the moment 
            if (currentTile == playerTiles[0]) { prevTile = currentTile; continue;}
            
            //Checks whether consecutive tiles have the same value,
            // if they do have teh same value checks if they are different colors the nincrements the counter
            // if not resets the counter
            if(prevTile.getValue() == currentTile.getValue()){
                if(prevTile.canFormChainWith(currentTile)){ counter++;}
            }
            else{
                counter = 1;
            }

            //if counter has reached four this means a chain has formed, so we increment noOfChains and set the counter to zero
            if(counter == 4){ 
                noOfChains++;
                counter = 1;
            }
    
            prevTile = currentTile;
        }

        //If the total number of chains is true then this means the player has won
        if(noOfChains >= 3) { return true;}
        else { return false;}
        
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
