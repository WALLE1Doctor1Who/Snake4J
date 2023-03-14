/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.event;

import java.util.EventListener;
import snake.*;
import snake.playfield.*;

/**
 * This is the listener interface for receiving play field events.
 * @author Milo Steier
 * @see PlayFieldEvent
 * @see Tile
 * @see PlayFieldModel
 * @see JPlayField
 */
public interface PlayFieldListener extends EventListener{
    /**
     * The method invoked when tiles in the range of rows {@code [}{@link 
     * PlayFieldEvent#getFirstRow() firstRow}{@code , }{@link 
     * PlayFieldEvent#getLastRow() lastRow}{@code ]}, inclusive, and the range 
     * of columns {@code [}{@link PlayFieldEvent#getFirstColumn() 
     * firstColumn}{@code , }{@link PlayFieldEvent#getLastColumn() 
     * lastColumn}{@code ]}, inclusive, have been added to a play field.
     * @param evt The PlayFieldEvent to be processed.
     */
    public void tilesAdded(PlayFieldEvent evt);
    /**
     * The method invoked when tiles in the range of rows {@code [}{@link 
     * PlayFieldEvent#getFirstRow() firstRow}{@code , }{@link 
     * PlayFieldEvent#getLastRow() lastRow}{@code ]}, inclusive, and the range 
     * of columns {@code [}{@link PlayFieldEvent#getFirstColumn() 
     * firstColumn}{@code , }{@link PlayFieldEvent#getLastColumn() 
     * lastColumn}{@code ]}, inclusive, have been removed from a play field.
     * @param evt The PlayFieldEvent to be processed.
     */
    public void tilesRemoved(PlayFieldEvent evt);
    /**
     * The method invoked when the tiles in the range of rows {@code [}{@link 
     * PlayFieldEvent#getFirstRow() firstRow}{@code , }{@link 
     * PlayFieldEvent#getLastRow() lastRow}{@code ]}, inclusive, and the range 
     * of columns {@code [}{@link PlayFieldEvent#getFirstColumn() 
     * firstColumn}{@code , }{@link PlayFieldEvent#getLastColumn() 
     * lastColumn}{@code ]}, inclusive, have changed or been updated. If the 
     * {@code firstRow} and/or the {@code firstColumn} are negative, then the 
     * play field has been completely changed or replaced.
     * @param evt The PlayFieldEvent to be processed.
     */
    public void tilesChanged(PlayFieldEvent evt);
}
