/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.event;

import java.util.EventObject;
import java.util.Objects;
import snake.*;
import snake.playfield.*;

/**
 * This is an event that indicates that a {@link Snake snake} has performed an 
 * action or had its state changed. <p>
 * 
 * The {@code id} parameter is used to indicate the type of event that occurred. 
 * An {@code id} that is out of the range from {@link #SNAKE_FIRST} to {@link 
 * #SNAKE_LAST}, inclusive, may result in an unspecified behavior. The {@code 
 * direction} parameter stores the direction flag(s) for the event. The {@code 
 * target} parameter stores the tile that was or would have been affected. This 
 * could be the tile that was added, removed, moved to, or that the snake failed 
 * to add or move to. The {@code target} may be null if no tile was affected.
 * 
 * @author Milo Steier
 * @see Snake
 * @see SnakeListener
 */
public class SnakeEvent extends EventObject implements SnakeConstants{
    /**
     * The first number in the range of IDs used for snake events.
     */
    public static final int SNAKE_FIRST = 0x00;
    /**
     * The ID for the event that indicates that a snake failed to perform an 
     * action. 
     */
    public static final int SNAKE_FAILED = 0x00;
    /**
     * The ID for the event that indicates that a tile was added to a snake.
     */
    public static final int SNAKE_ADDED_TILE = 0x01;
    /**
     * The ID for the event that indicates that a snake moved to a tile.
     */
    public static final int SNAKE_MOVED = 0x02;
    /**
     * The ID for the event that indicates that a tile was removed from a snake.
     */
    public static final int SNAKE_REMOVED_TILE = 0x03;
    /**
     * The ID for the event that indicates that a snake consumed an apple.
     */
    public static final int SNAKE_CONSUMED_APPLE = 0x04;
    /**
     * The ID for the event that indicates that a snake has been flipped.
     */
    public static final int SNAKE_FLIPPED = 0x05;
    /**
     * The ID for the event that indicates that a snake had crashed into 
     * something.
     */
    public static final int SNAKE_CRASHED = 0x06;
    /**
     * The ID for the event that indicates that a snake has been revived.
     */
    public static final int SNAKE_REVIVED = 0x07;
    /**
     * The ID for the event that indicates that a snake has been initialized.
     */
    public static final int SNAKE_INITIALIZED = 0x08;
    /**
     * The ID for the event that indicates that a snake has been reset.
     */
    public static final int SNAKE_RESET = 0x09;
    /**
     * The last number in the range of IDs used for snake events.
     */
    public static final int SNAKE_LAST = SNAKE_RESET;
    /**
     * This stores event ID for this event.
     */
    private int id;
    /**
     * This stores the direction(s) for the event.
     */
    private int dir;
    /**
     * This stores the timestamp for when this event occurred.
     */
    private long when;
    /**
     * This stores the tile that was the target for the snake.
     */
    private Tile target;
    /**
     * This constructs a SnakeEvent with the given source, event ID, direction 
     * flag(s), target tile, and timestamp.
     * @param source The snake that originated the event.
     * @param id An integer indicating the type of event. This should be between 
     * {@link SNAKE_FIRST} and {@link SNAKE_LAST}.
     * @param direction The direction flag(s) for the event. This should be a 
     * value between 0 and {@link #ALL_DIRECTIONS}.
     * @param target The target tile for the event, or null. This should be the 
     * tile that was or would have been affected by the snake, such as the tile 
     * that was added or removed. If no tile was affected, then this should be 
     * null.
     * @param when The timestamp for when the event occurred. It's recommended 
     * that this should be a positive non-zero value.
     * @throws IllegalArgumentException If the source Snake is null.
     * @see #getSnake 
     * @see #getID 
     * @see #getDirection 
     * @see #getTarget 
     * @see #getWhen 
     */
    public SnakeEvent(Snake source,int id,int direction,Tile target,long when){
        super(source);
        this.id = id;
        this.dir = SnakeUtilities.getDirections(direction);
        this.target = target;
        this.when = when;
    }
    /**
     * This constructs a SnakeEvent with the given source, event ID, direction 
     * flag(s), and target tile. The timestamp will be derived from the {@link 
     * System#currentTimeMillis() current system time}.
     * @param source The snake that originated the event.
     * @param id An integer indicating the type of event. This should be between 
     * {@link SNAKE_FIRST} and {@link SNAKE_LAST}.
     * @param direction The direction flag(s) for the event. This should be a 
     * value between 0 and {@link #ALL_DIRECTIONS}.
     * @param target The target tile for the event, or null. This should be the 
     * tile that was or would have been affected by the snake, such as the tile 
     * that was added or removed. If no tile was affected, then this should be 
     * null.
     * @throws IllegalArgumentException If the source Snake is null.
     * @see #getSnake 
     * @see #getDirection 
     * @see #getID 
     * @see #getTarget 
     */
    public SnakeEvent(Snake source, int id, int direction, Tile target){
        this(source,id,direction,target,System.currentTimeMillis());
    }
    /**
     * This constructs a SnakeEvent with the given source, event ID, direction 
     * flag(s), and timestamp. The target tile will be null.
     * @param source The snake that originated the event.
     * @param id An integer indicating the type of event. This should be between 
     * {@link SNAKE_FIRST} and {@link SNAKE_LAST}.
     * @param direction The direction flag(s) for the event. This should be a 
     * value between 0 and {@link #ALL_DIRECTIONS}.
     * @param when The timestamp for when the event occurred. It's recommended 
     * that this should be a positive non-zero value.
     * @throws IllegalArgumentException If the source Snake is null.
     * @see #getSnake 
     * @see #getID 
     * @see #getDirection 
     * @see #getWhen 
     */
    public SnakeEvent(Snake source, int id, int direction, long when){
        this(source,id,direction,null,when);
    }
    /**
     * This constructs a SnakeEvent with the given source, event ID, and 
     * direction flag(s). The timestamp will be derived from the {@link 
     * System#currentTimeMillis() current system time}. The target tile will be 
     * null.
     * @param source The snake that originated the event.
     * @param id An integer indicating the type of event. This should be between 
     * {@link SNAKE_FIRST} and {@link SNAKE_LAST}.
     * @param direction The direction flag(s) for the event. This should be a 
     * value between 0 and {@link #ALL_DIRECTIONS}.
     * @throws IllegalArgumentException If the source Snake is null.
     * @see #getSnake 
     * @see #getDirection 
     * @see #getID 
     */
    public SnakeEvent(Snake source, int id, int direction){
        this(source,id,direction,null);
    }
    /**
     * This returns the snake that originated this event.
     * @return The snake that originated the event, or null if the object is 
     * not a snake.
     * @see #getID 
     * @see #getDirection 
     * @see #getTarget 
     * @see #getWhen 
     */
    public Snake getSnake(){
            // If the source is a snake, return it. Otherwise, return null.
        return (source instanceof Snake) ? (Snake)source: null;
    }
    /**
     * This returns the direction provided when this event was created.
     * @return The direction flag(s) provided when this event was created.
     * @see #getSnake 
     * @see #getID 
     * @see #getTarget 
     * @see #getWhen 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     */
    public int getDirection(){
        return dir;
    }
    /**
     * This returns the ID for this event's type.
     * @return The ID for this event's type.
     * @see #SNAKE_FAILED
     * @see #SNAKE_ADDED_TILE
     * @see #SNAKE_MOVED
     * @see #SNAKE_REMOVED_TILE
     * @see #SNAKE_CONSUMED_APPLE
     * @see #SNAKE_FLIPPED
     * @see #SNAKE_CRASHED
     * @see #SNAKE_REVIVED
     * @see #SNAKE_INITIALIZED
     * @see #SNAKE_RESET
     * @see #getSnake 
     * @see #getDirection 
     * @see #getTarget 
     * @see #getWhen 
     */
    public int getID(){
        return id;
    }
    /**
     * This returns the tile that was the snake's target when this event 
     * occurred. This is the tile that was or would have been affected by the 
     * event. If no tile was affected or provided, then this will return null.
     * @return The target tile for this event, or null.
     * @see #getSnake 
     * @see #getDirection 
     * @see #getID 
     * @see #getWhen 
     */
    public Tile getTarget(){
        return target;
    }
    /**
     * This returns the timestamp for when this event occurred.
     * @return The timestamp for this event.
     * @see #getSnake 
     * @see #getDirection 
     * @see #getID 
     * @see #getTarget 
     */
    public long getWhen(){
        return when;
    }
    /**
     * This returns the String that identifies the event type.
     * @return The String corresponding to the event type, or null if the event 
     * is of an unknown type.
     * @see #paramString 
     */
    protected String eventString(){
        switch(getID()){
            case(SNAKE_FAILED):         // If the snake failed to do an action
                return "SNAKE_FAILED";
            case(SNAKE_ADDED_TILE):     // If the snake was added to
                return "SNAKE_ADDED_TILE";
            case(SNAKE_MOVED):          // If the snake moved
                return "SNAKE_MOVED";
            case(SNAKE_REMOVED_TILE):   // If the snake was removed from
                return "SNAKE_REMOVED_TILE";
             case(SNAKE_CONSUMED_APPLE):// If the snake consumed an apple
                return "SNAKE_CONSUMED_APPLE";
            case(SNAKE_CRASHED):        // If the snake crashed into something
                return "SNAKE_CRASHED";
            case(SNAKE_FLIPPED):        // If the snake has been flipped
                return "SNAKE_FLIPPED";
            case(SNAKE_REVIVED):        // If the snake has been revived after crashing
                return "SNAKE_REVIVED";
            case(SNAKE_INITIALIZED):    // If the snake has been initialized
                return "SNAKE_INITIALIZED";
            case(SNAKE_RESET):          // If the snake has been reset
                return "SNAKE_RESET";
            default:
                return null;
        }
    }
    /**
     * This returns a parameter String that identifies this event. This is 
     * useful for event-logging and debugging.
     * @return A String identifying this event.
     */
    public String paramString(){
        return Objects.toString(eventString(), "unknown type ("+getID()+")")+
                ","+SnakeUtilities.getDirectionString(getDirection())+
                ",target="+Objects.toString(getTarget(), "")+
                ",when="+getWhen();
    }
    /**
     * This returns a String representation of this SnakeEvent.
     * @return A String representation of this SnakeEvent.
     */
    @Override
    public String toString(){
        Snake snake = getSnake();       // Get the source snake
            // Get the name of the snake if there is one
        String name = (snake != null) ? snake.getName() : null;
        return getClass().getName() + "["+paramString()+"] on " + 
                Objects.toString(name, Objects.toString(source));
    }
}
