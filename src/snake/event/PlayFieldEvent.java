/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.event;

import java.util.EventObject;
import snake.*;
import snake.playfield.*;

/**
 * This is an event that indicates that there has been a change to the contents 
 * of a play field. This can be a change to the state of one or more tiles or a 
 * change to the number of rows or columns. This event describes the changes 
 * made and the region of tiles that may have been affected.
 * @author Milo Steier
 * @see PlayFieldListener
 * @see Tile
 * @see PlayFieldModel
 * @see JPlayField
 */
public class PlayFieldEvent extends EventObject{
    /**
     * This identifies the removal of rows or columns of tiles.
     */
    public static final int TILES_REMOVED = -1;
    /**
     * This identifies a change or update to one or more existing tiles.
     */
    public static final int TILES_CHANGED = 0;
    /**
     * This identifies the addition of new rows or columns of tiles.
     */
    public static final int TILES_ADDED = 1;
    /**
     * This stores the event type.
     */
    private int type;
    /**
     * This is the first row of tiles that were affected.
     */
    private int firstRow;
    /**
     * This is the last row of tiles that were affected.
     */
    private int lastRow;
    /**
     * This is the first column of tiles that were affected.
     */
    private int firstColumn;
    /**
     * This is the last column of tiles that were affected.
     */
    private int lastColumn;
    /**
     * This indicates whether a series of changes are being made.
     */
    private boolean isAdjusting;
    /**
     * This constructs a PlayFieldEvent that represents a change in the tiles 
     * between the rows {@code row0} and {@code row1}, inclusive, and between 
     * the columns {@code column0} and {@code column1}, inclusive. If {@code 
     * row1} is greater than {@code row0}, then {@code row0} and {@code row1} 
     * will be swapped so that {@code row0} will always be less than or equal to 
     * {@code row1}. The same goes for {@code column0} and {@code column1}, 
     * where {@code column0} will always be less than or equal to {@code 
     * column1}.
     * @param source The source of the event.
     * @param row0 One end of the range of rows that have changed.
     * @param row1 The other end of the range of rows that have changed.
     * @param column0 One end of the range of columns that have changed.
     * @param column1 The other end of the range of columns that have changed.
     * @param isAdjusting Whether or not this event is one in a series of 
     * multiple events, where changes are still being made.
     * @param type The type of event. This should be one of the following: 
     *      {@link TILES_ADDED}, 
     *      {@link TILES_CHANGED}, or
     *      {@link TILES_REMOVED}.
     */
    public PlayFieldEvent(Object source, int row0, int row1, int column0, 
            int column1, boolean isAdjusting, int type){
        super(source);
        this.type = type;
        this.firstRow = Math.min(row0, row1);
        this.lastRow = Math.max(row0, row1);
        this.firstColumn = Math.min(column0, column1);
        this.lastColumn = Math.max(column0, column1);
        this.isAdjusting = isAdjusting;
    }
    /**
     * This constructs a PlayFieldEvent that represents an update to the tiles 
     * between the rows {@code row0} and {@code row1}, inclusive, and between 
     * the columns {@code column0} and {@code column1}, inclusive. If {@code 
     * row1} is greater than {@code row0}, then {@code row0} and {@code row1} 
     * will be swapped so that {@code row0} will always be less than or equal to 
     * {@code row1}. The same goes for {@code column0} and {@code column1}, 
     * where {@code column0} will always be less than or equal to {@code 
     * column1}. If the smaller of the rows and/or the smaller of the columns 
     * are negative, then this will indicate that the play field has completely 
     * changed or been replaced.
     * @param source The source of the event.
     * @param row0 One end of the range of rows that have been updated.
     * @param row1 The other end of the range of rows that have been updated.
     * @param column0 One end of the range of columns that have been updated.
     * @param column1 The other end of the range of columns that have been 
     * updated.
     * @param isAdjusting Whether or not this event is one in a series of 
     * multiple events, where changes are still being made.
     */
    public PlayFieldEvent(Object source, int row0, int row1, int column0, 
            int column1, boolean isAdjusting){
        this(source,row0,row1,column0,column1,isAdjusting,TILES_CHANGED);
    }
    /**
     * This constructs a PlayFieldEvent that represents an update to the tile at 
     * the given row and column. If the row and/or column are negative, then 
     * this will indicate that the play field has completely changed or been 
     * replaced.
     * @param source The source of the event.
     * @param row The row of the tile that has changed.
     * @param column The column of the tile that has changed.
     * @param isAdjusting Whether or not this event is one in a series of 
     * multiple events, where changes are still being made.
     */
    public PlayFieldEvent(Object source, int row, int column, boolean isAdjusting){
        this(source,row,row,column,column,isAdjusting);
    }
    /**
     * This constructs a PlayFieldEvent that represents a change to the 
     * structure of the play field. In other words, this indicates that the play 
     * field has completely changed or been replaced.
     * @param source The source of the event.
     * @param isAdjusting Whether or not this event is one in a series of 
     * multiple events, where changes are still being made.
     */
    public PlayFieldEvent(Object source, boolean isAdjusting){
        this(source,-1,Integer.MAX_VALUE,-1,Integer.MAX_VALUE,isAdjusting);
    }
    /**
     * This returns the type of event that has occurred.
     * @return The type of event. This will be one of the following: 
     *      {@link TILES_ADDED}, 
     *      {@link TILES_CHANGED}, or
     *      {@link TILES_REMOVED}.
     */
    public int getType(){
        return type;
    }
    /**
     * This returns the first row of tiles in the play field that may have 
     * changed. If a single tile has changed, then this value is the same as 
     * the {@link #getLastRow() last row}. If this returns a negative integer, 
     * then the play field has been completely changed or replaced.
     * @return The first row of tiles that may have changed.
     */
    public int getFirstRow(){
        return firstRow;
    }
    /**
     * This returns the last row of tiles in the play field that may have 
     * changed. If a single tile has changed, then this value is the same as 
     * the {@link #getFirstRow() first row}.
     * @return The last row of tiles that may have changed.
     */
    public int getLastRow(){
        return lastRow;
    }
    /**
     * This returns the first column of tiles in the play field that may have 
     * changed. If a single tile has changed, then this value is the same as 
     * the {@link #getLastColumn() last column}. If this returns a negative 
     * integer, then the play field has been completely changed or replaced.
     * @return The first column of tiles that may have changed.
     */
    public int getFirstColumn(){
        return firstColumn;
    }
    /**
     * This returns the last column of tiles in the play field that may have 
     * changed. If a single tile has changed, then this value is the same as 
     * the {@link #getFirstColumn() first column}.
     * @return The last column of tiles that may have changed.
     */
    public int getLastColumn(){
        return lastColumn;
    }
    /**
     * This returns whether or not this event is one in a series of multiple 
     * events, where changes are still being made. Refer to the {@link 
     * PlayFieldModel#setTilesAreAdjusting(boolean) 
     * PlayFieldModel.setTilesAreAdjusting} method for more information.
     * @return Whether this event is one in a series of multiple events.
     * @see PlayFieldModel#setTilesAreAdjusting(boolean) 
     * @see PlayFieldModel#getTilesAreAdjusting() 
     */
    public boolean getTilesAreAdjusting(){
        return isAdjusting;
    }
    /**
     * This returns a String representation of this PlayFieldEvent. This method 
     * is primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this PlayFieldEvent.
     */
    protected String paramString(){
            // This gets the string representation of the event type
        String typeStr;
        switch(getType()){
            case(TILES_REMOVED):            // If tiles were removed
                typeStr = "TILES_REMOVED";
                break;
            case(TILES_CHANGED):            // If tiles were changed
                typeStr = "TILES_CHANGED";
                break;
            case(TILES_ADDED):              // If tiles were added
                typeStr = "TILES_ADDED";
                break;
            default:
                typeStr = "unknown type ("+getType()+")";
        }
        return "type="+typeStr+
                ",firstRow="+getFirstRow()+
                ",lastRow="+getLastRow()+
                ",firstColumn="+getFirstColumn()+
                ",lastColumn="+getLastColumn()+
                ",isAdjusting="+getTilesAreAdjusting();
    }
    /**
     * This returns a String representation of this PlayFieldEvent.
     * @return A String representation of this PlayFieldEvent.
     */
    @Override
    public String toString(){
        return getClass().getName() + "["+paramString()+"]";
    }
}
