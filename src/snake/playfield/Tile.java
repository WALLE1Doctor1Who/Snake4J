/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.playfield;

import snake.*;

/**
 * This is a tile that represents a space on the play field in the game Snake. 
 * Tiles store their state along with the row and column that they are located 
 * at on the play field. <p>
 * 
 * The state of a tile can be used to determine what should be drawn for a tile. 
 * Tiles can be {@link #isEmpty() empty}, an {@link #isApple() apple tile}, or 
 * a {@link #isSnake() snake tile}. Snake tiles represent a portion of a snake 
 * and can come in one of two variants, depending on whether the {@link 
 * #getType() type flag} is set. The type flag of a snake tile can be used 
 * indicate how the snake should be drawn, such as using a different color. This 
 * can be used to indicate a second player, an obstacle, or a wall. Apple tiles 
 * do not have a second variant as they are already considered a variant of 
 * empty tiles. That is to say, apple tiles are empty tiles with their type flag 
 * set. <p>
 * 
 * A snake tile can be facing one or more directions. The direction(s) a snake 
 * tile is facing can be used to determine in what should be drawn for a snake 
 * tile. Directions can be combined to make corners, along with horizontal and 
 * vertical segments. For example, a snake tile that is {@link #isFacingUp() 
 * facing up} and {@link #isFacingLeft() left} may be rendered as a corner 
 * connecting the lower and right tiles together. Another example is if the 
 * snake tile is facing both up and {@link #isFacingDown() down}, which may be 
 * rendered as a rectangle spanning the height of the tile. Apple and empty 
 * tiles cannot be facing any directions, and setting a direction on an apple or 
 * empty tile will turn it into a snake tile. The opposite will happen if a 
 * snake tile with only one direction set has that direction cleared. <p>
 * 
 * {@link TileObserver TileObservers} can be used to monitor the state of a 
 * tile. A tile can have up to two TileObservers, a general purpose tile 
 * observer and a tile observer typically provided by a {@link PlayFieldModel 
 * PlayFieldModel} to inform its {@code PlayFieldListener}s of changes to its 
 * tiles. The former can be set using the {@link #setTileObserver 
 * setTileObserver} method, and the latter can be set via either a constructor 
 * or the {@link #setModelTileObserver setModelTileObserver} method. Changing 
 * the model tile observer may cause issues such as the model loosing its 
 * ability to monitor the state of the tile and notify its {@code 
 * PlayFieldListener}s of changes. Most, though not all, setter methods will 
 * return the calling tile so that they can be chained together to change 
 * multiple properties with a single line of code.
 * 
 * @author Milo Steier
 * @see TileObserver
 * @see PlayFieldModel
 * @see AbstractPlayFieldModel
 * @see DefaultPlayFieldModel
 */
