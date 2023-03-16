/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.*;
import javax.swing.event.*;
import snake.action.*;
import snake.event.*;
import snake.playfield.*;

/**
 * This is a group of {@link Tile tiles} used to represent a snake in the game 
 * Snake. A snake is displayed on and moves around on a play field represented 
 * by a {@link PlayFieldModel PlayFieldModel}. When a snake is first constructed 
 * or when the {@code PlayFieldModel} for a snake changes, then the snake will 
 * need to be {@link #initialize(Tile) initialized} with a tile from the {@code 
 * PlayFieldModel} before it can be used. When a snake is initialized, the tile 
 * provided to it will be used as the snake's {@link #getHead head}. Once a 
 * snake has been initialized, it will be in a {@link #isValid() valid} state. 
 * For a snake to be in a valid state, the snake must not be {@link #isEmpty() 
 * empty}, its head must be a non-null tile {@link PlayFieldModel#contains(Tile) 
 * contained} within its {@code PlayFieldModel}, and the snake must be facing a 
 * single direction. A snake must be in a valid state for most of the snake's 
 * methods to work. If a method requires the snake to be in a valid state, then 
 * the method will typically throw an {@link IllegalStateException 
 * IllegalStateException} when the calling snake is not in a valid state. <p>
 * 
 * Snakes are treated as and are internally represented by a queue of tiles, 
 * with the {@link #getHead() head} and the {@link #getTail() tail} of the snake 
 * corresponding to either end of the queue. Technically speaking, the head of 
 * the snake is treated as the tail of the queue and the tail of the snake is 
 * treated as the head of the queue. When a tile is added to the snake, it 
 * becomes the new head of the snake, and when a tile is removed from the snake, 
 * the tile removed will be the snake's tail. If a snake is less than two tiles 
 * long, then the snake will not have a tail. In other words, the snake's {@link 
 * #getTail() getTail} method will return null. When this is the case, no tiles 
 * can be removed from the snake. When a snake is {@link #flip() flipped}, the 
 * order of the tiles in the snake will be reversed. This will result in the 
 * snake's head becoming the snake's tail and vice versa. <p>
 * 
 * Some of the operations provided by snakes are direction based. These methods 
 * are primarily involved with adding tiles to and moving a snake. Each of these 
 * methods comes in two forms: one that takes in an integer representing a 
 * direction, and the other uses the direction the snake is {@link 
 * #getDirectionFaced() facing}. The former form will typically require the 
 * value for the direction to be either zero or one of the four direction flags: 
 * {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and 
 * {@link #RIGHT_DIRECTION}. These methods will typically interpret a direction 
 * of zero as the direction that the snake is facing. In other words, these 
 * methods will substitute a direction of zero with the direction returned by 
 * the {@link #getDirectionFaced() getDirectionFaced} method. The latter form of 
 * these methods will typically invoke their respective former form with the 
 * direction the snake is facing. In other words, the latter form of the methods 
 * are equivalent to calling their respective former form with the direction 
 * returned by the {@code getDirectionFaced} method. A summary of the direction 
 * based methods can be found in the table below:
 * 
 * <table class="striped">
 * <caption>Summary of direction based Snake methods</caption>
 * <thead>
 *  <tr>
 *      <td></td>
 *          <th scope="col" style="font-weight:normal; font-style:italic">
 *              Uses given direction</th>
 *          <th scope="col" style="font-weight:normal; font-style:italic">
 *              Uses {@link #getDirectionFaced() direction faced}</th>
 *  </tr>
 *  </thead>
 *  <tbody>
 *      <tr>
 *          <th scope="row">Get adjacent tile</th>
 *          <td>{@link #getAdjacentToHead(int) getAdjacentToHead(int)}</td>
 *          <td>{@link #getTileBeingFaced() getTileBeingFaced()}</td>
 *      </tr>
 *      <tr>
 *          <th scope="row">Can move/add tile</th>
 *          <td>{@link #canMoveInDirection(int) canMoveInDirection(int)}</td>
 *          <td>{@link #canMoveForward() canMoveForward()}</td>
 *      </tr>
 *      <tr>
 *          <th scope="row">Add tile</th>
 *          <td>{@link #add(int) add(int)}</td>
 *          <td>{@link #add() add()}</td>
 *      </tr>
 *      <tr>
 *          <th scope="row">Move snake</th>
 *          <td>{@link #move(int) move(int)}</td>
 *          <td>{@link #move() move()}</td>
 *      </tr>
 *  </tbody>
 * </table>
 * <p>
 * 
 * The {@link #add(int) add(int)} and {@link #add() add()} methods are used to 
 * add tiles that are adjacent to a snake's head, and will return whether the 
 * tile was successfully added to the snake. The {@link #move(int) move(int)} 
 * and {@link #move() move()} methods are used to move a snake, and will return 
 * whether the snake was successfully moved. The main difference between adding 
 * tiles to a snake and moving a snake is that when a snake moves, its current 
 * tail is removed so as to maintain the snake's length after adding a tile to 
 * the snake. <p>
 * 
 * The {@link #getAdjacentToHead getAdjacentToHead} and {@link 
 * #getTileBeingFaced getTileBeingFaced} methods are used to provide the tile to 
 * be added to the snake from the {@code PlayFieldModel} by using the model's 
 * {@link PlayFieldModel#getAdjacentTile getAdjacentTile} method to get a tile 
 * adjacent to the snake's head. A snake that is configured to {@link 
 * #isWrapAroundEnabled() wrap around} will be able to get tiles from the other 
 * side of the play field when the adjacent tile would otherwise be out of 
 * bounds. If the snake is not configured to wrap around, then attempting to get 
 * an out of bounds adjacent tile would return null. As such, when a snake 
 * attempts to add or move to a tile that would be out of bounds, it will either 
 * wrap around and use a tile from the other side of the play field or the snake 
 * will fail to add or move to a tile, depending on whether the snake is 
 * configured to wrap around. <p>
 * 
 * Snakes can only add or move to a tile if it is a non-null tile that is either 
 * {@link Tile#isEmpty() empty} or an {@link Tile#isApple() apple tile}. Snakes 
 * are only able to add or move to apple tiles if they can {@link 
 * #isAppleConsumptionEnabled eat apples}. If a snake cannot eat apples, then 
 * the snake will only be able to add or move to empty tiles. When a snake adds 
 * or moves to an apple tile, the snake will fire a {@code SnakeEvent} 
 * indicating that the {@link SnakeEvent#SNAKE_CONSUMED_APPLE snake ate an 
 * apple} and the snake's {@link #hasConsumedApple hasConsumedApple} method will 
 * return {@code true}. The {@link #canMoveInDirection canMoveInDirection} and 
 * {@link #canMoveForward canMoveForward} methods can be used to check to see if 
 * a snake can add or move in a given direction based off the tile the snake 
 * would be attempting to add or move to. Additionally, a snake can only add or 
 * move to tiles if it has not {@link #isCrashed() crashed}. A snake will crash 
 * if it has {@link #getFailCount() repeatedly failed} to add or move to a tile 
 * more times than it is {@link #getAllowedFails() allowed to}. This does not 
 * include any attempt to add or move a snake backwards when it has a tail (i.e. 
 * when the snake is at least two tiles long, or when {@code getTail} returns a 
 * non-null tile). When the allowed number of failures is set to a negative 
 * number, then the snake cannot crash. A crashed snake cannot be moved or added 
 * to until it has been {@link #revive() revived}. <p>
 * 
 * The {@link #removeTail() removeTail} method will remove and return the 
 * snake's tail. If the snake does not have a tail, then the {@code removeTail} 
 * method will do nothing and return null.<p>
 * 
 * A snake's {@link #getPlayerType() player type} indicates what {@link 
 * Tile#getType() type} of {@link Tile#isSnake() snake tiles} a snake will be 
 * comprised of and whether a snake represents player one (i.e. a primary snake) 
 * or player two (i.e. a secondary snake). A snake does not necessarily need to 
 * represent an actual player, and multiple snakes may use the same player type. 
 * When a tile is added to a snake, the tile will have its type flag set to the 
 * snake's player type. <p>
 * 
 * Snakes have an {@link #getActionQueue() action queue} that can be used to 
 * store {@code Consumer}s to be performed later. The {@link 
 * #offerAction(Consumer) offerAction(Consumer)} method is used to add {@code 
 * Consumer}s to the action queue, and any non-null {@code Consumer} can be 
 * added as long as it accepts {@code Snake}s as an argument. Additionally, 
 * {@link SnakeCommand SnakeCommands} can also be added to the action queue 
 * using the {@link #offerAction(SnakeCommand) offerAction(SnakeCommand)} 
 * method, which will add a {@link SnakeActionCommand SnakeActionCommand} to the 
 * action queue which will cause the snake to perform the action associated with 
 * the command. When a snake's {@link #doNextAction doNextAction} method is 
 * invoked, it will first check to see if the snake has any {@code Consumer}s in 
 * its action queue. If there are any, then the {@code doNextAction} method will 
 * check to see whether the action should be performed or skipped (i.e. 
 * discarded). Refer to the documentation for the {@link #doNextAction 
 * doNextAction} method for information on how it decides which actions to 
 * perform and which actions to skip. If either the action queue is {@link 
 * #isActionQueueEmpty() empty} or all the actions in the action queue have been 
 * skipped, then the {@code doNextAction} will perform the snake's default 
 * action. <p>
 * 
 * A snake's {@link #getDefaultAction() default action} is a {@code Consumer} 
 * that the snake can invoke to perform some action. When the default action is 
 * set to a non-null value and is {@link #isDefaultActionEnabled() enabled}, the 
 * {@link #doDefaultAction doDefaultAction} method can be used to have the snake 
 * perform it's default action. If the default action is either null or 
 * disabled, then {@code doDefaultAction} will do nothing. The default action 
 * can be set either with a {@code Consumer} or with a {@code SnakeCommand}, 
 * the latter of which will result in the default action being set to a {@code 
 * SnakeActionCommand} that will perform the command. The {@code doNextAction} 
 * method will invoke the {@code doDefaultAction} method when it has run out of 
 * actions from the action queue to perform. <p>
 * 
 * The iterators returned by this class's {@code iterator} method are fail-fast,
 * i.e. if the snake is structurally modified in any way at any time after the 
 * iterator is created, such as adding or removing tiles from the snake or 
 * flipping the snake, the iterator will throw a {@link 
 * ConcurrentModificationException ConcurrentModificationException}. This way, 
 * when faced with concurrent modification, the iterator will fail quickly and 
 * cleanly instead of risking arbitrary, non-deterministic behavior. However, 
 * the fail-fast behavior of the iterator cannot be guaranteed, especially when 
 * dealing with unsynchronized concurrent modifications. The fail-fast iterators 
 * throw {@code ConcurrentModificationExceptions} on a best-effort basis. As 
 * such the fail-fast behavior should not be depended on for its correctness and 
 * should only be used to detect bugs.
 * 
 * @author Milo Steier
 * @see Tile
 * @see PlayFieldModel
 * @see AbstractPlayFieldModel
 * @see DefaultPlayFieldModel
 * @see JPlayField
 * @see SnakeCommand
 * @see SnakeActionCommand
 * @see DefaultSnakeActionCommand
 */
public class Snake extends AbstractQueue<Tile> implements SnakeConstants{
    /**
     * This is the flag used to indicate that a snake has consumed an apple.
     */
    protected static final int APPLE_CONSUMED_FLAG = 0x001;
    /**
     * This is the flag used to set whether a snake can eat apples.
     */
    protected static final int APPLE_CONSUMPTION_ENABLED_FLAG = 0x002;
    /**
     * This is the flag used to set whether eating an apple will cause a snake 
     * to grow in length.
     */
    protected static final int APPLE_GROWTH_ENABLED_FLAG = 0x004;
    /**
     * This is the flag used to set whether a snake will wrap around to the 
     * other side of the play field when it reaches an edge. When not set, a 
     * snake will fail to add or move when reaching the edge of the play field 
     * instead of wrapping around to the other side.
     */
    protected static final int WRAP_AROUND_ENABLED_FLAG = 0x008;
    /**
     * This is the flag used to set which player a snake represents. This does 
     * not necessarily have to be an actual player, since it could be used for 
     * a non-player controlled snake. This is primarily used to indicate what 
     * {@link Tile#getType() type} of {@link Tile#isSnake() snake tiles} 
     * should a snake be composed of.
     * @see SnakeConstants#ALTERNATE_TYPE_FLAG
     * @see Tile#isSnake()
     * @see Tile#getType()
     */
    protected static final int PLAYER_TYPE_FLAG = SnakeConstants.ALTERNATE_TYPE_FLAG;
    /**
     * This is the flag used to indicate whether a snake is flipped around.
     */
    protected static final int FLIPPED_FLAG = 0x020;
    /**
     * This is the flag used to indicate that a snake has crashed. A snake has 
     * crashed when it has failed to add or move more than the allotted amount 
     * of failures for that snake.
     */
    protected static final int CRASHED_FLAG = 0x040;
    /**
     * This is the flag used to set whether the default action of a snake is 
     * enabled.
     */
    protected static final int DEFAULT_ACTION_ENABLED_FLAG = 0x080;
    /**
     * This is the flag used to set whether the snake will skip any consecutive 
     * repeats of an action in the action queue. 
     */
    protected static final int SKIPS_REPEATED_ACTIONS_FLAG = 0x100;
    /**
     * This stores the flags that are set initially when a snake is constructed.
     */
    private static final int DEFAULT_FLAG_SETTINGS = 
            APPLE_CONSUMPTION_ENABLED_FLAG | APPLE_GROWTH_ENABLED_FLAG | 
            WRAP_AROUND_ENABLED_FLAG | DEFAULT_ACTION_ENABLED_FLAG | 
            SKIPS_REPEATED_ACTIONS_FLAG;
    /**
     * This stores the flags that are cleared when a snake is reset.
     */
    private static final int RESET_AFFECTED_FLAGS = APPLE_CONSUMED_FLAG | 
            FLIPPED_FLAG | CRASHED_FLAG;
    /**
     * This identifies the play field model for a snake has changed.
     */
    public static final String MODEL_PROPERTY_CHANGED = 
            "SnakePlayFieldModelPropertyChanged";
    /**
     * This identifies that the name of a snake has changed.
     */
    public static final String NAME_PROPERTY_CHANGED = 
            "SnakeNamePropertyChanged";
    /**
     * This identifies that a change has been made to a snake's ability to wrap 
     * around when it reaches the edges of a play field.
     */
    public static final String WRAP_AROUND_ENABLED_PROPERTY_CHANGED = 
            "SnakeWrapAroundEnabledPropertyChanged";
    /**
     * This identifies that a snake's ability to eat apples has changed.
     */
    public static final String APPLE_CONSUMPTION_ENABLED_PROPERTY_CHANGED = 
            "SnakeAppleConsumptionEnabledPropertyChanged";
    /**
     * This identifies that a change has been made to whether a snake will grow 
     * in length when it eats an apple.
     */
    public static final String APPLES_CAUSE_GROWTH_PROPERTY_CHANGED = 
            "SnakeAppleGrowthEnabledPropertyChanged";
    /**
     * This identifies that a change has been made to the flag controlling which 
     * player a snake belongs to. That is to say, this is a change to the flag 
     * controlling which {@link Tile#getType() type} of {@link Tile#isSnake() 
     * snake} {@link Tile tiles} a snake is composed of.
     */
    public static final String PLAYER_TYPE_PROPERTY_CHANGED = 
            "SnakePlayerTypePropertyChanged";
    /**
     * This identifies that a snake's default action has been changed.
     */
    public static final String DEFAULT_ACTION_PROPERTY_CHANGED = 
            "SnakeDefaultActionPropertyChanged";
    /**
     * This identifies that a snake's default action has been enabled or 
     * disabled.
     */
    public static final String DEFAULT_ACTION_ENABLED_PROPERTY_CHANGED = 
            "SnakeDefaultActionEnabledPropertyChanged";
    /**
     * This identifies that a change has been made to the number of failures 
     * that have to occur when attempting to move or add before a snake declares 
     * itself to have crashed.
     */
    public static final String ALLOWED_FAILS_PROPERTY_CHANGED = 
            "SnakeAllowedFailsPropertyChanged";
    /**
     * This identifies that a change has been made to whether a snake will skip 
     * adjacent repeated actions when performing actions from its action queue.
     */
    public static final String SKIPS_REPEATED_ACTIONS_PROPERTY_CHANGED = 
            "SnakeSkipsRepeatedActionsPropertyChanged";
    /**
     * This is an EventListenerList to store the listeners for this class.
     */
    protected EventListenerList listenerList  = new EventListenerList();
    /**
     * This is the PropertyChangeSupport used to handle changes to the 
     * properties of this snake.
     */
    private PropertyChangeSupport changeSupport;
    /**
     * This is the queue used to store the tiles that comprise the body of this 
     * snake. Snakes act as queues of tiles, with the front of the queue being 
     * the tail, and the end of the queue being the head. Snakes take advantage 
     * of this being a double ended queue by switching the front and end of the 
     * queue when a snake is "{@link #isFlipped() flipped}".
     */
    protected ArrayDeque<Tile> snakeBody = new ArrayDeque<>();
    /**
     * This stores the model for the play field that this snake gets its tiles 
     * from.
     */
    private PlayFieldModel model;
    /**
     * This stores the flags used to store the settings and state of this snake.
     */
    private int flags;
    /**
     * This stores the name for this snake.
     */
    private String name;
    /**
     * This stores the amount of times this snake has sequentially failed in a 
     * way that can cause it to crash. This gets reset whenever the snake does 
     * not fail.
     */
    private int failCount = 0;
    /**
     * This stores the number of sequential failures that this snake allows 
     * before crashing. If this negative, then a snake cannot crash.
     */
    private int allowedFails;
    /**
     * This stores the default action for this snake. This is the action to 
     * perform on this snake when the action queue is empty.
     */
    private Consumer<Snake> defaultAction = null;
    /**
     * This is a queue storing actions for the snake to perform. This is 
     * initially null, and will be initialized when it is first requested.
     */
    private SnakeActionQueue actionQueue = null;
    /**
     * This constructs a Snake that will be displayed on the given model. The 
     * snake will be able to wrap around and eat apples and will grow when it 
     * does. The snake will also represent the first player (i.e. this will be a 
     * primary snake) and can fail an unlimited amount of times without 
     * crashing. The snake's default action will be enabled and will move the 
     * snake forward. The snake will need to be {@link #initialize initialized} 
     * with an {@link Tile#isEmpty() empty} tile from the model before it can be 
     * used.
     * @param model The PlayFieldModel that provides the tiles for the snake 
     * (cannot be null). 
     * @throws NullPointerException If the model is null.
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     */
    public Snake(PlayFieldModel model){
        flags = DEFAULT_FLAG_SETTINGS;
        name = null;
        allowedFails = -1;
        Snake.this.setModel(model);
        Snake.this.setDefaultAction(SnakeCommand.MOVE_FORWARD);
        changeSupport = new PropertyChangeSupport(this);
    }
    /**
     * This constructs and {@link #initialize(Tile) initializes} a snake that 
     * will be displayed on the given model and with the given tile for the 
     * head. The snake will be able to wrap around and eat apples and will grow 
     * when it does. The snake will also represent the first player (i.e. this 
     * will be a primary snake) and can fail an unlimited amount of times 
     * without crashing. The snake's default action will be enabled and will 
     * move the snake forward. The snake will be facing left. <p>
     * 
     * This is equivalent to {@link Snake#Snake(PlayFieldModel) constructing} a 
     * snake without providing a tile for the head and then {@link 
     * #initialize(Tile) initializing} that snake. 
     * 
     * @param model The PlayFieldModel that provides the tiles for the snake 
     * (cannot be null). 
     * @param head The head for the snake. This must be a non-null, {@link 
     * Tile#isEmpty() empty} tile from the given model. 
     * @throws NullPointerException If either the model or the head are null.
     * @throws IllegalArgumentException If the head is either not in the given 
     * model or is not empty.
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see Tile#isEmpty()
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     */
    public Snake(PlayFieldModel model, Tile head){
        this(model);
        Snake.this.initialize(head);
    }
    /**
     * This constructs and {@link #initialize(int, int) initializes} a snake 
     * that will be displayed on the given model and with the tile at the given 
     * row and column in the model for the head. The snake will be able to wrap 
     * around and eat apples and will grow when it does. The snake will also 
     * represent the first player (i.e. this will be a primary snake) and can 
     * fail an unlimited amount of times without crashing. The snake's default 
     * action will be enabled and will move the snake forward. The snake will be 
     * facing left. <p>
     * 
     * This is equivalent to {@link Snake#Snake(PlayFieldModel) constructing} a 
     * snake without providing a tile for the head and then {@link 
     * #initialize(int, int) initializing} that snake. 
     * 
     * @param model The PlayFieldModel that provides the tiles for the snake 
     * (cannot be null). 
     * @param row The row in the model for the tile to use as the head of the 
     * snake.
     * @param column The column in the model for the tile to use as the head of 
     * the snake.
     * @throws NullPointerException If the model is null.
     * @throws IndexOutOfBoundsException If either the row or column are out of 
     * bounds for the given model.
     * @throws IllegalArgumentException If the tile at the given row and column 
     * is not empty.
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see Tile#isEmpty()
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     */
    public Snake(PlayFieldModel model, int row, int column){
        this(model);
        Snake.this.initialize(row, column);
    }
    
