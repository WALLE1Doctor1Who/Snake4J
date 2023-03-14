/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package snake.playfield;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import snake.*;
import snake.event.*;

/**
 * This is an interface representing a play field consisting of a grid of 
 * {@link Tile tiles}. This defines the methods that {@link JPlayField 
 * JPlayField} and similar components will use to get a tile from a specific row 
 * and column, along with the number of rows and columns of tiles. 
 * @author Milo Steier
 * @see Tile
 * @see AbstractPlayFieldModel
 * @see DefaultPlayFieldModel
 * @see JPlayField
 */
public interface PlayFieldModel extends Iterable<Tile>, SnakeConstants{
    /**
     * This returns the number of rows of tiles that are in this model.
     * @return The number of rows in this model.
     * @see #setRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    public int getRowCount();
    /**
     * This sets the number of rows of tiles that are in this model (optional 
     * operation). If the new size is greater than the current size, then new 
     * rows are added and populated with tiles. If the new size is less than the 
     * current size, then the tiles in at row {@code rows} and greater are 
     * discarded. 
     * @param rows The number of rows in this model (must be greater than zero).
     * @throws UnsupportedOperationException If changes to the number of rows is 
     * not supported by this model.
     * @throws IllegalArgumentException If {@code rows} is less than or equal to 
     * zero, or if {@code rows} is some number of rows that is unsupported by 
     * this model.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    public void setRowCount(int rows);
    /**
     * This returns the number of columns of tiles that are in this model.
     * @return The number of columms.
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    public int getColumnCount();
    /**
     * This sets the number of columns of tiles that are in this model (optional 
     * operation). If the new size is greater than the current size, then new 
     * columns are added and populated with tiles. If the new size is less than 
     * the current size, then the tiles in at column {@code columns} and greater 
     * are discarded. 
     * @param columns The number of columns in this model (must be greater than 
     * zero).
     * @throws UnsupportedOperationException If changes to the number of columns 
     * is not supported by this model.
     * @throws IllegalArgumentException If {@code columns} is less than or equal 
     * to zero, or if {@code columns} is some number of columns that is 
     * unsupported by this model.
     * @see #getColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    public void setColumnCount(int columns);
    /**
     * This returns the total number of tiles in that are in this model. 
     * 
     * @implSpec The default implementation returns {@link #getRowCount()} 
     * {@code *} {@link #getColumnCount()}.
     * 
     * @return The total number of tiles in this model.
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount() 
     * @see #getColumnCount 
     */
    public default int getTileCount(){
        return getRowCount() * getColumnCount();
    }
    /**
     * This returns the tile located at the given row and column. <p>
     * 
     * Please note that tiles store their corresponding row and column, and thus 
     * it is unnecessary to externally keep track of which row and column a 
     * tile was originally located at. The row and column can be retrieved from 
     * the tile at any time by using its {@link Tile#getRow() getRow} and {@link 
     * Tile#getColumn() getColumn} methods.
     * @param row The row of the tile to get.
     * @param column The column of the tile to get.
     * @return The tile located at the given row and column. This should never 
     * be null.
     * @throws IndexOutOfBoundsException If either the row or column are out of 
     * bounds.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    public Tile getTile(int row, int column);
    /**
     * This checks to see if the given row and column are within range of this 
     * model.
     * 
     * @implSpec The default implementation returns true if the given row is 
     * between zero and {@link #getRowCount() getRowCount}, exclusive, and the 
     * given column is between zero and {@link #getColumnCount() 
     * getColumnCount}, exclusive.
     * 
     * @param row The row to check.
     * @param column The column to check.
     * @return Whether the given row and column are within range.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #contains(Tile) 
     * @see #getTile 
     */
    public default boolean contains(int row, int column){
        return row >= 0 && row < getRowCount() && 
                column >= 0 && column < getColumnCount();
    }
    /**
     * This returns whether this model contains the given tile. 
     * @param tile The tile to check for.
     * @return Whether this model contains the given tile.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #contains(int, int) 
     * @see #getTile 
     */
    public boolean contains(Tile tile);
    /**
     * This {@link Tile#clear() clears} all the tiles in this model that are 
     * between the rows {@code fromRow} and {@code toRow}, exclusive, and 
     * between the columns {@code fromColumn} and {@code toColumn}, exclusive. 
     * After this is called, all the tiles in the given range will be {@link 
     * Tile#isEmpty() empty}.
     * @param fromRow The index of the first row of tiles to be cleared.
     * @param toRow The index of the row after the last row of tiles to be 
     * cleared.
     * @param fromColumn The index of the first column of tiles to be cleared.
     * @param toColumn The index of the column after the last column of tiles to 
     * be cleared.
     * @throws IndexOutOfBoundsException If either the {@code fromRow}, {@code 
     * toRow}, {@code fromColumn}, or {@code toColumn} are out of bounds or 
     * either the {@code fromRow} is greater than the {@code toRow} or the 
     * {@code fromColumn} is greater than the {@code toColumn}.
     * @see #clearTiles() 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     */
    public void clearTiles(int fromRow, int toRow, int fromColumn, int toColumn);
    /**
     * This {@link Tile#clear() clears} all the tiles in this model. After this 
     * is called, all the tiles in this model will be {@link Tile#isEmpty() 
     * empty}.
     * 
     * @implSpec The default implementation calls {@link #clearTiles 
     * clearTiles}{@code (0, }{@link #getRowCount()}{@code , 0, }{@link 
     * #getColumnCount()}{@code )}.
     * 
     * @see #clearTiles 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     */
    public default void clearTiles(){
        clearTiles(0,getRowCount(),0,getColumnCount());
    }
    /**
     * This returns the tile in this model that is adjacent to the given tile in 
     * the given direction. The direction must be one of the four direction 
     * flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. The {@code 
     * wrapAround} value will determine how the tiles at the edges of the play 
     * field are treated. If {@code wrapAround} is true and the adjacent tile is 
     * beyond the bounds of the model, then this will wrap around and get a tile 
     * from the other side of the model. Otherwise, this will return null. For 
     * example, when given a tile at {@link Tile#getRow() row 2}, {@link 
     * Tile#getColumn() column 0}, attempting to get the tile above will return 
     * the tile at row 1, column 0. If this is given the same tile as before but 
     * this is attempting to get the tile to the left, then this would return 
     * the tile at row 2, column {@code (}{@link #getColumnCount() }{@code -1)} 
     * if {@code wrapAround} is true and null if {@code wrapAround} is false.
     * 
     * @param tile The tile to get the adjacent tile of (cannot be null).
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@link #UP_DIRECTION} to get the tile above, 
     *      {@link #DOWN_DIRECTION} to get the tile below, 
     *      {@link #LEFT_DIRECTION} to get the tile to the left, or 
     *      {@link #RIGHT_DIRECTION} to get the tile to the right.
     * @param wrapAround Whether this should wrap around and get the tile on the 
     * opposite side of the play field if the adjacent tile would be out of 
     * bounds.
     * @return The tile adjacent to the given tile. If the given tile is out of 
     * bounds or the requested adjacent tile is out of bounds and {@code 
     * wrapAround} is false, then this will return null. 
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given direction is not one of the 
     * direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see SnakeUtilities#getDirections 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getAdjacentTile(Tile, int) 
     */
    public Tile getAdjacentTile(Tile tile, int direction, boolean wrapAround);
    /**
     * This returns the tile in this model that is adjacent to the given tile in 
     * the given direction. This version uses whether the {@link 
     * #ALTERNATE_TYPE_FLAG} flag is set on the {@code direction} to determine 
     * whether this should wrap around when getting an adjacent tile that is out 
     * of bounds, with the {@code ALTERNATE_MODE_FLAG} flag indicating that this 
     * should not wrap around. This is equivalent to calling {@link 
     * #getAdjacentTile(Tile, int, boolean) getAdjacentTile}{@code (tile, 
     * direction&~ALTERNATE_MODE_FLAG, !}{@link SnakeUtilities#getFlag 
     * SnakeUtilities.getFlag}{@code (direction, ALTERNATE_MODE_FLAG))}. As 
     * such, calling {@code getAdjacentTile(tile, } {@link 
     * #UP_DIRECTION}{@code )} is equivalent to calling {@code 
     * getAdjacentTile(tile, UP_DIRECTION_FLAG, true)} while calling {@code 
     * getAdjacentTile(tile, UP_DIRECTION_FLAG | ALTERNATE_MODE_FLAG)} is 
     * equivalent to calling {@code getAdjacentTile(tile, UP_DIRECTION_FLAG, 
     * false)}.
     * 
     * @param tile The tile to get the adjacent tile of (cannot be null).
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@link #UP_DIRECTION} to get the tile above, 
     *      {@link #DOWN_DIRECTION} to get the tile below, 
     *      {@link #LEFT_DIRECTION} to get the tile to the left, 
     *      {@link #RIGHT_DIRECTION} to get the tile to the right, or
     *      {@link #ALTERNATE_TYPE_FLAG} {@code OR}'d with any of the previously 
     *          stated values to indicate that this should not wrap around. 
     * @return The tile adjacent to the given tile. If the given tile is out of 
     * bounds or the requested adjacent tile is out of bounds and {@link 
     * #ALTERNATE_TYPE_FLAG} is set on the given {@code direction}, then this 
     * will return null. 
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given direction is not one of the 
     * direction flags with or without the {@link #ALTERNATE_TYPE_FLAG} set.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getFlag 
     * @see SnakeUtilities#setFlag 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getAdjacentTile(Tile, int, boolean) 
     */
    public default Tile getAdjacentTile(Tile tile, int direction){
        return getAdjacentTile(tile,direction&~ALTERNATE_TYPE_FLAG,
                !SnakeUtilities.getFlag(direction, ALTERNATE_TYPE_FLAG));
    }
    /**
     * This returns a list of lists containing the tiles in this model. The 
     * lists contained in the outer list each contain a single row of tiles. In 
     * other words, {@code toList().get(2).get(5)} will return the tile at row 
     * 2, column 5. <p>
     * 
     * The returned list of lists will be "safe" in that no references to the 
     * lists are maintained by this model. (In other words, this method must 
     * allocate a new list composed of new lists). The caller is thus free to 
     * modify the returned list and any of the lists within. 
     * 
     * @return A list of lists containing the tiles in this model.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTiles 
     * @see #getFilteredTileList 
     */
    public List<List<Tile>> getTileList();
    /**
     * This returns a two-dimensional array containing the tiles in this model. 
     * <p>
     * Like the list of lists returned by the {@link #getTileList() getTileList} 
     * method, the returned two-dimensional array will be "safe" in that no 
     * references to it are maintained by this model. (In other words, this 
     * method must allocate a new two-dimensional array). The caller is thus 
     * free to modify the returned two-dimensional array.
     * 
     * @return A two-dimensional array containing the tiles in this model.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTileList 
     * @see #getFilteredTileList 
     */
    public Tile[][] getTiles();
    /**
     * This returns a list of tiles in this model that currently satisfy the 
     * given predicate. <p>
     * 
     * Like the list of lists returned by the {@link #getTileList() getTileList} 
     * method, the returned list will be "safe" in that no references to it are 
     * maintained by this model. (In other words, this method must allocate a 
     * new list). The caller is thus free to modify the returned list.
     * 
     * @param filter The predicate to use to match the desired tiles. This 
     * cannot be null.
     * @return A list containing the tiles which match the given predicate.
     * @throws NullPointerException If the given predicate is null.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileCount 
     * @see #getEmptyTiles 
     * @see #getAppleTiles 
     */
    public List<Tile> getFilteredTileList(Predicate<? super Tile> filter);
    /**
     * This returns the number of tiles in this model that currently satisfy the 
     * given predicate.
     * 
     * @implSpec The default implementation returns the size of the list 
     * returned by {@link #getFilteredTileList getFilteredTileList}, or 0 if the 
     * returned list is null.
     * 
     * @param filter The predicate to use to match the tiles to count. This 
     * cannot be null.
     * @return The amount of tiles in this model that match the given predicate.
     * @throws NullPointerException If the given predicate is null.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see #getEmptyTileCount 
     * @see #getAppleTileCount 
     */
    public default int getFilteredTileCount(Predicate<? super Tile> filter){
            // Get the list of tiles that match the filter
        List<Tile> tiles = getFilteredTileList(filter);
            // If the list is not null, return its size. Otherwise, return 0.
        return (tiles != null) ? tiles.size() : 0;
    }
    /**
     * This returns a list of tiles in this model that are currently {@link 
     * Tile#isEmpty() empty}. 
     * @return A list of tiles that are empty, or an empty list if no tiles are 
     * empty.
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see #getEmptyTileCount 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles() 
     * @see #clearTiles(int, int, int, int) 
     */
    public List<Tile> getEmptyTiles();
    /**
     * This returns the number of {@link Tile#isEmpty() empty tiles} that are 
     * currently in this model.
     * 
     * @implSpec The default implementation returns the size of the list 
     * returned by {@link #getEmptyTiles() getEmptyTiles}, or 0 if the returned 
     * list is null.
     * 
     * @return The number of empty tiles in this model.
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getFilteredTileCount 
     * @see #getEmptyTiles 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles(int, int, int, int) 
     * @see #clearTiles() 
     */
    public default int getEmptyTileCount(){
            // If the list is not null, return its size. Otherwise, return 0.
        return (getEmptyTiles() != null) ? getEmptyTiles().size() : 0;
    }
    /**
     * This returns a list of tiles in this model that are currently {@link 
     * Tile#isApple() apple tiles}. 
     * @return A list of apple tiles in this model, or an empty list if no tiles 
     * are apple tiles.
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see #getAppleTileCount 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    public List<Tile> getAppleTiles();
    /**
     * This returns the number of {@link Tile#isApple() apple tiles} that are 
     * currently in this model.
     * 
     * @implSpec The default implementation returns the size of the list 
     * returned by {@link #getAppleTiles() getAppleTiles}, or 0 if the returned 
     * list is null.
     * 
     * @return The number of apple tiles in this model.
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getFilteredTileCount 
     * @see #getAppleTiles 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    public default int getAppleTileCount(){
            // If the list is not null, return its size. Otherwise, return 0.
        return (getAppleTiles() != null) ? getAppleTiles().size() : 0;
    }
    /**
     * This returns an iterator over the tiles in this model in their proper 
     * sequence. That is to say, the iterator iterates over the tiles from top 
     * to bottom, and from left to right. <p>
     * 
     * Please note that tiles store their corresponding row and column, and thus 
     * it is unnecessary to externally keep track of which row and column the
     * last tile returned by the iterator was located at. The row and column can 
     * be retrieved from a tile at any time by using its {@link Tile#getRow() 
     * getRow} and {@link Tile#getColumn() getColumn} methods.
     * 
     * @return An iterator over the tiles in this model in proper sequence.
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getTile 
     */
    @Override
    public Iterator<Tile> iterator();
    /**
     * This adds the given {@code PlayFieldListener} to this model.
     * @param l The {@code PlayFieldListener} to add.
     */
    public void addPlayFieldListener(PlayFieldListener l);
    /**
     * This removes the given {@code PlayFieldListener} from this model.
     * @param l The {@code PlayFieldListener} to remove.
     */
    public void removePlayFieldListener(PlayFieldListener l);
    /**
     * This sets whether the tiles are undergoing a series of changes. This 
     * indicates whether or not upcoming changes to the tiles should be 
     * considered part of a single change. This allows listeners to update only 
     * when a change has been finalized as opposed to handling all the 
     * individual tile changes. <p>
     * 
     * You may want to use this directly if making a series of changes that 
     * should be considered part of a single change.
     * 
     * @param isAdjusting Whether upcoming changes to the tiles should be 
     * considered part of a single change.
     * @see #getTilesAreAdjusting 
     * @see PlayFieldEvent#getTilesAreAdjusting
     */
    public void setTilesAreAdjusting(boolean isAdjusting);
    /**
     * This returns whether the tiles are undergoing a series of changes.
     * @return Whether the tiles are undergoing a series of changes.
     * @see #setTilesAreAdjusting 
     */
    public boolean getTilesAreAdjusting();
}
