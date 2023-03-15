/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.playfield;

import java.util.*;
import snake.*;

/**
 * This is the default implementation of {@link PlayFieldModel} which uses a 
 * two-dimensional array to store the {@link Tile tiles}, which can store a 
 * maximum of {@value MAXIMUM_ROW_COUNT} rows and {@value #MAXIMUM_COLUMN_COUNT} 
 * columns of tiles. The minimum number of rows and columns is {@value 
 * #MINIMUM_ROW_COUNT} by {@value MINIMUM_ROW_COUNT}. <p>
 * 
 * The tiles in this model are created using the protected {@code createTile} 
 * method which is given the row and column of the tile to create and returns a 
 * tile to populate that location. A subclass may override this method to return 
 * a subclass of {@code Tile}, however the returned tile must use the row and 
 * column given to the method. All tiles in this model will have this model as 
 * their {@link Tile#getModelTileObserver() model tile observer}, regardless of 
 * whether it was set by the {@code createTile} method. Changing the model tile 
 * observer of any tile in this model may result in issues such as this model 
 * not notifying any {@code PlayFieldListener}s added to it of changes made to 
 * the tiles. Tiles are only created once when their respective row and/or 
 * column are added to the model for the first time. Tiles that get removed from 
 * this model are never actually removed, and are instead made inaccessible and 
 * ignored as though they were removed. This allows removed tiles to be reused 
 * when they are added back to the model, so as to reduce the amount of {@code 
 * Tile} objects that this model creates. <p>
 * 
 * The iterators returned by this class's {@code iterator} method are fail-fast,
 * i.e. if the PlayFieldModel is structurally modified in any way at any time 
 * after the iterator is created, such as changing the number of rows and/or 
 * columns, the iterator will throw a {@link ConcurrentModificationException 
 * ConcurrentModificationException}. This way, when faced with concurrent 
 * modification, the iterator will fail quickly and cleanly instead of risking 
 * arbitrary, non-deterministic behavior. However, the fail-fast behavior of the 
 * iterator cannot be guaranteed, especially when dealing with unsynchronized 
 * concurrent modifications. It is also possible, though not guaranteed, for the 
 * iterator to not fail if the structure of the model is reverted to how it was 
 * when the iterator was created. The fail-fast iterators throw {@code 
 * ConcurrentModificationExceptions} on a best-effort basis. As such the 
 * fail-fast behavior should not be depended on for its correctness and should 
 * only be used to detect bugs.
 * 
 * @author Milo Steier
 * @see Tile
 * @see TileObserver
 * @see PlayFieldModel
 * @see AbstractPlayFieldModel
 * @see JPlayField
 */