    protected SnakeActionQueue createActionQueue(){
        return new SnakeActionQueue(this);
    }
    /**
     * This returns an integer storing the flags used to store the settings for 
     * this snake and control its state.
     * @return An integer containing the flags for this snake.
     * @see #getFlag
     * @see #setFlag
     * @see #toggleFlag
     * @see #APPLE_CONSUMED_FLAG
     * @see #APPLE_CONSUMPTION_ENABLED_FLAG
     * @see #APPLE_GROWTH_ENABLED_FLAG
     * @see #WRAP_AROUND_ENABLED_FLAG
     * @see #PLAYER_TYPE_FLAG
     * @see #FLIPPED_FLAG
     * @see #CRASHED_FLAG
     * @see #DEFAULT_ACTION_ENABLED_FLAG
     */
    protected int getFlags(){
        return flags;
    }
    /**
     * This gets whether the given flag is set for this snake.
     * @param flag The flag to check for.
     * @return Whether the flag is set.
     * @see #getFlags
     * @see #setFlag
     * @see #toggleFlag
     * @see SnakeUtilities#getFlag 
     * @see #APPLE_CONSUMED_FLAG
     * @see #APPLE_CONSUMPTION_ENABLED_FLAG
     * @see #APPLE_GROWTH_ENABLED_FLAG
     * @see #WRAP_AROUND_ENABLED_FLAG
     * @see #PLAYER_TYPE_FLAG
     * @see #FLIPPED_FLAG
     * @see #CRASHED_FLAG
     * @see #DEFAULT_ACTION_ENABLED_FLAG
     */
    protected boolean getFlag(int flag){
        return SnakeUtilities.getFlag(flags, flag);
    }
    /**
     * This sets whether the given flag is set for this snake based off the 
     * given value. This returns {@code true} if this snake changed as a result 
     * of the call, and {@code false} if no change is made.
     * @param flag The flag to be set or cleared based off {@code value}.
     * @param value Whether the flag should be set or cleared.
     * @return Whether this snake changed as a result of the call.
     * @see #getFlags 
     * @see #getFlag 
     * @see #toggleFlag 
     * @see SnakeUtilities#setFlag 
     * @see #APPLE_CONSUMED_FLAG
     * @see #APPLE_CONSUMPTION_ENABLED_FLAG
     * @see #APPLE_GROWTH_ENABLED_FLAG
     * @see #WRAP_AROUND_ENABLED_FLAG
     * @see #PLAYER_TYPE_FLAG
     * @see #FLIPPED_FLAG
     * @see #CRASHED_FLAG
     * @see #DEFAULT_ACTION_ENABLED_FLAG
     */
    protected boolean setFlag(int flag, boolean value){
        int old = flags;    // Get the old value for the flags
        flags = SnakeUtilities.setFlag(flags, flag, value);
        return flags != old;
    }
    /**
     * This toggles whether the given flag is set for this snake. This returns 
     * {@code true} if this snake changed as a result of the call, and {@code 
     * false} if no change is made. 
     * @param flag The flag to be toggled.
     * @return Whether this snake changed as a result of the call.
     * @see #getFlags 
     * @see #getFlag 
     * @see #setFlag 
     * @see SnakeUtilities#toggleFlag 
     * @see #APPLE_CONSUMED_FLAG
     * @see #APPLE_CONSUMPTION_ENABLED_FLAG
     * @see #APPLE_GROWTH_ENABLED_FLAG
     * @see #WRAP_AROUND_ENABLED_FLAG
     * @see #PLAYER_TYPE_FLAG
     * @see #FLIPPED_FLAG
     * @see #CRASHED_FLAG
     * @see #DEFAULT_ACTION_ENABLED_FLAG
     */
    protected boolean toggleFlag(int flag){
        int old = flags;    // Get the old value for the flags
        flags = SnakeUtilities.toggleFlag(flags, flag);
        return flags != old;
    }
    /**
     * This returns the length of this snake. In other words, this returns how 
     * many tiles make up the body of this snake.
     * @return The number of tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #isEmpty 
     */
    @Override
    public int size(){
        return snakeBody.size();
    }
    /**
     * This returns whether this snake is empty. In other words, this returns 
     * whether this snake contains no tiles. 
     * @return Whether this snake is empty.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isValid 
     */
    @Override
    public boolean isEmpty(){
        return snakeBody.isEmpty();
    }
//    /**
//     * This returns whether this snake contains the given tile. In other words, 
//     * this returns whether this snake contains at least one tile which {@link 
//     * Tile#equals equals} the given tile.
//     * @param o The tile to check for.
//     * @return Whether the given tile is in this snake.
//     * @see #contains(int, int) 
//     */
    @Override
    public boolean contains(Object o){
        return snakeBody.contains(o);
    }
    /**
     * This returns whether this snake contains the tile at the given row and 
     * column. In other words, this returns whether the tile at the given row 
     * and column of the {@link #getModel() model} matches one of the tiles in 
     * this snake.
     * @param row The row of the tile to check for.
     * @param column The column of the tile
     * @return Whether the tile at the given row and column is in this snake.
     * @see #contains(Tile) 
     * @see #getModel 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getTile(int, int) 
     */
    public boolean contains(int row, int column){
            // If the model contains the given row and column, check to see if 
            // this snake contains the tile at the given row and column
        return model.contains(row,column)&&contains(model.getTile(row,column));
    }
    /**
     * This adds the given tile to the body of this snake as the new head. This 
     * assumes that the given tile has a direction set for it. The given tile 
     * will have its {@link Tile#setType type} set to the {@link #getPlayerType 
     * player type} of this snake and will be used to {@link 
     * Tile#alterDirection(Tile) alter the direction} of the previous head if 
     * there is one.
     * @param tile The new head for this snake (cannot be null).
     * @throws NullPointerException If the given tile is null.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #getPlayerType 
     * @see #isFlipped 
     * @see Tile#setType 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#alterDirection(Tile) 
     * @see #pollTail 
     */
    protected void addHead(Tile tile){
        Objects.requireNonNull(tile);   // Check if the tile is not null
        if (!isEmpty()){                // If there is currently a head
            if (getTail() == null)      // If there is no tail yet
                    // Clear the old head since it's about to become the tail
                getHead().clear().setType(getPlayerType());
            getHead().alterDirection(tile);
        }
        tile.setType(getPlayerType());
        if (isFlipped())            // If the snake is flipped
            snakeBody.addLast(tile);
        else
            snakeBody.addFirst(tile);
    }
    /**
     * This removes and returns the tile at the end of the snake body which 
     * represents the tail of the snake. This ignores whether the snake actually 
     * has a {@link #getTail() tail} (i.e. that the snake is at least 2 tiles 
     * long), and will remove the head if there is only one tile in the snake. 
     * If the snake still has a tail after the current tail has been removed, 
     * then the new tail will have its {@link Tile#alterDirection(Tile) 
     * directions altered} based off the now removed tail. The removed tile will 
     * then be {@link Tile#clear() cleared} and returned.
     * @return The tile that was removed, or null if the snake is {@link 
     * #isEmpty() empty}.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see Tile#clear 
     * @see Tile#alterDirection(Tile) 
     * @see #addHead(Tile) 
     * @see #removeTail 
     */
    protected Tile pollTail(){
            // Remove the tail of the snake. If the snake is flipped, this will 
            // be the first tile in the queue. Otherwise, this will be the last 
            // tile in the queue
        Tile tile = (isFlipped()) ? snakeBody.pollFirst():snakeBody.pollLast();
        if (getTail() != null){ // If there is still a tail
            getTail().alterDirection(tile);
        }
        if (tile != null)   // If the old tail is not null
            tile.clear();
        return tile;
    }
    
    @Override
    public boolean offer(Tile tile){
        if (contains(tile))
            return false;
        if (tile == null)
            throw new NullPointerException();
        else if (!model.contains(tile))
            throw new IllegalArgumentException("Tile is not in model");
        else if (!tile.isEmpty())
            throw new IllegalArgumentException("Tile is not empty");
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        if (isEmpty())
            tile.setFacingLeft(true);
        else{
            Tile head = getHead();
            boolean vertical = ((head.isFacingUp() || head.isFacingDown()) && 
                    tile.getColumn() == head.getColumn()) || tile.getRow() != 
                    head.getRow();
            tile.setFacingUp(vertical && tile.getRow() < head.getRow());
            tile.setFacingDown(vertical && !tile.isFacingUp());
            tile.setFacingLeft(!vertical && tile.getColumn() < head.getColumn());
            tile.setFacingRight(!vertical && !tile.isFacingRight());
        }
        addHead(tile);
        model.setTilesAreAdjusting(adjusting);
        fireSnakeChange(SnakeEvent.SNAKE_ADDED_TILE,tile);
        return true;
    }
    
    @Override
    public boolean add(Tile tile){
        if (contains(tile))
            return false;
        return super.add(tile);
    }
    
    @Override
    public boolean addAll(Collection<? extends Tile> c){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        try{
            boolean modified = super.addAll(c);
            model.setTilesAreAdjusting(adjusting);
            return modified;
        }
        catch(RuntimeException ex){
            model.setTilesAreAdjusting(adjusting);
            throw ex;
        }
    }
    
    @Override
    public Tile peek(){
            // If the snake is flipped, return the first tile in the queue. 
            // Otherwise, return the last tile in the queue.
        return (isFlipped()) ? snakeBody.peekFirst() : snakeBody.peekLast();
    }
    
    @Override
    public Tile element(){
        return super.element();
    }
    