public class Tile implements Comparable<Tile>, SnakeConstants{
    /**
     * This is the state value for an empty tile. 
     */
    public static final int EMPTY_STATE = 0x00;
    /**
     * This is the state value for an apple tile. Apple tiles are effectively 
     * the {@link #ALTERNATE_TYPE_FLAG alternate} version of {@link #EMPTY_STATE 
     * empty tiles}.
     * @see #ALTERNATE_TYPE_FLAG
     * @see #EMPTY_STATE
     */
    public static final int APPLE_STATE = ALTERNATE_TYPE_FLAG;
    /**
     * This is the maximum value that a tile's state can be and still be valid.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #VERTICAL_DIRECTIONS
     * @see #HORIZONTAL_DIRECTIONS
     * @see #ALL_DIRECTIONS
     * @see #ALTERNATE_TYPE_FLAG
     * @see #EMPTY_STATE
     * @see #APPLE_STATE
     */
    public static final int MAXIMUM_VALID_STATE = ALL_DIRECTIONS | ALTERNATE_TYPE_FLAG;
    /**
     * This stores the state for the tile.
     */
    private int state = -1;
    /**
     * This stores which row this tile is in.
     */
    private final int row;
    /**
     * This stores which column this tile is in.
     */
    private final int col;
    /**
     * The tile observer to inform of changes to this tile.
     */
    private TileObserver observer = null;
    /**
     * An optional tile observer provided by the model containing this tile that 
     * is used to inform the model of changes to this tile.
     */
    private TileObserver modelObserver;
    /**
     * This constructs a Tile with the given row, column, initial state, and a 
     * tile observer to inform of changes to this tile.
     * @param row The row that this tile is in. (Cannot be negative)
     * @param column The column that this tile is in. (Cannot be negative)
     * @param state The initial state for this tile. (Must be a positive 
     * integer between 0 and {@value MAXIMUM_VALID_STATE}, inclusive)
     * @param modelObserver The tile observer to inform of any changes to this 
     * tile, or null.
     * @throws IllegalArgumentException If either the row or column are 
     * negative or if the initial state is invalid.
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #MAXIMUM_VALID_STATE
     */
    public Tile(int row, int column, int state, TileObserver modelObserver){
        if (row < 0)            // If the row is negative
            throw new IllegalArgumentException("Row cannot be negative ("+row+
                    " < 0)");
        else if (column < 0)    // If the column is negative
            throw new IllegalArgumentException("Column cannot be negative ("+
                    column + " < 0)");
        Tile.this.checkState(state);
        this.row = row;
        this.col = column;
        this.state = state;
        this.modelObserver = modelObserver;
    }
    /**
     * This constructs a Tile with the given row, column, and a tile observer to 
     * inform of changes to this tile. The initial state of this tile will be 
     * the {@link EMPTY_STATE empty state} ({@value EMPTY_STATE}).
     * @param row The row that this tile is in. (Cannot be negative)
     * @param column The column that this tile is in. (Cannot be negative)
     * @param modelObserver The tile observer to inform of any changes to this 
     * tile, or null.
     * @throws IllegalArgumentException If either the row or column are 
     * negative.
     */
    public Tile(int row, int column, TileObserver modelObserver){
        this(row,column,EMPTY_STATE,modelObserver);
    }
    /**
     * This constructs a Tile with the given row, column, and initial state. 
     * @param row The row that this tile is in. (Cannot be negative)
     * @param column The column that this tile is in. (Cannot be negative)
     * @param state The initial state for this tile. (Must be a positive 
     * integer between 0 and {@value MAXIMUM_VALID_STATE}, inclusive)
     * @throws IllegalArgumentException If either the row or column are 
     * negative or if the initial state is invalid.
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #MAXIMUM_VALID_STATE
     */
    public Tile(int row, int column, int state){
        this(row,column,state,null);
    }
    /**
     * This constructs a Tile with the given row and column. The initial state 
     * of this tile will be the {@link EMPTY_STATE empty state} ({@value 
     * EMPTY_STATE}).
     * @param row The row that this tile is in. (Cannot be negative)
     * @param column The column that this tile is in. (Cannot be negative)
     * @throws IllegalArgumentException If either the row or column are 
     * negative.
     */
    public Tile(int row, int column){
        this(row,column,EMPTY_STATE,null);
    }
    /**
     * This returns the row that this tile is in.
     * @return The row that this tile is in.
     * @see #getColumn
     */
    public final int getRow(){
        return row;
    }
    /**
     * This returns the column that this tile is in.
     * @return The column that this tile is in.
     * @see #getRow
     */
    public final int getColumn(){
        return col;
    }
    /**
     * This returns the tile observer that gets informed about changes to this 
     * tile. The tile observer returned is the general purpose tile observer 
     * and can be set using the {@link #setTileObserver setTileObserver} method.
     * @return The tile observer that is informed about changes to this tile, or 
     * null.
     * @see #setTileObserver
     * @see #getModelTileObserver 
     * @see #setState 
     * @see #getState 
     */
    public TileObserver getTileObserver(){
        return observer;
    }
    /**
     * This sets the tile observer to inform about changes to this tile. This is 
     * used to set the general purpose tile observer, and not the tile observer 
     * typically used by the parent {@link PlayFieldModel PlayFieldModel} to 
     * monitor this tile.
     * @param observer The tile observer to inform about changes to this tile.
     * @return This tile.
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     * @see #setModelTileObserver 
     * @see #setState 
     * @see #getState 
     */
    public Tile setTileObserver(TileObserver observer){
        this.observer = observer;
        return this;
    }
    /**
     * This returns the model tile observer that gets informed about changes to 
     * this tile. This is the tile observer that is primarily intended for and 
     * typically used by the parent {@link PlayFieldModel PlayFieldModel} to 
     * monitor this tile. To get the general purpose tile observer, use the 
     * {@link #getTileObserver() getTileObserver} method instead.
     * @return The tile observer used by the model to monitor this tile.
     * @see #getTileObserver 
     * @see #setTileObserver 
     * @see #setModelTileObserver 
     * @see #setState 
     * @see #getState 
     */
    public TileObserver getModelTileObserver(){
        return modelObserver;
    }
    /**
     * This sets the model tile observer to inform about changes to this tile. 
     * This is used to set the tile observer typically used by the parent {@link 
     * PlayFieldModel PlayFieldModel} to monitor this tile. To set the general 
     * purpose tile observer, use {@link #setTileObserver setTileObserver}. <p>
     * 
     * Changing this tile observer to one not provided by the parent model may 
     * result in undesirable behavior such as the model not notifying any {@code 
     * PlayFieldListener}s added to it of changes to this tile. 
     * 
     * @param observer The tile observer to use to inform the model about 
     * changes to this tile.
     * @return This tile.
     * @see #setTileObserver 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     * @see #setState 
     * @see #getState 
     */
    public Tile setModelTileObserver(TileObserver observer){
        this.modelObserver = observer;
        return this;
    }
    /**
     * This is used to notify the {@link TileObserver tile observers} that this 
     * tile has changed in some way. This will check to see if either the {@link 
     * #getTileObserver() general purpose tile observer} or the {@link 
     * #getModelTileObserver() model tile observer} have been set to a non-null 
     * value. If there is a non-null tile observer, then this will call its 
     * {@link TileObserver#tileUpdate tileUpdate} method with this tile.
     * @see #getTileObserver 
     * @see #setTileObserver 
     * @see #getModelTileObserver 
     * @see #setModelTileObserver 
     * @see TileObserver#tileUpdate 
     * @see #getState 
     * @see #setState 
     */
    protected void fireTileChanged(){
        if (observer != null)       // If the tile observer is not null
            observer.tileUpdate(this);
        if (modelObserver != null)  // If the initial tile observer is not null
            modelObserver.tileUpdate(this);
    }
    /**
     * This checks whether the given state is valid. The state is valid if it's 
     * between 0 and {@value MAXIMUM_VALID_STATE}, inclusive.
     * @param state The state to check.
     * @throws IllegalArgumentException If the state is invalid.
     * @see #setState 
     * @see #MAXIMUM_VALID_STATE
     */
    protected void checkState(int state){
            // If the state is out of bounds.
        if (state < 0 || state > MAXIMUM_VALID_STATE)
            throw new IllegalArgumentException("Invalid state for tile: "+state); 
    }
    /**
     * This returns the current state of this tile. Refer to the documentation 
     * for the {@link #setState(int) setState} method for more information. The 
     * default value for this is {@value EMPTY_STATE}.
     * @return The current state of this tile.
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #getType 
     * @see #setType 
     */
    public int getState(){
        return state;
    }
    /**
     * This sets the state of this tile. The state of a tile controls whether a 
     * tile is {@link #isEmpty() empty}, an {@link #isApple() apple tile}, or a 
     * {@link #isSnake() snake tile}. The state also controls which directions 
     * a snake tile is facing. Refer to the documentation for the {@link 
     * #isEmpty() isEmpty}, {@link #isApple() isApple}, and {@link #isSnake() 
     * isSnake} methods for more information about their respective kinds of 
     * tiles. If this tile has any tile observers set, then they will be 
     * notified of a change to this tile's state. <p>
     * 
     * The default value for this is {@value EMPTY_STATE}, which indicates that 
     * the tile is empty.
     * 
     * @param state The state for this tile. (Must be a positive integer 
     * between 0 and {@value MAXIMUM_VALID_STATE}, inclusive)
     * @return This tile.
     * @throws IllegalArgumentException If the given state is invalid.
     * @see #getState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #MAXIMUM_VALID_STATE
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setState(int state){
        if (state == this.state)    // If the old state is the same as the new one
            return this;
        checkState(state);
        this.state = state;
        fireTileChanged();
        return this;
    }
    /**
     * This returns whether the given flag has been set for the state. Refer to 
     * the documentation for the {@link #setState setState} method for more 
     * information about a tile's state and what it is used for.
     * @param flag The flag to check for.
     * @return Whether the given flag is set.
     * @see SnakeUtilities#getFlag 
     * @see #getState 
     * @see #setState 
     * @see #setFlag 
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #MAXIMUM_VALID_STATE
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getType 
     * @see #setType 
     */
    public boolean getFlag(int flag){
        return SnakeUtilities.getFlag(state,flag);
    }
    /**
     * This sets whether the given flag on the state is set based off the given 
     * value. Refer to the documentation for the {@link #setState setState} 
     * method for more information about what the tile's state is. If this tile 
     * has any tile observers set, then they will be notified of a change to 
     * this tile's state.
     * @param flag The flag to be set or cleared based off {@code value}.
     * @param value Whether the flag should be set or cleared.
     * @throws IllegalArgumentException If the state would become invalid as a 
     * result of setting the flag.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see SnakeUtilities#setFlag 
     * @see #EMPTY_STATE
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #APPLE_STATE
     * @see #MAXIMUM_VALID_STATE
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setFlag(int flag, boolean value){
        return setState(SnakeUtilities.setFlag(state,flag,value));
    }
    /**
     * This returns whether this tile is empty. A tile is empty when it does 
     * not contain an apple nor a snake segment. When the {@link #getType() 
     * type flag} is set for an empty tile, then it will become an {@link 
     * #isApple() apple tile}. If a direction is set for an empty tile, it will 
     * become a {@link #isSnake() snake tile}.
     * @return Whether this tile is empty.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #EMPTY_STATE
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getType 
     * @see #setType 
     */
    public boolean isEmpty(){
        return state == EMPTY_STATE;
    }
    /**
     * This clears this tile, setting it to an empty state. Refer to the 
     * documentation for the {@link isEmpty() isEmpty} method for more 
     * information about empty tiles. If this tile has any tile observers set, 
     * then they will be notified of a change to this tile's state.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #EMPTY_STATE
     * @see #isEmpty 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile clear(){
        return setState(EMPTY_STATE);
    }
    /**
     * This returns whether this tile is an apple tile. Apple tiles are 
     * considered the alternate version of {@link #isEmpty() empty tiles}. That 
     * is to say, apple tiles are empty tiles with their {@link #getType() type 
     * flag} set.
     * @return Whether this tile is an apple tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #APPLE_STATE
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isEmpty 
     * @see #clear 
     * @see #getType 
     * @see #setType 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     */
    public boolean isApple(){
        return state == APPLE_STATE;
    }
    /**
     * This sets this tile to be an apple tile. Refer to the documentation for 
     * the {@link #isApple() isApple} method for more information about apple 
     * tiles. If this tile has any tile observers set, then they will be 
     * notified of a change to this tile's state.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #APPLE_STATE
     * @see #isApple 
     * @see #setAppleIfEmpty 
     * @see #isEmpty 
     * @see #clear 
     * @see #getType 
     * @see #setType 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setApple(){
        return setState(APPLE_STATE);
    }
    /**
     * This sets this tile to be an apple tile if this tile is currently {@link 
     * #isEmpty() empty}. If this tile is not empty, then this will do nothing. 
     * Refer to the documentation for the {@link #isApple() isApple} method for 
     * more information about apple tiles. If this tile has any tile observers 
     * set and has become an apple tile as a result of this method, then the 
     * tile observers will be notified of a change to this tile's state.
     * @return Whether this tile is now an {@link #isApple() apple tile}.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #APPLE_STATE
     * @see #isApple 
     * @see #setApple 
     * @see #isEmpty 
     * @see #clear 
     * @see #getType 
     * @see #setType 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public boolean setAppleIfEmpty(){
        if (isEmpty())      // If this tile is empty
            setApple();
        return isApple();
    }
    /**
     * This returns whether this tile is a snake tile. A snake tile cannot be 
     * an {@link #isEmpty() empty tile} or an {@link #isApple() apple tile}. <p>
     * 
     * A snake tile can be facing one or more directions, which can be combined 
     * to form different shapes. This can be used to form corners, lines, and 
     * intersections. For example, a snake facing both {@link #isFacingUp() up} 
     * and to the {@link #isFacingLeft() left} will form a corner, while a snake 
     * facing both up and {@link #isFacingDown() down} will form a vertical
     * line. However, it is worth mentioning that the side being faced is the 
     * inverse of the side that should be rendered on. That is to say, if a 
     * snake tile is facing to the {@link #isFacingRight() right}, then the 
     * snake should be rendered on the left side of the tile. You can think of 
     * it as if the snake segment is an arrow and the direction being faced is
     * the direction the arrow is pointing in. If there are multiple directions 
     * being faced, then each direction can be visualized as a separate arrow. 
     * For example, if a snake tile is facing up, then it can be thought of as 
     * an arrow pointing up. If a snake tile is facing both up and to the left, 
     * then it can be thought of as two intersecting arrows with one pointing up 
     * and the other pointing to the left. If a snake tile is facing both up and 
     * down, then it can be thought of as a line.A snake tile must be facing at 
     * least one direction. If a snake tile is set to be facing no directions, 
     * then it will become either an {@link #isEmpty() empty tile} or an {@link 
     * #isApple() apple tile}, depending on whether the {@link #getType() type 
     * flag} is set. <p>
     * 
     * The {@link #getType() type flag} can be used to indicate how a snake tile 
     * should be rendered, such as using a different color to render snake tiles 
     * with the flag set. This can be used to denote a second player or an 
     * obstacle. How this flag is handled is dependent on the drawing routines 
     * responsible for rendering the tiles.
     * 
     * @return Whether this tile is a snake tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALTERNATE_TYPE_FLAG
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #getType 
     * @see #setType 
     */
    public boolean isSnake(){
        return !isEmpty() && !isApple();
    }
    /**
     * This returns whether this tile is a snake segment that is facing up. 
     * Refer to the documentation for the {@link #isSnake() isSnake} method for 
     * more information about snake tiles.
     * @return Whether this tile is a snake segment that is facing up.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     */
    public boolean isFacingUp(){
        return getFlag(UP_DIRECTION);
    }
    /**
     * This sets whether this tile is a snake segment that is facing up. Refer 
     * to the documentation for the {@link #isSnake() isSnake} method for more 
     * information about snake tiles. If this tile is currently an {@link 
     * #isEmpty() empty tile} or an {@link #isApple() apple tile}, and if {@code 
     * value} is true, then this tile will become a {@link #isSnake() snake 
     * tile}. If {@code value} is false and the snake segment is only facing up, 
     * then the tile will either become an empty tile or an apple tile, 
     * depending on whether the {@link #getType() type flag} is set for this 
     * tile. If this tile has any tile observers set, then they will be notified 
     * of a change to this tile's state.
     * @param value Whether the snake segment represented by this tile is facing 
     * up.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setFacingUp(boolean value){
        return setFlag(UP_DIRECTION, value);
    }
    /**
     * This returns whether this tile is a snake segment that is facing down. 
     * Refer to the documentation for the {@link #isSnake() isSnake} method for 
     * more information about snake tiles.
     * @return Whether this tile is a snake segment that is facing down.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #DOWN_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     */
    public boolean isFacingDown(){
        return getFlag(DOWN_DIRECTION);
    }
    /**
     * This sets whether this tile is a snake segment that is facing down. Refer 
     * to the documentation for the {@link #isSnake() isSnake} method for more 
     * information about snake tiles. If this tile is currently an {@link 
     * #isEmpty() empty tile} or an {@link #isApple() apple tile}, and if {@code 
     * value} is true, then this tile will become a {@link #isSnake() snake 
     * tile}. If {@code value} is false and the snake segment is only facing 
     * down, then the tile will either become an empty tile or an apple tile, 
     * depending on whether the {@link #getType() type flag} is set for this 
     * tile. If this tile has any tile observers set, then they will be notified 
     * of a change to this tile's state.
     * @param value Whether the snake segment represented by this tile is facing 
     * down.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #DOWN_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setFacingDown(boolean value){
        return setFlag(DOWN_DIRECTION,value);
    }
    /**
     * This returns whether this tile is a snake segment that is facing to the 
     * left. Refer to the documentation for the {@link #isSnake() isSnake} 
     * method for more information about snake tiles.
     * @return Whether this tile is a snake segment that is facing left.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #LEFT_DIRECTION
     * @see #isSnake() 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     */
    public boolean isFacingLeft(){
        return getFlag(LEFT_DIRECTION);
    }
    /**
     * This sets whether this tile is a snake segment that is facing to the 
     * left. Refer to the documentation for the {@link #isSnake() isSnake} 
     * method for more information about snake tiles. If this tile is currently 
     * an {@link #isEmpty() empty tile} or an {@link #isApple() apple tile}, and 
     * if {@code value} is true, then this tile will become a {@link #isSnake() 
     * snake tile}. If {@code value} is false and the snake segment is only 
     * facing to the left, then the tile will either become an empty tile or an 
     * apple tile, depending on whether the {@link #getType() type flag} is set 
     * for this tile. If this tile has any tile observers set, then they will be 
     * notified of a change to this tile's state.
     * @param value Whether the snake segment represented by this tile is facing 
     * left.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #LEFT_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setFacingLeft(boolean value){
        return setFlag(LEFT_DIRECTION,value);
    }
    /**
     * This returns whether this tile is a snake segment that is facing to the 
     * right. Refer to the documentation for the {@link #isSnake() isSnake} 
     * method for more information about snake tiles.
     * @return Whether this tile is a snake segment that is facing right.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #RIGHT_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #setFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     */
    public boolean isFacingRight(){
        return getFlag(RIGHT_DIRECTION);
    }
    /**
     * This sets whether this tile is a snake segment that is facing to the 
     * right. Refer to the documentation for the {@link #isSnake() isSnake} 
     * method for more information about snake tiles. If this tile is currently 
     * an {@link #isEmpty() empty tile} or an {@link #isApple() apple tile}, and 
     * if {@code value} is true, then this tile will become a {@link #isSnake() 
     * snake tile}. If {@code value} is false and the snake segment is only 
     * facing to the right, then the tile will either become an empty tile or an 
     * apple tile, depending on whether the {@link #getType() type flag} is set 
     * for this tile. If this tile has any tile observers set, then they will be 
     * notified of a change to this tile's state.
     * @param value Whether the snake segment represented by this tile is facing 
     * right.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #RIGHT_DIRECTION
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #flip 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #getType 
     * @see #setType 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setFacingRight(boolean value){
        return setFlag(RIGHT_DIRECTION,value);
    }
    /**
     * This returns the flags for the direction(s) that this tile is currently 
     * facing. A tile can face multiple directions (even if they are opposites), 
     * or no directions. When a tile is facing at least one direction, then it 
     * is a {@link #isSnake() snake tile}. Refer to the documentation for the 
     * {@link #isSnake() isSnake} method for more information about snake tiles. 
     * Tiles that are facing no directions can be either {@link #isEmpty() empty 
     * tiles} or {@link #isApple() apple tiles}, depending on whether their 
     * {@link #getType() type flag} is set. <p>
     * 
     * This is equivalent to calling {@link SnakeUtilities#getDirections(int) 
     * SnakeUtilities.getDirections}{@code (}{@link #getState 
     * getState}{@code ())}.
     * 
     * @return The direction flags for the directions that this tile is facing, 
     * or 0 if this tile is not facing any directions.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #isSnake 
     * @see #getDirectionsFacedCount 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #flip 
     * @see #getType 
     * @see #setType 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see SnakeUtilities#getDirections 
     */
    public int getDirectionsFaced(){
        return SnakeUtilities.getDirections(state);
    }
    /**
     * This returns the number of directions that this tile is currently facing.
     * A tile can face multiple directions (even if they are opposites), or no 
     * directions. When a tile is facing at least one direction, then it is a 
     * {@link #isSnake() snake tile}. Refer to the documentation for the {@link 
     * #isSnake() isSnake} method for more information about snake tiles. Tiles 
     * that are facing no directions can be either {@link #isEmpty() empty 
     * tiles} or {@link #isApple() apple tiles}, depending on whether their 
     * {@link #getType() type flag} is set.
     * @return The number of directions that this tile is facing.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getType 
     * @see #setType 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see SnakeUtilities#getDirectionCount 
     */
    public int getDirectionsFacedCount(){
        return SnakeUtilities.getDirectionCount(state);
    }
    /**
     * This returns whether the type flag has been set for this tile. For {@link 
     * #isSnake() snake tiles}, this flag can be used to indicate that the tile 
     * should be rendered in a different way, such as using a different color. 
     * This can be used to denote that a snake tile is part of a second player's 
     * snake or an obstacle. If a tile is not a snake tile, then this determines 
     * whether a tile is an {@link #isEmpty() empty tile} or an {@link 
     * #isApple() apple tile}, with apple tiles being empty tiles with this flag 
     * set.
     * @return Whether this tile is in its alternative mode.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #ALTERNATE_TYPE_FLAG
     * @see #setType 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     */
    public boolean getType(){
        return getFlag(ALTERNATE_TYPE_FLAG);
    }
    /**
     * This sets whether the type flag is set for this tile. Refer to the 
     * documentation for the {@link #getType() getType} method for more 
     * information on how this is used. If this tile has any tile observers set, 
     * then they will be notified of a change to this tile's state.
     * @param value Whether this tile should be in its alternative mode.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #ALTERNATE_TYPE_FLAG
     * @see #getType 
     * @see #isEmpty 
     * @see #clear 
     * @see #isApple 
     * @see #setApple 
     * @see #setAppleIfEmpty 
     * @see #isSnake 
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile setType(boolean value){
        return setFlag(ALTERNATE_TYPE_FLAG,value);
    }
    /**
     * This alters the directions of this tile based off the given value. When 
     * called, the direction flags set on the {@link #getState() state} are 
     * {@code XOR}'d with the {@link SnakeUtilities#invertDirections(int) 
     * inverse} of the direction flags set on the given {@code source} value. 
     * All other flags set on the state are not effected and all other flags set 
     * on the {@code source} value are ignored. If this tile has any tile 
     * observers set, then they will be notified of a change to this tile's 
     * state.
     * @param source The value to use to alter the directions of this tile.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#getDirections 
     * @see #alterDirection(Tile) 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile alterDirection(int source){
        return setState(state ^ SnakeUtilities.invertDirections(
                SnakeUtilities.getDirections(source)));
    }
    /**
     * This alters the directions of this tile based off the directions set on 
     * the given tile. When called, the direction flags set on this tile's 
     * {@link #getState() state} are {@code XOR}'d with the {@link 
     * SnakeUtilities#invertDirections(int) inverse} of the direction flags set 
     * on the given {@code source} tile's state. All other flags set on this 
     * tile's state are not effected. If this tile has any tile observers set, 
     * then they will be notified of a change to this tile's state. This is 
     * equivalent to calling {@link #alterDirection(int) 
     * alterDirection}{@code (source.}{@link #getState getState}{@code ())}.
     * @param source The tile to get the directions to use to alter the 
     * directions of this tile (cannot be null).
     * @return This tile.
     * @throws NullPointerException If the given tile is null.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#getDirections 
     * @see #alterDirection(int) 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile alterDirection(Tile source){
        if (source == null)     // If the source tile is null
            throw new NullPointerException("Source tile cannot be null");
        return alterDirection(source.getState());
    }
    /**
     * This is used to flip this tile. This inverts the direction(s) that this 
     * tile is facing. If this tile is {@link #isEmpty() empty} or an {@link 
     * #isApple() apple tile}, then this does nothing. If this tile has any tile 
     * observers set, then they will be notified of a change to this tile's 
     * state.
     * @return This tile.
     * @see #getState 
     * @see #setState 
     * @see #getFlag 
     * @see #setFlag 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     * @see #getDirectionsFaced 
     * @see #isFacingUp 
     * @see #setFacingUp 
     * @see #isFacingDown 
     * @see #setFacingDown 
     * @see #isFacingLeft 
     * @see #setFacingLeft 
     * @see #isFacingRight 
     * @see #setFacingRight 
     * @see SnakeUtilities#invertDirections 
     * @see #getTileObserver 
     * @see #getModelTileObserver 
     */
    public Tile flip(){
        return setState(SnakeUtilities.invertDirections(state));
    }
    /**
     * This returns a String stating the {@link #getRow() row}, {@link 
     * #getColumn() column}, and {@link #getState() state} of this tile. They 
     * are listed in that order, with the row and column being in parenthesis. 
     * The state is placed within square brackets and is given in hexadecimal. 
     * In other words, the String is formatted in this way: {@code 
     * (row,column)[state(in hex)]}.
     * @return A String stating the row, column, and state of this tile.
     * @see #getRow 
     * @see #getColumn 
     * @see #getState 
     */
    @Override
    public String toString(){
        return String.format("(%d,%d)[%02X]",getRow(),getColumn(),getState());
    }
    /**
     * This compares this tile with the given tile to get the order in which 
     * they should occur, and returns a negative integer, zero, or a positive 
     * integer depending on whether this tile comes before, is equal to, or 
     * comes after the given tile. To determine the value to return, this first 
     * compares this tile's row to that of the given tile's row, and if the rows 
     * are not equal, then this will return a negative integer or a positive 
     * integer depending on whether this tile's row is less than or greater than 
     * the given tile's row. If the rows are equal, then this compares this 
     * tile's column to that of the given tile's column. If the columns are also 
     * equal, then this compares this tile's state to that of the given tile's 
     * state. If all three values are equal, then this will return zero.
     * @param o The tile to be compared.
     * @return A negative integer, zero, or a positive integer depending on 
     * whether this tile comes before, is equal to, or comes after the given 
     * tile.
     * @see #compareToIgnoreState 
     * @see #getRow 
     * @see #getColumn 
     * @see #getState 
     */
    @Override
    public int compareTo(Tile o) {
            // This gets the ordering of the Tiles in the grid
        int value = compareToIgnoreState(o);
            // If the tiles are in the same row and column, compare the state
        return (value == 0) ? Integer.compare(state, o.state) : value;
    }
    /**
     * This compares this tile with the given tile to get the order in which 
     * they should occur, and returns a negative integer, zero, or a positive 
     * integer depending on whether this tile comes before, is equal to, or 
     * comes after the given tile. This version ignores the state of the tiles, 
     * and only compares their respective rows and columns. To determine the 
     * value to return, this first compares this tile's row to that of the given 
     * tile's row, and if the rows are not equal, then this will return a 
     * negative integer or a positive integer depending on whether this tile's 
     * row is less than or greater than the given tile's row. If the rows are 
     * equal, then this compares this tile's column to that of the given tile's 
     * column. If the columns are also equal, then this returns zero.
     * @param tile The tile to be compared.
     * @return A negative integer, zero, or a positive integer depending on 
     * whether this tile comes before, is equal to, or comes after the given 
     * tile.
     * @see #compareTo 
     * @see #getRow 
     * @see #getColumn 
     * @see #getState 
     */
    public int compareToIgnoreState(Tile tile){
            // If the rows are the same, compare the columns. Otherwise, compare 
        return (row == tile.row) ? Integer.compare(col, tile.col) : // the rows.
                Integer.compare(row, tile.row);
    }
}
