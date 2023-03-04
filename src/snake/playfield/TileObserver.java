/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.playfield;

/**
 * This is an interface for receiving notifications about changes to a {@link 
 * Tile tile}.
 * @author Milo Steier
 * @see Tile
 */
public interface TileObserver {
    /**
     * The method invoked when a change has been made to a tile.
     * @param tile The tile that has changed.
     */
    public void tileUpdate(Tile tile);
}