    @Override
    public Tile poll(){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        Tile tile = pollTail();
        model.setTilesAreAdjusting(adjusting);
        if (tile != null)
            fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,tile);
        return tile;
    }
    
    @Override
    public Tile remove(){
        return super.remove();
    }
    
    @Override
    public boolean removeAll(Collection<?> c){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        try{
            boolean modified = super.removeAll(c);
            model.setTilesAreAdjusting(adjusting);
            return modified;
        }
        catch(RuntimeException ex){
            model.setTilesAreAdjusting(adjusting);
            throw ex;
        }
    }
    
    @Override
    public boolean retainAll(Collection<?> c){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        try{
            boolean modified = super.retainAll(c);
            model.setTilesAreAdjusting(adjusting);
            return modified;
        }
        catch(RuntimeException ex){
            model.setTilesAreAdjusting(adjusting);
            throw ex;
        }
    }
    
    @Override
    public boolean removeIf(Predicate<? super Tile> filter){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        try{
            boolean modified = super.removeIf(filter);
            model.setTilesAreAdjusting(adjusting);
            return modified;
        }
        catch(RuntimeException ex){
            model.setTilesAreAdjusting(adjusting);
            throw ex;
        }
    }
    /**
     * This resets this snake by {@link #resetFailCount() resetting} the {@link 
     * #getFailCount() fail count}, {@link #clearActionQueue() clearing} the 
     * {@link #getActionQueue() action queue}, clearing any status flags set on 
     * this snake (such as {@link #APPLE_CONSUMED_FLAG} and {@link 
     * #CRASHED_FLAG}), and {@link clear removing all tiles} from this snake. If 
     * the flags or the tile contents of this snake changed as a result of the 
     * call, then this will fire a {@link SnakeEvent#SNAKE_RESET SNAKE_RESET} 
     * {@code SnakeEvent}.
     * @param model The model that this snake is/was being displayed on. This 
     * may be different from the currently set model, and may be the model that 
     * this snake is being removed from when {@link #setModel setting the 
     * model}. This is mainly provided so as to allow this snake to tell the 
     * model that the {@link PlayFieldModel#setTilesAreAdjusting tiles will be 
     * adjusting}.
     * @see #resetFailCount 
     * @see #clearActionQueue 
     * @see #setFlag 
     * @see PlayFieldModel#getTilesAreAdjusting 
     * @see PlayFieldModel#setTilesAreAdjusting 
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #clear 
     * @see SnakeEvent#SNAKE_RESET
     */
    protected void reset(PlayFieldModel model){
        resetFailCount();
        getActionQueue().clear();
            // Reset the flags that are affected when resetting a snake and get 
            // whether they changed
        boolean reset = setFlag(RESET_AFFECTED_FLAGS,false);
        if (!snakeBody.isEmpty()){  // If the snake body is not empty
            reset = true;   // The snake will be changed
                // This gets whether the tiles are currently adjusting, so as to 
                // restore this value when the reset is finished
            boolean adjusting = false;  
            if (model != null){ // If the model is not null
                adjusting = model.getTilesAreAdjusting();
                    // The tiles will be adjusting
                model.setTilesAreAdjusting(true);
            }    // A for loop to go through the body of this snake and clear 
            for (Tile tile : snakeBody) // each tile
                tile.clear();
            snakeBody.clear();           
            if (model != null)  // If the model is not null
                model.setTilesAreAdjusting(adjusting);
        }
        if (reset)  // If this snake was reset
            fireSnakeChange(SnakeEvent.SNAKE_RESET,0);
    }
    /**
     * This {@link Tile#clear() clears} and removes all the tiles from this 
     * snake.
     * @see #reset
     * @see Tile#clear 
     */
    @Override
    public void clear(){
        reset(model);
    }
    
    
    
    /**
     * This returns an array containing the tiles in this snake in the order in 
     * which they appear, starting at the {@link #getHead() head} and ending at 
     * the {@link #getTail() tail} of this snake. <p>
     * 
     * The returned array will be "safe" in that no references to the array will 
     * be maintained by this snake. (In other words, this method must allocate a 
     * new array). The caller is thus free to modify the returned array. 
     * 
     * @return An array containing the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #toList 
     * @see #toQueue 
     */
    @Override
    public Object[] toArray(){
        return super.toArray();
    }
    
    @Override
    public <T> T[] toArray(T[] a){
        return super.toArray(a);
    }
    /**
     * This returns an iterator over the tiles in this snake in the order in 
     * which they appear in this snake. The iterator starts at the {@link 
     * #getHead() head} of the snake and ends at the {@link #getTail() tail} of 
     * the snake. <p>
     * 
     * Please note that tiles store their corresponding row and column, and thus 
     * the location of each tile can be retrieved by using the tile's {@link 
     * Tile#getRow() getRow} and {@link Tile#getColumn() getColumn} methods. <p>
     * 
     * The returned iterator is fail-fast. That is to say, if the structure of
     * this snake changes at any time after the iterator is created, then the 
     * iterator will throw a {@link ConcurrentModificationException 
     * ConcurrentModificationException}.
     * 
     * @return An iterator over the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    @Override
    public Iterator<Tile> iterator() {
        return new SnakeIterator();
    }
    
    
    
    
    
    /**
     * This returns the model that this snake is displayed on and uses to get 
     * its {@link Tile tiles} from.
     * @return The PlayFieldModel that provides the tiles for this snake.
     * @see #setModel
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #isValid 
     * @see PlayFieldModel
     * @see AbstractPlayFieldModel
     * @see DefaultPlayFieldModel
     */
    public PlayFieldModel getModel(){
        return model;
    }
    /**
     * This sets the model that this snake is displayed on and uses to get its 
     * {@link Tile tiles} from. This will also result in this snake being reset, 
     * removing all tiles from this snake, resetting the status of this snake, 
     * and {@link #clearActionQueue() clearing the action queue}. All settings 
     * previously set for this snake will be maintained. This snake will need to 
     * be {@link #initialize(Tile) reinitialized} with a tile from the new model 
     * before it can be used. 
     * @param model The PlayFieldModel that provides the tiles for this snake 
     * (cannot be null).
     * @return This snake.
     * @throws NullPointerException If the model is null.
     * @see #getModel 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #isValid 
     * @see #clearActionQueue 
     * @see PlayFieldModel
     * @see AbstractPlayFieldModel
     * @see DefaultPlayFieldModel
     */
    public Snake setModel(PlayFieldModel model){
        if (model == null)      // If the model is null
            throw new NullPointerException("Play field model cannot be null");
            // If the old and new model are the same
        if (Objects.equals(this.model, model))
            return this;
        PlayFieldModel old = this.model;    // Get the old model
        this.model = model;
        firePropertyChange(MODEL_PROPERTY_CHANGED,old,model);
            // If the old model is not null, reset this snake. (The old model 
            // will only ever be null when the snake is first being constructed)
        if (old != null)
            reset(old);
        return this;
    }
    /**
     * This initializes this snake and sets the given tile to be the snake's 
     * {@link #getHead() head}. If this snake was previously initialized, then 
     * this will also reset the snake. This will result in all tiles being 
     * removed from this snake, the current status of this snake will be reset, 
     * and the action queue will be {@link #clearActionQueue() cleared}. All 
     * settings previously set for this snake will be maintained. After this is 
     * called, the snake will be one tile long, consisting of only the given 
     * tile, and will be facing {@link #isFacingLeft() left}.
     * @param head The head for this snake. This must be a non-null, {@link 
     * Tile#isEmpty() empty} tile from the {@link #getModel() model}. If this 
     * tile is currently part of this snake, then it just needs to be a non-null 
     * tile from the model.
     * @return This snake.
     * @throws NullPointerException If the head is null.
     * @throws IllegalArgumentException If the head is either not in the model 
     * or is neither empty nor currently part of this snake.
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(int, int) 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #isFacingLeft 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #revive 
     * @see #clearActionQueue 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#getState 
     * @see Tile#setState 
     * @see Tile#isSnake 
     * @see Tile#isFacingLeft 
     * @see Tile#setFacingLeft 
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     */
    public Snake initialize(Tile head){
        if (head == null)           // If the proposed head is null
            throw new NullPointerException("Head tile cannot be null");
        if (!model.contains(head))  // If the proposed head is not in the model
            throw new IllegalArgumentException("Head tile is not in model");
            // If the proposed head is neither empty nor part of this snake
        if (!head.isEmpty() && !contains(head))
            throw new IllegalArgumentException("Head tile is not empty");
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        reset(model);   // Reset the snake
            // Clear the new head, set it to be facing left, and set it to be 
        addHead(head.clear().setFacingLeft(true));  // the snake's head
        model.setTilesAreAdjusting(adjusting);
        fireSnakeChange(SnakeEvent.SNAKE_INITIALIZED,head);
        return this;
    }
    /**
     * This initializes this snake and sets the tile in the {@link #getModel() 
     * model} at the given row and column to be the snake's {@link #getHead() 
     * head}. If this snake was previously initialized, then this will also 
     * reset the snake.This will result in all tiles being removed from this 
     * snake, the current status of this snake will be reset, and the action 
     * queue will be {@link #clearActionQueue() cleared}. All settings 
     * previously set for this snake will be maintained. After this is called, 
     * the snake will be one tile long, consisting of only the given tile, and 
     * will be facing {@link #isFacingLeft() left}. <p>
     * 
     * This is equivalent to calling {@link #initialize(Tile) initialize} with 
     * the tile returned by the {@link #getModel() model}'s {@link 
     * PlayFieldModel#getTile getTile} method.
     * 
     * @param row The row in the model for the tile to use as the head of this 
     * snake.
     * @param column The column in the model for the tile to use as the head of 
     * this snake.
     * @return This snake.
     * @throws IndexOutOfBoundsException If either the row or column are out of 
     * bounds for the model.
     * @throws IllegalArgumentException If the tile at the given row and column 
     * is neither empty nor currently part of this snake.
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(Tile) 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #isFacingLeft 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #revive 
     * @see #clearActionQueue 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#getState 
     * @see Tile#setState 
     * @see Tile#isSnake 
     * @see Tile#isFacingLeft 
     * @see Tile#setFacingLeft 
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     */
    public Snake initialize(int row, int column){
        return initialize(model.getTile(row, column));
    }
    /**
     * This returns whether this snake is in a valid state. A snake is in a 
     * valid state if the snake is not {@link #isEmpty() empty}, its {@link 
     * #getHead() head} is a non-null tile {@link PlayFieldModel#contains(Tile) 
     * in} the {@link #getModel() model}, and it's {@link #getDirectionFaced() 
     * facing} in one direction and one direction only.
     * @return Whether this snake is in a valid state.
     * @see #isEmpty 
     * @see #size 
     * @see #getHead 
     * @see #getDirectionFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#contains(Tile) 
     * @see #getModel 
     */
    public boolean isValid(){
        return model != null && !isEmpty() && getHead() != null && 
                model.contains(getHead()) && 
                getHead().getDirectionsFacedCount() == 1;
    }
    /**
     * This checks to see if this snake is in a {@link #isValid() valid} state, 
     * and if not, throws an {@code IllegalStateException}.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isValid 
     * @see #getName 
     */
    protected void checkIfInvalid(){
            // If the snake is in an invalid state
        if (!isValid())                 // If the snake has a name, display it
            throw new IllegalStateException("Snake "+((name!=null)?name+" ":"")+
                    "is not in a valid state");
    }
    
    
    
    
    
