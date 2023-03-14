/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.playfield;

import java.util.*;
import java.util.function.Predicate;
import javax.swing.event.EventListenerList;
import snake.*;
import snake.event.*;

/**
 * This is an abstract implementation for the {@link PlayFieldModel model} 
 * representing a play field of {@link Tile tiles}. This implements the {@link 
 * TileObserver} interface so as to listen to the tiles contained within the 
 * model and inform the {@code PlayFieldListener}s added to the model of changes 
 * made to the tiles. <p>
 * 
 * The iterators returned by this class's {@code iterator} method are fail-fast,
 * i.e. if this PlayFieldModel is structurally modified in any way at any time 
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
 * @see DefaultPlayFieldModel
 * @see JPlayField
 */
public abstract class AbstractPlayFieldModel implements PlayFieldModel, 
        TileObserver{
    /**
     * This is an EventListenerList to store the listeners for this class.
     */
    protected EventListenerList listenerList = new EventListenerList();
    /**
     * This is the object used to handle the adjusting flag and the region of 
     * tiles that were affected while the adjusting flag is set.
     */
    private EventFlag adjustingFlag = new EventFlag();
    /**
     * This is the object used to handle the flag used to pause firing events 
     * and the region of tiles that were affected while firing the events are 
     * paused.
     */
    private EventFlag pausedFlag = new EventFlag();
    /**
     * {@inheritDoc }
     * @see #getTilesAreAdjusting 
     * @see PlayFieldEvent#getTilesAreAdjusting 
     */
    @Override
    public void setTilesAreAdjusting(boolean isAdjusting){
        adjustingFlag.setValue(isAdjusting, PlayFieldEvent.TILES_CHANGED);
    }
    /**
     * {@inheritDoc }
     * @see #setTilesAreAdjusting 
     */
    @Override
    public boolean getTilesAreAdjusting(){
        return adjustingFlag.getValue();
    }
    /**
     * This sets whether to pause firing {@link PlayFieldEvent PlayFieldEvents} 
     * while making changes to some of the tiles. This is similar to setting 
     * whether the {@link #setTilesAreAdjusting tiles are adjusting}. However, 
     * instead of allowing changes to each individual tile to fire a {@code 
     * PlayFieldEvent} and expecting the listeners to handle the intermediate 
     * events, setting this will cause the {@code PlayFieldEvent}s to be blocked 
     * entirely. As such, this has the potential to break things that rely on 
     * listening to the events from this model. This is only intended to be used 
     * internally by {@link AbstractPlayFieldModel} and subclasses of it, and 
     * should not be used by anything else for any reason. It is recommended to 
     * fire a {@code PlayFieldEvent} after setting this to {@code false}, so as 
     * to notify any listeners of the changes that were made since this was set 
     * to {@code true}. <p>
     * 
     * The {@code eventType} is used as the event type for the {@code 
     * PlayFieldEvent} that will be fired if {@code pauseEvents} is {@code 
     * false} and at least one tile has been changed since this was set to 
     * {@code true}. The {@code PlayFieldEvent} will encompass all the tiles 
     * that were changed while this {@code true}. No event will be fired if 
     * either {@code pauseEvents} is {@code true}, {@code eventType} is null, 
     * or no tiles were changed while this was set to {@code true}. 
     * 
     * @param pauseEvents Whether to pause firing events. If this is {@code 
     * true}, then no {@code PlayFieldEvent}s will be fired as long as this is 
     * {@code true}. If this is {@code false}, then {@code PlayFieldEvent}s 
     * will be allowed to be fired. When setting this to {@code false} and the 
     * {@code eventType} is not null, then a {@code PlayFieldEvent} will be 
     * fired with the given event type.
     * @param eventType The type of {@code PlayFieldEvent} to fire when resuming 
     * the firing of events. This should be one of the following: 
     *      {@link PlayFieldEvent#TILES_ADDED TILES_ADDED}, 
     *      {@link PlayFieldEvent#TILES_CHANGED TILES_CHANGED}, 
     *      {@link PlayFieldEvent#TILES_REMOVED TILES_REMOVED}, or
     *      {@code null} if no event should be fired.
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean)
     * @see #getEventsArePaused 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     */
    protected void setEventsArePaused(boolean pauseEvents, Integer eventType){
        pausedFlag.setValue(pauseEvents, eventType);
    }
    /**
     * This sets whether to pause firing {@link PlayFieldEvent PlayFieldEvents} 
     * while making changes to some of the tiles. This is similar to setting 
     * whether the {@link #setTilesAreAdjusting tiles are adjusting}. However, 
     * instead of allowing changes to each individual tile to fire a {@code 
     * PlayFieldEvent} and expecting the listeners to handle the intermediate 
     * events, setting this will cause the {@code PlayFieldEvent}s to be blocked 
     * entirely. As such, this has the potential to break things that rely on 
     * listening to the events from this model. This is only intended to be used 
     * internally by {@link AbstractPlayFieldModel} and subclasses of it, and 
     * should not be used by anything else for any reason. It is recommended to 
     * fire a {@code PlayFieldEvent} after setting this to {@code false}, so as 
     * to notify any listeners of the changes that were made since this was set 
     * to {@code true}. <p>
     * 
     * This version of the method is equivalent to calling {@link 
     * #setEventsArePaused(boolean, Integer) 
     * setEventsArePaused}{@code (pauseEvents, null)}. As such, this will not 
     * fire a PlayFieldEvent when setting this to {@code false}.
     * 
     * @param pauseEvents Whether to pause firing events. If this is {@code 
     * true}, then no {@code PlayFieldEvent}s will be fired as long as this is 
     * {@code true}. If this is {@code false}, then {@code PlayFieldEvent}s 
     * will be allowed to be fired.
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #getEventsArePaused 
     * @see #firePlayFieldChange 
     */
    protected void setEventsArePaused(boolean pauseEvents){
        setEventsArePaused(pauseEvents,null);
    }
    /**
     * This returns whether {@link PlayFieldEvent PlayFieldEvents} are being 
     * prevented from firing. In other words, this returns whether the {@code 
     * PlayFieldListener}s will not be getting notified of any {@code 
     * PlayFieldEvent}s that occur. This is used internally, often to 
     * concatenate changes to the tiles into a single {@code PlayFieldEvent}.
     * @return Whether the firing of events is paused. If this is {@code 
     * true}, then no {@code PlayFieldEvent}s will be fired as long as this is 
     * {@code true}. If this is {@code false}, then {@code PlayFieldEvent}s 
     * will be allowed to be fired.
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #firePlayFieldChange 
     */
    protected boolean getEventsArePaused(){
        return pausedFlag.getValue();
    }
    /**
     * This is used to listen to changes to the tiles in this model, and to fire 
     * a {@code PlayFieldEvent} when one changes. This does nothing if the given 
     * tile is null or not in this model.
     * @param tile {@inheritDoc }
     * @see #getTile 
     * @see #contains(Tile) 
     */
    @Override
    public void tileUpdate(Tile tile){
            // If the tile is not null and this contains the tile
        if (tile != null && contains(tile)){
            fireTilesChanged(tile.getRow(),tile.getColumn());
        }
    }
    /**
     * This returns an array of all the objects currently registered as 
     * <code><em>Foo</em>Listener</code>s on this model. 
     * <code><em>Foo</em>Listener</code>s are registered via the 
     * <code>add<em>Foo</em>Listener</code> method. <p>
     * 
     * The listener type can be specified using a class literal, such as 
     * <code><em>Foo</em>Listener.class</code>. If no such listeners exist, then 
     * an empty array will be returned.
     * @param <T> The type of {@code EventListener} being requested.
     * @param listenerType The type of listeners being requested. This should 
     * be an interface that descends from {@code EventListener}.
     * @return An array of the objects registered as the given listener type on 
     * this model, or an empty array if no such listeners have been added.
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType){
        return listenerList.getListeners(listenerType);
    }
    /**
     * {@inheritDoc }
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     */
    @Override
    public void addPlayFieldListener(PlayFieldListener l) {
        if (l != null)   // If the listener is not null
            listenerList.add(PlayFieldListener.class, l);
    }
    /**
     * {@inheritDoc }
     * @see #addPlayFieldListener 
     * @see #getPlayFieldListeners 
     */
    @Override
    public void removePlayFieldListener(PlayFieldListener l) {
        listenerList.remove(PlayFieldListener.class, l);
    }
    /**
     * This returns an array containing all the {@code PlayFieldListener}s that 
     * have been added to this model.
     * @return An array containing the {@code PlayFieldListener}s that have been 
     * added, or an empty array if none have been added.
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     */
    public PlayFieldListener[] getPlayFieldListeners(){
        return listenerList.getListeners(PlayFieldListener.class);
    }
    /**
     * This notifies the given {@code PlayFieldListener} of the given event. If 
     * the listener is null, then this does nothing. Otherwise, this checks the 
     * given event's {@link PlayFieldEvent#getType() type} and invokes the 
     * listener's corresponding method. This ignores whether {@link 
     * #getEventsArePaused() events are paused}. This is used by {@link 
     * #firePlayFieldChange(PlayFieldEvent) firePlayFieldChange} to notify each 
     * listener of the event given to it. 
     * @param evt The event to notify the listener of (cannot be null).
     * @param l The listener to notify.
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see PlayFieldEvent#getType 
     * @see PlayFieldEvent#TILES_CHANGED
     * @see PlayFieldEvent#TILES_ADDED
     * @see PlayFieldEvent#TILES_REMOVED
     * @see PlayFieldListener#tilesChanged 
     * @see PlayFieldListener#tilesAdded 
     * @see PlayFieldListener#tilesRemoved 
     */
    protected void notifyPlayFieldListener(PlayFieldEvent evt, PlayFieldListener l){
        if (l == null)          // If the listener is null
            return;
        switch(evt.getType()){  // Get the type of event
            case(PlayFieldEvent.TILES_CHANGED): // If tiles were changed
                l.tilesChanged(evt);
                return;
            case(PlayFieldEvent.TILES_ADDED):   // If tiles were added
                l.tilesAdded(evt);
                return;
            case(PlayFieldEvent.TILES_REMOVED): // If tiles were removed
                l.tilesRemoved(evt);
        }
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model of the given event if the given event is not null and events 
     * are not {@link #getEventsArePaused() paused}.
     * @param evt The {@code PlayFieldEvent} to be fired.
     * @see #firePlayFieldChange(int, int, int, int, boolean, int) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #tileUpdate 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see #notifyPlayFieldListener 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void firePlayFieldChange(PlayFieldEvent evt){
        if (evt == null)    // If the event is null
            return;
        adjustingFlag.processEvent(evt);
            // If firing events is paused
        if (pausedFlag.processEvent(evt))
            return;
            // A for loop to go through the listeners that have been added
        for (PlayFieldListener l : getPlayFieldListeners()){
            notifyPlayFieldListener(evt,l);
        }
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that the tiles in the region between the rows {@code row0} and 
     * {@code row1}, inclusive, and between the columns {@code column0} and 
     * {@code column1}, inclusive, have changed in some way. Note that {@code 
     * row0} does not necessarily need to be less than or equal to {@code row1}, 
     * and that {@code column0} does not necessarily need to be less than or 
     * equal to {@code column1}. If the {@link #getEventsArePaused() events are 
     * paused}, then the {@code PlayFieldListener}s will not be notified.
     * 
     * @param row0 One end of the range of rows that have changed.
     * @param row1 The other end of the range of rows that have changed.
     * @param column0 One end of the range of columns that have changed.
     * @param column1 The other end of the range of columns that have changed.
     * @param isAdjusting Whether or not this event is one in a series of 
     * multiple events, where changes are still being made.
     * @param type The type of event. This should be one of the following: 
     *      {@link PlayFieldEvent#TILES_ADDED TILES_ADDED}, 
     *      {@link PlayFieldEvent#TILES_CHANGED TILES_CHANGED}, or
     *      {@link PlayFieldEvent#TILES_REMOVED TILES_REMOVED}.
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #tileUpdate 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void firePlayFieldChange(int row0,int row1,int column0,int column1, 
            boolean isAdjusting, int type){
        firePlayFieldChange(new PlayFieldEvent(this,row0,row1,column0,column1,
                isAdjusting,type));
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that the tiles in the region between the rows {@code row0} and 
     * {@code row1}, inclusive, and between the columns {@code column0} and 
     * {@code column1}, inclusive, have changed in some way. The listeners will 
     * also be notified as to whether the {@link #getTilesAreAdjusting() tiles 
     * are adjusting}. Note that {@code row0} does not necessarily need to be 
     * less than or equal to {@code row1}, and that {@code column0} does not 
     * necessarily need to be less than or equal to {@code column1}. If the 
     * {@link #getEventsArePaused() events are paused}, then the {@code 
     * PlayFieldListener}s will not be notified.
     * 
     * @param row0 One end of the range of rows that have changed.
     * @param row1 The other end of the range of rows that have changed.
     * @param column0 One end of the range of columns that have changed.
     * @param column1 The other end of the range of columns that have changed.
     * @param type The type of event. This should be one of the following: 
     *      {@link PlayFieldEvent#TILES_ADDED TILES_ADDED}, 
     *      {@link PlayFieldEvent#TILES_CHANGED TILES_CHANGED}. or
     *      {@link PlayFieldEvent#TILES_REMOVED TILES_REMOVED}.
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, boolean, int) 
     * @see PlayFieldEvent
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #tileUpdate 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void firePlayFieldChange(int row0, int row1, int column0,
            int column1, int type){
        firePlayFieldChange(row0,row1,column0,column1,getTilesAreAdjusting(),
                type);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that the tiles in the region between the rows {@code row0} and 
     * {@code row1}, inclusive, and between the columns {@code column0} and 
     * {@code column1}, inclusive, have been changed or updated. Note that 
     * {@code row0} does not necessarily need to be less than or equal to {@code 
     * row1}, and that {@code column0} does not necessarily need to be less than 
     * or equal to {@code column1}. If the {@link #getEventsArePaused() events 
     * are paused}, then the {@code PlayFieldListener}s will not be notified.
     * 
     * @param row0 One end of the range of rows that have been updated.
     * @param row1 The other end of the range of rows that have been updated.
     * @param column0 One end of the range of columns that have been updated.
     * @param column1 The other end of the range of columns that have been 
     * updated.
     * @see #fireTilesChanged(int, int) 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_CHANGED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #tileUpdate 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesChanged(int row0,int row1,int column0,int column1){
        firePlayFieldChange(row0,row1,column0,column1,
                PlayFieldEvent.TILES_CHANGED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that the tile at the given {@code row} and {@code column} has 
     * been changed or updated. If the {@link #getEventsArePaused() events are 
     * paused}, then the {@code PlayFieldListener}s will not be notified.
     * @param row The row of the tile that has been updated.
     * @param column The column of the tile that has been updated.
     * @see #fireTilesChanged 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_CHANGED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #tileUpdate 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesChanged(int row, int column){
        fireTilesChanged(row,row,column,column);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles have been added to the region between the rows 
     * {@code row0} and {@code row1}, inclusive, and between the columns {@code 
     * column0} and {@code column1}, inclusive. Note that {@code row0} does not 
     * necessarily need to be less than or equal to {@code row1}, and that 
     * {@code column0} does not necessarily need to be less than or equal to 
     * {@code column1}. If the {@link #getEventsArePaused() events are paused}, 
     * then the {@code PlayFieldListener}s will not be notified.
     * 
     * @param row0 One end of the range of rows that have been added.
     * @param row1 The other end of the range of rows that have been added.
     * @param column0 One end of the range of columns that have been added.
     * @param column1 The other end of the range of columns that have been 
     * added.
     * @see #fireTileRowsAdded 
     * @see #fireTileColumnsAdded 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_ADDED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesAdded(int row0, int row1, int column0, int column1){
        firePlayFieldChange(row0,row1,column0,column1,
                PlayFieldEvent.TILES_ADDED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles have been added between the rows {@code row0} and 
     * {@code row1}, inclusive. Note that {@code row0} does not necessarily need 
     * to be less than or equal to {@code row1}. If the {@link 
     * #getEventsArePaused() events are paused}, then the {@code 
     * PlayFieldListener}s will not be notified.
     * @param row0 One end of the range of rows that have been added.
     * @param row1 The other end of the range of rows that have been added.
     * @see #fireTilesAdded 
     * @see #fireTileColumnsAdded 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_ADDED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTileRowsAdded(int row0, int row1){
        fireTilesAdded(row0, row1, 0, getColumnCount()-1);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles have been added between the columns {@code column0} 
     * and {@code column1}, inclusive. Note that {@code column0} does not 
     * necessarily need to be less than or equal to {@code column1}. If the 
     * {@link #getEventsArePaused() events are paused}, then the {@code 
     * PlayFieldListener}s will not be notified.
     * @param column0 One end of the range of columns that have been added.
     * @param column1 The other end of the range of columns that have been 
     * added.
     * @see #fireTilesAdded 
     * @see #fireTileRowsAdded 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_ADDED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener  
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesRemoved 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTileColumnsAdded(int column0, int column1){
        fireTilesAdded(0,getRowCount()-1,column0,column1);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles in the region between the rows {@code row0} and 
     * {@code row1}, inclusive, and between the columns {@code column0} and 
     * {@code column1}, inclusive, have been removed. Note that {@code row0} 
     * does not necessarily need to be less than or equal to {@code row1}, and 
     * that {@code column0} does not necessarily need to be less than or equal 
     * to {@code column1}. If the {@link #getEventsArePaused() events are 
     * paused}, then the {@code PlayFieldListener}s will not be notified.
     * 
     * @param row0 One end of the range of rows that have been removed.
     * @param row1 The other end of the range of rows that have been removed.
     * @param column0 One end of the range of columns that have been removed.
     * @param column1 The other end of the range of columns that have been 
     * removed.
     * @see #fireTileRowsRemoved 
     * @see #fireTileColumnsRemoved 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_REMOVED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTilesRemoved(int row0,int row1,int column0,int column1){
        firePlayFieldChange(row0,row1,column0,column1,
                PlayFieldEvent.TILES_REMOVED);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles have been removed from the rows {@code row0} and 
     * {@code row1}, inclusive. Note that {@code row0} does not necessarily need 
     * to be less than or equal to {@code row1}. If the {@link 
     * #getEventsArePaused() events are paused}, then the {@code 
     * PlayFieldListener}s will not be notified.
     * @param row0 One end of the range of rows that have been removed.
     * @param row1 The other end of the range of rows that have been removed.
     * @see #fireTilesRemoved 
     * @see #fireTileColumnsRemoved 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_REMOVED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTileRowsRemoved(int row0, int row1){
        fireTilesRemoved(row0, row1, 0, getColumnCount()-1);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that tiles have been removed from the columns {@code column0} 
     * and {@code column1}, inclusive. Note that {@code column0} does not 
     * necessarily need to be less than or equal to {@code column1}. If the 
     * {@link #getEventsArePaused() events are paused}, then the {@code 
     * PlayFieldListener}s will not be notified.
     * @param column0 One end of the range of columns that have been removed.
     * @param column1 The other end of the range of columns that have been 
     * removed.
     * @see #fireTilesRemoved 
     * @see #fireTileRowsRemoved 
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#TILES_REMOVED
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #setRowCount 
     * @see #setColumnCount 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #firePlayFieldStructureChanged 
     */
    protected void fireTileColumnsRemoved(int column0, int column1){
        fireTilesRemoved(0,getRowCount()-1,column0,column1);
    }
    /**
     * This notifies all the {@code PlayFieldListener}s that have been added to 
     * this model that the structure of the play field has completely changed. 
     * This is equivalent to calling {@link snake.JPlayField#setModel setModel} 
     * on the JPlayField. If the {@link #getEventsArePaused() events are 
     * paused}, then the {@code PlayFieldListener}s will not be notified.
     * @see #firePlayFieldChange(PlayFieldEvent) 
     * @see #firePlayFieldChange(int, int, int, int, boolean, int) 
     * @see #firePlayFieldChange(int, int, int, int, int) 
     * @see PlayFieldEvent
     * @see PlayFieldEvent#PlayFieldEvent(Object, boolean) 
     * @see #addPlayFieldListener 
     * @see #removePlayFieldListener 
     * @see #getPlayFieldListeners 
     * @see #setTilesAreAdjusting 
     * @see #getTilesAreAdjusting 
     * @see #setEventsArePaused(boolean, Integer) 
     * @see #setEventsArePaused(boolean) 
     * @see #getEventsArePaused 
     * @see #fireTilesChanged 
     * @see #fireTilesAdded 
     * @see #fireTilesRemoved 
     */
    protected void firePlayFieldStructureChanged(){
        firePlayFieldChange(new PlayFieldEvent(this,getTilesAreAdjusting()));
    }
    /**
     * {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     * @throws IllegalArgumentException {@inheritDoc }
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
    @Override
    public Tile getAdjacentTile(Tile tile, int direction, boolean wrapAround){
        if (tile == null)   // If the tile is null
            throw new NullPointerException("Tile must not be null");
        int row = tile.getRow();            // Get the tile's row
        int col = tile.getColumn();         // Get the tile's column
        if (!contains(row,col))             // If the tile is out of bounds
            return null;
            // Get the adjacent tile corresponding with the direction
        switch(direction){
            case (SnakeConstants.UP_DIRECTION):    // If getting the tile above
                row--;
                break;
            case (SnakeConstants.DOWN_DIRECTION):  // If getting the tile below
                row++;
                break;
            case(SnakeConstants.LEFT_DIRECTION):   // If getting the tile to the left
                col--;
                break;
            case(SnakeConstants.RIGHT_DIRECTION):  // If getting the tile to the right
                col++;
                break;
            default:
                throw new IllegalArgumentException("Invalid value for direction: " + direction);
        }
        if (wrapAround){    // If we are wrapping around the play field
            row = (row + getRowCount()) % getRowCount();
            col = (col + getColumnCount()) % getColumnCount();
        }   // If the adjacent tile is in bounds, return it. Otherwise, return 
        return (contains(row,col)) ? getTile(row,col) : null;   // null.
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation always throws an {@code 
     * UnsupportedOperationException}.
     * 
     * @param rows {@inheritDoc }
     * @throws UnsupportedOperationException {@inheritDoc }
     * @throws IllegalArgumentException {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #setColumnCount 
     * @see #getTileCount 
     */
    @Override
    public void setRowCount(int rows){
        throw new UnsupportedOperationException();
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation always throws an {@code 
     * UnsupportedOperationException}.
     * 
     * @param columns {@inheritDoc }
     * @throws UnsupportedOperationException {@inheritDoc }
     * @throws IllegalArgumentException {@inheritDoc }
     * @see #getColumnCount 
     * @see #getRowCount 
     * @see #setRowCount 
     * @see #getTileCount 
     */
    @Override
    public void setColumnCount(int columns){
        throw new UnsupportedOperationException();
    }
    /**
     * This checks to see if the {@code fromIndex} and {@code toIndex} are 
     * within range. The indexes are in range if they are within bounds and the 
     * {@code fromIndex} is less than or equal to the {@code toIndex}. If the 
     * range is out of bounds, then this will throw an 
     * IndexOutOfBoundsException.
     * @param fromIndex The index to start at.
     * @param toIndex The index to stop at, exclusive.
     * @param size The size (i.e. the upper bound, exclusive, of the range).
     * @param name The name for the indexes being checked.
     * @throws IndexOutOfBoundsException If the range is out of bounds.
     */
    private void checkRange(int fromIndex, int toIndex, int size, String name){
        if (fromIndex < 0)          // If the start index is negative
            throw new IndexOutOfBoundsException("From "+name+" out of bounds: "+ 
                    fromIndex + " < 0");
        else if (toIndex > size)    // If the end index is greater than the size
            throw new IndexOutOfBoundsException("To "+name+" out of bounds: " + 
                    toIndex + " > " + size);
            // If the start index is greater than the stop index
        else if (fromIndex > toIndex)   
            throw new IndexOutOfBoundsException("From "+name+" > To "+name+": "+
                    fromIndex+" > "+toIndex);
    }
    /**
     * {@inheritDoc }
     * @throws IndexOutOfBoundsException {@inheritDoc }
     * @see #clearTiles() 
     * @see #getTile 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     */
    @Override
    public void clearTiles(int fromRow, int toRow, int fromColumn, int toColumn){
        checkRange(fromRow,toRow,getRowCount(),"row");
        checkRange(fromColumn,toColumn,getColumnCount(),"column");
            // Get if the events are currently paused
        boolean paused = getEventsArePaused();
        setEventsArePaused(true);   // Pause them to concatenate the changes
            // A for loop to go through the rows of tiles to clear
        for (int r = fromRow; r < toRow; r++){
                // A for loop to go through the columns of tiles to clear
            for (int c = fromColumn; c < toColumn; c++){
                getTile(r,c).clear();
            }
        }   // Unpause the events (if they weren't already paused) and fire a 
            // tiles changed event
        setEventsArePaused(paused,PlayFieldEvent.TILES_CHANGED);
    }
    /**
     * {@inheritDoc } This model contains the given tile if the given tile is 
     * not null, the tile's {@link Tile#getRow() row} and {@link 
     * Tile#getColumn() column} are {@link #contains(int, int) within range} of 
     * this model, and the tile is {@link Tile#equals equal} to the {@link 
     * #getTile tile in this model at} the given tile's row and column.
     * @param tile {@inheritDoc }
     * @return {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #contains(int, int) 
     * @see #getTile 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#equals 
     */
    @Override
    public boolean contains(Tile tile) {
        return tile != null && contains(tile.getRow(),tile.getColumn()) && 
                tile.equals(getTile(tile.getRow(),tile.getColumn()));
    }
    /**
     * {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTiles 
     * @see #getFilteredTileList 
     */
    @Override
    public List<List<Tile>> getTileList(){
            // This creates a list of lists to store the tiles form this model
        List<List<Tile>> tiles = new ArrayList<>();
            // A for loop to go through the rows in this model
        for (int r = 0; r < getRowCount(); r++){
            tiles.add(new ArrayList<>());   // Create a list to store this row
                // A for loop to go through the columns in the current row
            for (int c = 0; c < getColumnCount(); c++){
                tiles.get(r).add(getTile(r,c));
            }
        }
        return tiles;
    }
    /**
     * {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #getTileList 
     * @see #getFilteredTileList 
     */
    @Override
    public Tile[][] getTiles(){
            // This creates a two-dimensional array to store the tiles from this
        Tile[][] arr = new Tile[getRowCount()][getColumnCount()];   // model
            // A for loop to go through the rows in this model
        for (int r = 0; r < getRowCount(); r++){
                // A for loop to go through the columns in the current row
            for (int c = 0; c < getColumnCount(); c++){
                    // If this model contains a tile at the current row and 
                    // column, get the tile. Otherwise, use null
                arr[r][c] = (contains(r,c)) ? getTile(r,c) : null;
            }
        }
        return arr;
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation traverses through all the tiles in 
     * this model using its {@link #iterator iterator}. Each tile is checked and 
     * matching tiles are added to a list.
     * 
     * @param filter {@inheritDoc }
     * @return {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #iterator 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileCount 
     * @see #getEmptyTiles 
     * @see #getAppleTiles 
     */
    @Override
    public List<Tile> getFilteredTileList(Predicate<? super Tile> filter){
            // Require a non-null filter predicate
        Objects.requireNonNull(filter);
            // A list to store the tiles that match the filter
        List<Tile> tiles = new ArrayList<>();
        for (Tile tile : this){     // A for loop to go through the tiles
            if (filter.test(tile))  // If the current tile matches the filter
                tiles.add(tile);
        }
        return tiles;
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation traverses through all the tiles in 
     * this model using its {@link #iterator iterator}, and increments a value 
     * every time a tile matches the predicate.
     * 
     * @param filter {@inheritDoc }
     * @return {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTileCount 
     * @see #getTile 
     * @see #contains(int, int) 
     * @see #contains(Tile) 
     * @see #iterator 
     * @see #getTileList 
     * @see #getTiles 
     * @see #getFilteredTileList 
     * @see #getEmptyTileCount 
     * @see #getAppleTileCount 
     */
    @Override
    public int getFilteredTileCount(Predicate<? super Tile> filter){
            // Require a non-null filter predicate
        Objects.requireNonNull(filter);
            // This gets the amount of tiles that match the filter
        int count = 0;              
        for (Tile tile : this){     // A for loop to go through the tiles
            if (filter.test(tile))  // If the current tile matches the filter
                count++;
        }
        return count;
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation calls {@link 
     * #getFilteredTileList getFilteredTileList}{@code (}{@link 
     * SnakeUtilities#getEmptyTilePredicate 
     * SnakeUtilities.getEmptyTilePredicate}{@code ())} and returns the 
     * resulting list.
     * 
     * @return {@inheritDoc }
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
    public List<Tile> getEmptyTiles(){
        return getFilteredTileList(SnakeUtilities.getEmptyTilePredicate());
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation calls {@link 
     * #getFilteredTileCount getFilteredTileCount}{@code (}{@link 
     * SnakeUtilities#getEmptyTilePredicate 
     * SnakeUtilities.getEmptyTilePredicate}{@code ())}.
     * 
     * @return {@inheritDoc }
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
        return getFilteredTileCount(SnakeUtilities.getEmptyTilePredicate());
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation calls {@link 
     * #getFilteredTileList getFilteredTileList}{@code (}{@link 
     * SnakeUtilities#getAppleTilePredicate 
     * SnakeUtilities.getAppleTilePredicate}{@code ())} and returns the 
     * resulting list.
     * 
     * @return {@inheritDoc }
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
    public List<Tile> getAppleTiles(){
        return getFilteredTileList(SnakeUtilities.getAppleTilePredicate());
    }
    /**
     * {@inheritDoc }
     * 
     * @implSpec The default implementation calls {@link 
     * #getFilteredTileCount getFilteredTileCount}{@code (}{@link 
     * SnakeUtilities#getAppleTilePredicate 
     * SnakeUtilities.getAppleTilePredicate}{@code ())}.
     * 
     * @return {@inheritDoc }
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
        return getFilteredTileCount(SnakeUtilities.getAppleTilePredicate());
    }
    /**
     * This returns a String representation of this play field model. The String 
     * consists of a representation of the {@link Tile#getState() states} of the 
     * tiles in this model. Each line of text in the String represents a row of 
     * tiles, and each tile's state is located at its corresponding row and 
     * column in this model. The tile's state is displayed in hexadecimal. 
     * @return A String representation of this model.
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see Tile#getState 
     */
    @Override
    public String toString(){
        String str = "";    // This gets a String representation of this model
            // A for loop to go through the rows in this this model
        for (int r = 0; r < getRowCount(); r++){
                // If this isn't the start of the first row (no need to put an 
            if (r > 0)  // end of line character at the start of the String)
                str += System.lineSeparator();
                // A for loop to go through the columns of tiles in the current 
            for (int c = 0; c < getColumnCount(); c++){ // row
                if (c > 0)  // If this is not the first column in this row
                    str += " ";
                    // Get the tile at the current row and column, or null if 
                    // no tile is present
                Tile tile = (contains(r,c)) ? getTile(r,c) : null;
                    // If the tile is non-null, add a String stating its state.
                    // Otherwise, put 2 'X's in the brackets to denote a null 
                str += (tile!=null)?String.format("[%02X]", // tile.
                        tile.getState()):"[XX]";
            }
        }
        return str;
    }
    /**
     * {@inheritDoc } <p>
     * 
     * The returned iterator is fail-fast. That is to say, if the structure of
     * this model changes at any time after the iterator is created, then the 
     * iterator will throw a {@link ConcurrentModificationException 
     * ConcurrentModificationException}.
     * 
     * @return {@inheritDoc }
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #getTile 
     */
    @Override
    public Iterator<Tile> iterator() {
        return new PlayFieldIterator();
    }
    /**
     * This is the Iterator implementation returned by {@link #iterator()}. 
     * This goes through the tiles within the current range, as dictated by 
     * {@link #getRowCount()} and {@link #getColumnCount()}. If the number of 
     * rows and/or columns is changed after this is created, then this will 
     * throw a {@link ConcurrentModificationException}.
     * @see #iterator 
     * @see #getRowCount 
     * @see #getColumnCount 
     * @see #getTile 
     * @see #getTileCount 
     */
    private class PlayFieldIterator implements Iterator<Tile>{
        /**
         * This contains the index of the next tile to return. Since the tiles 
         * are stored in a table, this is divided by the number of rows to get 
         * the row of the tile to return, and is modulus divided by the number 
         * of columns to get the column of the tile to return.
         */
        private int index;
        /**
         * This stores the number of rows that were being displayed when this 
         * iterator was constructed.
         */
        private final int rows;
        /**
         * This stores the number of columns that were being displayed when this 
         * iterator was constructed.
         */
        private final int columns;
        /**
         * This constructs a PlayFieldIterator.
         */
        PlayFieldIterator(){
            index = 0;
            rows = getRowCount();
            columns = getColumnCount();
        }
        @Override
        public boolean hasNext() {
            return index < getTileCount();
        }
        @Override
        public Tile next() {
                // Check for concurrent modification
            checkForConcurrentModification();   
            if (!hasNext()) // If there are no more tiles to return
                throw new NoSuchElementException();
                // This gets the tile to return
            Tile tile = getTile(Math.floorDiv(index,columns),index%columns);
            index++;
            return tile;
        }
        /**
         * This checks to see if the model has been modified since this iterator 
         * was constructed, and if so, throws a {@code 
         * ConcurrentModificationException}.
         * @throws ConcurrentModificationException If the model was modified 
         * since this iterator was constructed.
         */
        protected void checkForConcurrentModification(){
                // If the amount of rows or columns has changed since this 
                // iterator was created
            if (rows != getRowCount() || columns != getColumnCount())
                throw new ConcurrentModificationException();
        }
    }
       /**
     * This is a class that represents a flag used to control the firing of 
     * events, along with the region of tiles that were possibly affected while 
     * this flag was set. 
     */
    private class EventFlag{
        /**
         * This stores the state of the flag.
         */
        private boolean value;
        /**
         * These are the rows and columns that encompass the region of tiles 
         * that were affected. {@code r0} is the first row, {@code r1} is the 
         * last row, {@code c0} is the first column, and {@code c1} is the last 
         * column.
         */
        private int r0, r1, c0, c1;
        /**
         * This constructs an EventFlag with the given initial value.
         * @param value The initial state of the flag.
         */
        EventFlag(boolean value){
            this.value = value;
            resetRange();
        }
        /**
         * This constructs an EventFlag that is initially set to false.
         */
        EventFlag(){
            this(false);
        }
        /**
         * This resets the region of tiles that were affected while this flag is 
         * set.
         */
        private void resetRange(){
            r0 = c0 = Integer.MAX_VALUE;
            r1 = c1 = -1;
        }
        /**
         * This returns the state of the flag.
         * @return The state of the flag.
         */
        public boolean getValue(){
            return value;
        }
        /**
         * This sets the state of the flag to the given value. If the given 
         * event type is not null and tiles were affected, then a PlayFieldEvent 
         * will be fired with the given event type and encompassing the region 
         * of tiles that were affected while this flag was set. This does 
         * nothing if the flag would not change.
         * @param value The new state of the flag.
         * @param eventType The type of event to fire, or null if no event is to 
         * be fired.
         */
        public void setValue(boolean value, Integer eventType){
            if (this.value == value)    // If no change will occur
                return;
            this.value = value;
                // If the event type is not null and tiles were affected
            if (eventType != null && getTilesWereAffected())
                firePlayFieldChange(r0,r1,c0,c1,eventType);
        }
        /**
         * The first row that was affected while this flag was set, or the 
         * integer maximum if the flag is not set or no tiles have been 
         * affected.
         * @return The first row that was affected.
         */
        public int getRow0(){
            return r0;
        }
        /**
         * The last row that was affected while this flag was set, or -1 if the 
         * flag is not set or no tiles have been affected.
         * @return The last row that was affected.
         */
        public int getRow1(){
            return r1;
        }
        /**
         * The first column that was affected while this flag was set, or the 
         * integer maximum if the flag is not set or no tiles have been 
         * affected.
         * @return The first column that was affected.
         */
        public int getColumn0(){
            return c0;
        }
        /**
         * The last column that was affected while this flag was set, or -1 if 
         * the flag is not set or no tiles have been affected.
         * @return The last column that was affected.
         */
        public int getColumn1(){
            return c1;
        }
        /**
         * This returns whether tiles have been affected since the flag was set.
         * @return Whether tiles were affected since setting this flag.
         */
        public boolean getTilesWereAffected(){
            return r1 > -1 && c1 > -1;
        }
        /**
         * This processes the event to be fired, adding the tile(s) to the 
         * region of tiles that have been changed if the {@link #getValue() flag 
         * is set} and clearing it if it's not.
         * @param evt The PlayFieldEvent to be fired.
         * @return Whether the flag is set.
         * @see #resetRange 
         * @see #getValue 
         */
        public boolean processEvent(PlayFieldEvent evt){
            if (value){                     // If the flag is set
                    // Try to include the tile(s) that triggered the event into 
                    // the current range
                r0 = Math.min(r0, evt.getFirstRow());
                r1 = Math.max(r1, evt.getLastRow());
                c0 = Math.min(c0, evt.getFirstColumn());
                c1 = Math.max(c1, evt.getLastColumn());
            }
            else if (getTilesWereAffected()){// If the range has not been reset yet
                resetRange();
            }
            return value;
        }
    }
}