public class DefaultPlayFieldModel extends AbstractPlayFieldModel implements 
        SnakeConstants{
    /**
     * This is the minimum number of rows there can be in a 
     * DefaultPlayFieldModel.
     */
    public static final int MINIMUM_ROW_COUNT = 8;
    /**
     * This is the minimum number of columns there can be in a 
     * DefaultPlayFieldModel.
     */
    public static final int MINIMUM_COLUMN_COUNT = 8;
    /**
     * This is the maximum number of rows there can be in a 
     * DefaultPlayFieldModel.
     */
    public static final int MAXIMUM_ROW_COUNT = 100;
    /**
     * This is the maximum number of columns there can be in a 
     * DefaultPlayFieldModel.
     */
    public static final int MAXIMUM_COLUMN_COUNT = 100;
    /**
     * This stores the number of rows being displayed by this play field model.
     */
    private int rows = 0;
    /**
     * This stores the number of columns being displayed by this play field 
     * model.
     */
    private int columns = 0;
    /**
     * This is a two-dimensional array used to store the tiles that this 
     * play field model consists of.
     */
    private final Tile[][] tiles;
    /**
     * This is a set that contains all the apple tiles in this play field model.
     */
    private Set<Tile> appleTiles;
    /**
     * This is a set that contains all the empty tiles in this play field model.
     */
    private Set<Tile> emptyTiles;
    /**
     * This constructs a DefaultPlayFieldModel with the given initial number of 
     * rows and columns. 
     * @param rows The number of rows for the model (must be between {@value 
     * #MINIMUM_ROW_COUNT} and {@value #MAXIMUM_ROW_COUNT}, inclusive).
     * @param columns The number of columns for the model (must be between 
     * {@value #MINIMUM_COLUMN_COUNT} and {@value #MAXIMUM_COLUMN_COUNT}, 
     * inclusive).
     * @throws IllegalArgumentException If either the number of rows or columns 
     * are out of bounds.
     * @see #MINIMUM_ROW_COUNT
     * @see #MAXIMUM_ROW_COUNT
     * @see #MINIMUM_COLUMN_COUNT
     * @see #MAXIMUM_COLUMN_COUNT
     */
    public DefaultPlayFieldModel(int rows, int columns){
        super();
        tiles = new Tile[MAXIMUM_ROW_COUNT][MAXIMUM_COLUMN_COUNT];
        appleTiles = new TreeSet<>();
        emptyTiles = new TreeSet<>();
        DefaultPlayFieldModel.this.setRowCount(rows);
        DefaultPlayFieldModel.this.setColumnCount(columns);
    }
    /**
     * This constructs a DefaultPlayFieldModel with {@value #MINIMUM_ROW_COUNT} 
     * rows and {@value #MINIMUM_COLUMN_COUNT} columns.
     * @see #MINIMUM_ROW_COUNT
     * @see #MAXIMUM_ROW_COUNT
     * @see #MINIMUM_COLUMN_COUNT
     * @see #MAXIMUM_COLUMN_COUNT
     */
    public DefaultPlayFieldModel(){
        this(MINIMUM_ROW_COUNT,MINIMUM_COLUMN_COUNT);
    }
    /**
     * This constructs a tile at the given row and column, with this as the 
     * tile's {@link Tile#getModelTileObserver() model tile observer}. <p>
     * 
     * This method is here so that a subclass can override this to return a 
     * subclass of {@link Tile}. If you override this, the tile's row and 
     * column must be set to the given row and column. If you use a tile 
     * observer other than this model for the model tile observer, it will be 
     * replaced with this model.
     * 
     * @param r The row for the tile (cannot be negative).
     * @param c The column for the tile (cannot be negative).
     * @return The tile that was constructed.
     * @throws IllegalArgumentException If either the row or column are 
     * negative.
     * @see Tile#Tile(int, int, TileObserver) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#getModelTileObserver 
     * @see Tile#setModelTileObserver 
     */
    protected Tile createTile(int r, int c){
        return new Tile(r,c,this);
    }
    /**
     * {@inheritDoc }
     * @see #setRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    @Override
    public int getRowCount() {
        return rows;
    }
    /**
     * {@inheritDoc }
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    @Override
    public int getColumnCount() {
        return columns;
    }
    /**
     * This checks to see if the given value is within the given range, and if 
     * not, then this throws an IllegalArgumentException.
     * @param value The value to check.
     * @param min The minimum for the value, inclusive.
     * @param max The maximum for the value, inclusive.
     * @param name The name of the value.
     * @throws IllegalArgumentException If {@code value < min} or {@code value > 
     * max}.
     */
    private void checkSizeValue(int value, int min, int max, String name){
        if (value < min)        // If the value is too small
            throw new IllegalArgumentException("Invalid "+name+" count: "+value+
                    " < " + min);
        else if (value > max)   // If the value is too large
            throw new IllegalArgumentException("Invalid "+name+" count: "+value+
                    " > "+max);
    }
    /**
     * This is used to remove the tiles between rows {@code r0} and {@code r1}, 
     * exclusive, and between columns {@code c0} and {@code c1}, exclusive, from 
     * the model. Note that tiles aren't actually removed from the model, and 
     * are instead ignored and made inaccessible outside the model until they 
     * are "added" back to the model. This will also fire a {@code 
     * PlayFieldEvent} denoting that the tiles were removed.
     * @param r0 The first row of tiles to remove.
     * @param r1 The row after the last row of tiles to remove.
     * @param c0 The first column of tiles to remove.
     * @param c1 The column after the last column of tiles to remove.
     * @see #addTiles 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #fireTilesRemoved 
     */
    protected void removeTiles(int r0, int r1, int c0, int c1){
            // Go through the rows that were affected
        for (int r = r0; r < r1; r++){
                // Go through the columns that were affected
            for (int c = c0; c < c1; c++){
                appleTiles.remove(tiles[r][c]);
                emptyTiles.remove(tiles[r][c]);
            }
        }
        fireTilesRemoved(r0, r1-1, c0, c1-1);
    }
    /**
     * This is used to add tiles to the model into rows {@code r0} through 
     * {@code r1}, exclusive, and into columns {@code c0} through {@code c1}, 
     * exclusive. This will reuse any tiles that were previously "removed" from 
     * this model that fall into that region, {@link Tile#clear() clearing} them 
     * if necessary, and will {@link #createTile create} any tiles in the region 
     * that haven't been initialized yet. This will also fire a {@code 
     * PlayFieldEvent} denoting that tiles were added.
     * @param r0 The first row of tiles to add.
     * @param r1 The row after the last row of tiles to add.
     * @param c0 The first column of tiles to add.
     * @param c1 The column after the last column of tiles to add.
     * @see #createTile 
     * @see Tile#clear 
     * @see #removeTiles 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #fireTilesAdded 
     */
    protected void addTiles(int r0, int r1, int c0, int c1){
            // Get if the events are currently paused
        boolean paused = getEventsArePaused();
        setEventsArePaused(true);
            // Go through the rows that were affected
        for (int r = r0; r < r1; r++){
                // Go through the columns that were affected
            for (int c = c0; c < c1; c++){
                    // If the tile hasn't been initialized yet
                if (tiles[r][c] == null)    
                    tiles[r][c] = createTile(r,c).setModelTileObserver(this);
                emptyTiles.add(tiles[r][c].clear());
            }
        }
        setEventsArePaused(paused);
        fireTilesAdded(r0, r1-1, c0, c1-1);
    }
    /**
     * This sets the number of rows of tiles that are in this model. If the new 
     * size is greater than the current size ({@code rows >} {@link 
     * #getRowCount() getRowCount}), then new rows are added and populated with 
     * tiles. If the new size is less than the current size ({@code rows <} 
     * {@link #getRowCount() getRowCount}), then the tiles in at row {@code 
     * rows} and greater are discarded. 
     * @param rows The number of rows for this model (must be between {@value 
     * #MINIMUM_ROW_COUNT} and {@value #MAXIMUM_ROW_COUNT}, inclusive).
     * @throws IllegalArgumentException If {@code rows} is less than {@value 
     * #MINIMUM_ROW_COUNT} or greater than {@value #MAXIMUM_ROW_COUNT}.
     * @see #MINIMUM_ROW_COUNT
     * @see #MAXIMUM_ROW_COUNT
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    @Override
    public void setRowCount(int rows){
            // Check if the new number of rows is invalid
        checkSizeValue(rows,MINIMUM_ROW_COUNT,MAXIMUM_ROW_COUNT,"row");
        if (this.rows == rows)      // If the number of rows would not change
            return;
        int old = this.rows;        // Gets the old value for the rows
        this.rows = rows;
        if (rows < old)             // If rows are being removed
            removeTiles(rows,old,0,getColumnCount());
        else
            addTiles(old,rows,0,getColumnCount());
    }
    /**
     * This sets the number of columns of tiles that are in this model. If the 
     * new size is greater than the current size ({@code columns >} {@link 
     * #getColumnCount() getColumnCount}), then new columns are added and 
     * populated with tiles. If the new size is less than the current size 
     * ({@code columns <} {@link #getColumnCount() getColumnCount}), then the 
     * tiles in at column {@code columns} and greater are discarded. 
     * @param columns The number of columns for this model (must be between 
     * {@value #MINIMUM_COLUMN_COUNT} and {@value #MAXIMUM_COLUMN_COUNT}, 
     * inclusive).
     * @throws IllegalArgumentException If {@code columns} is less than {@value 
     * #MINIMUM_COLUMN_COUNT} or greater than {@value #MAXIMUM_COLUMN_COUNT}.
     * @see #MINIMUM_COLUMN_COUNT
     * @see #MAXIMUM_COLUMN_COUNT
     * @see #getColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    @Override
    public void setColumnCount(int columns){
            // Check if the new number of columns is invalid
        checkSizeValue(columns,MINIMUM_COLUMN_COUNT,MAXIMUM_COLUMN_COUNT,"column");
        if (this.columns == columns)// If the number of columns would not change
            return;
        int old = this.columns;     // Gets the old value for the columns
        this.columns = columns;
        if (columns < old)          // If columns are being removed
            removeTiles(0,getRowCount(),columns,old);
        else
            addTiles(0,getRowCount(),old,columns);
    }
    /**
     * This checks to see if the given index is within range. That is to say, 
     * this checks to see if the index is greater than or equal to 0 and is less 
     * than the given size. If the index is out of bounds, then this will throw 
     * an IndexOutOfBoundsException.
     * @param index The index being checked.
     * @param size The size (i.e. the upper bound, exclusive, of the range).
     * @param name The name for the index being checked.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    private void checkIndex(int index, int size, String name){
        if (index < 0 || index >= size) // If the index is out of bounds
            throw new IndexOutOfBoundsException(name+" index out of bounds ("+
                    name.toLowerCase()+": "+index+", size: "+size+")");
    }
    /**
     * {@inheritDoc }
     * @throws IndexOutOfBoundsException {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    @Override
    public Tile getTile(int row, int column) {
            // If this model does not contain the given row and/or column
        if (!contains(row,column)){     
                // Check if the row is out of bounds
            checkIndex(row,getRowCount(),"Row");
                // Check if the column is out of bounds
            checkIndex(column,getColumnCount(),"Column");
                // The tile at the given row and column must somehow not be 
                // contained in this model even though they are in range
            throw new IndexOutOfBoundsException("This does not contain a tile "
                    + "at row " + row + ", column " + column);
        }
        return tiles[row][column];
    }
    /**
     * This returns the tile located internally at the given row and column. 
     * This ignores the currently set {@link #getRowCount() row} and {@link 
     * #getColumnCount() column count} and returns whatever tile is located at 
     * the given row and column, which may be null if that tile is not within 
     * the currently set number of rows and columns. To get a tile that is 
     * currently in bounds, use {@link #getTile getTile}.
     * @param row The row of the tile to get (cannot be negative, must be less 
     * than {@value #MAXIMUM_ROW_COUNT}).
     * @param column The column of the tile to get (cannot be negative, must be 
     * less than {@value #MAXIMUM_COLUMN_COUNT}).
     * @return The tile that is internally at the given row and column (may be 
     * null if the tile has not been initialized).
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #MAXIMUM_ROW_COUNT
     * @see #MAXIMUM_COLUMN_COUNT
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    protected Tile getInternalTile(int row, int column){
        return tiles[row][column];
    }
    /**
     * This updates whether the given tile is in the given set based off the 
     * given value. In other words, this adds the given tile to the set if 
     * {@code value} is {@code true}, and removes it from the set if {@code 
     * value} is {@code false}.
     * @param tile The tile to add or remove from the set.
     * @param value The value to use to determine if the tile should be in the 
     * set.
     * @param set The set to add or remove the tile to/from.
     */
    private void updateTileSet(Tile tile, boolean value, Set<Tile> set){
        if (value)  // If the tile should be in the set
            set.add(tile);
        else
            set.remove(tile);
    }
    /**
     * {@inheritDoc }
     * @see #getTile
     * @see #contains(Tile) 
     */
    @Override
    public void tileUpdate(Tile tile){
            // If the tile is not null and this contains the tile
        if (tile != null && contains(tile)){
            updateTileSet(tile,tile.isEmpty(),emptyTiles);
            updateTileSet(tile,tile.isApple(),appleTiles);
        }
        super.tileUpdate(tile);
    }
    /**
     * {@inheritDoc }
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see SnakeUtilities#getEmptyTilePredicate 
     * @see #getEmptyTileCount 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles() 
     * @see #clearTiles(int, int, int, int) 
     */
    @Override
    public List<Tile> getEmptyTiles() {
        return new ArrayList<>(emptyTiles);
    }
    /**
     * {@inheritDoc }
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getFilteredTileCount 
     * @see SnakeUtilities#getEmptyTilePredicate 
     * @see #getEmptyTiles 
     * @see #getAppleTiles 
     * @see #getAppleTileCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see #clearTiles(int, int, int, int) 
     * @see #clearTiles() 
     */
    @Override
    public int getEmptyTileCount(){
        return emptyTiles.size();
    }
    /**
     * {@inheritDoc }
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see SnakeUtilities#getAppleTilePredicate 
     * @see #getAppleTileCount 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    @Override
    public List<Tile> getAppleTiles() {
        return new ArrayList<>(appleTiles);
    }
    /**
     * {@inheritDoc }
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getFilteredTileCount 
     * @see SnakeUtilities#getAppleTilePredicate 
     * @see #getAppleTiles 
     * @see #getEmptyTiles 
     * @see #getEmptyTileCount 
     * @see Tile#isApple 
     * @see Tile#setApple 
     * @see Tile#setAppleIfEmpty 
     */
    @Override
    public int getAppleTileCount(){
        return appleTiles.size();
    }
}