//    public boolean addAll()
    
    /**
     * This returns the tile that represents the head of this snake. If this 
     * snake has no head as a result of being {@link #isEmpty() empty}, then 
     * this returns null.
     * @return The head of this snake, or null if this snake has no head.
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     */
    public Tile getHead(){
            // If the snake is flipped, return the last tile in the queue. 
            // Otherwise, return the first tile in the queue.
        return (isFlipped()) ? snakeBody.peekLast() : snakeBody.peekFirst();
    }
    /**
     * This returns the tile that represents the tail of this snake. If this 
     * snake is either {@link #isEmpty() empty} or only has a {@link #getHead() 
     * head} (i.e. this snake is less than two tiles long), then this returns 
     * null.
     * @return The tail of this snake, or null if this snake has no tail.
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #removeTail 
     */
    public Tile getTail(){
            // If the snake is less than two tiles long, return null. Otherwise
            // peek at the queue to get the snake's tail
        return (size() < 2) ? null : peek();
    }
    /**
     * This is used to add the tiles in this snake to the given collection.
     * @param c The collection to add the tiles to.
     * @see #iterator 
     * @see #toList 
     * @see #toQueue 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     */
    private void addToCollection(java.util.Collection<? super Tile> c){
            // A for loop to go through the tiles in this snake
        for (Tile tile : this)
            c.add(tile);
    }
    /**
     * This returns a queue containing the tiles in this snake in the order in 
     * which they appear, starting at the {@link #getHead() head} and ending at 
     * the {@link #getTail() tail} of this snake. <p>
     * 
     * The returned queue will be "safe" in that no references to the queue will 
     * be maintained by this snake. (In other words, this method must allocate a 
     * new queue). The caller is thus free to modify the returned queue. 
     * 
     * @return A queue containing the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #toArray 
     * @see #toList 
     */
    public Deque<Tile> toQueue(){
            // A queue to get the tiles in this snake
        ArrayDeque<Tile> tiles = new ArrayDeque<>();
        addToCollection(tiles); // Add the tiles to the queue
        return tiles;
    }
    /**
     * This returns a list containing the tiles in this snake in the order in 
     * which they appear, starting at the {@link #getHead() head} and ending at 
     * the {@link #getTail() tail} of this snake. <p>
     * 
     * The returned list will be "safe" in that no references to the list will 
     * be maintained by this snake. (In other words, this method must allocate a 
     * new list). The caller is thus free to modify the returned list. 
     * 
     * @return A list containing the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #toArray 
     * @see #toQueue 
     */
    public List<Tile> toList(){
            // A list to get the tiles in this snake
        ArrayList<Tile> tiles = new ArrayList<>();
        addToCollection(tiles); // Add the tiles to the list
        return tiles;
    }
    /**
     * This returns whether this snake is facing up. This is equivalent to 
     * calling {@link Tile#isFacingUp() isFacingUp} on the {@link #getHead() 
     * head} of this snake. If this snake does not have a head, then this will 
     * return {@code false}.
     * @return Whether this snake is facing up.
     * @see #getHead 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see Tile#isFacingUp 
     * @see #isEmpty 
     */
    public boolean isFacingUp(){
        return getHead() != null && getHead().isFacingUp();
    }
    /**
     * This returns whether this snake is facing down. This is equivalent to 
     * calling {@link Tile#isFacingDown() isFacingDown} on the {@link #getHead() 
     * head} of this snake. If this snake does not have a head, then this will 
     * return {@code false}.
     * @return Whether this snake is facing down.
     * @see #getHead 
     * @see #isFacingUp 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see Tile#isFacingDown 
     * @see #isEmpty 
     */
    public boolean isFacingDown(){
        return getHead() != null && getHead().isFacingDown();
    }
    /**
     * This returns whether this snake is facing to the left. This is equivalent 
     * to calling {@link Tile#isFacingLeft() isFacingLeft} on the {@link 
     * #getHead() head} of this snake. If this snake does not have a head, then 
     * this will return {@code false}.
     * @return Whether this snake is facing to the left.
     * @see #getHead 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see Tile#isFacingLeft 
     * @see #isEmpty 
     */
    public boolean isFacingLeft(){
        return getHead() != null && getHead().isFacingLeft();
    }
    /**
     * This returns whether this snake is facing to the right. This is 
     * equivalent to calling {@link Tile#isFacingRight() isFacingRight} on the 
     * {@link #getHead() head} of this snake. If this snake does not have a 
     * head, then this will return {@code false}.
     * @return Whether this snake is facing to the right.
     * @see #getHead 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #getDirectionFaced 
     * @see Tile#isFacingRight 
     * @see #isEmpty 
     */
    public boolean isFacingRight(){
        return getHead() != null && getHead().isFacingRight();
    }
    /**
     * This returns the flag representing the direction that this snake is 
     * currently facing. This is equivalent to calling {@link 
     * Tile#getDirectionsFaced() getDirectionsFaced} on the {@link #getHead() 
     * head} of this snake. As a result, if the head of this snake is facing 
     * multiple directions, then this will return the flags for all the 
     * directions faced. However, keep in mind that a snake is only {@link 
     * #isValid() valid} if it has a head that is facing a single direction. If 
     * this snake does not have a head, then this will return zero.
     * @return The direction that this snake is facing.
     * @see #getHead 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see #isValid 
     * @see #isEmpty 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #ALL_DIRECTIONS
     */
    public int getDirectionFaced(){
            // If there is a head, get its directions. Otherwise, return 0
        return (getHead() != null) ? getHead().getDirectionsFaced() : 0;
    }
    /**
     * This checks the given direction to see if it has a single direction set. 
     * If the given value has no directions set (i.e. {@link 
     * SnakeUtilities#getDirectionCount 
     * SnakeUtilities.getDirectionCount}{@code (direction)} returns zero), then 
     * the direction is replaced with the {@link #getDirectionFaced() direction 
     * currently faced} by this snake.
     * @param direction The direction to check.
     * @return The direction to use.
     * @throws IllegalArgumentException If more than one direction flag is set 
     * on either the given direction or the currently faced direction.
     * @see #getDirectionFaced 
     * @see SnakeUtilities#getDirectionCount 
     * @see SnakeUtilities#requireSingleDirection 
     */
    private int checkDirection(int direction){
            // If the given value has no directions set
        if (SnakeUtilities.getDirectionCount(direction) == 0)
            direction |= getDirectionFaced();
        return SnakeUtilities.requireSingleDirection(direction);
    }
    /**
     * This returns the tile in the {@link #getModel() model} that is {@link 
     * PlayFieldModel#getAdjacentTile adjacent} to the {@link #getHead() head} 
     * of this snake in the given direction. The direction must be either zero 
     * or one of the four direction flags: {@link #UP_DIRECTION}, {@link 
     * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. 
     * If the direction is zero, then this will get the tile in front of this 
     * snake (i.e. the tile adjacent to the head in the {@link 
     * #getDirectionFaced() direction being faced}). This will use whether the 
     * snake {@link #isWrapAroundEnabled() wraps around} to determine how to 
     * treat getting the tile adjacent to the head when the adjacent tile would 
     * be out of bounds. If the snake can wrap around, then this will wrap 
     * around and get a tile from the other side of the model when the adjacent 
     * tile would be out of bounds. Otherwise, this will return null when the 
     * adjacent tile would be out of bounds. <p>
     * 
     * This calls the {@link PlayFieldModel#getAdjacentTile getAdjacentTile} 
     * method of the {@link #getModel() model}, providing it with the {@link 
     * #getHead() head} of the snake for the tile, the given direction 
     * (substituting it with the {@link #getDirectionFaced() direction faced} if 
     * the given direction is zero), and whether this snake is allowed to {@link 
     * #isWrapAroundEnabled() wrap around}. 
     * 
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@code 0} to get the tile being {@link #getDirectionFaced() faced}, 
     *      {@link #UP_DIRECTION} to get the tile above the head, 
     *      {@link #DOWN_DIRECTION} to get the tile below the head, 
     *      {@link #LEFT_DIRECTION} to get the tile to the left of the head, or 
     *      {@link #RIGHT_DIRECTION} to get the tile to the right of the head.
     * @return The tile adjacent to the head of this snake. If the requested 
     * adjacent tile is out of bounds and the snake does not wrap around, then 
     * this will return null.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionFaced
     * @see SnakeUtilities#getDirections 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #isValid 
     * @see #getHead 
     * @see #isWrapAroundEnabled
     * @see #setWrapAroundEnabled
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #getModel
     * @see #setModel
     * @see PlayFieldModel#getAdjacentTile(Tile, int, boolean) 
     * @see #getTileBeingFaced 
     */
    public Tile getAdjacentToHead(int direction){
        checkIfInvalid();   // If the snake is in an invalid state
        return model.getAdjacentTile(getHead(),checkDirection(direction),
                isWrapAroundEnabled());
    }
    /**
     * This returns the tile {@link #getDirectionFaced() in front} of the {@link 
     * #getHead() head} of this snake. This is equivalent to calling {@link 
     * #getAdjacentToHead getAdjacentToHead}{@code (}{@link #getDirectionFaced 
     * getDirectionFaced()}{@code )}. As such, the head should be facing a 
     * single direction, which it must be for the snake to be {@link #isValid() 
     * valid}. If the tile being faced is out of bounds, then this will either 
     * wrap around and return the tile on the other side of the {@link #getModel 
     * model} or return null, depending on whether the snake {@link 
     * #isWrapAroundEnabled() wraps around}.
     * 
     * @return The tile in front of the head, or null if the head is facing a 
     * boundary and the snake does not wrap around.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #isValid 
     * @see #getHead 
     * @see #isWrapAroundEnabled
     * @see #setWrapAroundEnabled
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #getModel
     * @see #setModel
     * @see PlayFieldModel#getAdjacentTile(Tile, int, boolean) 
     * @see #getAdjacentToHead 
     */
    public Tile getTileBeingFaced(){
        return getAdjacentToHead(getDirectionFaced());
    }
    /**
     * This returns the name for this snake. The default value for this is null. 
     * @return The name for this snake.
     * @see #setName
     */
    public String getName(){
        return name;
    }
    /**
     * This sets the name for this snake. The default value for this is null. 
     * @param name The new name for this snake.
     * @return This snake.
     * @see #getName
     */
    public Snake setName(String name){
            // If the old name is the same as the new name
        if (Objects.equals(this.name, name))
            return this;
        String old = this.name; // Get the old name for this snake
        this.name = name;
        firePropertyChange(NAME_PROPERTY_CHANGED,old,name);
        return this;
    }
    /**
     * This returns which player this snake represents. If this is {@code 
     * false}, then this is a first player (primary) snake. If this is {@code 
     * true}, then this is a second player (secondary) snake. The snake does not 
     * necessarily need to represent an actual player, and multiple snakes may 
     * use the same player type. This is primarily used to control what {@link 
     * Tile#getType() type} of {@link Tile#isSnake() snake tiles} should this 
     * snake use. The default value for this is {@code false}. 
     * @return Whether the {@link Tile#isSnake() snake tiles} in this snake 
     * should have their {@link Tile#getType() type flag} set.
     * @see #setPlayerType 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#isSnake 
     * @see #ALTERNATE_TYPE_FLAG
     */
    public boolean getPlayerType(){
        return getFlag(PLAYER_TYPE_FLAG);
    }
    /**
     * This sets which player this snake represents. Refer to the documentation 
     * for the {@link #getPlayerType() getPlayerType} method for more 
     * information on how this is used. If this snake is not {@link #isEmpty() 
     * empty}, then all the tiles in this snake will have their {@link 
     * Tile#getType() type flag} {@link Tile#setType(boolean) set} to the given 
     * value. The default value for this is {@code false}. 
     * @param value The value to use for the {@link Tile#getType() type flags} 
     * of the {@link Tile#isSnake() snake tiles} in this snake.
     * @return This snake.
     * @see #getPlayerType 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#isSnake 
     * @see ALTERNATE_TYPE_FLAG
     */
    public Snake setPlayerType(boolean value){
        if (setFlag(PLAYER_TYPE_FLAG,value)){    // If the flag has changed
            firePropertyChange(PLAYER_TYPE_PROPERTY_CHANGED,value);
            if (!isEmpty()){    // If this snake is not empty
                    // Get whether the tiles in the model are currently 
                    // adjusting, so as to restore this once done
                boolean adjusting = model.getTilesAreAdjusting();
                model.setTilesAreAdjusting(true);
                for (Tile tile : snakeBody)// A for loop to go through the tiles
                    tile.setType(value);
                model.setTilesAreAdjusting(adjusting);
            }
        }
        return this;
    }
    /**
     * This returns whether this snake is flipped. When a snake is flipped, the 
     * order of the tiles in the snake are reversed. 
     * @return Whether this snake is flipped.
     * @see #flip 
     * @see #getHead 
     * @see #getTail 
     */
    public boolean isFlipped(){
        return getFlag(FLIPPED_FLAG);
    }
    /**
     * This sets whether this snake is flipped. When a snake is flipped, the 
     * order of the tiles in the snake are reversed. If there is only one tile, 
     * then the {@link #getHead() head} of this snake will also {@link 
     * Tile#flip() flip}. This will also fire a {@code SnakeEvent} indicating 
     * that the snake has {@link SnakeEvent#SNAKE_FLIPPED flipped} if a change 
     * is made.
     * @param value Whether this snake should be flipped.
     * @return This snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFlipped 
     * @see #flip 
     * @see #getHead 
     * @see #getTail 
     * @see #isValid 
     * @see Tile#flip 
     * @see SnakeEvent#SNAKE_FLIPPED
     */
    protected Snake setFlipped(boolean value){
        checkIfInvalid();                   // Check if this snake is invalid
        if (setFlag(FLIPPED_FLAG,value)){   // If the flag changed
            if (getTail() == null)          // If this snake does not have a tail
                getHead().flip();
            fireSnakeChange(SnakeEvent.SNAKE_FLIPPED);
        }
        return this;
    }
    /**
     * This flips the orientation of this snake. This will reverse the order of 
     * the tiles in this snake, resulting in the {@link #getHead() head} 
     * becoming the {@link #getTail() tail} and vice versa. If this snake does 
     * not have a tail (i.e. there is only one tile in this snake, that being 
     * the head), then the head of the snake will be {@link Tile#flip flipped}. 
     * This will also fire a {@code SnakeEvent} indicating that the snake has 
     * {@link SnakeEvent#SNAKE_FLIPPED flipped}.
     * @return This snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFlipped 
     * @see #getHead 
     * @see #getTail 
     * @see #isValid 
     * @see Tile#flip 
     * @see SnakeEvent#SNAKE_FLIPPED
     */
    public Snake flip(){
        return setFlipped(!isFlipped());
    }
    /**
     * This returns whether this snake will wrap around to the other side of the 
     * play field when it reaches the boundaries of the {@link #getModel() 
     * model}. If this is {@code true} and this snake has reached the edge of 
     * the play field, then attempting to {@link #getAdjacentToHead get}, {@link 
     * #add(int) add}, or {@link #move(int) move to} a tile that would be out of 
     * bounds will result in the snake wrapping around and getting, adding, or 
     * moving to a tile on the other side of the play field. If this is {@code 
     * false}, then attempting to get a tile that would be out of bounds will 
     * return null and any attempt to add or move to a tile that would be out of 
     * bounds will fail. The default value for this is {@code true}. 
     * @return Whether this snake will wrap around when it reaches the 
     * boundaries of the model.
     * @see #setWrapAroundEnabled 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #getModel 
     * @see PlayFieldModel#getAdjacentTile
     */
    public boolean isWrapAroundEnabled(){
        return getFlag(WRAP_AROUND_ENABLED_FLAG);
    }
    /**
     * This sets whether this snake will wrap around when it reaches the 
     * boundaries of the {@link #getModel() model}. Refer to the documentation 
     * for the {@link #isWrapAroundEnabled() isWrapAroundEnabled} method for 
     * more information on how this is used. The default value for this is 
     * {@code true}. 
     * @param enabled Whether this snake will wrap around when it reaches the 
     * boundaries of the model.
     * @return This snake.
     * @see #isWrapAroundEnabled 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #getModel 
     * @see PlayFieldModel#getAdjacentTile
     */
    public Snake setWrapAroundEnabled(boolean enabled){
            // If the wrap around flag has changed
        if (setFlag(WRAP_AROUND_ENABLED_FLAG,enabled))  
            firePropertyChange(WRAP_AROUND_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    /**
     * This returns whether this snake can eat apples. If this is {@code true}, 
     * then this snake will be able to {@link #add(int) add} or {@link 
     * #move(int) move to} {@link Tile#isApple() apple tiles}. If this is {@code 
     * false}, then any attempt to add or move to an apple tile will fail. The 
     * default value for this is {@code true}. 
     * @return Whether this snake can eat apples.
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #hasConsumedApple 
     * @see Tile#isApple 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public boolean isAppleConsumptionEnabled(){
        return getFlag(APPLE_CONSUMPTION_ENABLED_FLAG);
    }
    /**
     * This sets whether this snake can eat apples. Refer to the documentation 
     * for the {@link #isAppleConsumptionEnabled() isAppleConsumptionEnabled} 
     * method for more information on how this is used. The default value for 
     * this is {@code true}. 
     * @param enabled Whether this snake can eat apples.
     * @return This snake.
     * @see #isAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #hasConsumedApple 
     * @see Tile#isApple 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public Snake setAppleConsumptionEnabled(boolean enabled){
            // If the flag has changed
        if (setFlag(APPLE_CONSUMPTION_ENABLED_FLAG,enabled))
            firePropertyChange(APPLE_CONSUMPTION_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    /**
     * This returns whether this snake will grow when it eats an apple. If this 
     * is {@code true}, then {@link #move(int) moving to} an {@link 
     * Tile#isApple() apple tile} will be treated the same as {@link #add(int) 
     * adding} an apple tile. In other words, when an apple is eaten, then this 
     * snake will grow by one tile regardless of whether this snake moved or was 
     * added to. If this is {@code false}, then moving to an apple tile will not 
     * cause this snake to grow. If this snake {@link #isAppleConsumptionEnabled 
     * cannot eat apples}, then this has no effect. The default value for this 
     * is {@code true}. 
     * @return Whether this snake grows when it eats an apple.
     * @see #setApplesCauseGrowth 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #hasConsumedApple 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public boolean getApplesCauseGrowth(){
        return getFlag(APPLE_GROWTH_ENABLED_FLAG);
    }
    /**
     * This sets whether this snake will grow when it eats apples. Refer to the 
     * documentation for the {@link #getApplesCauseGrowth getApplesCauseGrowth} 
     * method for more information on how this is used. The default value for 
     * this is {@code true}. 
     * @param value Whether this snake should grow when it eats an apple.
     * @return This snake.
     * @see #getApplesCauseGrowth 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #hasConsumedApple 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public Snake setApplesCauseGrowth(boolean value){
            // If the flag has changed
        if (setFlag(APPLE_GROWTH_ENABLED_FLAG,value))
            firePropertyChange(APPLES_CAUSE_GROWTH_PROPERTY_CHANGED,value);
        return this;
    }
    /**
     * This returns whether this snake just ate an apple. This will return 
     * {@code true} if the most recent tile to be {@link #add(int) added} or 
     * {@link #move(int) moved} to by this snake was an {@link Tile#isApple() 
     * apple tile}, and {@code false} if otherwise. 
     * @return Whether this snake ate an apple the last time it added or moved 
     * to a tile.
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see Tile#isApple 
     * @see #isValid 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public boolean hasConsumedApple(){
        return getFlag(APPLE_CONSUMED_FLAG);
    }
    
//    protected void setConsumedApple(boolean value, int direction)
    /**
     * This sets whether this snake has eaten an apple. This ignores whether 
     * this snake {@link #isAppleConsumptionEnabled() can even eat apples}. If 
     * the given {@code value} is {@code true}, then this will fire a {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} {@code SnakeEvent} 
     * with the given direction. 
     * @param value Whether this snake ate an apple. If this is {@code true}, 
     * then a {@link SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} will 
     * be fired.
     * @param direction The direction to use for the event fired if {@code 
     * value} is {@code true}.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #hasConsumedApple 
     * @see #setConsumedApple(boolean) 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #isValid 
     * @see #fireSnakeChange(int, Integer) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     */
    protected void setConsumedApple(boolean value, int direction){
        checkIfInvalid();                   // Check if this snake is invalid
        setFlag(APPLE_CONSUMED_FLAG,value);
        if (value)                          // If the value is true
            fireSnakeChange(SnakeEvent.SNAKE_CONSUMED_APPLE,direction);
    }
    /**
     * This sets whether this snake has eaten an apple. This ignores whether 
     * this snake {@link #isAppleConsumptionEnabled() can even eat apples}. If 
     * the given {@code value} is {@code true}, then this will fire a {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} {@code SnakeEvent} 
     * with the {@link #getDirectionFaced() direction currently being faced}.
     * @param value Whether this snake ate an apple. If this is {@code true}, 
     * then a {@link SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} will 
     * be fired.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #hasConsumedApple 
     * @see #setConsumedApple(boolean, int) 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #isValid 
     * @see #fireSnakeChange(int) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     */
    protected void setConsumedApple(boolean value){
        setConsumedApple(value,getDirectionFaced());
    }
    /**
     * This returns whether this snake has crashed. A snake will crash when it 
     * has {@link #getFailCount() repeatedly failed} to {@link #add(int) add} or 
     * {@link #move(int) move} more than the {@link #getAllowedFails() allowed 
     * number of failures}, not including any attempt to add or move the snake 
     * backwards. If the allowed number of failures is negative, then a snake is 
     * allowed to fail an unlimited number of times without crashing. <p>
     * 
     * A snake that has crashed will not be able to add or move to tiles until 
     * the snake has been {@link #revive() revived} or reset.
     * 
     * @return Whether this snake has crashed.
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #isValid 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public boolean isCrashed(){
        return getFlag(CRASHED_FLAG);
    }
    /**
     * This sets whether this snake has crashed. Refer to the documentation for 
     * the {@link #isCrashed() isCrashed} method for more information on what it 
     * means for a snake to crash. If {@code value} causes a change to whether 
     * this snake has crashed or not, then this will fire a snake event 
     * indicating the change made and using the given direction. If the snake 
     * has now crashed, then a {@link SnakeEvent#SNAKE_CRASHED SNAKE_CRASHED} 
     * {@code SnakeEvent} will be fired. Otherwise, a {@link 
     * SnakeEvent#SNAKE_REVIVED SNAKE_REVIVED} {@code SnakeEvent} will be fired. 
     * If the direction is null, then the event will use the {@link 
     * #getDirectionFaced() direction being faced}. <p>
     * 
     * It's recommended to use the {@link #revive() revive} method to revive a 
     * snake, as the {@code revive} method will also reset the conditions that 
     * resulted in the snake crashing.
     * 
     * @param value Whether this snake has crashed. 
     * @param direction The direction to use for the event fired if this has 
     * changed whether this snake has crashed or not, or or null to use the 
     * direction being faced.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isCrashed 
     * @see #setCrashed(boolean) 
     * @see #updateCrashed 
     * @see #revive 
     * @see #isValid 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #fireSnakeChange(int, Integer) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CRASHED
     * @see SnakeEvent#SNAKE_REVIVED
     */
    protected void setCrashed(boolean value, Integer direction){
        checkIfInvalid();                   // Check if this snake is invalid
        if (setFlag(CRASHED_FLAG,value)){   // If the flag changed
                // Fire a snake crashed event if the snake crashed, and a snake 
                // revived event if it is no longer crashed
            fireSnakeChange((value)?SnakeEvent.SNAKE_CRASHED:
                    SnakeEvent.SNAKE_REVIVED,direction);
        }
    }
    /**
     * This sets whether this snake has crashed. Refer to the documentation for 
     * the {@link #isCrashed() isCrashed} method for more information on what it 
     * means for a snake to crash. If {@code value} causes a change to whether 
     * this snake has crashed or not, then this will fire a snake event 
     * indicating the change made and using the {@link #getDirectionFaced() 
     * direction currently being faced}. If the snake has now crashed, then a 
     * {@link SnakeEvent#SNAKE_CRASHED SNAKE_CRASHED} {@code SnakeEvent} will be 
     * fired. Otherwise, a {@link SnakeEvent#SNAKE_REVIVED SNAKE_REVIVED} {@code 
     * SnakeEvent} will be fired. <p>
     * 
     * It's recommended to use the {@link #revive() revive} method to revive a 
     * snake, as the {@code revive} method will also reset the conditions that 
     * resulted in the snake crashing.
     * 
     * @param value Whether this snake has crashed. 
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isCrashed 
     * @see #setCrashed(boolean, Integer) 
     * @see #updateCrashed 
     * @see #revive 
     * @see #isValid 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #fireSnakeChange(int) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CRASHED
     * @see SnakeEvent#SNAKE_REVIVED
     */
    protected void setCrashed(boolean value){
        setCrashed(value,getDirectionFaced());
    }
    /**
     * This updates whether this snake has crashed based off whether the {@link 
     * #getFailCount() fail count} has exceeded a non-negative {@link 
     * #getAllowedFails() allowed number of failures}. This calls {@link 
     * #setCrashed setCrashed} to set whether the snake is crashed or not, 
     * passing the given direction to it if one is provided.
     * @param direction The direction to use for the event fired if this has 
     * changed whether this snake has crashed or not, or or null to use the 
     * direction being faced.
     * @see #isCrashed 
     * @see #setCrashed(boolean, Integer) 
     * @see #setCrashed(boolean) 
     * @see #revive 
     * @see #isValid 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #fireSnakeChange(int, Integer) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CRASHED
     * @see SnakeEvent#SNAKE_REVIVED
     */
    protected void updateCrashed(Integer direction){
        setCrashed(getAllowedFails() >= 0 && getFailCount() > getAllowedFails(),
                direction);
    }
    /**
     * This returns the maximum amount of consecutive failed attempts that this 
     * snake can make to {@link #add(int) add} or {@link #move(int) move} to a 
     * tile before it will {@link #isCrashed() crash}. In other words, if the 
     * {@link #getFailCount() amount of times that this snake has failed} to add 
     * or move to a tile consecutively exceeds this value, then this snake will 
     * crash. If this is negative, then this snake can fail to add or move an 
     * unlimited amount of times without crashing. The default value for this is 
     * -1. 
     * @return The amount of times that this snake can fail consecutively before 
     * it crashes. If this is negative, then this snake can fail an unlimited 
     * amount of times.
     * @see #setAllowedFails 
     * @see #isCrashed 
     * @see #revive 
     * @see #getFailCount 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public int getAllowedFails(){
        return allowedFails;
    }
    /**
     * This sets the maximum amount of consecutive failed attempts this snake 
     * can make to {@link #add(int) add} or {@link #move(int) move} to a tile 
     * before it will {@link #isCrashed() crash}. Refer to the documentation for 
     * the {@link #getAllowedFails() getAllowedFails} method for more 
     * information about how this value is used. If this snake is {@link 
     * #isValid() valid} and the allowed number of fails changes, then this will 
     * also {@link #revive() revive} this snake if necessary. The default value 
     * for this is -1. 
     * @param value The amount of times that this snake will be allowed to fail 
     * consecutively before it crashes. If this is negative, then this snake 
     * will be allowed to fail an unlimited amount of times.
     * @return This snake.
     * @see #getAllowedFails 
     * @see #isCrashed 
     * @see #revive 
     * @see #getFailCount 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public Snake setAllowedFails(int value){
        if (allowedFails == value)  // If the new value matches the old one
            return this;
        int old = allowedFails;     // Gets the old value
        allowedFails = value;
        firePropertyChange(ALLOWED_FAILS_PROPERTY_CHANGED,old,value);
        if (isValid())              // If the snake is valid
            revive();
        return this;
    }
    /**
     * This returns the amount of times this snake has consecutively failed to 
     * {@link #add(int) add} or {@link #move(int) move} to a tile. This value is 
     * incremented whenever this snake fails to add or move to a tile, excluding 
     * attempts to add or move the snake backwards and attempts to add or move 
     * the snake when it has {@link #isCrashed() crashed}. This value is reset 
     * to zero when either this snake successfully adds or moves to a tile or 
     * when this snake is {@link #revive() revived}. If the {@link 
     * #getAllowedFails() allowed number of failures} is not negative, then this 
     * snake will crash when this value exceeds the allowed number of failures. 
     * Refer to the documentation for {@link #getAllowedFails getAllowedFails} 
     * for more information.
     * @return The amount of times this snake has failed consecutively.
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #isCrashed 
     * @see #revive 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public int getFailCount(){
        return failCount;
    }
    /**
     * This sets the amount of times this snake has consecutively failed to 
     * {@link #add(int) add} or {@link #move(int) move} to a tile. Refer to the 
     * documentation for the {@link #getFailCount() getFailCount} method for 
     * more information.
     * @param count The amount of times this snake has failed consecutively.
     * @see #getFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, Integer) 
     * @see #setCrashed(boolean) 
     * @see #updateCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    protected void setFailCount(int count){
        failCount = count;
    }
    /**
     * This increments the amount of times that this snake has consecutively 
     * failed to {@link #add(int) add} or {@link #move(int) move} to a tile. 
     * Refer to the documentation for the {@link #getFailCount() getFailCount} 
     * method for more information.
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #resetFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, Integer) 
     * @see #setCrashed(boolean) 
     * @see #updateCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    protected void incrementFailCount(){
        failCount++;
    }
    /**
     * This clears the amount of times this snake has consecutively failed to 
     * {@link #add(int) add} or {@link #move(int) move} to a tile. Refer to the 
     * documentation for the {@link #getFailCount() getFailCount} method for 
     * more information. Since this does not clear the snake's crashed status, 
     * it is recommended to call {@link #revive() revive} instead if the desired 
     * goal is to revive the snake.
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, Integer) 
     * @see #setCrashed(boolean) 
     * @see #updateCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    protected void resetFailCount(){
        failCount = 0;
    }
    /**
     * This revives this snake after this snake has crashed. This will also 
     * reset the {@link #getFailCount() number of fails} to zero and fire a 
     * {@code SnakeEvent} indicating that the snake has been {@link 
     * SnakeEvent#SNAKE_REVIVED revived}. Refer to the documentation for the 
     * {@link #isCrashed() isCrashed} method for more information about what it 
     * means for a snake to crash. 
     * @return This snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isCrashed 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #isValid 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    public Snake revive(){
        checkIfInvalid();       // Check if this snake is invalid
        resetFailCount();       // Reset the fail count
            // Update if the snake has crashed (it shouldn't be)
        updateCrashed(null);    
        return this;
    }
    /**
     * This checks to see if this snake can add or move to the given tile. If 
     * the given tile is null, then this will return false. If the tile is an 
     * {@link Tile#isApple() apple tile}, then this returns whether this snake 
     * {@link #isAppleConsumptionEnabled() can eat apples}. Otherwise, this 
     * returns whether the tile is {@link #isEmpty() empty}. <p>
     * 
     * This is called by the {@link #addOrMove addOrMove} method to see if the 
     * tile it is attempting to add/move to can be added or moved to. This is 
     * also called by the {@link #canMoveInDirection canMoveInDirection} method 
     * to get whether the snake can add/move to a tile {@link #getAdjacentToHead 
     * adjacent} to the {@link #getHead() head}.
     * 
     * @param tile The tile to check.
     * @return Whether this snake can add or move to the given tile.
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getAdjacentToHead 
     */
    protected boolean canAddTile(Tile tile){
        if (tile == null)               // If the tile is null
            return false;
        if (tile.isApple())             // If the tile is an apple tile
            return isAppleConsumptionEnabled();
        return tile.isEmpty();
    }
    /**
     * This returns whether this snake can {@link #add(int) add} or {@link 
     * #move(int) move} in the given direction. The direction must be either 
     * zero or one of the four direction flags: {@link #UP_DIRECTION}, {@link 
     * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. 
     * If the direction is zero, then this will return whether this snake can 
     * add or move forward (i.e. whether this snake can add or move in the 
     * {@link #getDirectionFaced() direction faced}). A snake can add or move in 
     * a given direction if the snake has not {@link #isCrashed() crashed} and 
     * the tile {@link #getAdjacentToHead(int) adjacent} to the snake's {@link 
     * #getHead() head} is a non-null tile that is either {@link Tile#isEmpty() 
     * empty} or an {@link Tile#isApple() apple tile}, with the latter being 
     * dependent on whether the snake {@link #isAppleConsumptionEnabled() can 
     * eat apples}. If a snake cannot eat apples, then it cannot add or move to 
     * apple tiles. If a snake can {@link #isWrapAroundEnabled() wrap around}, 
     * then attempting to add or move beyond the bounds of the {@link #getModel 
     * play field} would result in the snake adding or moving to the tiles on 
     * the other side of the play field. If the snake cannot wrap around, then 
     * it will be stopped by the bounds of the play field. Refer to the 
     * documentation for the {@link #getAdjacentToHead getAdjacentToHead} method 
     * for more information on how this gets the tile adjacent to the head.
     * @param direction The direction in which to check for whether this snake 
     * can move. 
     * This should be one of the following: 
     *      {@code 0} to get if the snake can move {@link #getDirectionFaced() 
     *          forward}, 
     *      {@link #UP_DIRECTION} to get if the snake can move up, 
     *      {@link #DOWN_DIRECTION} to get if the snake can move down, 
     *      {@link #LEFT_DIRECTION} to get if the snake can move left, or 
     *      {@link #RIGHT_DIRECTION} to get if the snake can move right.
     * @return Whether this snake can move in the given direction.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the direction flags.
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionFaced
     * @see SnakeUtilities#getDirections 
     * @see #isValid 
     * @see #getHead
     * @see #isWrapAroundEnabled
     * @see #setWrapAroundEnabled
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #isCrashed 
     * @see #revive 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #canMoveForward 
     */
    public boolean canMoveInDirection(int direction){
        checkIfInvalid();       // Check if this snake is invalid
        return !isCrashed() && canAddTile(getAdjacentToHead(direction));
    }
    /**
     * This returns whether this snake can {@link #add() add} or {@link #move() 
     * move} {@link #getDirectionFaced() forward}. A snake can add or move 
     * forward if the snake has not {@link #isCrashed() crashed} and the tile 
     * {@link #getTileBeingFaced() in front} of the snake is a non-null tile 
     * that is either {@link Tile#isEmpty() empty} or an {@link Tile#isApple() 
     * apple tile}, with the latter being dependent on whether the snake {@link 
     * #isAppleConsumptionEnabled() can eat apples}. If a snake cannot eat 
     * apples, then it cannot add or move to apple tiles. If a snake can {@link 
     * #isWrapAroundEnabled() wrap around}, then attempting to add or move 
     * beyond the bounds of the {@link #getModel play field} would result in the 
     * snake adding or moving to the tiles on the other side of the play field. 
     * If the snake cannot wrap around, then it will be stopped by the bounds of 
     * the play field. Refer to the documentation for the {@link 
     * #getTileBeingFaced() getTileBeingFaced} method for more information on 
     * how this gets the tile in front of the snake. <p> 
     * 
     * This is equivalent to calling {@link #canMoveInDirection(int) 
     * canMoveInDirection}{@code (}{@link #getDirectionFaced() 
     * getDirectionFaced()}{@code )}. 
     * 
     * @return Whether this snake can move forward.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced
     * @see #isValid 
     * @see #getHead
     * @see #isWrapAroundEnabled
     * @see #setWrapAroundEnabled
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #isCrashed 
     * @see #revive 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #canMoveInDirection 
     */
    public boolean canMoveForward(){
        return canMoveInDirection(getDirectionFaced());
    }
    /**
     * This attempts to add a tile to or move this snake in the given direction, 
     * depending on the value of {@code moveSnake}. This will return {@code 
     * true} if a tile was successfully added or if the snake has successfully 
     * moved, and {@code false} if this fails to do so. If {@code moveSnake} is 
     * {@code true}, then this will attempt to move the snake in the given 
     * direction. Otherwise, this will attempt to add a tile from the given 
     * direction to this snake. The main difference between adding a tile and 
     * moving the snake is whether the current {@link #getTail() tail} gets 
     * removed. A snake can only add or move if it has not {@link #isCrashed 
     * crashed} and it {@link #canMoveInDirection(int) can move} in the given 
     * direction. The direction must be either zero or one of the four direction 
     * flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction is zero, 
     * then this will use the {@link #getDirectionFaced() direction being faced} 
     * instead. Refer to the documentation for the {@link #canMoveInDirection 
     * canMoveInDirection} method for more information about how this checks to 
     * see if this snake can move in a given direction. <p>
     * 
     * This starts by first checking to see if this snake has {@link #isCrashed 
     * crashed}, and if so, this fires a {@link SnakeEvent#SNAKE_FAILED 
     * SNAKE_FAILED} {@code SnakeEvent} and returns {@code false}. If this snake 
     * has not crashed, then this will get the tile {@link #getAdjacentToHead 
     * adjacent} to the {@link #getHead() head} in the given direction. If the 
     * direction is zero, then this will use the tile {@link #getTileBeingFaced 
     * in front} of this snake. Refer to the documentation for the {@link 
     * #getAdjacentToHead getAdjacentToHead} method for more information about 
     * how this gets the tile adjacent to the head. This will then check to see 
     * if this can add the tile to the snake using the {@link #canAddTile 
     * canAddTile} method. If this cannot add the tile, then this will fire a 
     * {@code SNAKE_FAILED SnakeEvent} and return {@code false}. If the snake 
     * is only a head (i.e. the snake is less than two tiles long) or if the 
     * snake did not try to go backwards, then this will also {@link 
     * #incrementFailCount() increment} the {@link #getFailCount() fail count} 
     * and call {@link #updateCrashed updateCrashed} to update whether this 
     * snake has crashed based off whether the fail count has exceeded a 
     * non-negative {@link #getAllowedFails allowed number of failures}. If this 
     * snake has crashed as a result, then a {@link SnakeEvent#SNAKE_CRASHED 
     * SNAKE_CRASHED} {@code SnakeEvent} will be fired. <p>
     * 
     * If this can add the tile, then this will check to see if the tile is an 
     * {@link Tile#isApple() apple tile} before setting the tile to be a {@link 
     * Tile#isSnake() snake tile} facing the given direction (or faced direction 
     * if the given direction was zero) and {@link #addHead add} it to the body 
     * of this snake as the new head. If the tile was an apple tile and this 
     * snake {@link #getApplesCauseGrowth() grows when it eats apples}, then 
     * {@code moveSnake} will be ignored and and the snake will grow by one 
     * tile. If this is moving the snake (i.e. {@code moveSnake} is {@code true} 
     * and either the tile that was added was not an apple tile or apples do not 
     * cause this snake to grow), then the snake's tail is {@link #pollTail() 
     * polled}. This will then {@link #resetFailCount() reset the fail count}, 
     * call {@link #setConsumedApple setConsumedApple} to update whether this 
     * snake ate an apple and fire a {@link SnakeEvent#SNAKE_CONSUMED_APPLE 
     * SNAKE_CONSUMED_APPLE} {@code SnakeEvent} if it did, fire either a 
     * {@link SnakeEvent#SNAKE_ADDED_TILE SNAKE_ADDED_TILE} {@code SnakeEvent} 
     * or a {@link SnakeEvent#SNAKE_MOVED SNAKE_MOVED} {@code SnakeEvent}, 
     * depending on whether this added a tile or moved the snake, and finally 
     * return {@code true} to indicate that this succeeded in adding to or 
     * moving the snake. <p>
     * 
     * The {@link #add(int) add} and {@link #move(int) move} methods delegate 
     * the task of adding and moving the snake to this method, forwarding the 
     * direction given to them to this method. The {@code add} method calls this 
     * method with {@code false} as the value for {@code moveSnake}, and the 
     * {@code move} method uses {@code true} for {@code moveSnake}.
     * 
     * @param direction The direction to use when getting the adjacent tile to 
     * become the new head. 
     * This should be one of the following: 
     *      {@code 0} to use the tile being {@link #getDirectionFaced() faced}, 
     *      {@link #UP_DIRECTION} to use the tile above the head, 
     *      {@link #DOWN_DIRECTION} to use the tile below the head, 
     *      {@link #LEFT_DIRECTION} to use the tile to the left of the head, or 
     *      {@link #RIGHT_DIRECTION} to use the tile to the right of the head.
     * @param moveSnake {@code true} if this is suppose to move the snake, 
     * {@code false} if this is suppose to add a tile to the snake.
     * @return Whether this successfully added a tile or moved the snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionFaced
     * @see SnakeUtilities#getDirections
     * @see SnakeUtilities#invertDirections 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canAddTile 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #addHead 
     * @see #pollTail 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #hasConsumedApple 
     * @see #setConsumedApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #isWrapAroundEnabled 
     * @see #setWrapAroundEnabled 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFlipped 
     * @see #flip 
     * @see #isCrashed 
     * @see #setCrashed 
     * @see #updateCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_MOVED
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_CRASHED
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
     */
    protected boolean addOrMove(int direction, boolean moveSnake){
        checkIfInvalid();       // Check if this snake is invalid
        direction = checkDirection(direction);  // Check the direction
        if (isCrashed()){       // If the snake has crashed
            fireSnakeFailed(direction);
            return false;
        }   // Get the tile adjacent to the head.
        Tile tile = getAdjacentToHead(direction);   
        if (!canAddTile(tile)){ // If the snake cannot add the tile
                // If the snake is only a head or the snake was not attempting 
                // to add or move backwards (if the snake was not trying to add 
                // the tile in the snake that's behind the head)
            if (size() < 2 || direction != 
                    SnakeUtilities.invertDirections(getDirectionFaced()))
                incrementFailCount();
            fireSnakeFailed(direction);
            updateCrashed(direction);
            return false;
        }   // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // Get whether the tile being added is (or was) an apple
        boolean ateApple = tile.isApple();  
        addHead(tile.setState(direction));  // Add the head to the snake
            // If the snake ate an apple and apples cause the snake to grow
        if (ateApple && getApplesCauseGrowth())
            moveSnake = false;
        if (moveSnake)      // If the snake is moving
            pollTail();     // Remove the current tail
        model.setTilesAreAdjusting(adjusting);
        resetFailCount();   // Reset the fail count
            // Set whether the snake ate an apple based off whether the tile was 
            // an apple tile. This will also fire an event if an apple was eaten
        setConsumedApple(ateApple,direction);
            // If the snake moved, fire a snake moved event. Otherise, fire a 
        fireSnakeChange((moveSnake)?SnakeEvent.SNAKE_MOVED:// snake added event
                SnakeEvent.SNAKE_ADDED_TILE,direction);
        return true;
    }
    /**
     * This attempts to add the tile {@link #getAdjacentToHead(int) adjacent} to 
     * the {@link #getHead head} of this snake. This will return {@code true} if 
     * the tile is successfully added to this snake, and {@code false} if this 
     * fails to add the tile. The tile will only be added if this snake has not 
     * {@link #isCrashed crashed} and {@link #canMoveInDirection(int) can move} 
     * in the given direction. The direction must be either zero or one of the 
     * four direction flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, 
     * {@link #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction 
     * is zero, then this will attempt to add the tile {@link #getTileBeingFaced 
     * in front} of this snake (i.e. this will attempt to add the tile adjacent 
     * to the head in the {@link #getDirectionFaced() direction being faced}). 
     * Refer to the documentation for the {@link #getAdjacentToHead 
     * getAdjacentToHead} method for more information about how this gets the 
     * tile adjacent to the head, and to the documentation for the {@link 
     * #canMoveInDirection canMoveInDirection} method for more information about 
     * how this checks to see if this snake can move in a given direction. <p>
     * 
     * If the tile is successfully added to this snake, then this snake will 
     * grow in length by one tile, the tile that was added will become the new 
     * head, and this will fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_ADDED_TILE tile was added}. If the tile was an {@link 
     * Tile#isApple() apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple() hasConsumedApple} will return 
     * {@code true}. Snakes can only add apple tiles if they are able to {@link 
     * #isAppleConsumptionEnabled() eat apples}. If the snake failed to add the 
     * tile, then this will fire a {@code SnakeEvent} indicating that the snake 
     * {@link SnakeEvent#SNAKE_FAILED failed} to add a tile. A snake will crash 
     * if the {@link #getFailCount amount of times it has consecutively failed} 
     * to move or add a tile exceeds its {@link #getAllowedFails allowed number 
     * of failures}. Attempting and failing to add or move the snake backwards 
     * does not contribute to a snake crashing unless the snake does not have a 
     * tail (i.e. the snake must be at least two tiles long for attempting to 
     * add or move the snake backwards to not count towards the fail count). If 
     * the allowed number of failures is negative, then the snake can fail to 
     * add or move to a tile an unlimited amount of times without crashing. In 
     * other words, if the allowed number of failures is negative, then the 
     * snake cannot crash by failing to add or move to a tile. If this snake 
     * crashes, then this will fire a {@code SnakeEvent} indicating that the 
     * snake has {@link SnakeEvent#SNAKE_CRASHED crashed}. Once a snake has 
     * crashed, it will be unable to add or move to a tile until it has been 
     * {@link #revive() revived}. <p>
     * 
     * The {@link #move(int) move} method works similarly to this, with the 
     * exception being that this snake will remain the same length unless an 
     * apple is eaten and the snake is set to {@link #getApplesCauseGrowth() 
     * grow when it eats an apple}.
     * 
     * @param direction The direction in which to get the tile to add to this 
     * snake. 
     * This should be one of the following: 
     *      {@code 0} to add the tile {@link #getDirectionFaced() in front} of 
     *          the snake, 
     *      {@link #UP_DIRECTION} to add the tile above the snake's head, 
     *      {@link #DOWN_DIRECTION} to add the tile below the snake's head, 
     *      {@link #LEFT_DIRECTION} to add the tile to the left of the snake's 
     *          head, or 
     *      {@link #RIGHT_DIRECTION} to add the tile to the right of the snake's 
     *          head.
     * @return Whether the adjacent tile was successfully added to this snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionFaced
     * @see SnakeUtilities#getDirections
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #hasConsumedApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #isWrapAroundEnabled 
     * @see #setWrapAroundEnabled 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFlipped 
     * @see #flip 
     * @see #isCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_CRASHED
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
     */
    public boolean add(int direction){
        return addOrMove(direction,false);
    }
    /**
     * This attempts to add the tile {@link #getTileBeingFaced() in front} of 
     * the {@link #getHead() head} of this snake. This will return {@code true} 
     * if the tile is successfully added to this snake, and {@code false} if 
     * this fails to add the tile. The tile will only be added if this snake has 
     * not {@link #isCrashed() crashed} and {@link #canMoveForward() can move 
     * forward}. Refer to the documentation for the {@link #getTileBeingFaced 
     * getTileBeingFaced} method for more information about how this gets the 
     * tile in front of the head, and to the documentation for the {@link 
     * #canMoveForward() canMoveForward} method for more information about how 
     * this checks to see if this snake can move forward. <p>
     * 
     * If the tile is successfully added to this snake, then this snake will 
     * grow in length by one tile, the tile that was added will become the new 
     * head, and this will fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_ADDED_TILE tile was added}. If the tile was an {@link 
     * Tile#isApple() apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple() hasConsumedApple} will return 
     * {@code true}. Snakes can only add apple tiles if they are able to {@link 
     * #isAppleConsumptionEnabled() eat apples}. If the snake failed to add the 
     * tile, then this will fire a {@code SnakeEvent} indicating that the snake 
     * {@link SnakeEvent#SNAKE_FAILED failed} to add a tile. A snake will crash 
     * if the {@link #getFailCount amount of times it has consecutively failed} 
     * to move or add a tile exceeds its {@link #getAllowedFails allowed number 
     * of failures}, excluding attempts to add or move the snake backwards when 
     * the snake is at least two tiles long. If the allowed number of failures 
     * is negative, then the snake can fail to add or move to a tile an 
     * unlimited amount of times without crashing. In other words, if the 
     * allowed number of failures is negative, then the snake cannot crash by 
     * failing to add or move to a tile. If this snake crashes, then this will 
     * fire a {@code SnakeEvent} indicating that the snake has {@link 
     * SnakeEvent#SNAKE_CRASHED crashed}. Once a snake has crashed, it will be 
     * unable to add or move to a tile until it has been {@link #revive() 
     * revived}. <p>
     * 
     * The {@link #move() move} method works similarly to this, with the 
     * exception being that this snake will remain the same length unless an 
     * apple is eaten and the snake is set to {@link #getApplesCauseGrowth() 
     * grow when it eats an apple}. <p>
     * 
     * This is equivalent to calling {@link #add(int) add}{@code (}{@link 
     * #getDirectionFaced getDirectionFaced()}{@code )}.
     * 
     * @return Whether the tile in front of this snake was successfully added to 
     * this snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #hasConsumedApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #isWrapAroundEnabled 
     * @see #setWrapAroundEnabled 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFlipped 
     * @see #flip 
     * @see #isCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_CRASHED
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see #add(int) 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
     */
    public boolean add(){
        return add(getDirectionFaced());
    }
    /**
     * This attempts to move this snake in the given direction. This will return 
     * {@code true} if this snake has successfully moved, and {@code false} if 
     * this fails to move the snake. Snakes can only move if they have not 
     * {@link #isCrashed() crashed} and {@link #canMoveInDirection can move} in 
     * the given direction. The direction must be either zero or one of the four 
     * direction flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction is zero, 
     * then this will attempt to move the snake {@link #getDirectionFaced 
     * forward}. Refer to the documentation for the {@link #canMoveInDirection 
     * canMoveInDirection} method for more information about how this checks to 
     * see if this snake can move in a given direction. <p>
     * 
     * If this snake successfully moved, then the tile {@link #getAdjacentToHead 
     * adjacent} to the {@link #getHead() head} of this snake will become the 
     * new head, the current {@link #getTail() tail} (or the current head if 
     * this snake does not have a tail) will be removed from this snake, and 
     * this will fire a {@code SnakeEvent} indicating that this snake has {@link 
     * SnakeEvent#SNAKE_MOVED moved}. Refer to the documentation for the {@link 
     * #getAdjacentToHead getAdjacentToHead} method for more information about 
     * how this gets the tile adjacent to the head. If the tile was an {@link 
     * Tile#isApple() apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple() hasConsumedApple} will return 
     * {@code true}. If this snake {@link #getApplesCauseGrowth() grows when it 
     * eats apples}, then eating an apple will cause a tile to be {@link #add 
     * added} to this snake instead of moving this snake. Snakes can only move 
     * to apple tiles if they are able to {@link #isAppleConsumptionEnabled eat 
     * apples}. If the snake failed to move, then this will fire a {@code 
     * SnakeEvent} indicating that the snake {@link SnakeEvent#SNAKE_FAILED 
     * failed} to move. A snake will crash if the {@link #getFailCount amount of 
     * times it has consecutively failed} to move or add a tile exceeds its 
     * {@link #getAllowedFails allowed number of failures}. Attempting and 
     * failing to add or move the snake backwards does not contribute to a snake 
     * crashing unless the snake does not have a tail (i.e. the snake must be at 
     * least two tiles long for attempting to add or move the snake backwards to 
     * not count towards the fail count). If the allowed number of failures is 
     * negative, then the snake can fail to add or move to a tile an unlimited 
     * amount of times without crashing. In other words, if the allowed number 
     * of failures is negative, then the snake cannot crash by failing to add or 
     * move to a tile. If this snake crashes, then this will fire a {@code 
     * SnakeEvent} indicating that the snake has {@link SnakeEvent#SNAKE_CRASHED 
     * crashed}. Once a snake has crashed, it will be unable to add or move to a 
     * tile until it has been {@link #revive() revived}. <p>
     * 
     * The {@link #add(int) add} method works similarly to this, with the 
     * exception being that this snake will become longer regardless of whether 
     * the snake ate an apple and would grow when it does.
     * 
     * @param direction The direction in which to move this snake. 
     * This should be one of the following: 
     *      {@code 0} to move the snake {@link #getDirectionFaced() forward}, 
     *      {@link #UP_DIRECTION} to move the snake up, 
     *      {@link #DOWN_DIRECTION} to move the snake down, 
     *      {@link #LEFT_DIRECTION} to move the snake left, or 
     *      {@link #RIGHT_DIRECTION} to move the snake right.
     * @return Whether this snake successfully moved in the given direction.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the direction flags.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #getDirectionFaced
     * @see SnakeUtilities#getDirections
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #hasConsumedApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #isWrapAroundEnabled 
     * @see #setWrapAroundEnabled 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFlipped 
     * @see #flip 
     * @see #isCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_MOVED
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_CRASHED
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see #add(int) 
     * @see #add() 
     * @see #move() 
     * @see #removeTail 
     */
    public boolean move(int direction){
        return addOrMove(direction,true);
    }
    /**
     * This attempts to move this snake {@link #getDirectionFaced() forward}. 
     * This will return {@code true} if this snake has successfully moved, and 
     * {@code false} if this fails to move the snake. Snakes can only move if 
     * they have not {@link #isCrashed() crashed} and {@link #canMoveForward() 
     * can move forward}. Refer to the documentation for the {@link 
     * #canMoveForward() canMoveForward} method for more information about how 
     * this checks to see if this snake can move forward. <p>
     * 
     * If this snake successfully moved, then the tile {@link #getTileBeingFaced
     * in front} of the {@link #getHead() head} of this snake will become the 
     * new head, the current {@link #getTail() tail} (or the current head if 
     * this snake does not have a tail) will be removed from this snake, and 
     * this will fire a {@code SnakeEvent} indicating that this snake has {@link 
     * SnakeEvent#SNAKE_MOVED moved}. Refer to the documentation for the {@link 
     * #getTileBeingFaced getTileBeingFaced} method for more information about 
     * how this gets the tile in front of the head. If the tile was an {@link 
     * Tile#isApple() apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple() hasConsumedApple} will return 
     * {@code true}. If this snake {@link #getApplesCauseGrowth() grows when it 
     * eats apples}, then eating an apple will cause a tile to be {@link #add() 
     * added} to this snake instead of moving this snake. Snakes can only move 
     * to apple tiles if they are able to {@link #isAppleConsumptionEnabled eat 
     * apples}. If the snake failed to move, then this will fire a {@code 
     * SnakeEvent} indicating that the snake {@link SnakeEvent#SNAKE_FAILED 
     * failed} to move. A snake will crash if the {@link #getFailCount amount of 
     * times it has consecutively failed} to move or add a tile exceeds its 
     * {@link #getAllowedFails() allowed number of failures}, excluding attempts 
     * to add or move the snake backwards when the snake is at least two tiles 
     * long. If the allowed number of failures is negative, then the snake can 
     * fail to add or move to a tile an unlimited amount of times without 
     * crashing. In other words, if the allowed number of failures is negative, 
     * then the snake cannot crash by failing to add or move to a tile. If this 
     * snake crashes, then this will fire a {@code SnakeEvent} indicating that 
     * the snake has {@link SnakeEvent#SNAKE_CRASHED crashed}. If a snake has 
     * crashed, it will be unable to add or move to a tile until it has been 
     * {@link #revive revived}. <p>
     * 
     * The {@link #add() add} method works similarly to this, with the exception 
     * being that this snake will become longer regardless of whether the snake 
     * ate an apple and would grow when it does. <p>
     * 
     * This is equivalent to calling {@link #move(int) move}{@code (}{@link 
     * #getDirectionFaced() getDirectionFaced()}{@code )}.
     * 
     * @return Whether this snake successfully moved forward.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Tile) 
     * @see #contains(int, int) 
     * @see #hasConsumedApple 
     * @see #isAppleConsumptionEnabled 
     * @see #setAppleConsumptionEnabled 
     * @see #getApplesCauseGrowth 
     * @see #setApplesCauseGrowth 
     * @see #isWrapAroundEnabled 
     * @see #setWrapAroundEnabled 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFlipped 
     * @see #flip 
     * @see #isCrashed 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_MOVED
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_CRASHED
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #removeTail 
     */
    public boolean move(){
        return move(getDirectionFaced());
    }
    /**
     * This removes and returns the {@link #getTail() tail} of this snake. If 
     * this snake does not have a tail (i.e. this snake is less than two tiles 
     * long), then this will fire a {@code SnakeEvent} indicating that it {@link 
     * SnakeEvent#SNAKE_FAILED failed} and return null. Otherwise, this will 
     * remove the tail from this snake, {@link Tile#clear() clear} the tile, and 
     * fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_REMOVED_TILE tile was removed}. 
     * @return The tile that was removed which use to be the tail of this snake, 
     * or null if this snake did not have a tail.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_REMOVED_TILE
     */
    public Tile removeTail(){
        checkIfInvalid();       // Check if this snake is invalid
        if (getTail() == null){ // If the snake doesn't have a tail
            fireSnakeFailed(0);
            return null;
        }   // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        Tile tile = pollTail(); // Poll the tail
        model.setTilesAreAdjusting(adjusting);
        if (tile == null)       // If a tile was not removed
            fireSnakeFailed(0);
        else
            fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,tile);
        return tile;
    }
    /**
     * This checks to see if the given command is the command for invoking the 
     * default action, and if so, will throw an IllegalArgumentException stating 
     * that the default action should not invoke the default action.
     * @param cmd The snake command to check.
     * @throws IllegalArgumentException If the given snake command is {@link 
     * SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION}.
     * @see #checkDefaultAction(Consumer) 
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #doDefaultAction 
     * @see SnakeCommand#DEFAULT_ACTION
     */
    protected void checkDefaultAction(SnakeCommand cmd){
            // If the command is for the default action
        if (cmd == SnakeCommand.DEFAULT_ACTION)
            throw new IllegalArgumentException("The default action should not "
                    + "invoke the default action");
    }
    /**
     * This checks to see if the given {@code Consumer} is a {@link 
     * SnakeActionCommand SnakeActionCommand} with the {@link 
     * SnakeActionCommand#getCommand() command} for invoking the default action, 
     * and if so, throws an IllegalArgumentException stating that the default 
     * action should not invoke the default action. This is equivalent to 
     * checking to see if the given {@code Consumer} is a {@code 
     * SnakeActionCommand}, and if so, invoking {@link 
     * #checkDefaultAction(SnakeCommand) checkDefaultAction} on the command 
     * returned by the {@code SnakeActionCommand}'s {@link 
     * SnakeActionCommand#getCommand() getCommand} method.
     * @param action The consumer to check.
     * @throws IllegalArgumentException If the {@code Consumer} is a {@code 
     * SnakeActionCommand} that invokes the {@link SnakeCommand#DEFAULT_ACTION 
     * DEFAULT_ACTION} command.
     * @see #checkDefaultAction(SnakeCommand) 
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #doDefaultAction 
     * @see SnakeCommand#DEFAULT_ACTION
     */
    protected void checkDefaultAction(Consumer<? super Snake> action){
            // If the consumer is a SnakeActionCommand
        if (action instanceof SnakeActionCommand)
            checkDefaultAction(((SnakeActionCommand)action).getCommand());
    }
    /**
     * This returns the {@code Consumer} used to perform the default action for 
     * this snake. This is the action performed by this snake when the {@link 
     * #doDefaultAction() doDefaultAction} method is invoked when the default 
     * action is {@link #isDefaultActionEnabled() enabled} and non-null. Refer 
     * to the documentation for the {@link #doDefaultAction() doDefaultAction} 
     * method for more information about how the default action is used by a 
     * snake. The default value for this is a {@link SnakeActionCommand 
     * SnakeActionCommand} that invokes the {@link SnakeCommand#MOVE_FORWARD 
     * MOVE_FORWARD} command. <p>
     * 
     * The default action can be used as the action for a snake to perform when 
     * no action is specified. For example, the {@link #doNextAction 
     * doNextAction} method will invoke the {@code doDefaultAction} method when 
     * the {@link #getActionQueue() action queue} is {@link #isActionQueueEmpty 
     * empty}. 
     * 
     * @return The {@code Consumer} that is used as the default action for this 
     * snake, or null.
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #doDefaultAction 
     * @see Consumer#accept 
     * @see SnakeCommand
     * @see SnakeCommand#DEFAULT_ACTION
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see DefaultSnakeActionCommand
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public Consumer<Snake> getDefaultAction(){
        return defaultAction;
    }
    /**
     * This sets the default action for this snake to the given {@code 
     * Consumer}. Refer to the documentation for the {@link #getDefaultAction() 
     * getDefaultAction} for more information about the default action. The 
     * default value for this is a {@link SnakeActionCommand SnakeActionCommand} 
     * that invokes the {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} command. 
     * <p>
     * Note that the default action should not invoke {@link #doDefaultAction 
     * doDefaultAction}, as doing so could result in undefined and unpredictable 
     * behavior. It is also recommended for the default action to not invoke the 
     * {@link #doNextAction doNextAction} method as the {@code doNextAction} may 
     * invoke the {@code doDefaultAction} method when the {@link #getActionQueue 
     * action queue} is {@link #isActionQueueEmpty() empty}.
     * 
     * @param action The {@code Consumer} to use for the default action, or 
     * null. This {@code Consumer} should not invoke {@code doDefaultAction}.
     * @return This snake.
     * @throws IllegalArgumentException If the given action is a {@link 
     * SnakeActionCommand SnakeActionCommand} with the {@link 
     * SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} command as the {@link 
     * SnakeActionCommand#getCommand() command it invokes}.
     * @see #getDefaultAction 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #doDefaultAction 
     * @see #doCommand 
     * @see #doNextAction 
     * @see Consumer#accept 
     * @see SnakeCommand
     * @see SnakeCommand#DEFAULT_ACTION
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see DefaultSnakeActionCommand
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public Snake setDefaultAction(Consumer<Snake> action){
            // If the old default action matches the current one
        if (defaultAction == action)
            return this;
            // Check if the action tries to invoke the default action
        checkDefaultAction(action);
        Consumer<Snake> old = defaultAction;    // Get the old default action
        defaultAction = action;
        firePropertyChange(DEFAULT_ACTION_PROPERTY_CHANGED,old,defaultAction);
        return this;
    }
    /**
     * This sets the default action for this snake to a {@link 
     * SnakeActionCommand SnakeActionCommand} that will invoke the action 
     * associated with the given command. If the given command is null, then the 
     * default action will be set to null. Otherwise, this will retrieve the 
     * {@code SnakeActionCommand} to use for the given command by calling {@link 
     * SnakeCommand#getActionForCommand SnakeCommand.getActionForCommand} with 
     * the command. Refer to the documentation for the {@link #getDefaultAction 
     * getDefaultAction} for more information about the default action. <p>
     * 
     * This is equivalent to calling {@link #setDefaultAction(Consumer) 
     * setDefaultAction} with either the {@code SnakeActionCommand} returned by 
     * {@link SnakeCommand#getActionForCommand SnakeCommand.getActionForCommand} 
     * for the given command or null if the given command is null. <p>
     * 
     * Note that the default action should not invoke {@link #doDefaultAction 
     * doDefaultAction}, as doing so could result in undefined and unpredictable 
     * behavior. As such, the command for the default action should not be 
     * {@link SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} since it would result 
     * in the default action invoking {@code doDefaultAction}.
     * 
     * @param command The command for the action to use as the default action, 
     * or null. This cannot be {@link SnakeCommand#DEFAULT_ACTION 
     * DEFAULT_ACTION}.
     * @return This snake.
     * @throws IllegalArgumentException If the given command is the {@link 
     * SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} command.
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #doDefaultAction 
     * @see #doCommand 
     * @see #doNextAction 
     * @see SnakeCommand
     * @see SnakeCommand#DEFAULT_ACTION
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see SnakeActionCommand#accept 
     * @see DefaultSnakeActionCommand
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public Snake setDefaultAction(SnakeCommand command){
            // Check if the action tries to invoke the default action
        checkDefaultAction(command);
            // If the command is not null, get the action for the given command. 
            // Otherwise, set the default action to null
        return setDefaultAction((command != null)?
                SnakeCommand.getActionForCommand(command):null);
    }
    /**
     * This returns whether the {@link #getDefaultAction() default action} is 
     * enabled. If the default action is enabled and non-null, then invoking the 
     * {@link #doDefaultAction() doDefaultAction} method will cause the default 
     * action to be performed. Refer to the documentation for the {@link 
     * #getDefaultAction getDefaultAction} method and the {@link 
     * #doDefaultAction() doDefaultAction} method for more information about the 
     * default action and how it is used. The default value for this is {@code 
     * true}. 
     * @return Whether the default action is enabled.
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #setDefaultActionEnabled 
     * @see #doDefaultAction 
     * @see #doCommand 
     * @see #doNextAction 
     */
    public boolean isDefaultActionEnabled(){
        return getFlag(DEFAULT_ACTION_ENABLED_FLAG);
    }
    /**
     * This sets whether the {@link #getDefaultAction() default action} is 
     * enabled. Refer to the documentation for the {@link 
     * #isDefaultActionEnabled() isDefaultActionEnabled} method for more 
     * information. The default value for this is {@code true}. 
     * @param enabled Whether the default action should be enabled.
     * @return This snake.
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled
     * @see #doDefaultAction 
     * @see #doCommand 
     * @see #doNextAction 
     */
    public Snake setDefaultActionEnabled(boolean enabled){
            // If the default action enabled flag changed
        if (setFlag(DEFAULT_ACTION_ENABLED_FLAG,enabled))
            firePropertyChange(DEFAULT_ACTION_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    /**
     * This performs the given operation on this snake if the given operation is 
     * not null. In other words, this calls the given {@code Consumer}'s {@link 
     * Consumer#accept accept} method on this snake. Any exception thrown while 
     * performing the given operation will be relayed to the caller of this 
     * method.
     * @param action The operation to perform on this snake, or null.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isValid 
     * @see Consumer#accept 
     */
    protected void doAction(Consumer<? super Snake> action){
        if (action == null)     // If the action is null
            return;
        checkIfInvalid();       // Check if this snake is invalid
        action.accept(this);
    }
    /**
     * This performs the {@link #getDefaultAction() default action} if the 
     * default action is {@link #isDefaultActionEnabled() enabled} and non-null. 
     * If the default action is non-null and enabled, then this will call the 
     * default action's {@link Consumer#accept accept} method and return {@code 
     * true}. Otherwise, this will do nothing and return {@code false}. Refer to 
     * the documentation for the {@link #getDefaultAction() getDefaultAction} 
     * method for more information about the default action. If performing the 
     * default action throws an exception, then the exception will be relayed to 
     * the caller. <p>
     * 
     * This method can be invoked either directly, by the {@link #doCommand 
     * doCommand} method when provided the {@link SnakeCommand#DEFAULT_ACTION 
     * DEFAULT_ACTION} command, or by the {@link #doNextAction doNextAction} 
     * method either when the {@link #peekFirstAction() next action} to perform 
     * invokes this method or when the {@link #getActionQueue() action queue} is 
     * {@link #isActionQueueEmpty() empty}. The last method allows for the 
     * default action to be used as the action to be performed when the snake 
     * has run out of queued actions to perform but still needs to perform an 
     * action on a regular basis. For example, the default action can be used to 
     * have a snake {@link #move() move forward} when the snake is player 
     * controlled, needs to move at a regular interval, and has not received any 
     * input from the player. <p>
     * 
     * Note that the default action should never invoke this method. If the 
     * default action were to invoke this, then this may result in undefined and 
     * unpredictable behavior. Most often this will either cause an exception to 
     * be thrown or result in an infinite loop of recursion. It is also 
     * recommended for the default action to not invoke the {@code doNextAction} 
     * method since the {@code doNextAction} method may invoke this method if 
     * the action queue has run out of actions to perform. 
     * 
     * @return Whether the default action was successfully performed. 
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the default action is a {@link 
     * SnakeActionCommand SnakeActionCommand} with the {@link 
     * SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} command as the {@link 
     * SnakeActionCommand#getCommand() command it invokes}.
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #doCommand 
     * @see #doNextAction 
     * @see #isValid 
     * @see Consumer#accept 
     * @see SnakeCommand
     * @see SnakeCommand#DEFAULT_ACTION
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see DefaultSnakeActionCommand
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public boolean doDefaultAction(){
        checkIfInvalid();       // Check if this snake is invalid
            // Check if the default action is invalid
        checkDefaultAction(getDefaultAction());
            // If the default action is not null and is enabled
        if (getDefaultAction() != null && isDefaultActionEnabled()){
            doAction(getDefaultAction());
            return true;
        }
        return false;
    }
    /**
     * This performs the action associated with the given command. Depending on 
     * the given command, this snake will do one of the following actions: <p>
     * 
     * {@link SnakeCommand#ADD_UP ADD_UP} - calls {@link #add add} with {@link 
     * #UP_DIRECTION up (UP_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#ADD_DOWN ADD_DOWN} - calls {@link #add add} with 
     * {@link #DOWN_DIRECTION down (DOWN_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#ADD_LEFT ADD_LEFT} - calls {@link #add add} with 
     * {@link #LEFT_DIRECTION left (LEFT_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#ADD_RIGHT ADD_RIGHT} - calls {@link #add add} with 
     * {@link #RIGHT_DIRECTION right (RIGHT_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#ADD_FORWARD ADD_FORWARD} - calls {@link #add() add} 
     * with no direction specified. <br>
     * {@link SnakeCommand#MOVE_UP MOVE_UP} - calls {@link #move move} with 
     * {@link #UP_DIRECTION up (UP_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#MOVE_DOWN MOVE_DOWN} - calls {@link #move move} with 
     * {@link #DOWN_DIRECTION down (DOWN_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#MOVE_LEFT MOVE_LEFT} - calls {@link #move move} with 
     * {@link #LEFT_DIRECTION left (LEFT_DIRECTION)} for the direction. <br>
     * {@link SnakeCommand#MOVE_RIGHT MOVE_RIGHT} - calls {@link #move move} 
     * with {@link #RIGHT_DIRECTION right (RIGHT_DIRECTION)} for the direction. 
     * <br>
     * {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} - calls {@link #move() 
     * move} with no direction specified. <br>
     * {@link SnakeCommand#FLIP FLIP} - calls {@link #flip() flip}. <br>
     * {@link SnakeCommand#REVIVE REVIVE} - calls {@link #revive() revive}. <br>
     * {@link SnakeCommand#REMOVE_TAIL REMOVE_TAIL} - calls {@link #removeTail 
     * removeTail}. <br>
     * {@link SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} - calls {@link 
     * #doDefaultAction() doDefaultAction}. <p>
     * 
     * If the command does not match any of the previously mention commands, 
     * then this will do nothing and fire a {@code SnakeEvent} indicating that 
     * the snake {@link SnakeEvent#SNAKE_FAILED failed}. If performing the 
     * action for the command throws an exception, then the exception will be 
     * relayed to the caller.
     * 
     * @param command The command representing which action to perform (cannot 
     * be null).
     * @return Whether this snake was successful in performing the requested 
     * action.
     * @throws NullPointerException If the given command is null.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see SnakeCommand
     * @see SnakeCommand#getDirectionOf 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #move(int) 
     * @see #move() 
     * @see #add(int) 
     * @see #add() 
     * @see #hasConsumedApple 
     * @see #flip 
     * @see #isFlipped 
     * @see #revive 
     * @see #isCrashed 
     * @see #removeTail 
     * @see #doDefaultAction 
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see SnakeUtilities#getDirections 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     */
    public boolean doCommand(SnakeCommand command){
        checkIfInvalid();       // Check if this snake is invalid
            // Check if the given command is not null, and if it isn't, 
            // determine which action to perform
        switch(Objects.requireNonNull(command,"Snake command cannot be null")){
            case MOVE_UP:       // If the command is the move up command
            case MOVE_DOWN:     // If the command is the move down command
            case MOVE_LEFT:     // If the command is the move left command
            case MOVE_RIGHT:    // If the command is the move right command
                    // Move the snake in the direction for the command
                return move(SnakeCommand.getDirectionOf(command));
            case MOVE_FORWARD:  // If the command is the move forward command
                return move();  // Move the snake forward
            case ADD_UP:        // If the command is the add up command
            case ADD_DOWN:      // If the command is the add down command
            case ADD_LEFT:      // If the command is the add left command
            case ADD_RIGHT:     // If the command is the add right command
                    // Add a tile to the snake in the direction for the command
                return add(SnakeCommand.getDirectionOf(command));
            case ADD_FORWARD:   // If the command is the add forward command
                return add();   // Add the tile in front of the snake
            case FLIP:          // If the command is the flip command
                    // Get whether the snake is currently flipped
                boolean flipped = isFlipped();
                flip();         // Flip the snake
                    // Return whether there's been a change to whether the snake
                return flipped != isFlipped();  // is flipped
            case REVIVE:        // If the command is the revive command
                revive();       // Revive the snake
                return !isCrashed();  // Return whether the snake is not crashed
            case REMOVE_TAIL:   // If the command is the remove tail command
                    // Return whether this was able to remove the tail
                return removeTail() != null;
            case DEFAULT_ACTION: // If the command is the default action command
                return doDefaultAction();   // Invoke the default action
            default:            // The command must be an unknown command
                fireSnakeFailed();  // The snake failed to perform a command
                return false;
        }
    }
    /**
     * This returns whether this snake will skip actions in the {@link 
     * #getActionQueue() action queue} when there are adjacent duplicates of an  
     * action. If this is {@code true}, then the {@link #doNextAction 
     * doNextAction} method will discard actions that match the action after it, 
     * resulting in each action being performed once. In other words, if this is 
     * {@code true}, then when this {@link #pollAction() polls} the next action 
     * to perform, the polled action will be compared with what is now the 
     * {@link #peekFirstAction() first action} in the action queue to see if 
     * they are equal to each other, and if so, the polled action will be 
     * discarded and the cycle will repeat until either the action queue is 
     * {@link #isActionQueueEmpty() empty} or the two actions are not equal to 
     * each other. If this is {@code false}, then the {@code doNextAction} 
     * method will not discard repeated actions. The {@code doNextAction} method 
     * may still discard an action for other reasons, such as if the action 
     * cannot be performed due to the snake not being in a state to do so. Refer 
     * to the documentation for the {@link #doNextAction() doNextAction} for 
     * more information on how this determines whether to skip an action and how 
     * this will perform the actions that it does not skip.
     * @return Whether this snake will skip repeats in the action queue.
     * @see #setSkipsRepeatedActions 
     * @see #getActionQueue 
     * @see #doNextAction 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #clearActionQueue 
     */
    public boolean getSkipsRepeatedActions(){
        return getFlag(SKIPS_REPEATED_ACTIONS_FLAG);
    }
    /**
     * This sets whether this snake will skip adjacent repeats of actions in the 
     * {@link #getActionQueue() action queue}. Refer to the documentation for 
     * the {@link #getSkipsRepeatedActions() getSkipsRepeatedActions} method for 
     * more information.
     * @param value Whether this snake should skip repeats in the action queue.
     * @return This snake.
     * @see #getSkipsRepeatedActions 
     * @see #getActionQueue 
     * @see #doNextAction 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #clearActionQueue 
     */
    public Snake setSkipsRepeatedActions(boolean value){
            // If the flag has changed
        if (setFlag(SKIPS_REPEATED_ACTIONS_FLAG, value))
            firePropertyChange(SKIPS_REPEATED_ACTIONS_PROPERTY_CHANGED,value);
        return this;
    }
    /**
     * This returns the action queue for this snake. This is a queue of {@code 
     * Consumer}s to be {@link Consumer#accept performed} on this snake by the 
     * {@link #doNextAction() doNextAction} method. This is intended to be used 
     * for when the actions to be performed by a snake can be generated faster 
     * than a snake is allowed to act upon them. For example, if a snake is set 
     * up to perform an action only when a repeating timer has elapsed, this can 
     * be used to store the actions generated before said timer has elapsed. 
     * @return The action queue for this snake.
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #clearActionQueue 
     * @see #doNextAction 
     */
    public SnakeActionQueue getActionQueue(){
            // If the action queue has not been initialized yet
        if (actionQueue == null)    
            actionQueue = Snake.this.createActionQueue();
        return actionQueue;
    }
    /**
     * This attempts to insert the given action into the {@link #getActionQueue 
     * action queue} and returns whether this was successful at doing so. 
     * @param action The action to add to the action queue (cannot be null).
     * @return Whether the action was added to the action queue.
     * @throws NullPointerException If the given action is null.
     * @see #offerAction(SnakeCommand) 
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     * @see Deque#offer 
     * @see SnakeCommand
     * @see SnakeActionCommand
     * @see DefaultSnakeActionCommand
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public boolean offerAction(Consumer<Snake> action){
        Objects.requireNonNull(action); // Check if the action is null
        return getActionQueue().offer(action);
    }
    /**
     * This attempts to insert a {@link SnakeActionCommand SnakeActionCommand} 
     * into the {@link #getActionQueue action queue} that will perform the given 
     * command and returns whether this was successful at doing so. The {@code 
     * SnakeActionCommand} for the given command will be retrieved by calling 
     * {@link SnakeCommand#getActionForCommand SnakeCommand.getActionForCommand} 
     * with the command. <p>
     * 
     * This is equivalent to calling {@link #offerAction(Consumer) offerAction} 
     * with the {@code SnakeActionCommand} returned by {@link 
     * SnakeCommand#getActionForCommand SnakeCommand.getActionForCommand} for 
     * the given command.
     * 
     * @param command The command for the action to add to the action queue 
     * (cannot be null).
     * @return Whether the action for the command was added to the action queue.
     * @throws NullPointerException If the given command is null.
     * @see #offerAction(Consumer) 
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     * @see Deque#offer 
     * @see SnakeCommand
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     * @see SnakeCommand#getCommandActionMap 
     * @see SnakeCommand#getActionForCommand 
     */
    public boolean offerAction(SnakeCommand command){
        return offerAction(SnakeCommand.getActionForCommand(command));
    }
    /**
     * This removes and returns the {@code Consumer} at the head of the {@link 
     * #getActionQueue() action queue}. If the action queue is {@link 
     * #isActionQueueEmpty() empty}, then this will do nothing and return null. 
     * @return The head of the action queue, or null if the action queue is 
     * empty.
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     * @see Deque#poll 
     */
    public Consumer<Snake> pollAction(){
            // If the action queue is initialized, remove the next action in the 
            // queue. Otherwise, return null.
        return getActionQueue().poll();
    }
    /**
     * This returns, but does not remove, the {@code Consumer} at the head of 
     * the {@link #getActionQueue() action queue}. If the action queue is {@link 
     * #isActionQueueEmpty() empty}, then this will return null. 
     * @return The head of the action queue, or null if the action queue is 
     * empty.
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     * @see Deque#peekFirst 
     */
    public Consumer<Snake> peekFirstAction(){
        return getActionQueue().peekFirst();
    }
    /**
     * This returns, but does not remove, the last {@code Consumer} in the 
     * {@link #getActionQueue() action queue}. If the action queue is {@link 
     * #isActionQueueEmpty() empty}, then this will return null. 
     * @return The tail of the action queue, or null if the action queue is 
     * empty.
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #pollAction 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     * @see Deque#peekLast 
     */
    public Consumer<Snake> peekLastAction(){
        return getActionQueue().peekLast();
    }
    /**
     * This returns the number of actions currently in the {@link 
     * #getActionQueue() action queue}. This may not be entirely indicative of 
     * how many actions this snake will perform as snakes may skip an action if 
     * the action is a repeat or if the action could not be performed by the 
     * snake.
     * @return The size of the action queue.
     * @see #getActionQueue 
     * @see #isActionQueueEmpty 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doNextAction 
     */
    public int getActionQueueSize(){
        return getActionQueue().size();
    }
    /**
     * This returns whether the {@link #getActionQueue() action queue} is empty. 
     * @return Whether the action queue is empty.
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #clearActionQueue 
     * @see #doNextAction 
     */
    public boolean isActionQueueEmpty(){
        return getActionQueue().isEmpty();
    }
    /**
     * This removes all the actions currently in the {@link #getActionQueue() 
     * action queue}. The action queue will be empty after this is called.
     * @see #getActionQueue 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #doNextAction 
     */
    public void clearActionQueue(){
        getActionQueue().clear();
    }
    /**
     * This returns whether this snake should skip the given action. This is 
     * called by {@link #pollNextAction() pollNextAction} to determine if an 
     * action should be skipped. If the action is null, then this will return 
     * {@code true}. If this snake {@link #getSkipsRepeatedActions() skips 
     * repeated actions}, then this will return {@code true} if the given action 
     * is equal to the {@link #peekFirstAction() next action} in the queue. 
     * Otherwise, this will return {@code true} if this snake may not be able to 
     * perform the given action at this moment. This can be the case if the 
     * given action is a {@link SnakeActionCommand SnakeActionCommand} with a 
     * {@link SnakeActionCommand#getCommand() command} that currently cannot be 
     * performed by this snake, such as if the command would result in the snake 
     * attempting to go backwards when the snake has a {@link #getTail() tail} 
     * (i.e. the snake is at least two tiles long), or if the command would 
     * result in the snake attempting to {@link #add(int) add} or {@link 
     * #move(int) move} at all when the snake has {@link #isCrashed() crashed}. 
     * Other such examples include actions with the {@link SnakeCommand#REVIVE 
     * REVIVE} command when the snake has not crashed, actions with the {@link 
     * SnakeCommand#REMOVE_TAIL REMOVE_TAIL} command when the snake does not 
     * have a tail (i.e. the snake is less than two tiles long), and actions 
     * with the {@link SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} when the 
     * snake cannot {@link #doDefaultAction() perform} the {@link 
     * #getDefaultAction() default action} due to it being either null or {@link 
     * #isDefaultActionEnabled() disabled}. <p>
     * 
     * If the given action fits none of the previously mentioned criteria, then 
     * this will return {@code false}.
     * 
     * @param action The action to either be performed or skipped by this snake.
     * @return {@code true} if the given action should be skipped, and {@code 
     * false} if the action should be {@link Consumer#accept performed}.
     * @see #pollNextAction 
     * @see #doNextAction 
     * @see #doCommand 
     * @see #doAction 
     * @see #getActionQueue 
     * @see #peekFirstAction 
     * @see #pollAction 
     * @see #isActionQueueEmpty 
     * @see #getActionQueueSize 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #getDirectionFaced 
     * @see #getTail 
     * @see #size 
     * @see #isCrashed 
     * @see #getDefaultAction 
     * @see #isDefaultActionEnabled 
     * @see #doDefaultAction 
     * @see Consumer#accept 
     * @see Object#equals 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeCommand
     * @see SnakeCommand#getDirectionOf 
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     */
    protected boolean willSkipAction(Consumer<? super Snake> action){
        if (action == null)                     // If the given action is null
            return true;
        else if (getSkipsRepeatedActions()){    // If this snake skips repeats
                // Peek at the next action in the queue
            Consumer<Snake> next = peekFirstAction();    
                // If the next action is the same as the given action
            if (action.equals(next))    
                return true;
        }   // If the action is a SnakeActionCommand
        if (action instanceof SnakeActionCommand){
                // Get the command for the action
            SnakeCommand cmd = ((SnakeActionCommand)action).getCommand();
            if (cmd == null)    // If the command is somehow null
                return true;
            if (getTail() != null){ // If the snake has a tail
                    // Get the direction for the command
                Integer dir = SnakeCommand.getDirectionOf(cmd);
                    // If the command has a direction and that direction is 
                    // opposite to the direction the snake is facing
                if (dir != null && dir == 
                        SnakeUtilities.invertDirections(getDirectionFaced()))
                    return true;
            }
            switch(cmd){            // Determine which command is this
                case MOVE_UP:       // If the command is the move up command
                case MOVE_DOWN:     // If the command is the move down command
                case MOVE_LEFT:     // If the command is the move left command
                case MOVE_RIGHT:    // If the command is the move right command
                case MOVE_FORWARD:  // If the command is the move forward command
                case ADD_UP:        // If the command is the add up command
                case ADD_DOWN:      // If the command is the add down command
                case ADD_LEFT:      // If the command is the add left command
                case ADD_RIGHT:     // If the command is the add right command
                case ADD_FORWARD:   // If the command is the add forward command
                    return isCrashed(); // Skip the action if the snake crashed
                case FLIP:          // If the command is the flip command
                    return false;       // Don't skip actions to flip
                case REVIVE:        // If the command is the revive command
                        // Skip the action if the snake hasn't crashed
                    return !isCrashed();
                case REMOVE_TAIL:   // If the command is the remove tail command
                        // Skip the action if the snake doesn't have a tail
                    return getTail() == null;   
                case DEFAULT_ACTION:// If the command is the default action command
                        // Skip the action if the default action is null or 
                        // disabled
                    return getDefaultAction()==null||!isDefaultActionEnabled();
                default:            // The command must be an unknown command
                    return true;    // Just skip this action, since it's unknown
            }
        }
        return false;
    }
    /**
     * This removes and returns the next action in the {@link #getActionQueue() 
     * action queue} to be performed by the {@link #doNextAction doNextAction} 
     * method. This will continuously {@link #pollAction() poll} the action 
     * queue for the action queue until either the action queue is {@link 
     * #isActionQueueEmpty() empty} or {@link #willSkipAction willSkipAction} 
     * returns {@code false} for the polled action, after which the last action 
     * to be polled will be returned. If the action queue is empty or {@code 
     * willSkipAction} returned {@code true} for all the actions that were in 
     * the queue, then this will return null to signify that {@code 
     * doNextAction} should perform the {@link #doDefaultAction() default 
     * action}. Refer to the documentation for the {@link #willSkipAction 
     * willSkipAction} method for more information on how this decides to skip 
     * an action, and for the {@link #doNextAction() doNextAction} method for 
     * how this performs the returned action.
     * @return The next action to be performed, or null to perform the default 
     * action.
     * @see #willSkipAction 
     * @see #doNextAction 
     * @see #doCommand 
     * @see #doAction 
     * @see #doDefaultAction 
     * @see #getActionQueue 
     * @see #pollAction 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #isActionQueueEmpty 
     * @see #getActionQueueSize 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see Consumer#accept 
     */
    protected Consumer<? super Snake> pollNextAction(){
        return getActionQueue().pollNext();
    }
    /**
     * This performs the next action in the {@link #getActionQueue() action 
     * queue}, or the {@link #doDefaultAction() default action} if no action 
     * from the action queue can be performed. If the action queue is not {@link 
     * #isActionQueueEmpty() empty}, then this will first {@link #pollAction() 
     * poll} the next action to perform and checks to see if the action should 
     * be skipped. When an action is skipped, it is discarded and the next 
     * action is polled from the action queue and checked. This is repeated 
     * until either this has reached an action in the action queue that should 
     * not be skipped or the action queue is empty. Once this has reached an 
     * action that can be performed, then this will call the action's {@link 
     * Consumer#accept accept} method on this snake. If the action queue does 
     * not contain any actions that can be performed, either because it is empty 
     * or because all the actions in the action queue have been discarded, then 
     * this will invoke the {@link #doDefaultAction() doDefaultAction} method to 
     * perform the {@link #getDefaultAction() default action} for this snake. 
     * Any exception thrown while performing the action will be relayed to the 
     * caller of this method. <p>
     * 
     * When checking to see if an action should be skipped, this will first 
     * check to see if the action is null. If the action is null, then it will 
     * be skipped. If the action is not null, then this will check to see if 
     * this snake {@link #getSkipsRepeatedActions skips repeated actions}. If 
     * this snake skips repeated actions, then this will compare the action in 
     * question with the action currently at the {@link #peekFirstAction front} 
     * of the action queue, and if they are equal to each other, then the action 
     * in question will be skipped. Otherwise, this checks to see if the action 
     * is a {@link SnakeActionCommand SnakeActionCommand} that currently cannot 
     * be performed by this snake due to its {@link 
     * SnakeActionCommand#getCommand() command}. This is the case if the command 
     * would result in the snake attempting to do one of the following: <p>
     * 
     * Attempt to {@link #add(int) add} or {@link #move(int) move} backwards 
     * when it has a {@link #getTail() tail} (i.e. when the snake is at least 
     * two tiles long). <br>
     * Attempt to add or move when the snake has {@link #isCrashed() crashed}. 
     * <br>
     * Attempt to {@link SnakeCommand#REVIVE revive} the snake when the snake 
     * has not crashed. <br>
     * Attempt to {@link SnakeCommand#REMOVE_TAIL remove its tail} when the 
     * snake does not have a tail (i.e. when the snake is less than two tiles 
     * long). <br>
     * Attempt to {@link SnakeCommand#DEFAULT_ACTION perform the default action} 
     * when the default action is either null or {@link #isDefaultActionEnabled 
     * disabled}. <p>
     * 
     * If an action does not fit any of the above criteria for being skipped, 
     * then it will be performed by this snake.
     * 
     * @throws IllegalStateException If this snake is not in a {@link #isValid 
     * valid} state.
     * @see #getActionQueue 
     * @see #offerAction(Consumer) 
     * @see #offerAction(SnakeCommand) 
     * @see #peekFirstAction 
     * @see #peekLastAction 
     * @see #pollAction 
     * @see #getActionQueueSize 
     * @see #isActionQueueEmpty 
     * @see #clearActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #doDefaultAction 
     * @see #getDefaultAction 
     * @see #setDefaultAction(Consumer) 
     * @see #setDefaultAction(SnakeCommand) 
     * @see #isDefaultActionEnabled 
     * @see #setDefaultActionEnabled 
     * @see #doCommand 
     * @see #isValid 
     * @see #getDirectionFaced 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isCrashed 
     * @see #revive 
     * @see Consumer#accept 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeCommand
     * @see SnakeCommand#getDirectionOf 
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     */
    public void doNextAction(){
        checkIfInvalid();       // Check if this snake is invalid
            // Get the next action for this snake to perform
        Consumer<? super Snake> action = pollNextAction();
        if (action != null)     // If the next action is not null
            doAction(action);   // Do the next action
        else
            doDefaultAction();  // Default to performing the default action
    }
    /**
     * This returns a String representation of this snake. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this snake.
     */
    protected String paramString(){
        return Objects.toString(getName(),"")+
                    // If the snake is invalid, then say that the snake is 
                ((isValid())?"":",invalid")+    // invalid
                ",head="+Objects.toString(getHead(),"")+
                ",tail="+Objects.toString(getTail(),"")+
                ",size="+size()+
                ",flags="+getFlags()+
                ",allowedFails="+getAllowedFails()+
                ",failCount="+getFailCount()+
                ",defaultAction="+Objects.toString(getDefaultAction(), "")+
                ",queuedActions="+getActionQueueSize()+
                ",nextAction="+Objects.toString(peekFirstAction(), "");
    }
    /**
     * This returns a String representation of this snake and its values.
     * @return A String representation of this snake.
     */
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
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
    @SuppressWarnings("unchecked")
    public <T extends EventListener> T[] getListeners(Class<T> listenerType){
            // If we're getting the PropertyChangeListeners
        if (listenerType == PropertyChangeListener.class)
            return (T[])getPropertyChangeListeners();
        else
            return listenerList.getListeners(listenerType);
    }
    /**
     * This adds the given {@code SnakeListener} to this snake.
     * @param l The {@code SnakeListener} to add.
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    public void addSnakeListener(SnakeListener l){
        if (l != null)  // If the listener is not null
            listenerList.add(SnakeListener.class, l);
    }
    /**
     * This removes the given {@code SnakeListener} from this snake.
     * @param l The {@code SnakeListener} to remove.
     * @see #addSnakeListener 
     * @see #getSnakeListeners 
     */
    public void removeSnakeListener(SnakeListener l){
        listenerList.remove(SnakeListener.class, l);
    }
    /**
     * This returns an array containing all the {@code SnakeListener}s that have 
     * been added to this snake.
     * @return An array containing the {@code SnakeListener}s that have been 
     * added, or an empty array if none have been added.
     * @see #addSnakeListener
     * @see #removeSnakeListener 
     */
    public SnakeListener[] getSnakeListeners(){
        return listenerList.getListeners(SnakeListener.class);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of the given event if the event is not null.
     * @param evt The {@code SnakeEvent} to be fired.
     * @see #fireSnakeChange(int, Integer, Tile) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeFailed() 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(SnakeEvent evt){
        if (evt == null)    // If the event is null
            return;
            // A for loop to go through the snake listeners
        for (SnakeListener l : getSnakeListeners()){
            if (l != null)  // If the listener is not null
                l.snakeChanged(evt);
        }
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID, direction, and target tile. If 
     * the given direction is null, then the {@link #getDirectionFaced() 
     * direction currently being faced} will be used.
     * @param id The event ID indicating what type of event occurred.
     * @param direction The direction for the event, or null to use the 
     * direction that the snake is facing.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id, Integer direction,Tile target){
        fireSnakeChange(new SnakeEvent(this,id,
                    // If the direction is not null, use it. Otherwise, use the 
                    // direction being faced by this snake
                (direction!=null)?direction:getDirectionFaced(),target));
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID and direction. If the given 
     * direction is null, then the {@link #getDirectionFaced() direction 
     * currently being faced} will be used. The target tile will be null.
     * @param id The event ID indicating what type of event occurred.
     * @param direction The direction for the event, or null to use the 
     * direction that the snake is facing.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer, Tile) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id, Integer direction){
        fireSnakeChange(id,direction,null);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID, the direction that this snake 
     * is {@link #getDirectionFaced() currently facing}, and the target tile.
     * @param id The event ID indicating what type of event occurred.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id, Tile target){
        fireSnakeChange(id,getDirectionFaced(),target);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID and the direction that this 
     * snake is {@link #getDirectionFaced() currently facing}. The target tile 
     * will be null.
     * @param id The event ID indicating what type of event occurred.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id){
        fireSnakeChange(id,getDirectionFaced(),null);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the given 
     * direction and with the given target tile. If the given direction is null, 
     * then the {@link #getDirectionFaced() direction currently being faced} 
     * will be used.
     * @param direction The direction for the event, or null to use the 
     * direction that the snake is facing.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeFailed() 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(Integer direction, Tile target){
        fireSnakeChange(SnakeEvent.SNAKE_FAILED,direction,target);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the given 
     * direction. If the given direction is null, then the {@link 
     * #getDirectionFaced() direction currently being faced} will be used. The 
     * target tile will be null.
     * @param direction The direction for the event, or null to use the 
     * direction that the snake is facing.
     * @see #fireSnakeFailed() 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(Integer direction){
        fireSnakeFailed(direction,null);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the {@link 
     * #getDirectionFaced() direction currently being faced} by the snake and 
     * with the given target tile.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(Tile target){
        fireSnakeFailed(getDirectionFaced(),target);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the {@link 
     * #getDirectionFaced() direction currently being faced} by the snake. The 
     * target tile will be null.
     * @see #fireSnakeFailed(Integer) 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, Integer) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(){
        fireSnakeFailed(getDirectionFaced(),null);
    }
    /**
     * This adds a {@code PropertyChangeListener} to this snake. This listener 
     * is registered for all bound properties of this snake. 
     * @param l The listener to be added.
     * @see #addPropertyChangeListener(String, PropertyChangeListener) 
     * @see #removePropertyChangeListener(PropertyChangeListener) 
     * @see #getPropertyChangeListeners() 
     */
    public void addPropertyChangeListener(PropertyChangeListener l){
        changeSupport.addPropertyChangeListener(l);
    }
    /**
     * This removes a {@code PropertyChangeListener} from this snake. This 
     * method should be used to remove {@code PropertyChangeListener}s that were 
     * registered for all bound properties of this snake. 
     * @param l The listener to be removed.
     * @see #addPropertyChangeListener(PropertyChangeListener) 
     * @see #removePropertyChangeListener(String, PropertyChangeListener) 
     * @see #getPropertyChangeListeners() 
     */
    public void removePropertyChangeListener(PropertyChangeListener l){
        changeSupport.removePropertyChangeListener(l);
    }
    /**
     * This returns an array of all {@code PropertyChangeListener}s that are 
     * registered on this snake.
     * @return An array of the {@code PropertyChangeListener}s that have been 
     * added, or an empty array if no listeners have been added.
     * @see #getPropertyChangeListeners(String) 
     * @see #addPropertyChangeListener(PropertyChangeListener) 
     * @see #removePropertyChangeListener(PropertyChangeListener) 
     */
    public PropertyChangeListener[] getPropertyChangeListeners(){
        return changeSupport.getPropertyChangeListeners();
    }
    /**
     * This adds a {@code PropertyChangeListener} to this snake that listens for 
     * a specific property.
     * @param propertyName The name of the property to listen for.
     * @param l The listener to be added.
     * @see #addPropertyChangeListener(PropertyChangeListener) 
     * @see #removePropertyChangeListener(String, PropertyChangeListener) 
     * @see #getPropertyChangeListeners(String) 
     */
    public void addPropertyChangeListener(String propertyName, 
            PropertyChangeListener l){
        changeSupport.addPropertyChangeListener(propertyName, l);
    }
    /**
     * This removes a {@code PropertyChangeListener} to this snake that listens 
     * for a specific property. This method should be used to remove {@code 
     * PropertyChangeListener}s that were registered for a specific property
     * @param propertyName The name of the property.
     * @param l The listener to be removed.
     * @see #removePropertyChangeListener(PropertyChangeListener)
     * @see #addPropertyChangeListener(String, PropertyChangeListener) 
     * @see #getPropertyChangeListeners(String) 
     */
    public void removePropertyChangeListener(String propertyName, 
            PropertyChangeListener l){
        changeSupport.removePropertyChangeListener(propertyName, l);
    }
    /**
     * This returns an array of all {@code PropertyChangeListener}s that are 
     * registered on this snake for a specific property.
     * @param propertyName The name of the property.
     * @return An array of the {@code PropertyChangeListener}s that have been 
     * added for the specified property, or an empty array if no listeners have 
     * been added or the specified property is null.
     * @see #getPropertyChangeListeners() 
     * @see #addPropertyChangeListener(String, PropertyChangeListener) 
     * @see #removePropertyChangeListener(String, PropertyChangeListener) 
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName){
        return changeSupport.getPropertyChangeListeners(propertyName);
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name,
     * old value, and new value. This method is for {@code Object} properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, Object oldValue, 
            Object newValue){
            // If the PropertyChangeSupport has been initialized
        if (changeSupport != null)  
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for {@code boolean} properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, boolean oldValue, 
            boolean newValue){
            // If the PropertyChangeSupport has been initialized
        if (changeSupport != null)  
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name and 
     * new value. This method is for {@code boolean} properties and the old 
     * value is assumed to be the inverse of the new value.
     * @param propertyName The name of the property.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, boolean newValue){
        firePropertyChange(propertyName, !newValue, newValue);
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for integer properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, int oldValue, 
            int newValue){
            // If the PropertyChangeSupport has been initialized
        if (changeSupport != null)  
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for byte properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, byte oldValue, 
            byte newValue){
        firePropertyChange(propertyName,Byte.valueOf(oldValue),
                Byte.valueOf(newValue));
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for character properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, char oldValue, 
            char newValue){
        firePropertyChange(propertyName,Character.valueOf(oldValue),
                Character.valueOf(newValue));
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for short properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, short oldValue, 
            short newValue){
        firePropertyChange(propertyName,Short.valueOf(oldValue),
                Short.valueOf(newValue));
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for long properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, long oldValue, 
            long newValue){
        firePropertyChange(propertyName,Long.valueOf(oldValue),
                Long.valueOf(newValue));
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for float properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, float oldValue, 
            float newValue){
        firePropertyChange(propertyName,Float.valueOf(oldValue),
                Float.valueOf(newValue));
    }
    /**
     * This fires a {@code PropertyChangeEvent} with the given property name, 
     * old value, and new value. This method is for double properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, double oldValue, 
            double newValue){
        firePropertyChange(propertyName,Double.valueOf(oldValue),
                Double.valueOf(newValue));
    }
    /**
     * This is the Iterator implementation returned by {@link #iterator()} which  
     * goes through the tiles that comprise the body of this snake. This 
     * internally uses an iterator retrieved from the queue used to store the 
     * tiles that make up the body of this snake. The iterator used will depend 
     * on whether this snake was {@link #isFlipped() flipped} or not when this 
     * iterator was constructed, with the {@link ArrayDeque#descendingIterator 
     * descending iterator} used when the snake is flipped and the {@link 
     * ArrayDeque#iterator() ascending iterator} used when the snake is not 
     * flipped. If the snake is flipped or if any change is made to the snake's 
     * body after this is created, then this will generally throw a {@link 
     * ConcurrentModificationException}.
     */
    private class SnakeIterator implements Iterator<Tile>{
        /**
         * The iterator from the ArrayDeque used to store the snake body. This 
         * is either the ascending iterator or the descending iterator 
         * depending on whether the snake was flipped when this iterator was 
         * constructed.
         */
        private final Iterator<Tile> iterator;
        /**
         * This stores whether the snake was flipped or not when this iterator 
         * was constructed.
         */
        private final boolean flipped;
        
        private Tile previous = null;
        
        private Tile current = null;
        /**
         * This constructs a SnakeIterator.
         */
        public SnakeIterator(){
                // If the snake is flipped, get the descending iterator from the 
                // snake body. Otherwise, get the normal iterator from the body
            iterator = (isFlipped()) ? snakeBody.descendingIterator() : 
                    snakeBody.iterator();
            flipped = isFlipped();
        }
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
        @Override
        public Tile next() {
                // If the snake has been flipped since this iterator's 
            if (flipped != isFlipped()) // construction
                throw new ConcurrentModificationException();
            previous = current;
            current = iterator.next();
            return current;
        }
        
//        public void remove(){
//            // Remove tile, then try to heal the snake by ensuring the previous tile will join up with the next
//        }
    }
}
