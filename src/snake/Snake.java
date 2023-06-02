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
 * This is a collection of {@link Tile tiles} used to represent a snake in the 
 * game Snake. Snakes are displayed on and move around on a play field 
 * represented by a {@link PlayFieldModel PlayFieldModel}. Snakes implement the 
 * {@link Queue Queue} and {@link Set Set} interfaces, inheriting the element 
 * order from {@code Queue} and the stipulations on duplicate elements from 
 * {@code Set}. As such, snakes order tiles in a FIFO (first-in-first-out) 
 * manner like a queue, and snakes contain no duplicate tiles like a set. Null 
 * tiles and tiles not contained within the snake's {@code PlayFieldModel} are 
 * prohibited. <p>
 * 
 * Tiles are inserted at the front of the snake (the <em>tail</em> of the 
 * queue), and the last tile in the snake is the <em>head</em> of the queue (the 
 * tile which would be removed by {@link #remove() remove} or {@link #poll 
 * poll}). However, since the <em>head</em> of the snake is the tile at the 
 * front of the snake, and the <em>tail</em> of the snake is the tile at the end 
 * of the snake, this means that snakes act as though they a queue but in 
 * reverse. As such, the <em>head</em> of the <em>snake</em> is the 
 * <em>tail</em> of the <em>queue</em>, and the <em>tail</em> of the 
 * <em>snake</em> is the <em>head</em> of the <em>queue</em>. Unless otherwise 
 * specified, the first tile in the snake will be referred to as the 
 * <em>head</em>, and the last tile in the snake as the <em>tail</em>. This 
 * includes the {@link #getHead getHead} and {@link #getTail getTail} methods, 
 * which return the first and last tiles in the snake, respectively. However, if 
 * the snake is less than two tiles long, then the snake is said to not have a 
 * tail and {@code getTail} method will return null. When the snake is two or 
 * more tiles long, then the snake is said to have a tail and the {@code 
 * getTail} method is equivalent to calling {@link #peek peek}. The iterators 
 * returned by the snake's {@link #iterator iterator} method iterates through 
 * the tiles in the snake starting at the head of the <em>snake</em> and ending 
 * at the <em>tail</em> of the snake. Snakes can be {@link #flip flipped}. When 
 * a snake is flipped, the order of the tiles in the snake will be reversed. <p>
 * 
 * A snake must be initialized with a tile from the {@code PlayFieldModel} 
 * before it can be used. A snake will need to be initialized after it is 
 * constructed if no tile was provided to the constructor. A snake will need to 
 * be reinitialized if it ever becomes {@link #isEmpty empty}, such as when the 
 * {@code PlayFieldModel} for the snake changes. A snake can be initialized via 
 * either the {@code Snake} constructors that take in either a tile or a row and 
 * column, the {@link #initialize(Tile) initialize} methods, or by adding a tile 
 * to an empty snake via the {@link #add(Tile) add(Tile)} and {@link #offer 
 * offer(Tile)} methods. When a snake is initialized, the snake will be reset 
 * and the tile provided to it will be used as the snake's head. When a snake is 
 * {@link #clear reset}, all the tiles currently in the snake are removed and 
 * {@link Tile#clear cleared}, the status for the snake will be reset, and the 
 * snake's fail count will be reset to zero. <p>
 * 
 * Once a snake has been initialized, it will be in a {@link #isValid valid} 
 * state. For a snake to be in a valid state, the snake must not be empty, its 
 * head must be a non-null tile {@link PlayFieldModel#contains(Tile) contained} 
 * within its {@code PlayFieldModel}, and the snake must be facing a single 
 * direction. A snake must be in a valid state for certain methods to work, such 
 * as status changing methods like {@link #flip flip} and {@link #revive 
 * revive}, methods that get tiles adjacent to the snake's head like {@link 
 * #getAdjacentToHead getAdjacentToHead} and {@link #getTileBeingFaced 
 * getTileBeingFaced}, methods involved with moving the snake like {@link 
 * #add(int) add(int)} and {@link #move(int) move(int)}, and methods involved 
 * with performing actions like {@link #doCommand doCommand}, {@link 
 * #doDefaultAction doDefaultAction}, and {@link #doNextAction doNextAction}. If 
 * a method requires the snake to be in a valid state, then the method will 
 * typically throw an {@link IllegalStateException IllegalStateException} when 
 * the calling snake is not in a valid state. <p>
 * 
 * Some of the operations provided by snakes are direction based. These methods 
 * are primarily involved with adding tiles to and moving a snake. Each of these 
 * methods comes in two forms: one that takes in an integer representing a 
 * direction, and the other uses the direction the snake is facing. The former 
 * form will typically require the value for the direction to be either zero or 
 * one of the four direction flags: {@link #UP_DIRECTION}, {@link 
 * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. 
 * These methods will typically interpret a direction of zero as the direction 
 * that the snake is facing. In other words, these methods will substitute a 
 * direction of zero with the direction returned by the {@link 
 * #getDirectionFaced getDirectionFaced} method. The latter form of these 
 * methods will typically invoke their respective former form with the direction 
 * the snake is facing. In other words, the latter form of the methods are 
 * equivalent to calling their respective former form with the direction 
 * returned by the {@code getDirectionFaced} method. A summary of the direction 
 * based methods can be found in the table below:
 * 
 * <table class="striped">
 * <caption>Summary of direction based {@code Snake} methods</caption>
 * <thead>
 *  <tr>
 *      <th></th>
 *      <th>Uses given direction</th>
 *      <th>Uses direction faced</th>
 *  </tr>
 *  </thead>
 *  <tbody>
 *      <tr>
 *          <th scope="row">Get adjacent tile</th>
 *          <td>{@link #getAdjacentToHead getAdjacentToHead(int)}</td>
 *          <td>{@link #getTileBeingFaced getTileBeingFaced()}</td>
 *      </tr>
 *      <tr>
 *          <th scope="row">Can move/add tile</th>
 *          <td>{@link #canMoveInDirection canMoveInDirection(int)}</td>
 *          <td>{@link #canMoveForward canMoveForward()}</td>
 *      </tr>
 *      <tr>
 *          <th scope="row">Add adjacent tile</th>
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
 * The {@code add(int)} and {@code add()} methods are used to add tiles that are 
 * adjacent to a snake's head, and will return whether the tile was successfully 
 * added to the snake. The {@code move(int)} and {@code move()} methods are used 
 * to move a snake, and will return whether the snake was successfully moved. 
 * The main difference between adding tiles to a snake and moving a snake is 
 * that when a snake moves, its current tail is removed so as to maintain the 
 * snake's length after adding a tile to the snake. <p>
 * 
 * The {@code getAdjacentToHead} and {@code getTileBeingFaced} methods are used 
 * to provide the tile to be added to the snake from the {@code PlayFieldModel} 
 * by using the model's {@link PlayFieldModel#getAdjacentTile getAdjacentTile} 
 * method to get a tile adjacent to the snake's head. A snake that is configured 
 * to {@link #isWrapAroundEnabled wrap around} will be able to get tiles from 
 * the other side of the play field when the adjacent tile would otherwise be 
 * out of bounds. If the snake is not configured to wrap around, then attempting 
 * to get an out of bounds adjacent tile would return null. As such, when a 
 * snake attempts to add or move to a tile that would be out of bounds, it will 
 * either wrap around and use a tile from the other side of the play field or 
 * the snake will fail to add or move to a tile, depending on whether the snake 
 * is configured to wrap around. <p>
 * 
 * Snakes can only add or move to a tile if it is a non-null tile that is either 
 * {@link Tile#isEmpty empty} or an {@link Tile#isApple apple tile}. Snakes are 
 * only able to add or move to apple tiles if they can {@link 
 * #isAppleConsumptionEnabled eat apples}. If a snake cannot eat apples, then 
 * the snake will only be able to add or move to empty tiles. When a snake adds 
 * or moves to an apple tile, the snake will fire a {@code SnakeEvent} 
 * indicating that the {@link SnakeEvent#SNAKE_CONSUMED_APPLE snake ate an 
 * apple} and the snake's {@link #hasConsumedApple hasConsumedApple} method will 
 * return {@code true}. The {@link #canMoveInDirection canMoveInDirection} and 
 * {@link #canMoveForward canMoveForward} methods can be used to check to see if 
 * a snake can add or move in a given direction based off the tile the snake 
 * would be attempting to add or move to. Additionally, a snake can only add or 
 * move to tiles if it has not {@link #isCrashed crashed}. A snake will crash if 
 * its {@link #getFailCount fail count} exceeds a set {@link #getAllowedFails 
 * allowed number of failures}. If a snakes allowed number of failures is 
 * negative, then the snake will be allowed to fail an unlimited number of times 
 * without crashing. A snake's fail count will be incremented whenever it fails 
 * to add or move to a tile, excluding attempts to add or move the snake 
 * backwards (i.e. in the direction opposite to the direction the snake is 
 * facing) when the snake has a tail and attempts to add or move the snake when 
 * it has crashed. A snake's fail count is reset when either the snake 
 * successfully adds or moves to a tile, the snake is reset, or when the snake 
 * is revived. A crashed snake cannot be moved or added to until it has been 
 * revived. This does not apply to the {@code add(Tile)} and {@code offer(Tile)} 
 * methods, for which cannot add apple tiles and ignore whether the snake has 
 * crashed. They will, however, reset the fail count but this will not revive 
 * the snake if it has crashed. <p>
 * 
 * The {@link #removeTail removeTail} method is similar to the {@link #poll 
 * poll} method in that it will remove and return the last tile (the snake's 
 * tail). However, unlike the {@code poll} method, if the snake does not have a 
 * tail, then the {@code removeTail} method will do nothing and return null. <p>
 * 
 * A snake's {@link #getPlayerType player type} indicates what {@link 
 * Tile#getType type} of {@link Tile#isSnake snake tiles} a snake will be 
 * comprised of and whether a snake represents player one (i.e. a primary snake) 
 * or player two (i.e. a secondary snake). A snake does not necessarily need to 
 * represent an actual player, and multiple snakes may use the same player type. 
 * When a tile is added to a snake, the tile will have its type flag set to the 
 * snake's player type. <p>
 * 
 * Snakes make certain assumptions about the tiles they are comprised of. Snakes 
 * assume that they are continuous. That is to say, when there are more than one 
 * tile in the snake, then each tile in the snake will lead into the tiles 
 * before and after it. For this to be the case, the direction(s) set on each 
 * tile in the snake should result in it joining up with the tiles before and 
 * after it. For two tiles, {@code tile1} and {@code tile2}, to join up, {@code 
 * tile1} must have at least one direction set on it that is opposite to a 
 * direction set on {@code tile2}, or equivalently {@code tile1} must have at 
 * least one direction set on it for which invoking {@code tile1.}{@link 
 * Tile#alterDirection(Tile) alterDirection}{@code (tile2)} would result in 
 * {@code tile1} no longer having that direction set. Snakes also assume that, 
 * with the exception of the first and last tiles, all tiles contained within 
 * the snake have two directions set, and that the first and last tiles only 
 * have one direction set. Another assumption snakes make is that, after a tile 
 * has been added to the snake, the tile will remain present within the snake's 
 * {@code PlayFieldModel} for the entire duration that it is a part of the 
 * snake, and will not be removed from the {@code PlayFieldModel} before being 
 * removed from the snake. If these assumptions turn out to be wrong, then a 
 * snake may exhibit undefined and unpredictable behavior. The {@link #repair 
 * repair} method of a snake can be used at any time to correct this and ensure 
 * that these assumptions reign true for all the tiles in the snake. <p>
 * 
 * Snakes have an {@link Snake.ActionQueue action queue} that can be used to 
 * store {@code Consumer}s to be performed later. It is intended to be used for 
 * when the actions to be performed by a snake can be generated faster than a 
 * snake is allowed to act upon them. For example, if a player controlled snake 
 * is set up to perform an action only when a repeating timer has elapsed, then 
 * the action queue can be used to store the actions generated by the player 
 * before said timer has elapsed. This way, these actions can be performed by 
 * the snake even if they are generated too early or too late, or if they are 
 * generated at a rate faster than they can be performed. <p>
 * 
 * The action queue for a snake can be accessed via the {@link #getActionQueue 
 * getActionQueue} method. The {@link #doNextAction doNextAction} method is used 
 * to have a snake perform the next action in its action queue. When invoked, 
 * the {@code doNextAction} method will invoke the action queue's {@link 
 * ActionQueue#pollNext pollNext} method to get the next action to perform. If 
 * the action queue's {@code pollNext} method returns null, then the {@code 
 * doNextAction} method will perform the snake's default action.  <p>
 * 
 * A snake's {@link #getDefaultAction default action} is a {@code Consumer} that 
 * the snake can invoke to perform some action. When the default action is set 
 * to a non-null value and is {@link #isDefaultActionEnabled enabled}, the 
 * {@link #doDefaultAction doDefaultAction} method can be used to have the snake 
 * perform it's default action. If the default action is either null or 
 * disabled, then {@code doDefaultAction} will do nothing. The default action 
 * can be set either with a {@code Consumer} or with a {@link SnakeCommand 
 * SnakeCommand}, the latter of which will result in the default action being 
 * set to a {@link SnakeActionCommand SnakeActionCommand} that will perform the 
 * command. The {@code doNextAction} method will invoke the {@code 
 * doDefaultAction} method when the action queue's {@code pollNext} method 
 * returns null. <p>
 * 
 * The iterators returned by this class's {@code iterator} method are fail-fast,
 * i.e. if the snake is structurally modified in any way at any time after the 
 * iterator is created except via the iterator's own {@code remove} method, then 
 * the iterator will generally throw a {@link ConcurrentModificationException 
 * ConcurrentModificationException}. This way, when faced with concurrent 
 * modification, the iterator will fail quickly and cleanly instead of risking 
 * arbitrary, non-deterministic behavior. However, the fail-fast behavior of the 
 * iterator cannot be guaranteed, especially when dealing with unsynchronized 
 * concurrent modifications. The fail-fast iterators throw {@code 
 * ConcurrentModificationExceptions} on a best-effort basis. As such the 
 * fail-fast behavior should not be depended on for its correctness and should 
 * only be used to detect bugs.
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
 * @see ActionQueue
 */
public class Snake extends AbstractQueue<Tile> implements SnakeConstants, 
        Set<Tile>{
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
     * queue when a snake is "{@link #isFlipped() flipped}". There should be no 
     * duplicate tiles in this queue.
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
     * initially null, and will be initialized when it is first requested via 
     * the {@link #getActionQueue getActionQueue} method.
     */
    private ActionQueue actionQueue = null;
    /**
     * This stores the most recently performed action from the action queue. 
     */
    private Consumer<Snake> prevAction = null;
    /**
     * This constructs a Snake that will be displayed on the given model. The 
     * snake will be able to wrap around and eat apples and will grow when it 
     * does. The snake will also represent the first player (i.e. this will be a 
     * primary snake) and can fail an unlimited amount of times without 
     * crashing. The snake's default action will be enabled and will move the 
     * snake forward. The snake will need to be {@link #initialize initialized} 
     * with a tile from the model before it can be used.
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
     * move the snake forward. <p>
     * 
     * This is equivalent to {@code new } {@link Snake#Snake(PlayFieldModel) 
     * Snake}{@code (model).}{@link #initialize(Tile) initialize}{@code (head)}.
     * 
     * @param model The PlayFieldModel that provides the tiles for the snake 
     * (cannot be null). 
     * @param head The tile to use as the head for the snake. 
     * @throws NullPointerException If either the model or the tile are null.
     * @throws IllegalArgumentException If the given tile is either not in the 
     * given model or is neither empty nor a snake tile. If the given tile is a 
     * snake tile, then this will also be thrown if it is either facing multiple 
     * directions or if the tile's {@link Tile#getType type flag} is set.
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
     * action will be enabled and will move the snake forward. <p>
     * 
     * This is equivalent to {@code new } {@link Snake#Snake(PlayFieldModel) 
     * Snake}{@code (model).}{@link #initialize(int, int) 
     * initialize}{@code (row, column)}. 
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
     * in the given model is neither empty nor a snake tile. If the tile is a 
     * snake tile, then this will also be thrown if it is either facing multiple 
     * directions or if the tile's {@link Tile#getType type flag} is set.
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
    /**
     * This constructs an action queue for this snake.
     * @return The action queue that was constructed.
     * @see #getActionQueue 
     * @see ActionQueue
     */
    protected ActionQueue createActionQueue(){
        return new ActionQueue();
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
     * @see #SKIPS_REPEATED_ACTIONS_FLAG
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
     * @see #SKIPS_REPEATED_ACTIONS_FLAG
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
     * @see #SKIPS_REPEATED_ACTIONS_FLAG
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
     * @see #SKIPS_REPEATED_ACTIONS_FLAG
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
    /**
     * This returns whether this snake contains the given object. More formally, 
     * this returns {@code true} if this snake contains an element {@code e} 
     * such that {@code Objects.equals(o, e)}.
     * @param o The object to check for.
     * @return Whether the given object is in this snake.
     * @see #contains(int, int) 
     */
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
     * @see #contains(Object) 
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
     * assumes that the given tile is from the {@link #getModel model} and has a 
     * direction set for it. The given tile will have its {@link Tile#setType 
     * type} set to the {@link #getPlayerType player type} of this snake and 
     * will be used to {@link Tile#alterDirection(Tile) alter the direction} of 
     * the previous head if there is one. This will also {@link #resetFailCount 
     * reset} the {@link #getFailCount fail count} for this snake. However, this 
     * snake will not be {@link #revive revived} automatically. If the given 
     * tile is already in this snake, then this will do nothing and return 
     * {@code false}.
     * @param tile The new head for this snake (cannot be null).
     * @return Whether the tile was added to this snake.
     * @throws NullPointerException If the given tile is null.
     * @see #insertTail 
     * @see #pollHead 
     * @see #pollTail 
     * @see #checkOfferedTile 
     * @see #offer 
     * @see #add(Tile) 
     * @see #canAddTile 
     * @see #addOrMove 
     * @see #getHead 
     * @see #getTail 
     * @see #peek
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #size 
     * @see #getPlayerType 
     * @see #isFlipped 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #resetFailCount 
     * @see #revive 
     * @see Tile#setType 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#alterDirection(Tile) 
     */
    protected boolean insertHead(Tile tile){
        if (contains(tile))             // If the tile is already in the snake
            return false;
        Objects.requireNonNull(tile);   // Check if the tile is not null
        if (!isEmpty()){                // If there is currently a head
            if (!hasTail())             // If there is no tail yet
                    // Clear the old head since it's about to become the tail
                getHead().clear().setType(getPlayerType());
            getHead().alterDirection(tile);
        }
        tile.setType(getPlayerType());  // Set the tile's type
            // This gets whether the tile was successfully added. If the snake 
            // is flipped, then the tile is added to the end of the queue. 
            // Otherwise, it is added to the front of the queue.
        boolean modified = (isFlipped()) ? snakeBody.offerLast(tile) : 
                snakeBody.offerFirst(tile);
        resetFailCount();               // Reset the fail count
        return modified;
    }
    /**
     * This adds the given tile to the body of this snake as the new tail. This 
     * assumes that the given tile is from the {@link #getModel model} and has a 
     * direction set for it. The given tile will have its {@link Tile#setType 
     * type} set to the {@link #getPlayerType player type} of this snake and 
     * will be used to {@link Tile#alterDirection(Tile) alter the direction} of 
     * the previous tail if there is one. This will also {@link #resetFailCount 
     * reset} the {@link #getFailCount fail count} for this snake. However, this 
     * snake will not be {@link #revive revived} automatically. If the given 
     * tile is already in this snake, then this will do nothing and return 
     * {@code false}. If this snake is currently {@link #isEmpty empty}, then 
     * this is equivalent to {@link #insertHead insertHead}.
     * @param tile The new tail for this snake (cannot be null).
     * @return Whether the tile was added to this snake.
     * @throws NullPointerException If the given tile is null.
     * @see #insertHead 
     * @see #pollHead 
     * @see #pollTail 
     * @see #checkOfferedTile 
     * @see #offer 
     * @see #add(Tile) 
     * @see #canAddTile 
     * @see #addOrMove 
     * @see #getHead 
     * @see #getTail 
     * @see #peek
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #size 
     * @see #getPlayerType 
     * @see #isFlipped 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #resetFailCount 
     * @see #revive 
     * @see Tile#setType 
     * @see Tile#clear 
     * @see Tile#setState 
     * @see Tile#alterDirection(Tile) 
     */
    protected boolean insertTail(Tile tile){
        if (contains(tile))             // If the tile is already in the snake
            return false;
        Objects.requireNonNull(tile);   // Check if the tile is not null
        if (isEmpty())                  // If the snake is currently empty
                // Insert it as the head, since there's no difference either way
            return insertHead(tile);    
        if (hasTail())                  // If there is currently a tail
            getTail().alterDirection(tile);
        tile.setType(getPlayerType());  // Set the tile's type
            // This gets whether the tile was successfully added. If the snake 
            // is flipped, then the tile is added to the front of the queue. 
            // Otherwise, it is added to the end of the queue.
        boolean modified = (isFlipped())?snakeBody.offerFirst(tile):
                snakeBody.offerLast(tile);
        resetFailCount();               // Reset the fail count
        return modified;
    }
    /**
     * This removes and returns the tile at the front of the snake body which 
     * represents the head of the snake. If the snake is still not {@link 
     * #isEmpty empty} after the current head is removed, then if the new head 
     * was previously the tail for the snake, then the new head will be facing 
     * the same direction as the now removed head was. Otherwise, if the snake 
     * still has a tail, then the new head will have its {@link 
     * Tile#alterDirection(Tile) directions altered} based off the now removed 
     * head. The removed head will then be {@link Tile#clear cleared} and 
     * returned.
     * @return The tile that was removed, or null if the snake is {@link 
     * #isEmpty empty}.
     * @see #insertHead 
     * @see #insertTail 
     * @see #pollTail 
     * @see #removeTail 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see Tile#clear 
     * @see Tile#alterDirection(Tile) 
     * @see #poll 
     * @see #remove() 
     */
    protected Tile pollHead(){
            // Remove the head of the snake. If the snake is flipped, this will 
            // be the last tile in the queue. Otherwise, this will be the first 
            // tile in the queue
        Tile tile = (isFlipped()) ? snakeBody.pollLast():snakeBody.pollFirst();
        if (tile != null){              // If the old head is not null
                // If the snake is not empty (the snake still has a head)
            if (!isEmpty()){
                if (hasTail())  // If the snake still has a tail
                    getHead().alterDirection(tile);
                else            // The new head was previously the tail
                    getHead().setState(tile.getState());
            }
            tile.clear();
        }
        return tile;
    }
    /**
     * This removes and returns the tile at the end of the snake body which 
     * represents the tail of the snake. This ignores whether the snake actually 
     * has a {@link #getTail tail} (i.e. that the snake is at least 2 tiles 
     * long), and will remove the head if there is only one tile in the snake. 
     * If the snake still has a tail after the current tail has been removed, 
     * then the new tail will have its {@link Tile#alterDirection(Tile) 
     * directions altered} based off the now removed tail. The removed tile will 
     * then be {@link Tile#clear cleared} and returned.
     * @return The tile that was removed, or null if the snake is {@link 
     * #isEmpty empty}.
     * @see #insertHead 
     * @see #insertTail 
     * @see #pollHead 
     * @see #removeTail 
     * @see #getHead 
     * @see #getTail 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see Tile#clear 
     * @see Tile#alterDirection(Tile) 
     * @see #poll 
     * @see #remove() 
     */
    protected Tile pollTail(){
            // Remove the tail of the snake. If the snake is flipped, this will 
            // be the first tile in the queue. Otherwise, this will be the last 
            // tile in the queue
        Tile tile = (isFlipped()) ? snakeBody.pollFirst():snakeBody.pollLast();
        if (tile != null){              // If the old tail is not null
            if (hasTail()){             // If there is still a tail
                getTail().alterDirection(tile);
            }
            tile.clear();
        }
        return tile;
    }
    /**
     * This checks the offered tile to see if it can be added to this snake via 
     * the {@link #offer offer(Tile)} and {@link #add(Tile) add(Tile)} methods, 
     * and if not, throws an IllegalArgumentException. Tiles not in the {@link 
     * #getModel model} cannot be added. If a tile is not {@link Tile#isEmpty 
     * empty}, then it must be a {@link Tile#isSnake snake tile} with its {@link 
     * Tile#getType type flag} set to this snake's {@link #getPlayerType player 
     * type}, and must not be facing more than one direction. In addition, if 
     * the {@code dirFaced} value is not null and the tile is facing the 
     * direction opposite to the direction set for {@code dirFaced}, then the 
     * tile cannot be added.
     * @param tile The tile that is being offered (cannot be null.
     * @param dirFaced The direction opposite to the direction the tile must not 
     * be facing, or null if the tile can face any direction. (Typically, when 
     * not null, this will be the direction the snake is facing)
     * @throws NullPointerException If the tile is null.
     * @throws IllegalArgumentException If the tile is not in the model, if the 
     * tile is neither empty nor a snake tile facing a single direction with 
     * this snake's player type as its type, or if the tile is a snake tile 
     * facing the direction opposite to the direction set on {@code dirFaced} 
     * when {@code dirFaced} is not null.
     * @see #checkOfferedTile(Tile) 
     * @see #offer 
     * @see #add(Tile) 
     * @see #addAll 
     * @see #insertHead 
     * @see #insertTail 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #getDirectionFaced 
     * @see #getPlayerType 
     * @see PlayFieldModel#contains(Tile) 
     * @see Tile#getState 
     * @see Tile#isEmpty 
     * @see Tile#isSnake 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see Tile#getType 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#requireSingleDirection 
     */
    protected void checkOfferedTile(Tile tile, Integer dirFaced){
        Objects.requireNonNull(tile);   // Check if the tile is null
        if (!model.contains(tile))      // If the tile is not in the model
            throw new IllegalArgumentException("Tile is not in model");
        if (tile.isEmpty())             // If the tile is empty
            return;
            // If the tile is not a snake tile of the same type as this snake's 
        if (!tile.isSnake() || tile.getType() != getPlayerType())// player type
            throw new IllegalArgumentException("Tile " + tile + 
                    " is not a valid snake tile");
            // Ensure the tile is facing a single direction
        SnakeUtilities.requireSingleDirection(tile.getDirectionsFaced());
            // If the given faced direction is not null and the tile is facing 
            // in the direction opposite to the direction set on it
        if (dirFaced != null && dirFaced == 
                SnakeUtilities.invertDirections(tile.getDirectionsFaced()))
            throw new IllegalArgumentException("Tile is facing in the opposite "
                    + "direction to the snake");
    }
    /**
     * This checks the offered tile to see if it can be added to this snake via 
     * the {@link #offer offer(Tile)} and {@link #add(Tile) add(Tile)} methods, 
     * and if not, throws an IllegalArgumentException. Tiles not in the {@link 
     * #getModel model} cannot be added. If a tile is not {@link Tile#isEmpty 
     * empty}, then it must be a {@link Tile#isSnake snake tile} with its {@link 
     * Tile#getType type flag} set to this snake's {@link #getPlayerType player 
     * type}, and must not be facing more than one direction. In addition, if 
     * this snake has a {@link #getTail tail} and the tile is facing the 
     * direction opposite to the direction this snake is {@link 
     * #getDirectionFaced facing}, then the tile cannot be added. <p>
     * 
     * This is equivalent to calling {@link #checkOfferedTile(Tile, Integer) 
     * checkOfferedTile}{@code (tile, (}{@link #getTail getTail()} {@code != 
     * null) ? }{@link #getDirectionFaced getDirectionFaced()} {@code : null)}.
     * 
     * @param tile The tile that is being offered (cannot be null.
     * @throws NullPointerException If the tile is null.
     * @throws IllegalArgumentException If the tile is not in the model, if the 
     * tile is neither empty nor a snake tile facing a single direction with 
     * this snake's player type as its type, or if the tile is a snake tile 
     * facing the direction opposite to the direction this snake is facing when 
     * the snake has a tail.
     * @see #checkOfferedTile(Tile, Integer) 
     * @see #offer 
     * @see #add(Tile) 
     * @see #addAll 
     * @see #insertHead 
     * @see #insertTail 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #getDirectionFaced 
     * @see #getPlayerType 
     * @see PlayFieldModel#contains(Tile) 
     * @see Tile#getState 
     * @see Tile#isEmpty 
     * @see Tile#isSnake 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see Tile#getType 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#requireSingleDirection 
     */
    protected void checkOfferedTile(Tile tile){
            // If this snake has a tail, then the tile must not be facing in the 
            // direction opposite to the direction this snake is facing. 
            // Otherwise, the tile can be facing in any single direction.
        checkOfferedTile(tile,(hasTail())?getDirectionFaced():null);
    }
    /**
     * This inserts the given tile at the front of this snake if the tile is not 
     * already present in this snake. More formally, this adds the given tile to 
     * this snake if this snake contains no tile {@code tile2} such that {@code 
     * Objects.equals(tile, tile2)}. If this snake already contains the tile, 
     * then no changes will be made to the snake and this will return {@code 
     * false}. <p>
     * 
     * The tile must be a non-null tile from this snake's {@link #getModel 
     * model}. Additionally, the tile must either be {@link Tile#isEmpty empty} 
     * or a {@link Tile#isSnake snake tile}. If the tile is a snake tile, then 
     * its {@link Tile#getType type flag} must be set to this snake's {@link 
     * #getPlayerType player type} and it must not be facing more than one 
     * direction. If this snake has a {@link #getTail tail} (i.e. if this snake 
     * is at least two tiles long), then the tile must not be facing in the 
     * direction opposite to the direction that this snake is {@link 
     * #getDirectionFaced facing}. This ignores whether this snake has {@link 
     * #isCrashed crashed}, and thus can be used to add tiles to a crashed snake 
     * without {@link #revive reviving} it. <p>
     * 
     * If the tile is added to this snake, then it will become the new {@link 
     * #getHead head} of the snake and the snake's {@link #getFailCount fail 
     * count} will be reset to zero. However, if the snake has crashed, then it 
     * will still need to be revived, as this alone will not revive a snake. If 
     * this snake was {@link #isEmpty empty} before this was called, then the 
     * snake will be {@link #initialize(Tile) initialized} using the given tile. 
     * If the tile is empty, then this will determine the most appropriate 
     * direction for the tile to be facing based off the location of the tile 
     * relative to the previous head of this snake. If this snake does not have 
     * a previous head as a result of being empty, and the given tile is empty, 
     * then the tile will be facing {@link Tile#isFacingLeft left}. 
     * 
     * @param tile The tile to add to the snake. 
     * @return Whether the tile was added to this snake ({@code true} if this 
     * snake did not already contain the given tile, as specified by {@link 
     * Set#add Set.add}).
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given tile is either not in the 
     * model or is neither empty nor a snake tile. If the given tile is a snake 
     * tile, then this will also be thrown if it is either facing multiple 
     * directions, facing the direction opposite to what this snake is facing, 
     * or if the tile's type is not the same as this snake's player type.
     * @see #add(Tile) 
     * @see #addAll 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isValid 
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #getModel 
     * @see #setModel
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see #isFlipped 
     * @see #getFailCount 
     * @see #isCrashed 
     * @see #revive 
     * @see #clear 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#getState 
     * @see Tile#setState 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see Tile#isSnake 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see Tile#isFacingUp 
     * @see Tile#setFacingUp 
     * @see Tile#isFacingDown 
     * @see Tile#setFacingDown 
     * @see Tile#isFacingLeft 
     * @see Tile#setFacingLeft 
     * @see Tile#isFacingRight 
     * @see Tile#setFacingRight 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getDirectionCount 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#requireSingleDirection 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_RESET
     * @see SnakeEvent#SNAKE_INITIALIZED
     */
    @Override
    public boolean offer(Tile tile){
        if (contains(tile))         // If the tile is already in the snake
            return false;
        checkOfferedTile(tile);     // Check if the tile can be added
        if (isEmpty()){             // If the snake is currently empty
            initialize(tile);       // Use the tile to initialize the snake
                // If the tile was added, the snake should no longer be empty
            return !isEmpty();      
        }   // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        if (tile.isEmpty()){            // If the tile is empty
            Tile head = getHead();      // Get the current head
                // Get whether the new head should be vertical (up/down) or 
            boolean vertical;       // horizontal (left/right)
                // If the current head is facing either up or down
            if (head.isFacingUp() || head.isFacingDown())
                    // Base the new head's verticallity off whether the 
                    // tiles are on the same column
                vertical = tile.getColumn() == head.getColumn();
            else    // Base the new head's verticallity off whether the 
                    // tiles are on different rows
                vertical = tile.getRow() != head.getRow();
            if (size() > 1){    // If this snake is more than 1 tile long
                tile.setFacingUp(vertical && head.isFacingUp());
                tile.setFacingDown(vertical && head.isFacingDown());
                tile.setFacingLeft(!vertical && head.isFacingLeft());
                tile.setFacingRight(!vertical && head.isFacingRight());
            }
            if (tile.isEmpty()){    // If the tile is still empty
                tile.setFacingUp(vertical && tile.getRow() < head.getRow());
                tile.setFacingDown(vertical && !tile.isFacingUp());
                tile.setFacingLeft(!vertical && tile.getColumn() < head.getColumn());
                tile.setFacingRight(!vertical && !tile.isFacingLeft());
            }
        }   // Try to add the tile and get whether it was added to the snake
        boolean modified = insertHead(tile);
        if (modified){                  // If the tile was added to the snake
            setConsumedApple(false);    // We haven't consumed an apple
            fireSnakeChange(SnakeEvent.SNAKE_ADDED_TILE,tile);
        }
        model.setTilesAreAdjusting(adjusting);
        return modified;
    }
    /**
     * This inserts the given tile at the front of this snake if the tile is not 
     * already present in this snake. More formally, this adds the given tile to 
     * this snake if this snake contains no tile {@code tile2} such that {@code 
     * Objects.equals(tile, tile2)}. If this snake already contains the tile, 
     * then no changes will be made to the snake and this will return {@code 
     * false}. <p>
     * 
     * The tile must be a non-null tile from this snake's {@link #getModel 
     * model}. Additionally, the tile must either be {@link Tile#isEmpty empty} 
     * or a {@link Tile#isSnake snake tile}. If the tile is a snake tile, then 
     * its {@link Tile#getType type flag} must be set to this snake's {@link 
     * #getPlayerType player type} and it must not be facing more than one 
     * direction. If this snake has a {@link #getTail tail} (i.e. if this snake 
     * is at least two tiles long), then the tile must not be facing in the 
     * direction opposite to the direction that this snake is {@link 
     * #getDirectionFaced facing}. This ignores whether this snake has {@link 
     * #isCrashed crashed}, and thus can be used to add tiles to a crashed snake 
     * without {@link #revive reviving} it. <p>
     * 
     * If the tile is added to this snake, then it will become the new {@link 
     * #getHead head} of the snake and the snake's {@link #getFailCount fail 
     * count} will be reset to zero. However, if the snake has crashed, then it 
     * will still need to be revived, as this alone will not revive a snake. If 
     * this snake was {@link #isEmpty empty} before this was called, then the 
     * snake will be {@link #initialize(Tile) initialized} using the given tile. 
     * If the tile is empty, then this will determine the most appropriate 
     * direction for the tile to be facing based off the location of the tile 
     * relative to the previous head of this snake. If this snake does not have 
     * a previous head as a result of being empty, and the given tile is empty, 
     * then the tile will be facing {@link Tile#isFacingLeft left}. <p>
     * 
     * This method is equivalent to {@link #offer offer(Tile)}.
     * 
     * @param tile The tile to add to the snake. 
     * @return Whether the tile was added to this snake ({@code true} if this 
     * snake did not already contain the given tile, as specified by {@link 
     * Set#add Set.add}).
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given tile is either not in the 
     * model or is neither empty nor a snake tile. If the given tile is a snake 
     * tile, then this will also be thrown if it is either facing multiple 
     * directions, facing the direction opposite to what this snake is facing, 
     * or if the tile's type is not the same as this snake's player type.
     * @see #offer 
     * @see #addAll 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isValid 
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #getModel 
     * @see #setModel
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see #isFlipped 
     * @see #getFailCount 
     * @see #isCrashed 
     * @see #revive 
     * @see #clear 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see PlayFieldModel#getTile 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see PlayFieldModel#getRowCount 
     * @see PlayFieldModel#getColumnCount 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#getState 
     * @see Tile#setState 
     * @see Tile#isEmpty 
     * @see Tile#clear 
     * @see Tile#isSnake 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see Tile#isFacingUp 
     * @see Tile#setFacingUp 
     * @see Tile#isFacingDown 
     * @see Tile#setFacingDown 
     * @see Tile#isFacingLeft 
     * @see Tile#setFacingLeft 
     * @see Tile#isFacingRight 
     * @see Tile#setFacingRight 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#alterDirection(Tile) 
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getDirectionCount 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeUtilities#requireSingleDirection 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_RESET
     * @see SnakeEvent#SNAKE_INITIALIZED
     */
    @Override
    public boolean add(Tile tile){
        if (contains(tile))     // If the tile is already in the snake
            return false;
        return super.add(tile);
    }
    /**
     * This adds all of the elements in the given collection, in the order they 
     * are returned by the collection's iterator, to this snake if they're not 
     * already present. If the given collection is a set, then this operation 
     * effectively modifies this snake so that it is the <i>union</i> of the set 
     * and the snake, since snakes are themselves sets. The behavior of this 
     * operation is undefined if the given collection is modified while this 
     * operation is in progress. If an exception is thrown while adding an 
     * element, then only the elements up to that point will be added to the 
     * snake. If the given collection is this snake, then this will do nothing 
     * and return {@code false} (getting the the <i>union</i> of a set with 
     * itself just results in the original set, and, as previously mentioned, 
     * snakes are sets). If the given collection is another snake, then the 
     * tiles will be added in reverse order, starting from the snake's {@link 
     * #peek tail} and ending at the snake's {@link #getHead head}. 
     * 
     * @implSpec Currently, adding a snake to another snake does not work 
     * correctly, and may have unintended effects.
     * 
     * @todo Make it so that this method also allows the insertion of tiles with 
     * at least two directions set, as opposed to the one or none limitation of 
     * the normal {@code add(Tile)} method. This way, this could be used to 
     * populate a snake with a collection of tiles without having to conform to 
     * the restrictions of the {@code add} method, and would allow a snake to be 
     * added to another snake.
     * 
     * @param c The collection containing the elements to be added to this snake 
     * (cannot be null).
     * @return {@code true} if this snake changed as a result of the call.
     * @throws NullPointerException If the given collection contains a null 
     * element, or if the given collection is itself null.
     * @throws IllegalArgumentException If an element from the given collection 
     * cannot be added to this snake.
     * @see #offer(Tile) 
     * @see #add(Tile) 
     */
    @Override
    public boolean addAll(Collection<? extends Tile> c){
        Objects.requireNonNull(c);  // Check if the collection is null
            // If the collection is either this snake or its body somehow 
            // (the union of a set with itself does nothing), or if the 
            // collection is empty (nothing would be added)
        if (c == this || c == snakeBody || c.isEmpty())
            return false;
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // This gets if this snake was modified as a result of calling this
        boolean modified = false;   
            // This gets any exceptions thrown while adding the tiles, so that 
        RuntimeException exc = null;    // it can be relayed after we're done
        try{
                // This gets an iterator to go through and add the tiles from c 
            Iterator<? extends Tile> itr;
            if (c instanceof Snake)     // If c is a snake
                    // Get the descending iterator, so as to add the tiles from 
                itr = ((Snake)c).iterator(true);    // the tail to the head
            else
                itr = c.iterator();     // Get an iterator from c
            while (itr.hasNext()){      // While the iterator has tiles
                if (add(itr.next()))//If the next tile in the iterator was added
                    modified = true;    // The snake has been modified
            }
        }
        catch(RuntimeException ex){
            exc = ex;
        }
        model.setTilesAreAdjusting(adjusting);
        if (exc != null)    // If an exception was thrown while adding the tiles
            throw exc;
        return modified;
    }
    /**
     * This returns, but does not remove, the head of the queue represented by 
     * this snake, or null if this snake is {@link #isEmpty empty}. This will be 
     * the last tile in this snake, and thus will typically be the tile that 
     * represents the tail of the snake, since snakes act like a queue but in 
     * reverse. This method differs from {@link #getTail getTail} only in that 
     * this method will only return null if this snake is empty, whereas {@code 
     * getTail} will return null if this snake is less than two tiles long. 
     * @return The head of the queue (the tail of this snake), or null if this 
     * snake is empty.
     * @see #getTail 
     * @see #element 
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #poll 
     * @see #remove() 
     * @see #isValid 
     */
    @Override
    public Tile peek(){
            // If the snake is flipped, return the first tile in the queue. 
            // Otherwise, return the last tile in the queue.
        return (isFlipped()) ? snakeBody.peekFirst() : snakeBody.peekLast();
    }
    /**
     * This returns, but does not remove, the head of the queue represented by 
     * this snake. This will be the last tile in this snake, and thus will 
     * typically be the tile that represents the tail of the snake, since snakes 
     * act like a queue but in reverse. This method differs from {@link #peek 
     * peek} only in that it will throw an exception if this snake is {@link 
     * #isEmpty empty}.
     * @return The head of the queue (the tail of this snake).
     * @throws NoSuchElementException If this snake is empty.
     * @see #peek 
     * @see #getTail 
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #poll 
     * @see #remove() 
     * @see #isValid 
     */
    @Override
    public Tile element(){
        return super.element();
    }
    /**
     * This removes and returns the head of the queue represented by this snake, 
     * or returns null if this snake is {@link #isEmpty empty}. The removed tile 
     * will be {@link Tile#clear cleared} before it is returned. The removed 
     * tile will be the last tile in this snake, and thus will typically be the 
     * tile that represented the {@link #getTail tail} of the snake, since 
     * snakes act like a queue but in reverse. This method differs from {@link 
     * #removeTail removeTail} in that this method will only return null if this 
     * snake is empty and will not fire a {@code SnakeEvent} when it does, 
     * whereas {@code removeTail} will fire a {@code SnakeEvent} indicating that 
     * it failed and return null if this snake is less than two tiles long. 
     * @return The head of the queue (the tail of this snake), or null if this 
     * snake is empty.
     * @see #remove() 
     * @see #removeTail 
     * @see #getTail 
     * @see #peek
     * @see #element 
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #isValid 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_REMOVED_TILE
     */
    @Override
    public Tile poll(){
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        Tile tile = pollTail();     // Remove the current tail
        if (tile != null)           // If a tile was removed
            fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,tile);
        model.setTilesAreAdjusting(adjusting);
        return tile;
    }
    /**
     * This removes and returns the head of the queue represented by this snake. 
     * The removed tile will be {@link Tile#clear cleared} before it is 
     * returned. The removed tile will be the last tile in this snake, and thus 
     * will typically be the tile that represented the {@link #getTail tail} of 
     * the snake, since snakes act like a queue but in reverse. This method 
     * differs from {@link #poll poll} only in that it will throw an exception 
     * if this snake is {@link #isEmpty empty}.
     * @return The head of the queue (the tail of this snake).
     * @throws NoSuchElementException If this snake is empty.
     * @see #poll 
     * @see #removeTail 
     * @see #getTail 
     * @see #peek
     * @see #element 
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #isValid 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_REMOVED_TILE
     */
    @Override
    public Tile remove(){
        return super.remove();
    }
    /**
     * This removes the specified element from this snake if it is present. More 
     * formally, this will remove an element {@code e} such that {@code 
     * Objects.equals(o, e)} from this snake if this snake contains such an 
     * element. If this snake contained the specified element (or equivalently, 
     * if this snake changed as a result of the call), then this will return 
     * {@code true}. 
     * @param o The object to be removed from this snake, if present.
     * @return {@code true} if this snake contained the specified element, else 
     * {@code false}.
     * @see #repair 
     */
    @Override
    public boolean remove(Object o){
            // If either the object is null or this snake is empty
        if (o == null || isEmpty()) 
            return false;
            // If the object is the tail of this snake (the head of the queue)
        if (Objects.equals(o, peek()))
            return poll() != null;  // Poll the tail (head of the queue)
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        boolean removed;    // This gets whether a tile was removed
            // If the object is the head of the snake
        if (Objects.equals(o, getHead())){
            Tile tile = pollHead(); // Poll the head of the snake
                // Get if a tile was removed (most likely to be true)
            removed = tile != null; 
            if (removed)            // If a tile was removed
                fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,tile);
        }
        else
            removed = super.remove(o);  // Remove the tile using the iterator
        model.setTilesAreAdjusting(adjusting);
        return removed;
    }
    /**
     * This removes all of the elements in this snake that are also contained in 
     * the specified collection. After this call returns, this snake will 
     * contain no elements in common with the specified collection. If the 
     * specified collection is a set, then this operation effectively modifies 
     * this snake so that it is the <i>asymmetric set difference</i> of the set 
     * and the snake, since snakes are themselves sets. If the specified 
     * collection is this snake, then this is equivalent to calling {@link 
     * #clear clear} to remove all elements from this snake.
     * @param c A collection containing the elements to be removed from this 
     * snake (cannot be null).
     * @return {@code true} if this snake changed as a result of the call.
     * @throws NullPointerException If the specified collection is null.
     * @see #remove(Object) 
     * @see #contains(Object) 
     * @see #clear 
     * @see #repair 
     */
    @Override
    public boolean removeAll(Collection<?> c){
        Objects.requireNonNull(c);      // Check if the collection is null
            // If the collection is empty or this snake is empty, then nothing 
        if (c.isEmpty() || isEmpty())   // would be removed
            return false;
            // If the collection is this snake or its body somehow
        if (c == this || c == snakeBody){
            clear();        // We would just end up removing everything anyway
                // Everything has been removed (we already checked if this snake 
            return true;    // was empty)
        }   // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // Remove the objects in the given collection and get whether this 
        boolean modified = super.removeAll(c); // snake was modified as a result
        repair();           // Repair the snake (may be removed later)
        model.setTilesAreAdjusting(adjusting);
        return modified;
    }
    /**
     * This retains only the elements in this snake that are contained in the 
     * specified collection. In other words, this removes all the elements in 
     * this snake that are not contained in the specified collection. After this 
     * call returns, this snake will only contain elements that are in common 
     * with the specified collection. If the specified collection is a set, then 
     * this operation effectively modifies this snake so that it is the 
     * <i>intersection</i> of the set and the snake, since snakes are themselves 
     * sets. If the specified collection is this snake, then this does nothing. 
     * If the specified collection is empty, then this is equivalent to calling 
     * {@link #clear clear} to remove all elements from this snake.
     * @param c A collection containing the elements to be retained by this 
     * snake (cannot be null).
     * @return {@code true} if this snake changed as a result of the call.
     * @throws NullPointerException If the specified collection is null.
     * @see #remove(Object) 
     * @see #contains(Object) 
     * @see #clear 
     * @see #repair 
     */
    @Override
    public boolean retainAll(Collection<?> c){
        Objects.requireNonNull(c);  // Check if the collection is null
            // If this snake is empty (nothing to retain) or the collection is 
            // this snake or its body (and thus everything would be retained)
        if (isEmpty() || c == this || c == snakeBody)          
            return false;
            // If the collection is empty (everything would be removed)
        if (c.isEmpty()){
            clear();                // Remove everything
            return true;
        }   // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // Retain only the objects in the given collection and get whether 
            // this snake was modified as a result
        boolean modified = super.retainAll(c);
        repair();                   // Repair the snake (may be removed later)
        model.setTilesAreAdjusting(adjusting);
        return modified;
    }
    /**
     * This removes all of the elements in this snake that satisfy the given 
     * predicate. Any errors or runtime exceptions thrown by the predicate will 
     * be relayed to the caller. It is worth noting that snakes will attempt to 
     * {@link #repair repair} themselves as tiles are removed, and thus removing 
     * tiles on the basis of the direction(s) they face may have unintended 
     * results.
     * @param filter A predicate which will return {@code true} for the elements 
     * to be removed (cannot be null).
     * @return {@code true} if any elements were removed from this snake.
     * @throws NullPointerException If the given filter predicate is null.
     * @see #repair 
     */
    @Override
    public boolean removeIf(Predicate<? super Tile> filter){
        Objects.requireNonNull(filter); // Check if the filter is null
        if (isEmpty())      // If this snake is empty (nothing to remove)
            return false;
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // This gets if this snake was modified as a result of removing the 
        boolean modified = false;       // tiles that matched the filter
        // This gets any exceptions thrown while removing the tiles, so that 
        RuntimeException exc = null;    // it can be relayed after we're done
        try{    // Remove the tiles that match the filter
            modified = super.removeIf(filter);  
        }
        catch(RuntimeException ex){
            exc = ex;
        }
        repair();                    // Repair the snake (may be removed later)
        model.setTilesAreAdjusting(adjusting);
        if (exc != null)    // If an exception was thrown while removing tiles
            throw exc;
        return modified;
    }
    /**
     * This resets this snake by {@link #resetFailCount resetting} the {@link 
     * #getFailCount fail count}, clearing any status flags set on this snake 
     * (such as {@link #APPLE_CONSUMED_FLAG} and {@link #CRASHED_FLAG}), and 
     * removing all tiles from this snake. If the flags or the tile contents of 
     * this snake changed as a result of the call, then this will fire a {@link 
     * SnakeEvent#SNAKE_RESET SNAKE_RESET} {@code SnakeEvent}.
     * @param model The model that this snake is/was being displayed on. This 
     * may be different from the currently set model, and may be the model that 
     * this snake is being removed from when {@link #setModel setting the 
     * model}. This is mainly provided so as to allow this snake to tell the 
     * model that the {@link PlayFieldModel#setTilesAreAdjusting tiles will be 
     * adjusting}.
     * @see #resetFailCount 
     * @see #setFlag 
     * @see PlayFieldModel#getTilesAreAdjusting 
     * @see PlayFieldModel#setTilesAreAdjusting 
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #clear 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_RESET
     */
    protected void reset(PlayFieldModel model){
            // This gets whether the tiles are currently adjusting, so as to 
            // restore this value when the reset is finished
        boolean adjusting = false;  
        if (model != null){ // If the model is not null
            adjusting = model.getTilesAreAdjusting();
                // The tiles will be adjusting
            model.setTilesAreAdjusting(true);
        }
        resetFailCount();           // Reset the fail count
            // Reset the flags that are affected when resetting a snake and get 
            // whether they changed
        boolean reset = setFlag(RESET_AFFECTED_FLAGS,false);
        if (!snakeBody.isEmpty()){  // If the snake body is not empty
            reset = true;           // The snake will be changed
                // A for loop to go through the body of this snake and clear 
            for (Tile tile : snakeBody) // each tile
                tile.clear();       // Clear the current tile
            snakeBody.clear();      // Empty the snake
        }
        if (reset)                  // If this snake was affected by the reset
            fireSnakeChange(SnakeEvent.SNAKE_RESET,0);
        if (model != null)          // If the model is not null
            model.setTilesAreAdjusting(adjusting);
    }
    /**
     * This removes and {@link Tile#clear clears} all of the tiles from this 
     * snake. This will also reset this snake, resulting in the status and the 
     * {@link #getFailCount fail count} of this snake being reset. Any and all 
     * settings previously set for this snake will be maintained. If this snake 
     * changed as a result of calling this, then this will fire a {@code 
     * SnakeEvent} indicating that the snake has been {@link 
     * SnakeEvent#SNAKE_RESET reset}. This snake will be {@link #isEmpty empty}, 
     * its fail count will be zero, it will not be {@link #isFlipped flipped}, 
     * and it will not have {@link #hasConsumedApple eaten an apple} or {@link 
     * #isCrashed crashed} after this call returns. 
     * @see #isValid 
     * @see #isEmpty 
     * @see #size 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_RESET
     */
    @Override
    public void clear(){
        reset(model);
    }
    /**
     * This returns an array containing the tiles in this snake in the order in 
     * which they appear, starting at the {@link #getHead head} and ending at 
     * the {@link #getTail tail} of this snake. <p>
     * 
     * The returned array will be "safe" in that no references to the array will 
     * be maintained by this snake. (In other words, this method must allocate a 
     * new array). The caller is thus free to modify the returned array. 
     * 
     * @return An array containing the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #contains(Object) 
     * @see #contains(int, int) 
     */
    @Override
    public Object[] toArray(){
        return super.toArray();
    }
    /**
     * This returns an iterator over the tiles in this snake. The order in which 
     * the tiles are iterated over depends on the value for {@code descending}. 
     * If {@code descending} is {@code false}, then the iterator will iterate 
     * over the tiles in the order in which they appear in this snake, starting 
     * at the {@link #getHead head} of the snake, and ending at the {@link #peek 
     * tail} of the snake. If {@code descending} is {@code true}, then the 
     * iterator will iterate over the tiles in the reverse order, starting at 
     * the tail of the snake, and ending at the head of the snake. <p>
     * 
     * Please note that tiles store their corresponding row and column, and thus 
     * the location of each tile can be retrieved by using the tile's {@link 
     * Tile#getRow getRow} and {@link Tile#getColumn getColumn} methods. <p>
     * 
     * The returned iterator is fail-fast. That is to say, if the structure of
     * this snake changes at any time after the iterator is created except via 
     * the iterator's {@link Iterator#remove remove} method, then the iterator 
     * will throw a {@link ConcurrentModificationException 
     * ConcurrentModificationException}. 
     * 
     * @todo Finish implementing the {@link #repairAfterRemoval 
     * repairAfterRemoval} protected method to allow the snake to repair itself 
     * after a tile is removed automatically.
     * 
     * @param descending Whether the iterator should be an ascending iterator or 
     * a descending iterator ({@code false} for an ascending iterator, and 
     * {@code true} for a descending iterator).
     * @return An iterator over the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #repair 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see #iterator() 
     */
    protected Iterator<Tile> iterator(boolean descending) {
        return new SnakeIterator(descending);
    }
    /**
     * This returns an iterator over the tiles in this snake in the order in 
     * which they appear in this snake. The iterator starts at the {@link 
     * #getHead head} of the snake and ends at the {@link #peek tail} of the 
     * snake. <p>
     * 
     * Please note that tiles store their corresponding row and column, and thus 
     * the location of each tile can be retrieved by using the tile's {@link 
     * Tile#getRow getRow} and {@link Tile#getColumn getColumn} methods. <p>
     * 
     * The returned iterator is fail-fast. That is to say, if the structure of
     * this snake changes at any time after the iterator is created except via 
     * the iterator's {@link Iterator#remove remove} method, then the iterator 
     * will throw a {@link ConcurrentModificationException 
     * ConcurrentModificationException}. 
     * 
     * @todo Finish implementing the {@link #repairAfterRemoval 
     * repairAfterRemoval} protected method to allow the snake to repair itself 
     * after a tile is removed automatically.
     * 
     * @return An iterator over the tiles in this snake.
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #repair 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     */
    @Override
    public Iterator<Tile> iterator() {
        return iterator(false);
    }
    /**
     * This repairs this snake. This attempts to ensure that all the tiles in 
     * this snake join up. In other words, this attempts to ensure that each 
     * tile in this snake will lead into the tiles before and after it in this 
     * snake, so that this snake would appear continuous if each tile in this 
     * snake was adjacent to the tile that comes before it and the tile that 
     * comes after it in this snake. After this is called, each tile in this 
     * snake will have at least one direction set for it (with the {@link 
     * #getHead head} and {@link #peek tail} having only one direction set, and 
     * the rest having two directions set), the direction(s) set for each tile 
     * will result in it joining up with the tiles before an after it, and all 
     * tiles in this snake will have their {@link Tile#getType type flag} set to 
     * this snake's {@link #getPlayerType player type}. This will also remove 
     * any tiles in this snake that are not currently in the {@link #getModel 
     * model}. If any tiles have been changed or removed as a result of this 
     * call, then this will fire a {@code SnakeEvent} indicating that the snake 
     * has been {@link SnakeEvent#SNAKE_REPAIRED repaired}.
     * 
     * @todo Finish implementing the {@link #repairAfterRemoval 
     * repairAfterRemoval} protected methods, so that the snake can repair 
     * itself after a tile is removed via the iterator's remove method. Finish 
     * workshopping the {@link #repairSegment repairSegment} protected method to 
     * optimize repairing a snake.
     * 
     * @return This snake.
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #getModel 
     * @see #setModel
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see Tile#getState 
     * @see Tile#setState 
     * @see Tile#isEmpty 
     * @see Tile#isSnake 
     * @see Tile#getDirectionsFaced 
     * @see Tile#getDirectionsFacedCount 
     * @see Tile#getType 
     * @see Tile#setType 
     * @see Tile#alterDirection(int) 
     * @see Tile#alterDirection(Tile) 
     * @see PlayFieldModel#contains(Tile) 
     * @see PlayFieldModel#contains(int, int) 
     * @see SnakeUtilities#getDirections 
     * @see SnakeUtilities#getDirectionCount 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeEvent#SNAKE_REPAIRED
     */
    public Snake repair(){
        if (isEmpty())      // If this snake is empty
            return this;
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // An iterator to go through the tiles in this snake
        Iterator<Tile> itr = iterator();
        boolean repaired = false;// This gets whether any changes have been made
        Tile prev;              // This stores the tile before the current tile
        Tile curr = null;      // This stores the tile currently being worked on
        Tile next = itr.next(); // This stores the tile after the current tile
            // This stores the direction that the previous tile will join up 
        int connectValue = 0;   // with the current tile.
            // While the next tile in the snake is not in the model, and the 
            // iterator still has tiles to go through (stops short of the last 
        while (!model.contains(next) && itr.hasNext()){ // tile in the snake)
            itr.remove();       // Remove the tile
            next = itr.next();  // Get the next tile
            repaired = true;    // We have made a change to the snake
        }   // This will go through the tiles in the snake and repair it up to 
        while (itr.hasNext()){  // the second to last tile in the snake
            Tile temp = itr.next();     // Get the next tile in the snake
            if (model.contains(temp)){  // If the model contains the next tile
                prev = curr;        // The current tile is now the previous tile
                curr = next;        // The next tile is now the current tile
                next = temp;        // Get the next tile
                    // Get the current state of the current tile, so that it can 
                int old = curr.getState();  // be compared to after repairing it
                    // Repair the segment made up of the previous, current, and 
                    // next tiles
                connectValue = repairSegment(prev, curr, next, connectValue); 
                    // Update this to reflect if any changes have been made to 
                    // any of the tiles so far
                repaired = repaired || old != curr.getState();
            }
            else{   // Tile is not in the model
                itr.remove();       // Remove the tile
                repaired = true;    // We have made a change to the snake
            }
        }   // Next should contain the last tile in the snake
        if (model.contains(next)){  // If the next tile is in the model
                // Get the current state of the next (current) tile, so that it 
            int old = next.getState();  // can be compared to after repairing it
                // Repair the segment made up of the current (previous) and next 
            repairSegment(curr, next, null, connectValue);//(current/last) tiles
                // Update this to reflect if a change has been made to a tile
            repaired = repaired || old != next.getState();
        }
        else{   // Next should still be the last tile returned by the iterator 
                // at this point, since for it to both be the last tile in the 
                // snake and still not be in the model, the second loop must 
                // have been skipped
            itr.remove();       // Remove the tile
            repaired = true;    // We have made a change to the snake
        }   // If the state of any of the tiles in this snake changed or if any 
        if (repaired) // tiles were removed as a result of calling this
            fireSnakeChange(SnakeEvent.SNAKE_REPAIRED,0);
        model.setTilesAreAdjusting(adjusting);
        return this;
    }
    /**
     * This attempts to repair the given segment of this snake by altering the 
     * direction(s) faced by the {@code current} tile to connect the {@code 
     * previous} and {@code next} tiles if possible. This method is called by 
     * the {@link #repair repair} method, and is responsible for ensuring that 
     * the {@code current} tile joins up with the {@code previous} and {@code 
     * next} tiles if possible, so as to allow the snake to be continuous 
     * (assuming that each tile in the snake was adjacent to the tile before and 
     * after it in the snake). The {@code previous} tile will be null for the 
     * {@link #getHead first tile} in the snake, and the {@code next} tile will 
     * be null for the {@link #peek last tile} in the snake. If both the {@code 
     * previous} and {@code next} tiles are null, then it can be assumed that 
     * the snake consists of only one tile. When the {@code previous} tile is 
     * not null, then it will typically have one (if the {@code previous} tile 
     * is the first tile in the snake, i.e. if the {@code current} tile is the 
     * second tile in the snake) or two (for all {@code current} tiles other 
     * than the second tile) directions set for it. The {@code next} tile may 
     * have any number of directions set for it. The {@code previous}, {@code 
     * current}, and {@code next} tiles may or may not be adjacent to each 
     * other. After this is called, the {@code current} tile should have only 
     * one (if the {@code current} tile is either the first or last tile in the 
     * snake) or two (for all {@code current} tiles other than the first or last 
     * tiles in the snake) directions set for it. The direction(s) set on the 
     * {@code current} tile should ideally result in it joining up with the 
     * {@code previous} and {@code next} tiles when possible. If the {@code 
     * current} tile cannot be made to join up with the {@code next} tile, then 
     * this should attempt to set up the {@code current} tile in a way that, the 
     * {@code next} tile can be made to join up with when this method is called 
     * by the {@code repair} method with the {@code current} tile, {@code next} 
     * tile, and the tile after the {@code next} tile as the {@code previous}, 
     * {@code current}, and {@code next} tiles, respectively. <p>
     * 
     * This assumes that only one direction is set on {@code connectDir} if any, 
     * that the direction set on {@code connectDir} is a direction set on the 
     * {@code previous} tile when the {@code previous} tile is non-null, and 
     * that {@code connectDir} is the direction in which the {@code previous} 
     * tile will be joining up with the {@code current} tile. This also assumes 
     * that the {@code next} tile, when non-null, will not be the same tile as 
     * the {@code current} tile, nor will the {@code next} tile's {@link 
     * Tile#getRow row} and {@link Tile#getColumn column} are not the same row 
     * and column as the {@code current} tile. When called by the {@code repair}
     * method, these assumptions will be true. The value returned will be the 
     * direction that the {@code next} tile will join up with the {@code 
     * current} tile. In other words, the value returned will be the value to 
     * use for {@code connectDir} when this is used to repair the {@code next} 
     * tile.
     * 
     * @todo Finish workshopping this method to optimize repairing a snake.
     * 
     * @param previous The tile in the snake that comes before the {@code 
     * current} tile, or null.
     * @param current The tile in the snake that is to be altered, if necessary, 
     * to make the snake to be continuous (cannot be null).
     * @param next The tile in the snake that comes after the {@code current} 
     * tile, or null.
     * @param connectDir The direction set on the {@code previous} tile that 
     * will join up with the {@code current} tile, or zero if the {@code 
     * previous} tile is null.
     * @return The direction set on the {@code current} tile that will join up 
     * with the {@code next} tile.
     * @see #repair 
     */
    protected int repairSegment(Tile previous, Tile current, Tile next, 
            int connectDir){
            // If there is a previous tile and it is facing a single direction
        if (previous != null && previous.getDirectionsFacedCount() == 1)
                // There is only one direction that will cause the current tile 
                // to join up with the previous tile
            connectDir = previous.getDirectionsFaced();
        else if (previous == null)  // If there isn't a previous tile
            connectDir = 0; // Ensure that this direction is zero
            // Invert the direction to get the direction needed to join up with 
            // the previous tile, instead of the direction that was from the 
            // previous tile
        connectDir = SnakeUtilities.invertDirections(connectDir);
            // If the next tile is null (the current tile is the last tile in 
        if (next == null){      // the snake)
                // If the previous tile is null (the current tile is the only 
            if (previous == null){  // tile in the snake)
                    // If the current tile isn't facing any directions
                if (current.getDirectionsFaced() == 0)
                    current.setFacingLeft(true);
                    // If the current tile is facing more than one direction
                else if (current.getDirectionsFacedCount() != 1){   
                        // If the current tile is facing both of the horizontal 
                    if (current.getFlag(HORIZONTAL_DIRECTIONS)) // directions
                        current.setFacingRight(false);
                        // If the current tile is facing both of the vertical
                    if (current.getFlag(VERTICAL_DIRECTIONS))   // directions
                        current.setFacingDown(false);
                        // If the current tile is still facing more than one 
                        // direction at this point
                    if (current.getDirectionsFacedCount() != 1)
                        current.setFlag(VERTICAL_DIRECTIONS, false);
                }
            }
            else    //The last tile just needs to join up with the previous tile
                current.setState(connectDir);
        }
        else{   // The current tile must not be the last tile in the snake
                // If the next tile is below the current tile, make the current 
                // tile face upwards
            current.setFacingUp(current.getRow() < next.getRow());
                // If the next tile is above the current tile, make the current 
                // tile face downwards
            current.setFacingDown(current.getRow() > next.getRow());
                // If the next tile is to the right of the current tile, make 
                // the current tile face to the left
            current.setFacingLeft(current.getColumn() < next.getColumn());
                // If the next tile is to the left of the current tile, make the 
                // current tile face to the right
            current.setFacingRight(current.getColumn() > next.getColumn());
                // If the current tile is now facing two directions (this is the 
                // case if the next tile is neither in the same row nor the same 
                // column as the current tile)
            if (current.getDirectionsFacedCount() == 2){
                if (previous == null){  // If the previous tile is null (the 
                        // current tile is the first tile in the snake)
                        // If the next tile is facing either up or down, remove 
                        // any horizontal directions set on the current tile. 
                        // Otherwise, remove any vertical directions set on the 
                        // current tile.
                    current.setFlag((next.isFacingUp() || next.isFacingDown())?
                            HORIZONTAL_DIRECTIONS:VERTICAL_DIRECTIONS, false);
                }
                else    // Remove the direction opposite to the direction that 
                        // will connect the current tile to the previous tile, 
                        // if set
                    current.setFlag(SnakeUtilities.invertDirections(connectDir), 
                            false);
            }
            if (previous != null){  // If the previous tile is not null (the 
                    // current tile is not the first tile in the snake)
                    // If the current tile is currently only facing the 
                    // direction needed to connect to the previous tile
                if (current.getDirectionsFaced() == connectDir)
                    current.setFlag(SnakeUtilities.invertDirections(connectDir), 
                            true);
                else
                    current.setFlag(connectDir, true);
            }
        }
        current.setType(getPlayerType());   // Enforce the snake's player type
            // Get the direction that was set on the current tile, so that we 
            // can connect the next tile to the current tile
        return SnakeUtilities.setFlag(current.getDirectionsFaced(), connectDir, 
                false);
    }
    /**
     * This is used to repair the snake after a tile is removed. This is called 
     * by the {@link Iterator#remove remove} method of the {@link #iterator 
     * iterator} returned by this snake. This should attempt to ensure that the 
     * {@code previous} and {@code next} tiles will join up (i.e. that, if the 
     * {@code previous} and {@code next} tiles were next together, the snake 
     * would be continuous) if possible. The {@code previous} tile will be null 
     * if the tile at the start of the snake was removed. The {@code next} tile 
     * will be null if the tile at the end of the snake was removed. If both are 
     * null, then the snake will be empty.
     * 
     * @todo Finish developing this methods to allow the snake to repair itself 
     * automatically when a tile is removed.
     * 
     * @param previous The tile in the snake that came before the tile that was 
     * removed, or null.
     * @param removed The tile that was removed from the snake.
     * @param next The tile in the snake that came after the tile that was 
     * removed, or null.
     * @see #iterator 
     * @see Iterator#remove
     * @see #isEmpty 
     * @see #repair 
     * @see #repairSegment 
     */
    protected void repairAfterRemoval(Tile previous, Tile removed, Tile next){
            // If the snake is empty or both previous and next tiles are null 
            // (which they would be if the snake is now empty)
        if (isEmpty() || (previous == null && next == null))    
            return;
            // If the previous tile is null (head was removed)
        if (previous == null){  
            if (hasTail())          // If the snake still has a tail
                next.alterDirection(removed);
            else
                next.setState(removed.getState());
        }
        else if (next == null){ // If the next tile is null (tail was removed)
            previous.alterDirection(removed);
        }
        else if (previous.getDirectionsFaced() != removed.getDirectionsFaced() && 
                next.getDirectionsFaced() != removed.getDirectionsFaced() && 
                removed.getDirectionsFaced() != HORIZONTAL_DIRECTIONS && 
                removed.getDirectionsFaced() != VERTICAL_DIRECTIONS && 
                removed.getDirectionsFacedCount() == 2){
            if (previous.getDirectionsFacedCount() == 1 || next.getDirectionsFacedCount() == 1){
                Tile tile = (previous.getDirectionsFacedCount() == 1) ? previous : next;
                tile.alterDirection(removed).flip();
            }
            else{
                
            }
        }
//        else if (previous != peek() && removed.isSnake() && 
//                previous.getDirectionsFaced() != removed.getDirectionsFaced()){
//            if (!previous.isSnake())
//                previous.setState(removed.getState());
//
//            // try to repair the snake by ensuring the previous tile will join 
//            // up with the next
//        }
    }
    /**
     * This returns the model that this snake is displayed on and uses to get 
     * its {@link Tile tiles} from.
     * @return The PlayFieldModel that provides the tiles for this snake.
     * @see #setModel
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
     * {@link Tile tiles} from. This will also cause this snake to be {@link 
     * #clear reset}, resulting in all tiles being removed, the {@link 
     * #getFailCount fail count} being reset to zero, and the status being 
     * reset. Any and all settings previously set for this snake will be
     * maintained. This snake will need to be {@link #initialize(Tile) 
     * reinitialized} with a tile from the new model before it can be used. 
     * @param model The PlayFieldModel that provides the tiles for this snake 
     * (cannot be null).
     * @return This snake.
     * @throws NullPointerException If the model is null.
     * @see #getModel 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #clear 
     * @see #isValid 
     * @see PlayFieldModel
     * @see AbstractPlayFieldModel
     * @see DefaultPlayFieldModel
     */
    public Snake setModel(PlayFieldModel model){
        Objects.requireNonNull(model);      // Check if the model is null
            // If the old and new model are the same
        if (Objects.equals(this.model, model))
            return this;
        PlayFieldModel old = this.model;    // Get the old model
        this.model = model;
        firePropertyChange(MODEL_PROPERTY_CHANGED,old,model);
            // If the old model is not null, reset this snake. (The old model 
        if (old != null)    // will only ever be null during the constructor)
            reset(old);
        return this;
    }
    /**
     * This initializes this snake and sets the given tile to be the snake's 
     * {@link #getHead head}. If this snake was previously initialized, then 
     * this will also {@link #clear reset} the snake. This will result in all 
     * tiles being removed from this snake, the current status of this snake 
     * being reset, and this snake's {@link #getFailCount fail count} being 
     * reset to zero. Any and all settings previously set for this snake will be 
     * maintained. <p>
     * 
     * The tile must be a non-null tile from this snake's {@link #getModel 
     * model}. Additionally, if the tile is not currently in this snake, then 
     * the tile must either be {@link Tile#isEmpty empty} or a {@link 
     * Tile#isSnake snake tile}. If the tile is a snake tile, then 
     * its {@link Tile#getType type flag} must be set to this snake's {@link 
     * #getPlayerType player type} and it must not be facing more than one 
     * direction. If the tile is currently in this snake, then it will be {@link 
     * Tile#clear cleared}, regardless of its current state. As such, if the 
     * tile is currently in this snake, then it will be treated as if it were an 
     * empty tile. <p>
     * 
     * After this is called, the snake will be one tile long, consisting of only 
     * the given tile. If the given tile was either empty or a part of this 
     * snake, then this snake will be facing {@link #isFacingLeft() left}. 
     * Otherwise, if the tile was facing a direction, then this snake will be 
     * facing the direction that the tile was facing.
     * 
     * @param head The tile to use as the head for this snake.  
     * @return This snake.
     * @throws NullPointerException If the given tile is null.
     * @throws IllegalArgumentException If the given tile is either not in the 
     * model or is neither empty, a snake tile, nor currently part of this 
     * snake. If the given tile is a snake tile that is not currently in this 
     * snake, then this will also be thrown if it is either facing multiple 
     * directions or if the tile's type is not the same as this snake's player 
     * type.
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(int, int) 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #offer 
     * @see #add(Tile) 
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #revive 
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
        Objects.requireNonNull(head);   // Check if the proposed head is null
            // If the proposed head is not currently a part of this snake (or if 
            // it is, but it's not in the model)
        if (!contains(head) || !model.contains(head))
            checkOfferedTile(head,null);// Check if the tile can be added
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once we're done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        clear();                        // Reset the snake
        if (head.isEmpty())         // If the new head is empty (no thoughts)
            head.setFacingLeft(true);   // Set the new head to be facing left
        insertHead(head);               // Add the tile as the head of the snake
        fireSnakeChange(SnakeEvent.SNAKE_INITIALIZED,head);
        fireSnakeChange(SnakeEvent.SNAKE_ADDED_TILE,head);
        model.setTilesAreAdjusting(adjusting);
        return this;
    }
    /**
     * This initializes this snake and sets the tile in the {@link #getModel 
     * model} at the given row and column to be the snake's {@link #getHead 
     * head}. If this snake was previously initialized, then this will also 
     * {@link #clear reset} the snake. This will result in all tiles being 
     * removed from this snake, the current status of this snake being reset, 
     * and this snake's {@link #getFailCount fail count} being reset to zero. 
     * Any and all settings previously set for this snake will be maintained. 
     * <p>
     * 
     * If this snake was previously initialized, then this will also 
     * reset the snake.This will result in all tiles being removed from this 
     * snake, the current status of this snake will be reset, and the action 
     * queue will be {@code #clearActionQueue() cleared}. All settings 
     * previously set for this snake will be maintained. After this is called, 
     * the snake will be one tile long, consisting of only the given tile, and 
     * will be facing {@link #isFacingLeft() left}. <p>
     * 
     * If the tile is not currently in this snake, then the tile must either be 
     * {@link Tile#isEmpty empty} or a {@link Tile#isSnake snake tile}. If the 
     * tile is a snake tile, then its {@link Tile#getType type flag} must be set 
     * to this snake's {@link #getPlayerType player type} and it must not be 
     * facing more than one direction. If the tile is currently in this snake, 
     * then it will be {@link Tile#clear cleared}, regardless of its current 
     * state. As such, if the tile is currently in this snake, then it will be 
     * treated as if it were an empty tile. <p>
     * 
     * This is equivalent to calling {@link #initialize(Tile) 
     * initialize}{@code (}{@link #getModel getModel()}{@code .}{@link 
     * PlayFieldModel#getTile getTile}{@code (row, column))}.
     * 
     * @param row The row in the model for the tile to use as the head of this 
     * snake.
     * @param column The column in the model for the tile to use as the head of 
     * this snake.
     * @return This snake.
     * @throws IndexOutOfBoundsException If either the row or column are out of 
     * bounds for the model.
     * @throws IllegalArgumentException If the tile at the given row and column 
     * is neither empty, a snake tile, nor currently part of this snake. If the 
     * tile is a snake tile that is not currently in this snake, then this will 
     * also be thrown if it is either facing multiple directions or if the 
     * tile's type is not the same as this snake's player type.
     * @see #getModel 
     * @see #setModel 
     * @see #initialize(Tile) 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #offer 
     * @see #add(Tile) 
     * @see #contains(Object) 
     * @see #contains(int, int) 
     * @see #isFacingLeft 
     * @see #getPlayerType 
     * @see #setPlayerType 
     * @see #revive 
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
     * valid state when it is not {@link #isEmpty empty}, its {@link #getHead 
     * head} is a non-null tile {@link PlayFieldModel#contains(Tile) contained} 
     * in its {@link #getModel model}, and it's {@link #getDirectionFaced 
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
     * @see #repair 
     */
    public boolean isValid(){
        return model != null && !isEmpty() && getHead() != null && 
                model.contains(getHead()) && 
                getHead().getDirectionsFacedCount()==1;
    }
    /**
     * This checks to see if this snake is in a {@link #isValid valid} state, 
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
    /**
     * This returns the tile that represents the start or head of this snake. 
     * This will be the first tile in this snake. If this is {@link #isEmpty 
     * empty}, then this returns null. <p>
     * 
     * Note that this is different from the head of the queue represented by 
     * this snake. Snakes act like a queue but in reverse, and thus the 
     * <em>head</em> of the <em>snake</em> is technically the <em>tail</em> of 
     * the <em>queue</em>, and vice versa. The {@link #peek peek} method can be 
     * used to get the head of the <em>queue</em> represented by this snake. The 
     * {@link #getTail getTail} method can also be used to get the head of the 
     * queue, but only when the snake is at least two tiles long, as it will 
     * otherwise return null.
     * 
     * @return The head of this snake, or null if this snake has no head.
     * @see #getTail 
     * @see #peek 
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
     * This returns whether this snake has a tail. This is used internally to 
     * determine if the snake has a tail, and returns whether the snake is at 
     * least two tiles long.
     * @return Whether this snake has a tail.
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #isEmpty 
     * @see #size 
     */
    protected boolean hasTail(){
        return size() >= 2;
    }
    /**
     * This returns the tile that represents the end or tail of this snake. If 
     * this snake is either {@link #isEmpty empty} or only has a {@link #getHead 
     * head} (i.e. this snake is less than two tiles long), then this returns 
     * null. This method is similar to {@link #peek peek}, with the exception 
     * that this will return null if this snake is less than two tiles long. <p>
     * 
     * Note that this is different from the tail of the queue represented by 
     * this snake. Snakes act like a queue but in reverse, and thus the 
     * <em>tail</em> of the <em>snake</em> is technically the <em>head</em> of 
     * the <em>queue</em>, and vice versa. As such, the {@link #getHead getHead} 
     * method can be used to get the tail of the <em>queue</em> represented by 
     * this snake.
     * 
     * @return The tail of this snake, or null if this snake has no tail.
     * @see #peek 
     * @see #getHead 
     * @see #size 
     * @see #isEmpty 
     * @see #flip 
     * @see #isFlipped 
     * @see #poll
     * @see #remove() 
     * @see #removeTail 
     */
    public Tile getTail(){
            // If the snake has a tail, peek at the queue to get the snake's 
            // tail. Otherwise, return null.
        return (hasTail()) ? peek() : null;
    }
    /**
     * This returns whether this snake is facing up. This is equivalent to 
     * calling {@link Tile#isFacingUp isFacingUp} on the {@link #getHead head} 
     * of this snake. If this snake does not have a head, then this will return 
     * {@code false}.
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
     * calling {@link Tile#isFacingDown isFacingDown} on the {@link #getHead 
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
     * to calling {@link Tile#isFacingLeft isFacingLeft} on the {@link #getHead 
     * head} of this snake. If this snake does not have a head, then this will 
     * return {@code false}.
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
     * equivalent to calling {@link Tile#isFacingRight isFacingRight} on the 
     * {@link #getHead head} of this snake. If this snake does not have a head, 
     * then this will return {@code false}.
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
     * Tile#getDirectionsFaced getDirectionsFaced} on the {@link #getHead head} 
     * of this snake. As a result, if the head of this snake is facing multiple 
     * directions, then this will return the flags for all the directions faced. 
     * However, keep in mind that a snake is only {@link #isValid valid} if it 
     * has a head that is facing a single direction. If this snake does not have 
     * a head (i.e. if this snake is {@link #isEmpty empty}), then this will 
     * return zero.
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
     * @see #size 
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
     * the direction is replaced with the {@link #getDirectionFaced direction 
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
     * This returns the tile in the {@link #getModel model} that is {@link 
     * PlayFieldModel#getAdjacentTile adjacent} to the {@link #getHead head} of 
     * this snake in the given direction. The direction must be either zero or 
     * one of the four direction flags: {@link #UP_DIRECTION}, {@link 
     * #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. 
     * If the direction is zero, then this will get the tile in front of this 
     * snake (i.e. the tile adjacent to the head in the {@link 
     * #getDirectionFaced direction being faced}). This will use whether the 
     * snake {@link #isWrapAroundEnabled wraps around} to determine how to treat 
     * getting the tile adjacent to the head when the adjacent tile would be out 
     * of bounds. If the snake can wrap around, then this will wrap around and 
     * get a tile from the other side of the model when the adjacent tile would 
     * be out of bounds. Otherwise, this will return null when the adjacent tile 
     * would be out of bounds. <p>
     * 
     * This calls the {@link PlayFieldModel#getAdjacentTile getAdjacentTile} 
     * method of the {@link #getModel model}, providing it with the {@link 
     * #getHead head} of the snake for the tile, the given direction 
     * (substituting it with the {@link #getDirectionFaced direction faced} if 
     * the given direction is zero), and whether this snake is allowed to {@link 
     * #isWrapAroundEnabled wrap around}. 
     * 
     * @param direction The direction indicating which adjacent tile to return. 
     * This should be one of the following: 
     *      {@code 0} to get the tile being {@link #getDirectionFaced faced}, 
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
     * nor one of the four direction flags.
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
     * @see #contains(Object) 
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
     * This returns the tile {@link #getDirectionFaced in front} of the {@link 
     * #getHead head} of this snake. This is equivalent to calling {@link 
     * #getAdjacentToHead getAdjacentToHead}{@code (}{@link #getDirectionFaced 
     * getDirectionFaced()}{@code )}. As such, the head should be facing a 
     * single direction, which it must be for the snake to be {@link #isValid 
     * valid}. If the tile being faced is out of bounds, then this will either 
     * wrap around and return the tile on the other side of the {@link #getModel 
     * model} or return null, depending on whether the snake {@link
     * #isWrapAroundEnabled wraps around}.
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
     * @see #contains(Object) 
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
     * @see #peek 
     */
    public boolean isFlipped(){
        return getFlag(FLIPPED_FLAG);
    }
    /**
     * This sets whether this snake is flipped. When a snake is flipped, the 
     * order of the tiles in the snake are reversed. If there is only one tile, 
     * then the {@link #getHead() head} of this snake will also {@link Tile#flip 
     * flip}. This will also fire a {@code SnakeEvent} indicating that the snake 
     * has {@link SnakeEvent#SNAKE_FLIPPED flipped} if a change is made.
     * @param value Whether this snake should be flipped.
     * @return This snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFlipped 
     * @see #flip 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #isValid 
     * @see Tile#flip 
     * @see SnakeEvent#SNAKE_FLIPPED
     */
    protected Snake setFlipped(boolean value){
        checkIfInvalid();                   // Check if this snake is invalid
        if (setFlag(FLIPPED_FLAG,value)){   // If the flag changed
            if (!hasTail())                // If this snake does not have a tail
                getHead().flip();           // Flip the head
            fireSnakeChange(SnakeEvent.SNAKE_FLIPPED);
        }
        return this;
    }
    /**
     * This flips the orientation of this snake. This will reverse the order of 
     * the tiles in this snake, resulting in the {@link #getHead head} becoming 
     * the {@link #getTail() tail} and vice versa. If this snake does not have a 
     * tail (i.e. there is only one tile in this snake, that being the head), 
     * then the head of the snake will be {@link Tile#flip flipped}. This will 
     * also fire a {@code SnakeEvent} indicating that the snake has {@link 
     * SnakeEvent#SNAKE_FLIPPED flipped}.
     * @return This snake.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFlipped 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #isValid 
     * @see Tile#flip 
     * @see SnakeEvent#SNAKE_FLIPPED
     */
    public Snake flip(){
        return setFlipped(!isFlipped());
    }
    /**
     * This returns whether this snake will wrap around to the other side of the 
     * play field when it reaches the boundaries of the {@link #getModel model}. 
     * If this is {@code true} and this snake has reached the edge of the play 
     * field, then attempting to {@link #getAdjacentToHead get}, {@link 
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
     * boundaries of the {@link #getModel model}. Refer to the documentation for 
     * the {@link #isWrapAroundEnabled() isWrapAroundEnabled} method for more 
     * information on how this is used. The default value for this is {@code 
     * true}. 
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
     * #move(int) move} to {@link Tile#isApple apple tiles}. If this is {@code 
     * false}, then any attempt to add or move to an apple tile will fail. This 
     * does not affect directly {@link #add(Tile) adding} or {@link #offer 
     * offering} tiles to this snake. The default value for this is {@code 
     * true}. 
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
     * is {@code true}, then {@link #move(int) moving to} an {@link Tile#isApple 
     * apple tile} will be treated the same as {@link #add(int) adding} an apple 
     * tile. In other words, when an apple is eaten, then this snake will grow 
     * by one tile regardless of whether this snake moved or was added to. If 
     * this is {@code false}, then moving to an apple tile will not cause this 
     * snake to grow. If this snake {@link #isAppleConsumptionEnabled cannot eat 
     * apples}, then this has no effect. The default value for this is {@code 
     * true}. 
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
    /**
     * This sets whether this snake has eaten an apple. This ignores whether 
     * this snake {@link #isAppleConsumptionEnabled can even eat apples}. If the 
     * given {@code value} is {@code true}, then this will fire a {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} {@code SnakeEvent} 
     * with the given direction and target tile. 
     * @param value Whether this snake ate an apple. If this is {@code true}, 
     * then a {@link SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} will 
     * be fired.
     * @param direction The direction to use for the event fired if {@code 
     * value} is {@code true}.
     * @param tile The target tile to use for the event fired if {@code value} 
     * is {@code true}, or null.
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
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #getDirectionFaced 
     * @see #getHead 
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     */
    protected void setConsumedApple(boolean value, int direction, Tile tile){
        checkIfInvalid();                   // Check if this snake is invalid
        setFlag(APPLE_CONSUMED_FLAG,value);
        if (value)                          // If the value is true
            fireSnakeChange(SnakeEvent.SNAKE_CONSUMED_APPLE,direction,tile);
    }
    /**
     * This sets whether this snake has eaten an apple. This ignores whether 
     * this snake {@link #isAppleConsumptionEnabled can even eat apples}. If the 
     * given {@code value} is {@code true}, then this will fire a {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} {@code SnakeEvent} 
     * with the {@link #getDirectionFaced direction currently being faced} and 
     * the current {@link #getHead head} as the target tile.
     * @param value Whether this snake ate an apple. If this is {@code true}, 
     * then a {@link SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} will 
     * be fired.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #hasConsumedApple 
     * @see #setConsumedApple(boolean, int, Tile) 
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
     * @see #getHead 
     * @see SnakeEvent#SNAKE_CONSUMED_APPLE
     */
    protected void setConsumedApple(boolean value){
        setConsumedApple(value,getDirectionFaced(),getHead());
    }
    /**
     * This returns whether this snake has crashed. A snake will crash when its 
     * {@link #getFailCount fail count} has exceeded its set {@link 
     * #getAllowedFails allowed number of failures}. If the allowed number of 
     * failures is negative, then a snake is allowed to fail an unlimited number 
     * of times without crashing. <p>
     * 
     * A snake that has crashed will not be able to {@link #add(int) add} or 
     * {@link #move(int) move} to tiles until the snake has been {@link #revive 
     * revived} or {@link #clear reset}. This does not affect the {@link #offer 
     * offer(Tile)}, {@link #add(Tile) add(Tile)}, and {@link #addAll addAll} 
     * methods.
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
     * the {@link #isCrashed isCrashed} method for more information on what it 
     * means for a snake to crash. If {@code value} causes a change to whether 
     * this snake has crashed or not, then this will fire a snake event 
     * indicating the change made and using the given direction and target tile. 
     * If the snake has now crashed, then a {@link SnakeEvent#SNAKE_CRASHED 
     * SNAKE_CRASHED} {@code SnakeEvent} will be fired. Otherwise, a {@link 
     * SnakeEvent#SNAKE_REVIVED SNAKE_REVIVED} {@code SnakeEvent} will be fired. 
     * <p>
     * It's recommended to use the {@link #revive() revive} method to revive a 
     * snake, as the {@code revive} method will also reset the conditions that 
     * resulted in the snake crashing.
     * 
     * @param value Whether this snake has crashed. 
     * @param direction The direction to use for the event fired if this has 
     * changed whether this snake has crashed or not.
     * @param target The target tile to use for the event fired if this has 
     * changed whether this snake has crashed or not, or null.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isCrashed 
     * @see #setCrashed(boolean) 
     * @see #getSnakeWillNowCrash 
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
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #getDirectionFaced 
     * @see SnakeEvent#SNAKE_CRASHED
     * @see SnakeEvent#SNAKE_REVIVED
     */
    protected void setCrashed(boolean value, int direction, Tile target){
        checkIfInvalid();                   // Check if this snake is invalid
        if (setFlag(CRASHED_FLAG,value)){   // If the flag changed
                // Fire a snake crashed event if the snake crashed, and a snake 
                // revived event if it is no longer crashed
            fireSnakeChange((value)?SnakeEvent.SNAKE_CRASHED:
                    SnakeEvent.SNAKE_REVIVED,direction,target);
        }
    }
    /**
     * This sets whether this snake has crashed. Refer to the documentation for 
     * the {@link #isCrashed isCrashed} method for more information on what it 
     * means for a snake to crash. If {@code value} causes a change to whether 
     * this snake has crashed or not, then this will fire a snake event 
     * indicating the change made and using the {@link #getDirectionFaced 
     * direction currently being faced} and with no target tile. If the snake 
     * has now crashed, then a {@link SnakeEvent#SNAKE_CRASHED SNAKE_CRASHED} 
     * {@code SnakeEvent} will be fired. Otherwise, a {@link 
     * SnakeEvent#SNAKE_REVIVED SNAKE_REVIVED} {@code SnakeEvent} will be fired. 
     * <p>
     * It's recommended to use the {@link #revive revive} method to revive a 
     * snake, as the {@code revive} method will also reset the conditions that 
     * resulted in the snake crashing.
     * 
     * @param value Whether this snake has crashed. 
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isCrashed 
     * @see #setCrashed(boolean, int, Tile) 
     * @see #getSnakeWillNowCrash 
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
        setCrashed(value,getDirectionFaced(),null);
    }
    /**
     * This returns whether this snake will {@link #isCrashed crash}. This is 
     * used to update whether the snake has crashed when it fails to {@link 
     * #addOrMove add or move to} a tile. This is based off whether the snake's 
     * {@link #getFailCount fail count} has exceeded a non-negative {@link 
     * #getAllowedFails allowed number of failures}. To set whether the snake 
     * has crashed, use the {@link #setCrashed setCrashed} method. This method 
     * is here so that a subclass can override this to add or remove conditions 
     * that, when met, will cause the snake to crash.
     * @return {@code true} if the snake will crash, else {@code false}.
     * @see #isCrashed 
     * @see #setCrashed 
     * @see #setCrashed(boolean) 
     * @see #revive 
     * @see #isValid 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #canAddTile 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #addOrMove 
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     */
    protected boolean getSnakeWillNowCrash(){
        return getAllowedFails() >= 0 && getFailCount() > getAllowedFails();
    }
    /**
     * This returns the maximum amount of consecutive failed attempts that this 
     * snake can make before it will {@link #isCrashed crash}. In other words, 
     * if this snake's {@link #getFailCount fail count} exceeds this value, then 
     * this snake will crash. If this is negative, then this snake can fail an 
     * unlimited amount of times without crashing. The default value for this is 
     * -1. 
     * @return The amount of times that this snake can fail consecutively before 
     * it crashes. If this is negative, then this snake can fail an unlimited 
     * amount of times.
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #isCrashed 
     * @see #revive 
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
     * can make before it will {@link #isCrashed crash}. Refer to the 
     * documentation for the {@link #getAllowedFails getAllowedFails} method for 
     * more information about how this value is used. If this snake is {@link 
     * #isValid valid} and the allowed number of fails changes, then this will 
     * also {@link #revive revive} this snake if necessary. The default value 
     * for this is -1. 
     * @param value The amount of times that this snake will be allowed to fail 
     * consecutively before it crashes. If this is negative, then this snake 
     * will be allowed to fail an unlimited amount of times.
     * @return This snake.
     * @see #getAllowedFails 
     * @see #getFailCount 
     * @see #isCrashed 
     * @see #revive 
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
     * attempts to add or move the snake backwards when it has a tail (when 
     * {@link #getTail getTail} returns a non-null tile, or equivalently when 
     * the snake is at least two tiles long) and attempts to add or move the 
     * snake when it has {@link #isCrashed crashed}. This value is reset to zero 
     * when either this snake successfully adds or moves to a tile, when this 
     * snake is {@link #clear reset}, or when this snake is {@link #revive 
     * revived}. If the {@link #getAllowedFails allowed number of failures} is 
     * not negative, then this snake will crash when this value exceeds the 
     * allowed number of failures. 
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
     * This sets the fail count for this snake. Refer to the documentation for 
     * the {@link #getFailCount getFailCount} method for more information about 
     * how the fail count is used.
     * @param count The fail count for this snake.
     * @see #getFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, int, Tile) 
     * @see #setCrashed(boolean) 
     * @see #getSnakeWillNowCrash 
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
     * This increments this snake's fail count. Refer to the documentation for 
     * the {@link #getFailCount getFailCount} method for more information about 
     * how the fail count is used.
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #resetFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, int, Tile) 
     * @see #setCrashed(boolean) 
     * @see #getSnakeWillNowCrash 
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
     * This clears the fail count for this snake, resetting it back to zero. 
     * Refer to the documentation for the {@link #getFailCount getFailCount} 
     * method for more information about how the fail count is used. This method 
     * does not clear the snake's crashed status, and thus it is recommended to 
     * call {@link #revive revive} if the desired goal is to revive the snake.
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #isCrashed 
     * @see #setCrashed(boolean, int, Tile) 
     * @see #setCrashed(boolean) 
     * @see #getSnakeWillNowCrash 
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
     * reset the {@link #getFailCount number of fails} to zero and fire a {@code 
     * SnakeEvent} indicating that the snake has been {@link 
     * SnakeEvent#SNAKE_REVIVED revived}. Refer to the documentation for the 
     * {@link #isCrashed isCrashed} method for more information about what it 
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
        setCrashed(false);      // Clear the crashed status  
        return this;
    }
    /**
     * This checks to see if this snake can add or move to the given tile. If 
     * the given tile is null, then this will return false. If the tile is an 
     * {@link Tile#isApple apple tile}, then this returns whether this snake 
     * {@link #isAppleConsumptionEnabled can eat apples}. Otherwise, this 
     * returns whether the tile is {@link Tile#isEmpty empty}. <p>
     * 
     * This is called by the {@link #addOrMove addOrMove} method to see if the 
     * tile it is attempting to add/move to can be added or moved to. This is 
     * also called by the {@link #canMoveInDirection canMoveInDirection} method 
     * to get whether the snake can add/move to a tile {@link #getAdjacentToHead 
     * adjacent} to the {@link #getHead head}.
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
     * @see #isCrashed 
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
     * #move(int) move} to the tile {@link #getAdjacentToHead adjacent} to the 
     * {@link #getHead head} of this snake in the given direction. The direction 
     * must be either zero or one of the four direction flags: {@link 
     * #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link #LEFT_DIRECTION}, and 
     * {@link #RIGHT_DIRECTION}. If the direction is zero, then this will return 
     * whether this snake can {@link #add() add} or {@link #move() move} forward 
     * (i.e. whether this snake can add or move in the {@link #getDirectionFaced 
     * direction faced}). A snake can add or move in a given direction if the 
     * snake has not {@link #isCrashed crashed} and the tile adjacent to the 
     * snake's head is a non-null tile that is either {@link Tile#isEmpty empty} 
     * or an {@link Tile#isApple apple tile}, with the latter being dependent on 
     * whether the snake {@link #isAppleConsumptionEnabled can eat apples}. If a 
     * snake cannot eat apples, then it cannot add or move to apple tiles. If a 
     * snake can {@link #isWrapAroundEnabled wrap around}, then attempting to 
     * add or move beyond the bounds of the {@link #getModel play field} would 
     * result in the snake adding or moving to the tiles on the other side of 
     * the play field. If the snake cannot wrap around, then it will be stopped 
     * by the bounds of the play field. Refer to the documentation for the 
     * {@link #getAdjacentToHead getAdjacentToHead} method for more information 
     * on how this gets the tile adjacent to the head.
     * @param direction The direction in which to check for whether this snake 
     * can move. 
     * This should be one of the following: 
     *      {@code 0} to get if the snake can move {@link #getDirectionFaced 
     *          forward}, 
     *      {@link #UP_DIRECTION} to get if the snake can move up, 
     *      {@link #DOWN_DIRECTION} to get if the snake can move down, 
     *      {@link #LEFT_DIRECTION} to get if the snake can move left, or 
     *      {@link #RIGHT_DIRECTION} to get if the snake can move right.
     * @return Whether this snake can move in the given direction.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @throws IllegalArgumentException If the given direction is neither zero 
     * nor one of the four direction flags.
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveForward 
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
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #canPerformAction(SnakeCommand) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
     */
    public boolean canMoveInDirection(int direction){
        checkIfInvalid();       // Check if this snake is invalid
        return !isCrashed() && canAddTile(getAdjacentToHead(direction));
    }
    /**
     * This returns whether this snake can {@link #add() add} or {@link #move() 
     * move} {@link #getDirectionFaced forward}. A snake can add or move forward 
     * if the snake has not {@link #isCrashed crashed} and the tile {@link 
     * #getTileBeingFaced in front} of the snake is a non-null tile that is 
     * either {@link Tile#isEmpty empty} or an {@link Tile#isApple apple tile}, 
     * with the latter being dependent on whether the snake {@link 
     * #isAppleConsumptionEnabled can eat apples}. If a snake cannot eat apples, 
     * then it cannot add or move to apple tiles. If a snake can {@link 
     * #isWrapAroundEnabled wrap around}, then attempting to add or move beyond 
     * the bounds of the {@link #getModel play field} would result in the snake 
     * adding or moving to the tiles on the other side of the play field. If the 
     * snake cannot wrap around, then it will be stopped by the bounds of the 
     * play field. Refer to the documentation for the {@link #getTileBeingFaced 
     * getTileBeingFaced} method for more information on how this gets the tile 
     * in front of the snake. <p> 
     * 
     * This is equivalent to calling {@link #canMoveInDirection 
     * canMoveInDirection}{@code (}{@link #getDirectionFaced 
     * getDirectionFaced()}{@code )}. 
     * 
     * @return Whether this snake can move forward.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canMoveInDirection 
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
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #canPerformAction(SnakeCommand) 
     * @see Tile#getRow 
     * @see Tile#getColumn 
     * @see Tile#isEmpty 
     * @see Tile#isApple 
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
     * moving the snake is whether the current {@link #getTail tail} gets 
     * removed. A snake can only add or move if it has not {@link #isCrashed 
     * crashed} and it {@link #canMoveInDirection can move} in the given 
     * direction. The direction must be either zero or one of the four direction 
     * flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction is zero, 
     * then this will use the {@link #getDirectionFaced direction being faced} 
     * instead. Refer to the documentation for the {@link #canMoveInDirection 
     * canMoveInDirection} method for more information about how this checks to 
     * see if this snake can move in a given direction. <p>
     * 
     * This starts by first checking to see if this snake has crashed, and if 
     * so, this fires a {@link SnakeEvent#SNAKE_FAILED SNAKE_FAILED} {@code 
     * SnakeEvent} and returns {@code false}. If this snake has not crashed, 
     * then this will get the tile {@link #getAdjacentToHead adjacent} to the 
     * {@link #getHead head} in the given direction. If the direction is zero, 
     * then this will use the tile {@link #getTileBeingFaced in front} of this 
     * snake. Refer to the documentation for the {@link #getAdjacentToHead 
     * getAdjacentToHead} method for more information about how this gets the 
     * tile adjacent to the head. This will then check to see if this can add 
     * the tile to the snake using the {@link #canAddTile canAddTile} method. If 
     * this cannot add the tile, then this will fire a {@code SNAKE_FAILED 
     * SnakeEvent} and return {@code false}. If the snake is only a head (i.e. 
     * the snake is less than two tiles long) or if the snake did not try to go 
     * backwards, then this will also {@link #incrementFailCount increment} the 
     * {@link #getFailCount fail count}. This will then call {@link #setCrashed 
     * setCrashed} with the value returned by {@link #getSnakeWillNowCrash 
     * getSnakeWillNowCrash}, so as to update whether this snake has crashed due 
     * to it's fail count exceeding a non-negative {@link #getAllowedFails 
     * allowed number of failures}. If this snake has crashed as a result, then 
     * a {@link SnakeEvent#SNAKE_CRASHED SNAKE_CRASHED} {@code SnakeEvent} will 
     * be fired. <p>
     * 
     * If this can add the tile, then this will check to see if the tile is an 
     * {@link Tile#isApple apple tile} before setting the tile to be a {@link 
     * Tile#isSnake snake tile} facing the given direction (or faced direction 
     * if the given direction was zero) and {@link #insertHead add} it to the 
     * body of this snake as the new head. This will also result in the fail 
     * count being {@link #resetFailCount reset}. If the tile was an apple tile 
     * and this snake {@link #getApplesCauseGrowth grows when it eats apples}, 
     * then {@code moveSnake} will be ignored and and the snake will grow by one 
     * tile. If this is moving the snake (i.e. {@code moveSnake} is {@code true} 
     * and either the tile that was added was not an apple tile or apples do not 
     * cause this snake to grow), then the snake's tail is {@link #pollTail 
     * removed}. This will then call {@link #setConsumedApple setConsumedApple} 
     * to update whether this snake ate an apple and fire a {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE SNAKE_CONSUMED_APPLE} {@code SnakeEvent} 
     * if it did. This will then fire a {@link SnakeEvent#SNAKE_ADDED_TILE 
     * SNAKE_ADDED_TILE} {@code SnakeEvent}. If this moved the snake, then this 
     * will also fire a {@link SnakeEvent#SNAKE_REMOVED_TILE SNAKE_REMOVED_TILE} 
     * {@code SnakeEvent} and a {@link SnakeEvent#SNAKE_MOVED SNAKE_MOVED} 
     * {@code SnakeEvent} to indicate that the tail was removed and that the 
     * snake has moved. Afterwards, this will finally return {@code true} to 
     * indicate that this succeeded in adding to or moving the snake. <p>
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
     *      {@code 0} to use the tile being {@link #getDirectionFaced faced}, 
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
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
     * @see #getAdjacentToHead 
     * @see #getTileBeingFaced 
     * @see #canAddTile 
     * @see #canMoveInDirection 
     * @see #canMoveForward 
     * @see #getModel 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #insertHead 
     * @see #pollTail 
     * @see #isValid 
     * @see #initialize(Tile) 
     * @see #initialize(int, int) 
     * @see #contains(Object) 
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
     * @see #getSnakeWillNowCrash 
     * @see #revive 
     * @see #getAllowedFails 
     * @see #setAllowedFails 
     * @see #getFailCount 
     * @see #setFailCount 
     * @see #incrementFailCount 
     * @see #resetFailCount 
     * @see #offer 
     * @see #add(Tile) 
     * @see #poll 
     * @see #remove() 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_REMOVED_TILE
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
     */
    protected boolean addOrMove(int direction, boolean moveSnake){
        checkIfInvalid();       // Check if this snake is invalid
        direction = checkDirection(direction);  // Check the direction
            // Get the tile adjacent to the head.
        Tile tile = getAdjacentToHead(direction);   
            // Get whether the tiles in the model are currently adjusting, so as 
            // to restore this once done
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
            // If either the snake has crashed or the snake cannot add the tile
        if (isCrashed() || !canAddTile(tile)){
                // If the snake has not crashed (the snake must not have been 
                // able to add the tile) and if the snake is only a head or the 
                // snake was not attempting to add or move backwards (if the 
                // snake was not trying to add the tile in the snake that's 
            if (!isCrashed() && (!hasTail() || direction != // behind the head)
                    SnakeUtilities.invertDirections(getDirectionFaced())))
                incrementFailCount();       // Increment the fail count
            fireSnakeFailed(direction,tile);// Fire a snake failed event
            if (!isCrashed())               // If the snake has not crashed yet
                    // Update whether the snake has now crashed
                setCrashed(getSnakeWillNowCrash(),direction,tile);
            model.setTilesAreAdjusting(adjusting);
            return false;
        }   // Get whether the tile being added is (or was) an apple
        boolean ateApple = tile.isApple();  
        insertHead(tile.setState(direction));   // Add the tile to the snake
            // If the snake ate an apple and apples cause the snake to grow
        if (ateApple && getApplesCauseGrowth())
            moveSnake = false;  // Override moveSnake to make the snake grow
        Tile tail = null;       // This gets the tail if the snake is moving
        if (moveSnake)          // If the snake is moving
            tail = pollTail();  // Remove the current tail
            // Set whether the snake ate an apple based off whether the tile was 
            // an apple tile. This will also fire an event if an apple was eaten
        setConsumedApple(ateApple,direction,tile);
            // Fire a snake added tile event
        fireSnakeChange(SnakeEvent.SNAKE_ADDED_TILE,direction,tile);
        if (moveSnake){         // If the snake moved
            if (tail != null)   // If a tile was removed
                    // Fire a snake removed tile event
                fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,tail);
                // Fire a snake moved event
            fireSnakeChange(SnakeEvent.SNAKE_MOVED,direction,tile);
        }
        model.setTilesAreAdjusting(adjusting);
        return true;
    }
    /**
     * This attempts to add the tile {@link #getAdjacentToHead adjacent} to the 
     * {@link #getHead head} of this snake. This will return {@code true} if the 
     * tile is successfully added to this snake, and {@code false} if this fails 
     * to add the tile. The tile will only be added if this snake has not {@link 
     * #isCrashed crashed} and {@link #canMoveInDirection can move} in the given 
     * direction. The direction must be either zero or one of the four direction 
     * flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction is zero, 
     * then this will attempt to add the tile {@link #getTileBeingFaced in 
     * front} of this snake (i.e. this will attempt to add the tile adjacent to 
     * the head in the {@link #getDirectionFaced direction being faced}). Refer 
     * to the documentation for the {@link #getAdjacentToHead getAdjacentToHead} 
     * method for more information about how this gets the tile adjacent to the 
     * head, and to the documentation for the {@link #canMoveInDirection 
     * canMoveInDirection} method for more information about how this checks to 
     * see if this snake can move in a given direction. <p>
     * 
     * If the tile is successfully added to this snake, then this snake will 
     * grow in length by one tile, the tile that was added will become the new 
     * head, the snake's {@link #getFailCount fail count} will be reset to zero, 
     * and this will fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_ADDED_TILE tile was added}. If the tile was an {@link 
     * Tile#isApple apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple hasConsumedApple} will return 
     * {@code true}. Snakes can only add apple tiles if they are able to {@link 
     * #isAppleConsumptionEnabled eat apples}. If the snake failed to add the 
     * tile, then this will fire a {@code SnakeEvent} indicating that the snake 
     * {@link SnakeEvent#SNAKE_FAILED failed} to add a tile. When a snake fails 
     * to move or add a tile, and it was not attempting to add or move to the 
     * tile in the snake behind the head (if the snake was not attempting to add 
     * or move backwards), then its fail count will be incremented. If a snake's 
     * fail count exceeds its {@link #getAllowedFails allowed number of 
     * failures}, then it will crash. If the allowed number of failures is 
     * negative, then the snake will be allowed to fail an unlimited amount of 
     * times without crashing. In other words, if the allowed number of failures 
     * is negative, then the snake cannot crash by failing to add or move to a 
     * tile. If this snake crashes, then this will fire a {@code SnakeEvent} 
     * indicating that the snake has {@link SnakeEvent#SNAKE_CRASHED crashed}. 
     * Once a snake has crashed, it will be unable to add or move to a tile 
     * until it has been {@link #revive revived}. <p>
     * 
     * The {@link #move(int) move(int)} method works similarly to this, with the 
     * exception being that this snake will remain the same length unless an 
     * apple is eaten and the snake is set to {@link #getApplesCauseGrowth grow 
     * when it eats an apple}.
     * 
     * @param direction The direction in which to get the tile to add to this 
     * snake. 
     * This should be one of the following: 
     *      {@code 0} to add the tile {@link #getDirectionFaced in front} of the 
     *          snake, 
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
     * @see #add() 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
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
     * @see #contains(Object) 
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
     * @see #offer 
     * @see #add(Tile) 
     * @see #peek 
     * @see #poll 
     * @see #remove() 
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
     */
    public boolean add(int direction){
        return addOrMove(direction,false);
    }
    /**
     * This attempts to add the tile {@link #getTileBeingFaced in front} of the 
     * {@link #getHead head} of this snake. This will return {@code true} if the 
     * tile is successfully added to this snake, and {@code false} if this fails 
     * to add the tile. The tile will only be added if this snake has not {@link 
     * #isCrashed crashed} and {@link #canMoveForward can move forward}. Refer 
     * to the documentation for the {@link #getTileBeingFaced getTileBeingFaced} 
     * method for more information about how this gets the tile in front of the 
     * head, and to the documentation for the {@link #canMoveForward 
     * canMoveForward} method for more information about how this checks to see 
     * if this snake can move forward. <p>
     * 
     * If the tile is successfully added to this snake, then this snake will 
     * grow in length by one tile, the tile that was added will become the new 
     * head, the snake's {@link #getFailCount fail count} will be reset to zero, 
     * and this will fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_ADDED_TILE tile was added}. If the tile was an {@link 
     * Tile#isApple apple tile}, then this will also fire a {@code SnakeEvent} 
     * indicating that an {@link SnakeEvent#SNAKE_CONSUMED_APPLE apple was 
     * consumed} and {@link #hasConsumedApple hasConsumedApple} will return 
     * {@code true}. Snakes can only add apple tiles if they are able to {@link 
     * #isAppleConsumptionEnabled eat apples}. If the snake failed to add the 
     * tile, then this will fire a {@code SnakeEvent} indicating that the snake 
     * {@link SnakeEvent#SNAKE_FAILED failed} to add a tile. When a snake fails 
     * to move or add a tile, and it was not attempting to add or move to the 
     * tile in the snake behind the head (if the snake was not attempting to add 
     * or move backwards), then its fail count will be incremented. If a snake's 
     * fail count exceeds its {@link #getAllowedFails allowed number of 
     * failures}, then it will crash. If the allowed number of failures is 
     * negative, then the snake will be allowed to fail an unlimited amount of 
     * times without crashing. In other words, if the allowed number of failures 
     * is negative, then the snake cannot crash by failing to add or move to a 
     * tile. If this snake crashes, then this will fire a {@code SnakeEvent} 
     * indicating that the snake has {@link SnakeEvent#SNAKE_CRASHED crashed}. 
     * Once a snake has crashed, it will be unable to add or move to a tile 
     * until it has been {@link #revive revived}. <p>
     * 
     * The {@link #move() move()} method works similarly to this, with the 
     * exception being that this snake will remain the same length unless an 
     * apple is eaten and the snake is set to {@link #getApplesCauseGrowth grow 
     * when it eats an apple}. <p>
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
     * @see #add(int) 
     * @see #move(int) 
     * @see #move() 
     * @see #removeTail 
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
     * @see #contains(Object) 
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
     * @see #offer 
     * @see #add(Tile) 
     * @see #peek 
     * @see #poll 
     * @see #remove() 
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
     */
    public boolean add(){
        return add(getDirectionFaced());
    }
    /**
     * This attempts to move this snake in the given direction. This will return 
     * {@code true} if this snake has successfully moved, and {@code false} if 
     * this fails to move the snake. Snakes can only move if they have not 
     * {@link #isCrashed crashed} and {@link #canMoveInDirection can move} in 
     * the given direction. The direction must be either zero or one of the four 
     * direction flags: {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
     * #LEFT_DIRECTION}, and {@link #RIGHT_DIRECTION}. If the direction is zero, 
     * then this will attempt to move the snake {@link #getDirectionFaced 
     * forward}. Refer to the documentation for the {@link #canMoveInDirection 
     * canMoveInDirection} method for more information about how this checks to 
     * see if this snake can move in a given direction. <p>
     * 
     * If this snake successfully moved, then the tile {@link #getAdjacentToHead 
     * adjacent} to the {@link #getHead head} of this snake will become the new 
     * head, the current {@link #peek tail} (or the current head if this snake 
     * does not have a tail) will be removed from this snake, the snake's {@link 
     * #getFailCount fail count} will be reset to zero, and this will fire a 
     * {@code SnakeEvent} indicating that this snake has {@link 
     * SnakeEvent#SNAKE_MOVED moved}, along with {@code SnakeEvent}s indicating 
     * that a tile was {@link SnakeEvent#SNAKE_ADDED_TILE added} and that a tile 
     * was {@link SnakeEvent#SNAKE_REMOVED_TILE removed}. Refer to the 
     * documentation for the {@link #getAdjacentToHead getAdjacentToHead} method 
     * for more information about how this gets the tile adjacent to the head. 
     * If the tile was an {@link Tile#isApple apple tile}, then this will also 
     * fire a {@code SnakeEvent} indicating that an {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE apple was consumed} and {@link 
     * #hasConsumedApple hasConsumedApple} will return {@code true}. If this 
     * snake {@link #getApplesCauseGrowth grows when it eats apples}, then 
     * eating an apple will cause a tile to be {@link #add(int) added} to this 
     * snake instead of moving this snake. Snakes can only move to apple tiles 
     * if they are able to {@link #isAppleConsumptionEnabled eat apples}. If the 
     * snake failed to move, then this will fire a {@code SnakeEvent} indicating 
     * that the snake {@link SnakeEvent#SNAKE_FAILED failed} to move. When a 
     * snake fails to move or add a tile, and it was not attempting to add or 
     * move to the tile in the snake behind the head (if the snake was not 
     * attempting to add or move backwards), then its fail count will be 
     * incremented. If a snake's fail count exceeds its {@link #getAllowedFails 
     * allowed number of failures}, then it will crash. If the allowed number of 
     * failures is negative, then the snake will be allowed to fail an unlimited 
     * amount of times without crashing. In other words, if the allowed number 
     * of failures is negative, then the snake cannot crash by failing to add or 
     * move to a tile. If this snake crashes, then this will fire a {@code 
     * SnakeEvent} indicating that the snake has {@link SnakeEvent#SNAKE_CRASHED 
     * crashed}. Once a snake has crashed, it will be unable to add or move to a 
     * tile until it has been {@link #revive revived}. <p>
     * 
     * The {@link #add(int) add(int)} method works similarly to this, with the 
     * exception being that this snake will become longer regardless of whether 
     * the snake ate an apple and would grow when it does.
     * 
     * @param direction The direction in which to move this snake. 
     * This should be one of the following: 
     *      {@code 0} to move the snake {@link #getDirectionFaced forward}, 
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
     * @see #add(int) 
     * @see #add() 
     * @see #move() 
     * @see #removeTail 
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
     * @see #contains(Object) 
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
     * @see #offer 
     * @see #add(Tile) 
     * @see #peek 
     * @see #poll 
     * @see #remove() 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_REMOVED_TILE
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
     */
    public boolean move(int direction){
        return addOrMove(direction,true);
    }
    /**
     * This attempts to move this snake {@link #getDirectionFaced forward}. This 
     * will return {@code true} if this snake has successfully moved, and {@code 
     * false} if this fails to move the snake. Snakes can only move if they have 
     * not {@link #isCrashed crashed} and {@link #canMoveForward can move 
     * forward}. Refer to the documentation for the {@link #canMoveForward 
     * canMoveForward} method for more information about how this checks to see 
     * if this snake can move forward. <p>
     * 
     * If this snake successfully moved, then the tile {@link #getTileBeingFaced
     * in front} of the {@link #getHead head} of this snake will become the new 
     * head, the current {@link #peek tail} (or the current head if this snake 
     * does not have a tail) will be removed from this snake, the snake's {@link 
     * #getFailCount fail count} will be reset to zero, and this will fire a 
     * {@code SnakeEvent} indicating that this snake has {@link 
     * SnakeEvent#SNAKE_MOVED moved}, along with {@code SnakeEvent}s indicating 
     * that a tile was {@link SnakeEvent#SNAKE_ADDED_TILE added} and that a tile 
     * was {@link SnakeEvent#SNAKE_REMOVED_TILE removed}. Refer to the 
     * documentation for the {@link #getTileBeingFaced getTileBeingFaced} method 
     * for more information about how this gets the tile in front of the head. 
     * If the tile was an {@link Tile#isApple apple tile}, then this will also 
     * fire a {@code SnakeEvent} indicating that an {@link 
     * SnakeEvent#SNAKE_CONSUMED_APPLE apple was consumed} and {@link 
     * #hasConsumedApple hasConsumedApple} will return {@code true}. If this 
     * snake {@link #getApplesCauseGrowth grows when it eats apples}, then 
     * eating an apple will cause a tile to be {@link #add() added} to this 
     * snake instead of moving this snake. Snakes can only move to apple tiles 
     * if they are able to {@link #isAppleConsumptionEnabled eat apples}. If the 
     * snake failed to move, then this will fire a {@code SnakeEvent} indicating 
     * that the snake {@link SnakeEvent#SNAKE_FAILED failed} to move. When a 
     * snake fails to move or add a tile, and it was not attempting to add or 
     * move to the tile in the snake behind the head (if the snake was not 
     * attempting to add or move backwards), then its fail count will be 
     * incremented. If a snake's fail count exceeds its {@link #getAllowedFails 
     * allowed number of failures}, then it will crash. If the allowed number of 
     * failures is negative, then the snake will be allowed to fail an unlimited 
     * amount of times without crashing. In other words, if the allowed number 
     * of failures is negative, then the snake cannot crash by failing to add or 
     * move to a tile. If this snake crashes, then this will fire a {@code 
     * SnakeEvent} indicating that the snake has {@link SnakeEvent#SNAKE_CRASHED 
     * crashed}. Once a snake has crashed, it will be unable to add or move to a 
     * tile until it has been {@link #revive revived}. <p>
     * 
     * The {@link #add() add()} method works similarly to this, with the 
     * exception being that this snake will become longer regardless of whether 
     * the snake ate an apple and would grow when it does. <p>
     * 
     * This is equivalent to calling {@link #move(int) move}{@code (}{@link 
     * #getDirectionFaced getDirectionFaced()}{@code )}.
     * 
     * @return Whether this snake successfully moved forward.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced
     * @see #add(int) 
     * @see #add() 
     * @see #move(int) 
     * @see #removeTail 
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
     * @see #contains(Object) 
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
     * @see #offer 
     * @see #add(Tile) 
     * @see #peek 
     * @see #poll 
     * @see #remove() 
     * @see SnakeEvent#SNAKE_ADDED_TILE
     * @see SnakeEvent#SNAKE_REMOVED_TILE
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
     */
    public boolean move(){
        return move(getDirectionFaced());
    }
    /**
     * This removes and returns the {@link #getTail tail} of this snake. If this 
     * snake does not have a tail (i.e. this snake is less than two tiles long), 
     * then this will fire a {@code SnakeEvent} indicating that it {@link 
     * SnakeEvent#SNAKE_FAILED failed} and return null. Otherwise, this will 
     * remove the tail from this snake, {@link Tile#clear clear} the tile, and 
     * fire a {@code SnakeEvent} indicating that a {@link 
     * SnakeEvent#SNAKE_REMOVED_TILE tile was removed}. This method is similar 
     * to {@link #poll poll}, with the exception that this will fire a {@code 
     * SnakeEvent}, leave the snake unchanged, and return null if this snake is 
     * less than two tiles long.
     * @return The tile that was removed which use to be the tail of this snake, 
     * or null if this snake did not have a tail.
     * @see #poll 
     * @see #remove() 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFlipped 
     * @see #flip 
     * @see #isValid 
     * @see Tile#clear 
     * @see SnakeEvent#SNAKE_FAILED
     * @see SnakeEvent#SNAKE_REMOVED_TILE
     */
    public Tile removeTail(){
            // If this snake has a tail, poll it and get the tile that was 
            // removed. Otherwise, no tile will be removed
        Tile tile = (hasTail()) ? poll() : null;
        if (tile == null)       // If no tile was removed
            fireSnakeFailed(0);
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
     * SnakeActionCommand#getCommand command} for invoking the default action, 
     * and if so, throws an IllegalArgumentException stating that the default 
     * action should not invoke the default action. This is equivalent to 
     * checking to see if the given {@code Consumer} is a {@code 
     * SnakeActionCommand}, and if so, invoking {@link 
     * #checkDefaultAction(SnakeCommand) checkDefaultAction} on the command 
     * returned by the {@code SnakeActionCommand}'s {@link 
     * SnakeActionCommand#getCommand getCommand} method.
     * @param action The {@code Consumer} to check.
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
     * #doDefaultAction doDefaultAction} method is invoked when the default 
     * action is {@link #isDefaultActionEnabled enabled} and non-null. Refer to 
     * the documentation for the {@link #doDefaultAction doDefaultAction} method 
     * for more information about how the default action is used by a snake. The 
     * default value for this is a {@link SnakeActionCommand SnakeActionCommand} 
     * that invokes the {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} command. 
     * <p>
     * The default action can be used as the action for a snake to perform when 
     * no action is specified. For example, the {@link #doNextAction 
     * doNextAction} method will invoke the {@code doDefaultAction} method when 
     * the {@link #getActionQueue action queue} is empty. Hence why it is called 
     * the default action.
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
     * Consumer}. Refer to the documentation for the {@link #getDefaultAction 
     * getDefaultAction} for more information about the default action. The 
     * default value for this is a {@link SnakeActionCommand SnakeActionCommand} 
     * that invokes the {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} command. 
     * <p>
     * Note that the default action should not invoke {@link #doDefaultAction 
     * doDefaultAction}, as doing so could result in undefined and unpredictable 
     * behavior. It is also recommended for the default action to not invoke the 
     * {@link #doNextAction doNextAction} method as the {@code doNextAction} may 
     * invoke the {@code doDefaultAction} method when the {@link #getActionQueue 
     * action queue} is empty.
     * 
     * @param action The {@code Consumer} to use for the default action, or 
     * null. This {@code Consumer} should not invoke {@code doDefaultAction}.
     * @return This snake.
     * @throws IllegalArgumentException If the given action is a {@link 
     * SnakeActionCommand SnakeActionCommand} with the {@link 
     * SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} command as the {@link 
     * SnakeActionCommand#getCommand command it invokes}.
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
        if (Objects.equals(defaultAction, action))
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
     * This returns whether the {@link #getDefaultAction default action} is 
     * enabled. If the default action is enabled and non-null, then invoking the 
     * {@link #doDefaultAction doDefaultAction} method will cause the default 
     * action to be performed. Refer to the documentation for the {@link 
     * #getDefaultAction getDefaultAction} method and the {@link 
     * #doDefaultAction doDefaultAction} method for more information about the 
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
     * This sets whether the {@link #getDefaultAction default action} is 
     * enabled. Refer to the documentation for the {@link 
     * #isDefaultActionEnabled isDefaultActionEnabled} method for more 
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
     * @see #doDefaultAction 
     * @see #doNextAction 
     */
    protected void doAction(Consumer<? super Snake> action){
        if (action == null)     // If the action is null
            return;
        checkIfInvalid();       // Check if this snake is invalid
        action.accept(this);
    }
    /**
     * This performs the {@link #getDefaultAction default action} if the default 
     * action is {@link #isDefaultActionEnabled enabled} and non-null. If the 
     * default action is non-null and enabled, then this will call the default 
     * action's {@link Consumer#accept accept} method and return {@code true}. 
     * Otherwise, this will do nothing and return {@code false}. Refer to the 
     * documentation for the {@link #getDefaultAction getDefaultAction} method 
     * for more information about the default action. If performing the default 
     * action throws an exception, then the exception will be relayed to the 
     * caller. <p>
     * 
     * This method can be invoked either directly, by the {@link #doCommand 
     * doCommand} method when provided the {@link SnakeCommand#DEFAULT_ACTION 
     * DEFAULT_ACTION} command, or by the {@link #doNextAction doNextAction} 
     * method either when the {@link ActionQueue#pollNext next action} to 
     * perform invokes this method or when the {@link #getActionQueue action 
     * queue} is empty. The last method allows for the default action to be used 
     * as the action to be performed when the snake has run out of queued 
     * actions to perform but still needs to perform an action on a regular 
     * basis. For example, the default action can be used to have a snake {@link 
     * #move() move forward} when the snake is player controlled, needs to move 
     * at a regular interval, and has not received any input from the player. 
     * <p>
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
     * the given command, this snake will do one of the following actions: 
     * <br><br>
     * <ul>
     * <li> {@link SnakeCommand#ADD_UP ADD_UP} - calls {@link #add(int) add} 
     * with {@link #UP_DIRECTION up (UP_DIRECTION)} for the direction. </li>
     * <li> {@link SnakeCommand#ADD_DOWN ADD_DOWN} - calls {@link #add(int) add} 
     * with {@link #DOWN_DIRECTION down (DOWN_DIRECTION)} for the direction. 
     * </li>
     * <li> {@link SnakeCommand#ADD_LEFT ADD_LEFT} - calls {@link #add(int) add} 
     * with {@link #LEFT_DIRECTION left (LEFT_DIRECTION)} for the direction. 
     * </li>
     * <li> {@link SnakeCommand#ADD_RIGHT ADD_RIGHT} - calls {@link #add(int) 
     * add} with {@link #RIGHT_DIRECTION right (RIGHT_DIRECTION)} for the 
     * direction. </li>
     * <li> {@link SnakeCommand#ADD_FORWARD ADD_FORWARD} - calls {@link #add() 
     * add} with no direction specified. </li>
     * <li> {@link SnakeCommand#MOVE_UP MOVE_UP} - calls {@link #move move} with 
     * {@link #UP_DIRECTION up (UP_DIRECTION)} for the direction. </li>
     * <li> {@link SnakeCommand#MOVE_DOWN MOVE_DOWN} - calls {@link #move move} 
     * with {@link #DOWN_DIRECTION down (DOWN_DIRECTION)} for the direction. 
     * </li>
     * <li> {@link SnakeCommand#MOVE_LEFT MOVE_LEFT} - calls {@link #move move} 
     * with {@link #LEFT_DIRECTION left (LEFT_DIRECTION)} for the direction. 
     * </li>
     * <li> {@link SnakeCommand#MOVE_RIGHT MOVE_RIGHT} - calls {@link #move 
     * move} with {@link #RIGHT_DIRECTION right (RIGHT_DIRECTION)} for the 
     * direction. </li>
     * <li> {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} - calls {@link 
     * #move() move} with no direction specified. </li>
     * <li> {@link SnakeCommand#FLIP FLIP} - calls {@link #flip() flip}. </li>
     * <li> {@link SnakeCommand#REVIVE REVIVE} - calls {@link #revive() revive}. 
     * </li>
     * <li> {@link SnakeCommand#REMOVE_TAIL REMOVE_TAIL} - calls {@link 
     * #removeTail removeTail}. </li>
     * <li> {@link SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} - calls {@link 
     * #doDefaultAction doDefaultAction}. </li>
     * </ul> <p>
     * 
     * If the command does not match any of the previously mentioned commands, 
     * then this will do nothing and fire a {@code SnakeEvent} indicating that 
     * the snake {@link SnakeEvent#SNAKE_FAILED failed}. If performing the 
     * action for the command throws an exception, then the exception will be 
     * relayed to the caller. To check if this snake is in a state where it will  
     * most likely able to successfully perform the given command, use the 
     * {@link #canPerformAction(SnakeCommand) canPerformAction} method.
     * 
     * @param command The command representing which action to perform (cannot 
     * be null).
     * @return Whether this snake was successful in performing the requested 
     * action.
     * @throws NullPointerException If the given command is null.
     * @throws IllegalStateException If this snake is not in a {@link #isValid() 
     * valid} state.
     * @see #canPerformAction(SnakeCommand) 
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
            default:                // The command must be an unknown command
                fireSnakeFailed();  // The snake failed to perform a command
                return false;
        }
    }
    /**
     * This returns whether this snake will be able to perform the given 
     * command. If the given command is null or this snake is not in a {@link 
     * #isValid valid} state, then this will return {@code false}. Otherwise, 
     * this will only return {@code true} if the command is one of the following 
     * commands and the conditions for that command are met (it can be assumed 
     * that the snake must be in a valid state for all of them): <br><br>
     * 
     * <ul>
     * <li> {@link SnakeCommand#ADD_UP ADD_UP} - the snake must not have {@link 
     * #isCrashed crashed} and must not be facing {@link #isFacingDown down}. 
     * </li>
     * <li> {@link SnakeCommand#ADD_DOWN ADD_DOWN} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing {@link #isFacingUp up}. 
     * </li>
     * <li> {@link SnakeCommand#ADD_LEFT ADD_LEFT} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing to the {@link 
     * #isFacingRight right}. </li>
     * <li> {@link SnakeCommand#ADD_RIGHT ADD_RIGHT} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing to the {@link 
     * #isFacingLeft left}. </li>
     * <li> {@link SnakeCommand#ADD_FORWARD ADD_FORWARD} - the snake must not 
     * have {@link #isCrashed crashed}. </li>
     * <li> {@link SnakeCommand#MOVE_UP MOVE_UP} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing {@link #isFacingDown 
     * down}. </li>
     * <li> {@link SnakeCommand#MOVE_DOWN MOVE_DOWN} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing {@link #isFacingUp up}. 
     * </li>
     * <li> {@link SnakeCommand#MOVE_LEFT MOVE_LEFT} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing to the {@link 
     * #isFacingRight right}. </li>
     * <li> {@link SnakeCommand#MOVE_RIGHT MOVE_RIGHT} - the snake must not have 
     * {@link #isCrashed crashed} and must not be facing to the {@link 
     * #isFacingLeft left}. </li>
     * <li> {@link SnakeCommand#MOVE_FORWARD MOVE_FORWARD} - the snake must not 
     * have {@link #isCrashed crashed}. </li>
     * <li> {@link SnakeCommand#FLIP FLIP} - there are no conditions for 
     * flipping a {@link #isValid() valid} snake (this will always return {@code 
     * true} if the snake is valid). </li>
     * <li> {@link SnakeCommand#REVIVE REVIVE} - the snake must have {@link 
     * #isCrashed crashed}. </li>
     * <li> {@link SnakeCommand#REMOVE_TAIL REMOVE_TAIL} - the snake must have a 
     * tail ({@link #getTail getTail} must return a non-null tile). </li>
     * <li> {@link SnakeCommand#DEFAULT_ACTION DEFAULT_ACTION} - the snake's 
     * {@link #getDefaultAction default action} must be non-null and {@link 
     * #isDefaultActionEnabled enabled}. </li>
     * </ul> <p>
     * 
     * If the command does not match any of the previously mentioned commands, 
     * or if it does match one of the commands but this snake does not meet the 
     * criteria for it to be able to perform the command, then this will return 
     * {@code false}.
     * 
     * @param command The command that will be evaluated as to whether it can be 
     * performed by this snake.
     * @return Whether this snake can currently perform the given command.
     * @see #canPerformAction(Consumer) 
     * @see #isValid 
     * @see #doCommand 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see #isFacingUp 
     * @see #isFacingDown 
     * @see #isFacingLeft 
     * @see #isFacingRight 
     * @see #getDirectionFaced 
     * @see #isCrashed 
     * @see #getDefaultAction 
     * @see #isDefaultActionEnabled 
     * @see SnakeUtilities#invertDirections 
     * @see SnakeCommand
     * @see SnakeCommand#getDirectionOf 
     */
    public boolean canPerformAction(SnakeCommand command){
            // If the command is null or this snake is invalid
        if (command == null || !isValid())  
            return false;
        if (hasTail()){             // If this snake has a tail
                // Get the direction for the command
            Integer dir = SnakeCommand.getDirectionOf(command);
                    // If the command has a direction and that direction is 
                    // opposite to the direction this snake is facing
            if (dir != null && dir == 
                    SnakeUtilities.invertDirections(getDirectionFaced()))
                return false;
        }
        switch(command){        // Determine which command is this
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
                return !isCrashed();// The snake cannot move or add when crashed
            case FLIP:          // If the command is the flip command
                return true;    // The snake can always flip
            case REVIVE:        // If the command is the revive command
                    //The snake can really only be revived if crashed
                return isCrashed();
            case REMOVE_TAIL:   // If the command is the remove tail command
                    // The tail can only be removed if the snake has one
                return hasTail();   
            case DEFAULT_ACTION:// If the command is the default action command
                    // The default action can only be performed if it's non-null
                    // and enabled
                return getDefaultAction()!=null&&isDefaultActionEnabled();
            default:            // The command must be an unknown command
                    // The snake cannot perform the action since it's unknown
                return false;   
        }
    }
    /**
     * This returns whether this snake will be able to perform the given {@code 
     * Consumer}. If the given {@code Consumer} is null or this snake is not in 
     * a {@link #isValid valid} state, then this will return {@code false}. 
     * Otherwise, if the given {@code Consumer} is a {@link SnakeActionCommand 
     * SnakeActionCommand}, then this will return whether this snake will be 
     * able to perform the {@code SnakeActionCommand}'s {@link 
     * SnakeActionCommand#getCommand command}. That is to say, if the {@code 
     * Consumer} is a {@code SnakeActionCommand}, then this will forward the 
     * call to {@link #canPerformAction(SnakeCommand) 
     * canPerformAction(SnakeCommand)} with the given {@code 
     * SnakeActionCommand}'s command. Otherwise, this will return {@code true}. 
     * @param action The {@code Consumer} that will be evaluated as to whether 
     * it can be performed by this snake.
     * @return Whether this snake can currently perform the given {@code 
     * Consumer}.
     * @see #canPerformAction(SnakeCommand) 
     * @see #isValid 
     * @see #getHead 
     * @see #getTail 
     * @see #peek 
     * @see #size 
     * @see #isEmpty 
     * @see SnakeCommand
     * @see SnakeActionCommand
     * @see SnakeActionCommand#getCommand 
     */
    public boolean canPerformAction(Consumer<? super Snake> action){
            // If the action is a SnakeActionCommand
        if (action instanceof SnakeActionCommand)
            return canPerformAction(((SnakeActionCommand)action).getCommand());
        else
            return action != null && isValid();
    }
    /**
     * This returns whether this snake's {@link #getActionQueue action queue} 
     * will skip (discard) an action when there is an adjacent duplicate of that 
     * action. If this is {@code true}, then the {@link ActionQueue#peekNext 
     * peekNext}, {@link ActionQueue#getNext getNext}, {@link 
     * ActionQueue#pollNext pollNext}, {@link ActionQueue#removeNext 
     * removeNext}, and {@link ActionQueue#popNext popNext} methods of the 
     * action queue for this snake, along with any other {@link ActionQueue 
     * ActionQueues} constructed using this snake, will skip elements that match 
     * the element that comes after it in the action queue. If this is {@code 
     * false}, then the action queue will not skip repeated actions. The action 
     * queue may still skip an action for other reasons, namely if the snake is 
     * {@link #canPerformAction(Consumer) currently unable} to perform an 
     * action. The action queue's {@link ActionQueue#willSkipNextAction 
     * willSkipNextAction} method can be used to determine if the current head 
     * of the action queue will be skipped. The action queue's own {@link 
     * ActionQueue#getSkipsRepeatedActions getSkipsRepeatedActions} is a cover 
     * method that delegates to this method.
     * @return Whether the action queue for this snake should skip repeated 
     * actions.
     * @see #setSkipsRepeatedActions 
     * @see #getActionQueue 
     * @see #doNextAction 
     * @see #canPerformAction(Consumer) 
     * @see #canPerformAction(SnakeCommand) 
     * @see ActionQueue
     * @see ActionQueue#willSkipNextAction 
     * @see ActionQueue#getSkipsRepeatedActions 
     * @see ActionQueue#setSkipsRepeatedActions 
     * @see ActionQueue#peekNext 
     * @see ActionQueue#getNext 
     * @see ActionQueue#pollNext 
     * @see ActionQueue#removeNext
     * @see ActionQueue#popNext 
     */
    public boolean getSkipsRepeatedActions(){
        return getFlag(SKIPS_REPEATED_ACTIONS_FLAG);
    }
    /**
     * This sets whether the {@link #getActionQueue action queue} for this snake 
     * will skip adjacent repeats of actions. Refer to the documentation for the 
     * {@link #getSkipsRepeatedActions getSkipsRepeatedActions} method for more 
     * information about how this affects the action queue. 
     * @param value Whether this snake should skip repeats in the action queue.
     * @return This snake.
     * @see #getSkipsRepeatedActions 
     * @see #getActionQueue 
     * @see #doNextAction 
     * @see #canPerformAction(Consumer) 
     * @see #canPerformAction(SnakeCommand) 
     * @see ActionQueue
     * @see ActionQueue#willSkipNextAction 
     * @see ActionQueue#getSkipsRepeatedActions 
     * @see ActionQueue#setSkipsRepeatedActions 
     * @see ActionQueue#peekNext 
     * @see ActionQueue#getNext 
     * @see ActionQueue#pollNext 
     * @see ActionQueue#removeNext
     * @see ActionQueue#popNext 
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
     * {@link #doNextAction doNextAction} method. This is intended to be used 
     * for when the actions to be performed by a snake can be generated faster 
     * than a snake is allowed to act upon them. For example, if a snake is set 
     * up to perform an action only when a repeating timer has elapsed, this can 
     * be used to store the actions generated before said timer has elapsed. 
     * @return The action queue for this snake.
     * @see #doNextAction 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see ActionQueue
     * @see ActionQueue#getSkipsRepeatedActions 
     * @see ActionQueue#setSkipsRepeatedActions 
     */
    public ActionQueue getActionQueue(){
            // If the action queue has not been initialized yet
        if (actionQueue == null)    
            actionQueue = Snake.this.createActionQueue();
        return actionQueue;
    }
    /**
     * This removes and performs the next action in the {@link #getActionQueue 
     * action queue}, or performs the {@link #doDefaultAction default action} if 
     * no action from the action queue can be performed. This will first attempt 
     * to poll the action queue for the next action to perform using the action 
     * queue's {@link ActionQueue#pollNext pollNext} method, which will remove 
     * and return the first {@code Consumer} in the action queue that this snake 
     * can perform, or will return null if it contains no such {@code Consumer}. 
     * The action queue's {@link ActionQueue#willSkipNextAction 
     * willSkipNextAction} can be used to determine if the current head of the 
     * action queue will be skipped (discarded) or performed by this snake, and 
     * the action queue's {@link ActionQueue#peekNext peekNext} method can be 
     * used to peek at the next action that will be performed by this snake if 
     * one is present. If the action queue's {@code pollNext} method returns a 
     * non-null {@code Consumer}, then this will call the returned {@code 
     * Consumer}'s {@link Consumer#accept accept} on this snake. If the action 
     * queue's {@code pollNext} method returns null, either due to it being 
     * empty or because all the {@code Consumer}s in it have been discarded 
     * since none of them could be performed, then this will invoke the {@link 
     * #doDefaultAction doDefaultAction} method to perform the {@link 
     * #getDefaultAction default action} for this snake. Any exception thrown 
     * while performing the action, whether it be the default action or an 
     * action from the action queue, will be relayed to the caller of this 
     * method. 
     * @throws IllegalStateException If this snake is not in a {@link #isValid 
     * valid} state.
     * @see #getActionQueue 
     * @see #getSkipsRepeatedActions 
     * @see #setSkipsRepeatedActions 
     * @see #canPerformAction(Consumer) 
     * @see #canPerformAction(SnakeCommand) 
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
     * @see #peek 
     * @see #size 
     * @see #isCrashed 
     * @see #revive 
     * @see Consumer#accept 
     * @see SnakeCommand
     * @see SnakeActionCommand
     * @see ActionQueue
     * @see ActionQueue#willSkipNextAction 
     * @see ActionQueue#getSkipsRepeatedActions 
     * @see ActionQueue#setSkipsRepeatedActions 
     * @see ActionQueue#peekNext 
     * @see ActionQueue#getNext 
     * @see ActionQueue#pollNext 
     * @see ActionQueue#removeNext 
     * @see ActionQueue#popNext 
     */
    public void doNextAction(){
        checkIfInvalid();           // Check if this snake is invalid
            // This will get the next action for this snake to perform
        Consumer<Snake> action = null;
            // If the action queue has been initialized (don't use 
            // getActionQueue, since it would trigger the creation of the action 
        if (actionQueue != null)    // queue if it hasn't been created yet)
            action = actionQueue.pollNext();
        if (action != null)         // If the next action is not null
            doAction(action);       // Do the next action
        else
            doDefaultAction();      // Default to performing the default action
    }
    /**
     * This returns a String representation of this snake. This method is 
     * primarily intended to be used only for debugging purposes, and the 
     * content and format of the returned String may vary between 
     * implementations.
     * @return A String representation of this snake.
     */
    protected String paramString(){
            // Don't use getActionQueue, since it would trigger the creation of 
            // the action queue if it hasn't been created yet.
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
                    // If the action queue is initialized, get its size. 
                    // Otherwise, use zero for the action queue's size.
                ",queuedActions="+((actionQueue != null)?actionQueue.size():0)+
                    // If the action queue is initialized, peek at the next 
                    // action in the action queue. Otherwise, there is no next 
                ",nextAction="+Objects.toString((actionQueue!=null)?  // action
                        actionQueue.peekNext():null,"");
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
     * This compares the specified object with this set for equality. This 
     * returns {@code true} if the specified object is also a set, the two sets 
     * are the same size, and every member of the specified set is contained in 
     * this set and vice versa. This ensures that the {@code equals} method will 
     * work properly across different implementations of the {@code Set} 
     * interface.
     * @param obj The object to be compared for equality with this set.
     * @return {@code true} if the specified object is equal to this set.
     */
    @Override
    public boolean equals(Object obj){
        if (obj == this)                // If the object is this snake
            return true;
        else if (!(obj instanceof Set)) // If the object is not a set
            return false;
        Set<?> set = (Set<?>) obj;      // Get the object as a set
        return size() == set.size() && containsAll(set);
    }
    /**
     * This returns the hash code value for this set. The hash code of a set is 
     * defined as the sum of the hash codes of the elements in the set, where 
     * the hash code of a {@code null} element is defined to be zero. This 
     * ensures that, for any two sets {@code s1} and {@code s2}, {@code 
     * s1.equals(s2)} implies that {@code s1.hashCode()==s2.hashCode()}, as 
     * required by the general contract of {@link Object#hashCode 
     * Object.hashCode}.
     * @return The hash code value for this set.
     */
    @Override
    public int hashCode() {
        int hash = 0;                   // This gets the hash code
        for (Tile tile : snakeBody){    // Go through the tiles in the snake
            if (tile != null)           //If the tile is not null (just in case)
                hash += tile.hashCode();// Add the current tile's hash code
        }
        return hash;
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
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
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
     * snake of an event with the given event ID, direction, and target tile.
     * @param id The event ID indicating what type of event occurred.
     * @param direction The direction for the event.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id, int direction, Tile target){
        fireSnakeChange(new SnakeEvent(this,id,direction,target));
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID and direction. The target tile 
     * will be null.
     * @param id The event ID indicating what type of event occurred.
     * @param direction The direction for the event.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
     * @see #fireSnakeFailed() 
     * @see SnakeEvent
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeChange(int id, int direction){
        fireSnakeChange(id,direction,null);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake of an event with the given event ID, the direction that this snake 
     * is {@link #getDirectionFaced() currently facing}, and the target tile.
     * @param id The event ID indicating what type of event occurred.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int) 
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
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
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
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
     * direction and with the given target tile. 
     * @param direction The direction for the event.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
     * @see #fireSnakeFailed() 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(int direction, Tile target){
        fireSnakeChange(SnakeEvent.SNAKE_FAILED,direction,target);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the given 
     * direction. The target tile will be null.
     * @param direction The direction for the event.
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(Tile) 
     * @see #fireSnakeFailed() 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
     * @see #fireSnakeChange(int) 
     * @see SnakeEvent
     * @see SnakeEvent#SNAKE_FAILED
     * @see #getDirectionFaced 
     * @see #addSnakeListener 
     * @see #removeSnakeListener 
     * @see #getSnakeListeners 
     */
    protected void fireSnakeFailed(int direction){
        fireSnakeFailed(direction,null);
    }
    /**
     * This notifies all the {@code SnakeListener}s that have been added to this 
     * snake that this snake has failed to perform an action in the {@link 
     * #getDirectionFaced() direction currently being faced} by the snake and 
     * with the given target tile.
     * @param target The tile that was the target for the event, or null.
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed() 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
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
     * @see #fireSnakeFailed(int, Tile) 
     * @see #fireSnakeFailed(int) 
     * @see #fireSnakeFailed(Tile) 
     * @see #fireSnakeChange(SnakeEvent) 
     * @see #fireSnakeChange(int, int, Tile) 
     * @see #fireSnakeChange(int, int) 
     * @see #fireSnakeChange(int, Tile) 
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
     * flipped. If this is constructed with a value of {@code true} for {@code 
     * descending}, then the internal iterators used will be swapped around. If 
     * the snake is flipped, the model is changed, or if any change is made to 
     * the snake's body after this is created, then this will generally throw a 
     * {@link ConcurrentModificationException}.
     */
    private class SnakeIterator implements Iterator<Tile>{
        /**
         * The iterator from the ArrayDeque used to store the snake body. This 
         * is either the ascending iterator or the descending iterator 
         * depending on whether the snake was flipped when this iterator was 
         * constructed, and whether this SnakeIterator is a descending iterator.
         */
        private final Iterator<Tile> iterator;
        /**
         * This stores whether the snake was flipped or not when this iterator 
         * was constructed.
         */
        private final boolean flipped;
        /**
         * This stores the snake's model when this iterator was constructed.
         */
        private final PlayFieldModel model;
        /**
         * This stores the length of the snake as it was when this iterator was 
         * constructed. This is updated if a tile is removed via the {@code 
         * remove} method of this iterator.
         */
        private int size;
        /**
         * This stores the head of the snake when this iterator was constructed. 
         * This is updated if a tile is removed via the {@code remove} method of 
         * this iterator.
         */
        private Tile head;
        /**
         * This stores the tail of the snake when this iterator was constructed. 
         * This is updated if a tile is removed via the {@code remove} method of 
         * this iterator.
         */
        private Tile tail;
        /**
         * This stores the tile that was previously returned by the {@code next} 
         * method of this iterator. This will be null if the {@code next} method 
         * has not been called at least twice yet. 
         */
        private Tile previous = null;
        /**
         * This stores the tile that was most recently returned by the {@code 
         * next} method of the iterator. This will be null if the {@code next} 
         * has not been called yet. 
         */
        private Tile current = null;
        /**
         * This stores the tile to return the next time the {@code next} method 
         * is called, or null if the internal iterator is to return the next 
         * tile. This is used by the {@code remove} method to store the next 
         * tile returned by the internal iterator when attempting to repair the 
         * snake body after removing a tile, so that it can be returned by the 
         * {@code next} method of this iterator.
         */
        private Tile next = null;
        /**
         * This constructs a SnakeIterator.
         * @param descending Whether this is an ascending or descending 
         * iterator ({@code true} for descending, and {@code false} for 
         * ascending).
         */
        SnakeIterator(boolean descending){
                // If either the snake is flipped or this is a descending 
                // iterator, but not both, then get the descending iterator from 
                // the snake body. If either the snake is flipped and this is a 
                // descending iterator or if the snake is not flipped and this 
                // is an ascending iterator, then get the normal iterator from 
            iterator = (isFlipped() != descending) ? // the body
                    snakeBody.descendingIterator() : snakeBody.iterator();
            flipped = isFlipped();
            model = getModel();
            size = size();
            head = getHead();
            tail = peek();
        }
        /**
         * {@inheritDoc }
         * @return {@inheritDoc }
         */
        @Override
        public boolean hasNext() {
            return next != null || iterator.hasNext();
        }
        /**
         * {@inheritDoc }
         * @return {@inheritDoc }
         * @throws NoSuchElementException {@inheritDoc }
         * @throws ConcurrentModificationException If the snake has been 
         * modified while the iteration is in progress in any way other than via 
         * the {@code remove} method of this iterator.
         */
        @Override
        public Tile next() {
                // Check for concurrent modification
            checkForConcurrentModification();
            if (!hasNext())     // If there are no more tiles in this snake
                throw new NoSuchElementException();
            previous = current; 
                // If the next tile is not null, then it will be returned. 
                // Otherwise, get the tile to return from the internal iterator
            current = (next != null) ? next : iterator.next();
            next = null;    // Set the next tile to null
            return current;
        }
        /**
         * {@inheritDoc }
         * @throws IllegalStateException {@inheritDoc }
         * @throws ConcurrentModificationException If the snake has been 
         * modified while the iteration is in progress in any way other than via 
         * this method.
         */
        @Override
        public void remove() {
                // If the previous and current tiles are the same tile (this is 
                // the case if next has not been called yet since this iterator 
                // was constructed or since this method was last called) or if 
                // the next tile is not null (this will be the case after this 
                // is called and before next is called)
            if (previous == current || next != null)    
                throw new IllegalStateException();
                // Check for concurrent modification
            checkForConcurrentModification();
                // Get whether the tiles in the model are currently adjusting, 
                // so as to restore this once we're done
            boolean adjusting = model.getTilesAreAdjusting();
            model.setTilesAreAdjusting(true);
            iterator.remove();      // Remove the tile
                // Get a sneak preview of the next tile to be returned if there 
            next = (iterator.hasNext()) ? iterator.next() : null;   // is one
                // Attempt to repair the snake by ensuring the previous tile 
            repairAfterRemoval(previous,current,next); // joins up with the next
            current.clear();        // Clear the removed tile
            model.setTilesAreAdjusting(adjusting);
            fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0,current);
            if (head == current)    // If the head was just removed
                head = getHead();   // Update the head accordingly
            if (tail == current)    // If the tail was just removed
                tail = peek();      // Update the tail accordingly
            size--;                 // Update the size accordingly
            current = previous;     // The current tile is now the previous tile
        }
        /**
         * This checks to see if the snake has been modified externally since 
         * this iterator was constructed, and if so, throws a {@code 
         * ConcurrentModificationException}.
         * @throws ConcurrentModificationException If the snake was modified 
         * since this iterator was constructed.
         */
        protected void checkForConcurrentModification(){
                // If the snake's model has changed, the snake has been flipped, 
                // or tiles have been added or removed from this snake via means 
                // other than this iterator's methods since this iterator's 
                // construction
            if (model != getModel()||flipped != isFlipped() || size != size() || 
                    head != getHead() || tail != peek())
                throw new ConcurrentModificationException();
        }
    }
    
    /**
     * This is a deque that can be used as an action queue for a snake to store 
     * {@code Consumer}s to be performed later by or on that snake. Null 
     * elements are prohibited. Any non-null {@code Consumer} can be added to an 
     * action queue. In addition, {@link SnakeCommand SnakeCommands} can also be 
     * added to an action queue, so long as they are not null and the protected 
     * {@link #getActionForCommand getActionForCommand} method returns a 
     * corresponding non-null {@code Consumer} for the command. Action queues 
     * (currently) do not have any limitations on the number of {@code 
     * Consumer}s they may contain. <p>
     * 
     * The {@link #peekNext peekNext}, {@link #getNext getNext}, {@link 
     * #pollNext pollNext}, {@link #removeNext removeNext}, and {@link #popNext 
     * popNext} methods can be used to retrieve the next action for a snake to 
     * perform. These methods will skip (either ignore or discard, depending on 
     * whether the method is used to examine or remove an element) {@code 
     * Consumer}s that cannot or should not be performed by or on a snake. Refer 
     * to the documentation for the {@link #willSkipNextAction 
     * willSkipNextAction} method for more information on how the next action 
     * methods decide whether to return or skip a {@code Consumer}. The 
     * following table shows a comparison between the standard {@code Queue} and 
     * {@code Deque} examine and removal methods verses the next action methods 
     * provided by {@code ActionQueue}: 
     * 
     * <table class="striped">
     * <caption>Comparison of {@code Queue}, {@code Deque}, and the {@code 
     * ActionQueue} next action methods</caption>
     * <thead>
     *  <tr>
     *      <th rowspan="2"></th>
     *      <th scope="col" colspan="2" style="text-align:center;">
     *          {@code Queue} Method</th>
     *      <th scope="col" colspan="2" style="text-align:center;">
     *          {@code Deque} Method</th>
     *      <th scope="col" colspan="2" style="text-align:center;">
     *          Next Action Method</th>
     *  </tr>
     *  <tr>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Throws exception</th>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Special value</th>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Throws exception</th>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Special value</th>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Throws exception</th>
     *      <th scope="col" style="font-weight:normal; font-style:italic">
     *          Special value</th>
     *  </tr>
     *  </thead>
     *  <tbody>
     *      <tr>
     *          <th scope="row">Remove</th>
     *          <td>{@link Queue#remove() remove()}</td>
     *          <td>{@link Queue#poll() poll()}</td>
     *          <td>{@link Deque#removeFirst() removeFirst()}</td>
     *          <td>{@link Deque#pollFirst() pollFirst()}</td>
     *          <td>{@link #removeNext() removeNext()}</td>
     *          <td>{@link #pollNext() pollNext()}</td>
     *      </tr>
     *      <tr>
     *          <th scope="row">Examine</th>
     *          <td>{@link Queue#element() element()}</td>
     *          <td>{@link Queue#peek() peek()}</td>
     *          <td>{@link Deque#getFirst() getFirst()}</td>
     *          <td>{@link Deque#peekFirst() peekFirst()}</td>
     *          <td>{@link #getNext() getNext()}</td>
     *          <td>{@link #peekNext() peekNext()}</td>
     *      </tr>
     *      <tr>
     *          <th scope="row">Stack Remove</th>
     *          <td></td>
     *          <td></td>
     *          <td>{@link Deque#pop() pop()}</td>
     *          <td></td>
     *          <td>{@link #popNext() popNext()}</td>
     *          <td></td>
     *      </tr>
     *  </tbody>
     * </table>
     * <p>
     * 
     * The iterators returned by this class's {@code iterator} and {@code 
     * descendingIterator} methods are fail-fast, i.e. if the action queue is 
     * structurally modified in any way at any time after the iterator is 
     * created except via the iterator's own {@code remove} method, the iterator 
     * will generally throw a {@link ConcurrentModificationException 
     * ConcurrentModificationException}. This way, when faced with concurrent 
     * modification, the iterator will fail quickly and cleanly instead of 
     * risking arbitrary, non-deterministic behavior. However, the fail-fast 
     * behavior of the iterator cannot be guaranteed, especially when dealing 
     * with unsynchronized concurrent modifications. The fail-fast iterators 
     * throw {@code ConcurrentModificationExceptions} on a best-effort basis. As 
     * such the fail-fast behavior should not be depended on for its correctness 
     * and should only be used to detect bugs.
     * 
     * @author Milo Steier
     * @see Snake
     * @see Snake#getActionQueue 
     * @see Snake#doNextAction
     * @see SnakeCommand
     * @see SnakeActionCommand
     * @see DefaultSnakeActionCommand
     * @see #getSnake 
     * @see #willSkipNextAction 
     * @see #peekNext 
     * @see #getNext 
     * @see #pollNext 
     * @see #removeNext 
     * @see #popNext 
     */
    public class ActionQueue extends AbstractCollection<Consumer<Snake>> 
            implements Deque<Consumer<Snake>>, Cloneable{
        /**
         * The ArrayDeque that this uses to store the {@code Consumer}s in the 
         * action queue.
         */
        private ArrayDeque<Consumer<Snake>> queue;
        /**
         * This stores the {@code Consumer} that was most recently returned by 
         * {@code pollNext}.
         */
        private Consumer<Snake> prevAction = null;
        /**
         * This constructs an empty ActionQueue.
         */
        public ActionQueue(){
            queue = new ArrayDeque<>();
        }
        /**
         * This constructs an ActionQueue containing the elements of the given 
         * collection, in the order they are returned by the collection's 
         * iterator.
         * @param c The collection of elements to be placed into this action 
         * queue (cannot be null)
         * @throws NullPointerException If the collection contains a null 
         * element, or if the collection itself is null.
         */
        public ActionQueue(Collection<? extends Consumer<Snake>>c){
            queue = new ArrayDeque<>(c);
        }
        /**
         * This returns the {@code ArrayDeque} used internally to store the 
         * action queue. 
         * @return The {@code ArrayDeque} used to store the queue.
         */
        protected ArrayDeque<Consumer<Snake>> getInternalQueue(){
            return queue;
        }
        /**
         * This returns the parent snake that this serves as an action queue 
         * for. This is the snake that this action queue was constructed from 
         * and which this inherits its settings from.
         * @return The parent snake for this action queue.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see Snake#doNextAction 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         */
        public Snake getSnake(){
            return Snake.this;
        }
        /**
         * This returns whether this action queue will skip (discard) an action 
         * when there is an adjacent duplicate of that action. If this is {@code 
         * true}, then the {@link #peekNext peekNext}, {@link #getNext getNext}, 
         * {@link #pollNext pollNext}, {@link #removeNext removeNext}, and 
         * {@link #popNext popNext} methods of this action queue will skip 
         * elements that match the element that comes after it. If this is 
         * {@code false}, then this action queue will not skip repeated actions. 
         * This action queue may still skip an action for other reasons, namely 
         * if the {@link #getSnake parent snake} is {@link 
         * Snake#canPerformAction(Consumer) currently unable} to perform an 
         * action. The {@link #willSkipNextAction willSkipNextAction} method can 
         * be used to determine if the current head of the action queue will be 
         * skipped. <p>
         * 
         * This is a cover method that delegates to the {@link 
         * Snake#getSkipsRepeatedActions getSkipsRepeatedActions} method of the 
         * {@link #getSnake() parent snake}.
         * 
         * @return Whether this action queue will skip repeated actions.
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see #willSkipNextAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         */
        public boolean getSkipsRepeatedActions(){
            return getSnake().getSkipsRepeatedActions();
        }
        /**
         * This sets whether this action queue will skip adjacent repeats of 
         * actions. Refer to the documentation for the {@link 
         * #getSkipsRepeatedActions getSkipsRepeatedActions} method for more 
         * information. <p>
         * 
         * This is a cover method that delegates to the {@link 
         * Snake#setSkipsRepeatedActions setSkipsRepeatedActions} method of the 
         * {@link #getSnake() parent snake}.
         * 
         * @param value Whether this action queue should skip repeated actions. 
         * @see #getSkipsRepeatedActions 
         * @see #getSnake 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see #willSkipNextAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         */
        public void setSkipsRepeatedActions(boolean value){
            getSnake().setSkipsRepeatedActions(value);
        }
        /**
         * This returns the action most recently removed and returned by the 
         * {@link #pollNext pollNext} method. This is used by the {@link 
         * #willSkipAction(Consumer, Consumer, boolean) willSkipAction} methods 
         * to determine if the given action is a repeat of the previously 
         * returned action.
         * @return The action that was most recently returned by next removal 
         * methods.
         * @since 1.1.0
         * @see #willSkipAction(Consumer, Consumer, boolean) 
         * @see #willSkipAction(Consumer, boolean) 
         * @see #willSkipAction(Consumer, Consumer) 
         * @see #willSkipAction(Consumer) 
         * @see #willSkipNextAction 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         */
        protected Consumer<Snake> getPreviousAction(){
            return prevAction;
        }
        /**
         * This returns whether this will skip (discard) the given {@code 
         * Consumer}. This is called by {@link #peekNext peekNext} and {@link 
         * #pollNext pollNext} to determine if an action should be skipped. This 
         * is also called by {@link #willSkipNextAction willSkipNextAction} to 
         * return whether the {@link #peek next} {@code Consumer} in the action 
         * queue will likely be skipped. If this returns {@code true}, then the 
         * action will be skipped. <p>
         * 
         * This will always return {@code true} if the given action is null. If 
         * the action is not null, then if {@code skipsRepeats} is {@code true}, 
         * then this will return {@code true} if the given action matches either 
         * the given next action in the queue or the action previously returned  
         * by the {@code pollNext} method. That is to say, this will return 
         * {@code true} if {@code skipRepeats} is {@code true} and either {@code
         * Objects.equals(action, next)} or {@code Objects.equals(action,} 
         * {@link #getPreviousAction getPreviousAction()}{@code )} return {@code 
         * true}. Otherwise, this will check to see if the {@link #getSnake 
         * parent snake} is in a state where it can perform the given action 
         * using the snake's {@link Snake#canPerformAction(Consumer) 
         * canPerformAction} method. If the snake will be unable to perform the 
         * given action at the current moment, then this will return {@code 
         * true}. Otherwise, this will return {@code false}. <p>
         * 
         * In other words, if {@code action} is null, then this will return 
         * {@code true}. Otherwise, if {@code action} matches either {@code 
         * next} or the {@code Consumer} returned by {@code getPreviousAction} 
         * and {@code skipsRepeats} is {@code true}, then this will return 
         * {@code true}. Otherwise, this will return {@code !}{@link #getSnake 
         * getSnake()}{@code .}{@link Snake#canPerformAction(Consumer) 
         * canPerformAction}{@code (action)}. 
         * 
         * @param action The {@code Consumer} to be evaluated as to whether it 
         * should be performed or skipped.
         * @param next The next {@code Consumer} in the action queue.
         * @param skipRepeats If the given {@code Consumer} should be skipped if 
         * it is equal to the {@code next Consumer}.
         * @return {@code true} if the given action should be skipped, otherwise 
         * {@code false}.
         * @see #willSkipAction(Consumer, boolean) 
         * @see #willSkipAction(Consumer, Consumer) 
         * @see #willSkipAction(Consumer) 
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getPreviousAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #getSnake 
         * @see #poll 
         * @see #peek 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#doNextAction 
         * @see Snake#doCommand 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see SnakeCommand
         * @see SnakeActionCommand
         * @see SnakeActionCommand#getCommand 
         */
        protected boolean willSkipAction(Consumer<? super Snake> action, 
                Consumer<? super Snake> next, boolean skipRepeats){
            if (action == null)     // If the given action is null
                return true;
            else if (skipRepeats){  // If this is to skip repeats
                    // If the either the next action or the previously returned 
                    // action are the same as the given action
                if (action.equals(next) || action.equals(getPreviousAction()))    
                    return true;
            }   // Check with the snake to see if it can perform the action
            return !getSnake().canPerformAction(action); 
        }
        /**
         * This returns whether this will skip (discard) the given {@code 
         * Consumer}. This is called by {@link #peekNext peekNext} and {@link 
         * #pollNext pollNext} to determine if an action should be skipped. This 
         * is also called by {@link #willSkipNextAction willSkipNextAction} to 
         * return whether the {@link #peek next} {@code Consumer} in the action 
         * queue will likely be skipped. If this returns {@code true}, then the 
         * action will be skipped. <p>
         * 
         * This will always return {@code true} if the given action is null. If 
         * the action is not null, then if {@code skipsRepeats} is {@code true}, 
         * then this will return {@code true} if the given action matches either 
         * the next action in the queue or the action previously returned by the 
         * {@code pollNext} method. That is to say, this will return {@code 
         * true} if {@code skipRepeats} is {@code true} and either {@code 
         * Objects.equals(action, } {@link #peek peek()}{@code )} or {@code 
         * Objects.equals(action,} {@link #getPreviousAction 
         * getPreviousAction()}{@code )} return {@code true}. Otherwise, this 
         * will check to see if the {@link #getSnake parent snake} is in a state 
         * where it can perform the given action using the snake's {@link 
         * Snake#canPerformAction(Consumer) canPerformAction} method. If the 
         * snake will be unable to perform the given action at the current 
         * moment, then this will return {@code true}. Otherwise, this will 
         * return {@code false}. <p>
         * 
         * In other words, if {@code action} is null, then this will return 
         * {@code true}. Otherwise, if {@code skipsRepeats} is {@code true} and 
         * {@code action} matches either the {@code Consumer} returned by {@code 
         * peek()} or the {@code Consumer} returned by {@code 
         * getPreviousAction}, then this will return {@code true}. Otherwise, 
         * this will return {@code !}{@link #getSnake getSnake()}{@code .}{@link 
         * Snake#canPerformAction(Consumer) canPerformAction}{@code (action)}. 
         * <p>
         * This is equivalent to calling {@link #willSkipAction(Consumer, 
         * Consumer, boolean) willSkipAction}{@code (action, }{@link #peek 
         * peek()}{@code , skipsRepeats)}.
         * 
         * @param action The {@code Consumer} to be evaluated as to whether it 
         * should be performed or skipped.
         * @param skipRepeats If the given {@code Consumer} should be skipped if 
         * it is equal to the current head of this action queue.
         * @return {@code true} if the given action should be skipped, otherwise 
         * {@code false}.
         * @see #willSkipAction(Consumer, Consumer, boolean) 
         * @see #willSkipAction(Consumer, Consumer) 
         * @see #willSkipAction(Consumer) 
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getPreviousAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #getSnake 
         * @see #poll 
         * @see #peek 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#doNextAction 
         * @see Snake#doCommand 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see SnakeCommand
         * @see SnakeActionCommand
         * @see SnakeActionCommand#getCommand 
         */
        protected boolean willSkipAction(Consumer<? super Snake> action, 
                boolean skipRepeats){
            return willSkipAction(action,peek(),skipRepeats);
        }
        /**
         * This returns whether this will skip (discard) the given {@code 
         * Consumer}. This is called by {@link #peekNext peekNext} and {@link 
         * #pollNext pollNext} to determine if an action should be skipped. This 
         * is also called by {@link #willSkipNextAction willSkipNextAction} to 
         * return whether the {@link #peek next} {@code Consumer} in the action 
         * queue will likely be skipped. If this returns {@code true}, then the 
         * action will be skipped. <p>
         * 
         * This will always return {@code true} if the given action is null. If 
         * the action is not null, then if this action queue {@link 
         * #getSkipsRepeatedActions skips repeated actions}, then this will 
         * return {@code true} if the given action matches either the given next 
         * action in the queue or the action previously returned  by the {@code 
         * pollNext} method. That is to say, this will return {@code true} if 
         * both {@link #getSkipsRepeatedActions getSkipsRepeatedActions()} and 
         * either {@code Objects.equals(action, next)} or {@code 
         * Objects.equals(action,} {@link #getPreviousAction 
         * getPreviousAction()}{@code )} return {@code true}. Otherwise, this 
         * will check to see if the {@link #getSnake parent snake} is in a state 
         * where it can perform the given action using the snake's {@link 
         * Snake#canPerformAction(Consumer) canPerformAction} method. If the 
         * snake will be unable to perform the given action at the current 
         * moment, then this will return {@code true}. Otherwise, this will 
         * return {@code false}. <p>
         * 
         * In other words, if {@code action} is null, then this will return 
         * {@code true}. Otherwise, if {@code action} matches either {@code 
         * next} or the {@code Consumer} returned by {@code getPreviousAction} 
         * and {@code getSkipsRepeatedActions()} returns {@code true}, then this 
         * will return {@code true}. Otherwise, this will return {@code !}{@link 
         * #getSnake getSnake()}{@code .}{@link Snake#canPerformAction(Consumer) 
         * canPerformAction}{@code (action)}. <p>
         * 
         * This is equivalent to calling {@link #willSkipAction(Consumer, 
         * Consumer, boolean) willSkipAction}{@code (action, next,} {@link 
         * #getSkipsRepeatedActions getSkipsRepeatedActions()}{@code )}.
         * 
         * @param action The {@code Consumer} to be evaluated as to whether it 
         * should be performed or skipped.
         * @param next The next {@code Consumer} in the action queue.
         * @return {@code true} if the given action should be skipped, otherwise 
         * {@code false}.
         * @see #willSkipAction(Consumer, Consumer, boolean) 
         * @see #willSkipAction(Consumer, boolean) 
         * @see #willSkipAction(Consumer) 
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getPreviousAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #getSnake 
         * @see #poll 
         * @see #peek 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#doNextAction 
         * @see Snake#doCommand 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see SnakeCommand
         * @see SnakeActionCommand
         * @see SnakeActionCommand#getCommand 
         */
        protected boolean willSkipAction(Consumer<? super Snake> action, 
                Consumer<? super Snake> next){
            return willSkipAction(action,next,getSkipsRepeatedActions());
        }
        /**
         * This returns whether this will skip (discard) the given {@code 
         * Consumer}. This is called by {@link #peekNext peekNext} and {@link 
         * #pollNext pollNext} to determine if an action should be skipped. This 
         * is also called by {@link #willSkipNextAction willSkipNextAction} to 
         * return whether the {@link #peek next} {@code Consumer} in the action 
         * queue will likely be skipped. If this returns {@code true}, then the 
         * action will be skipped. <p>
         * 
         * This will always return {@code true} if the given action is null. If 
         * the action is not null, then if this action queue {@link 
         * #getSkipsRepeatedActions skips repeated actions}, then this will 
         * return {@code true} if the given action matches either the next 
         * action in the queue or the action previously returned  by the {@code 
         * pollNext} method. That is to say, this will return {@code true} if 
         * both {@link #getSkipsRepeatedActions getSkipsRepeatedActions()} and 
         * either {@code Objects.equals(action, }{@link #peek peek()}{@code )} 
         * or {@code Objects.equals(action,} {@link #getPreviousAction 
         * getPreviousAction()}{@code )} return {@code true}. Otherwise, this 
         * will check to see if the {@link #getSnake parent snake} is in a state 
         * where it can perform the given action using the snake's {@link 
         * Snake#canPerformAction(Consumer) canPerformAction} method. If the 
         * snake will be unable to perform the given action at the current 
         * moment, then this will return {@code true}. Otherwise, this will 
         * return {@code false}. <p>
         * 
         * In other words, if {@code action} is null, then this will return 
         * {@code true}. Otherwise, if {@code getSkipsRepeatedActions()} returns 
         * {@code true} and {@code action} matches either the {@code Consumer} 
         * returned by {@code peek()} or the {@code Consumer} returned by {@code 
         * getPreviousAction}, then this will return {@code true}. Otherwise, 
         * this will return {@code !}{@link #getSnake getSnake()}{@code .}{@link 
         * Snake#canPerformAction(Consumer) canPerformAction}{@code (action)}. 
         * <p>
         * This is equivalent to calling {@link #willSkipAction(Consumer, 
         * boolean) willSkipAction}{@code (action, }{@link 
         * #getSkipsRepeatedActions getSkipsRepeatedActions()}{@code )}.
         * 
         * @param action The {@code Consumer} to be evaluated as to whether it 
         * should be performed or skipped.
         * @return {@code true} if the given action should be skipped, otherwise 
         * {@code false}.
         * @see #willSkipAction(Consumer, Consumer, boolean) 
         * @see #willSkipAction(Consumer, boolean) 
         * @see #willSkipAction(Consumer, Consumer) 
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getPreviousAction 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #getSnake 
         * @see #poll 
         * @see #peek 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#doNextAction 
         * @see Snake#doCommand 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see SnakeCommand
         * @see SnakeActionCommand
         * @see SnakeActionCommand#getCommand 
         */
        protected boolean willSkipAction(Consumer<? super Snake> action){
            return willSkipAction(action,getSkipsRepeatedActions());
        }
        /**
         * This returns whether the {@link #peek current head} of this action 
         * queue will be either skipped (discarded) or returned when getting the 
         * next action for the {@link #getSnake parent snake} to perform. If 
         * this returns {@code true} and this action queue is not {@link 
         * #isEmpty empty}, then the {@link #peekNext peekNext}, {@link #getNext 
         * getNext}, {@link #pollNext pollNext}, {@link #removeNext removeNext}, 
         * and {@link #popNext popNext} methods will skip the current head of 
         * this action queue. If this action queue is empty or if this returns 
         * {@code false}, then the {@code peekNext}, {@code getNext}, {@code 
         * pollNext}, {@code removeNext}, and {@code popNext} methods will act 
         * like the {@link #peek peek}, {@link #element element}, {@link #poll 
         * poll}, {@link #remove() remove}, and {@link #pop pop} methods, 
         * respectively. <p>
         * 
         * If this action queue is empty, then this will return {@code true}. 
         * Otherwise, if this queue {@link getSkipsRepeatedActions skips 
         * repeated actions}, then this will return {@code true} if the current 
         * head of this action queue matches the {@code Consumer} that comes 
         * after it. In other words, this will return {@code true} if both the 
         * {@link getSkipsRepeatedActions getSkipsRepeatedActions} method 
         * returns {@code true} and {@code Objects.equals} returns {@code true} 
         * when given the {@code Consumer} returned by {@code peek()} and the 
         * {@code Consumer} that would be returned by {@code peek()} after 
         * calling {@code poll()}. This will also return {@code true} when 
         * skipping repeated actions if the current head of this action queue 
         * matches the {@code Consumer} that was most recently returned by 
         * either the {@code pollNext}, {@code removeNext}, or {@code popNext} 
         * methods. Otherwise, this will return whether the parent snake will be 
         * unable to perform the current head of this action queue. In other 
         * words, this will return {@code !}{@link #getSnake 
         * getSnake()}{@code .}{@link Snake#canPerformAction(Consumer) 
         * canPerformAction}{@code (}{@link #peek peek()}{@code )}.
         * 
         * @return Whether the current head of this action queue will be skipped 
         * or returned by the next action methods.
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #peek 
         * @see #element 
         * @see #poll 
         * @see #remove() 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         * @see #getSnake 
         * @see Snake#doNextAction 
         * @see Snake#doCommand 
         * @see Snake#getActionQueue 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#setSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         * @see Snake#canPerformAction(SnakeCommand) 
         * @see SnakeCommand
         * @see SnakeActionCommand
         * @see SnakeActionCommand#getCommand 
         */
        public boolean willSkipNextAction(){
            if (isEmpty())          // If this action queue is empty
                return true;
                // This will get the action after the current head of the queue
            Consumer<Snake> next = null;    
                // Create an iterator to get the action after the queue's head
            Iterator<Consumer<Snake>> itr = iterator();
                // Discard the first action in the iterator, as it's the head of 
            itr.next();         // the queue
            if (itr.hasNext())  // If there's an action after the queue's head
                next = itr.next();
            return willSkipAction(peek(),next);
        }
        /**
         * This returns the number of elements in this action queue.
         * @return The number of elements in this action queue.
         */
        @Override
        public int size() {
            return queue.size();
        }
        /**
         * This returns whether this action queue is empty. In other words, this 
         * returns whether this action queue contains no elements. 
         * @return Whether this action queue is empty.
         */
        @Override
        public boolean isEmpty(){
            return queue.isEmpty();
        }
        /**
         * This returns whether this action queue contains the specified 
         * element. More formally, this returns {@code true} if this action 
         * queue contains at least one element {@code e} such that {@code 
         * Objects.equals(o, e)}.
         * @param o The element to check for in this action queue.
         * @return Whether this action queue contains the specified element.
         */
        @Override
        public boolean contains(Object o){
            return queue.contains(o);
        }
        /**
         * This returns the {@code Consumer} to add to this action queue that 
         * will perform the given command. <p>
         * 
         * This forwards the call to {@link SnakeCommand#getActionForCommand 
         * SnakeCommand.getActionForCommand}, and throws an {@code 
         * IllegalArgumentException} if it returns null. This method is here so 
         * that a subclass could change the {@code Consumer} used to represent a 
         * given command.
         * 
         * @param command The command to get the {@code Consumer} for (cannot be 
         * null).
         * @return The {@code Consumer} for the command.
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @see SnakeCommand#getCommandActionMap 
         * @see SnakeCommand#getActionForCommand 
         * @see SnakeActionCommand
         * @see DefaultSnakeActionCommand
         */
        protected Consumer<Snake> getActionForCommand(SnakeCommand command){
                // Get the Consumer for the given command
            Consumer<Snake> action = SnakeCommand.getActionForCommand(command);
            if (action == null) // If the Consumer is somehow null
                throw new IllegalArgumentException(
                        "No action available for command " + command);
            return action;
        }
        /**
         * This returns whether the given {@code Consumer} can be added to this 
         * action queue without violating any capacity restrictions. This also 
         * checks to see if the {@code Consumer} violates any other restrictions 
         * imposed on the elements of this queue, and throws the appropriate 
         * exception if it does. 
         * @param e The {@code Consumer} to check (can be assumed to be 
         * non-null).
         * @return Whether the {@code Consumer} can be added without violating 
         * any capacity restrictions.
         * @throws IllegalArgumentException If some property of the given {@code 
         * Consumer} prevents it from being added.
         * @see #offerFirst(Consumer) 
         * @see #offerLast(Consumer) 
         * @see #offer(Consumer) 
         */
        protected boolean checkOffer(Consumer<? super Snake> e){
            return true;
        }
        /**
         * This attempts to insert the given {@code Consumer} at the front of 
         * this action queue, and returns whether this was successful at doing 
         * so. If this action queue is capacity restricted, then this method is 
         * generally preferable to the {@link #addFirst(Consumer) 
         * addFirst(Consumer)} method, which can fail to insert an element only 
         * by throwing an exception.
         * @param e The {@code Consumer} to add (cannot be null).
         * @return {@code true} if the {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @see #offerFirst(SnakeCommand) 
         * @see #offerLast(Consumer) 
         * @see #offerLast(SnakeCommand) 
         * @see #offer(Consumer) 
         * @see #offer(SnakeCommand) 
         * @see #addFirst(Consumer) 
         * @see #push(Consumer) 
         */
        @Override
        public boolean offerFirst(Consumer<Snake> e) {
            Objects.requireNonNull(e);  // Check if the action is null
            if (checkOffer(e))          // Check if the action can be added
                return queue.offerFirst(e);
            else
                return false;
        }
        /**
         * This attempts to insert a {@code Consumer} at the front of this action 
         * queue that will perform the given command, and returns whether this 
         * was successful at doing so. If this action queue is capacity 
         * restricted, then this method is generally preferable to the {@link 
         * #addFirst(SnakeCommand) addFirst(SnakeCommand)} method, which can 
         * fail to insert an element only by throwing an exception. <p>
         * 
         * This method is equivalent to calling {@link #offerFirst(Consumer) 
         * offerFirst(Consumer)} with the {@code Consumer} returned by the 
         * protected {@link #getActionForCommand(SnakeCommand) 
         * getActionForCommand} method.
         * 
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @return {@code true} if a {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @see #offerFirst(Consumer) 
         * @see #offerLast(Consumer) 
         * @see #offerLast(SnakeCommand) 
         * @see #offer(Consumer) 
         * @see #offer(SnakeCommand) 
         * @see #addFirst(SnakeCommand) 
         * @see #push(SnakeCommand) 
         */
        public boolean offerFirst(SnakeCommand command){
            return offerFirst(getActionForCommand(
                    Objects.requireNonNull(command)));
        }
        /**
         * This attempts to insert the given {@code Consumer} at the end of this 
         * action queue, and returns whether this was successful at doing so. If 
         * this action queue is capacity restricted, then this method is 
         * generally preferable to the {@link #addLast(Consumer) 
         * addLast(Consumer)} method, which can fail to insert an element only 
         * by throwing an exception.
         * @param e The {@code Consumer} to add (cannot be null).
         * @return {@code true} if the {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @see #offerFirst(Consumer) 
         * @see #offerFirst(SnakeCommand) 
         * @see #offerLast(SnakeCommand) 
         * @see #offer(Consumer) 
         * @see #offer(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #push(Consumer) 
         */
        @Override
        public boolean offerLast(Consumer<Snake> e) {
            Objects.requireNonNull(e);  // Check if the action is null
            if (checkOffer(e))          // Check if the action can be added
                return queue.offerLast(e);
            else
                return false;
        }
        /**
         * This attempts to insert a {@code Consumer} at the end of this action 
         * queue that will perform the given command, and returns whether this 
         * was successful at doing so. If this action queue is capacity 
         * restricted, then this method is generally preferable to the {@link 
         * #addLast(SnakeCommand) addLast(SnakeCommand)} method, which can fail 
         * to insert an element only by throwing an exception. <p>
         * 
         * This method is equivalent to calling {@link #offerLast(Consumer) 
         * offerLast(Consumer)} with the {@code Consumer} returned by the 
         * protected {@link #getActionForCommand(SnakeCommand) 
         * getActionForCommand} method.
         * 
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @return {@code true} if a {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @see #offerFirst(Consumer) 
         * @see #offerFirst(SnakeCommand) 
         * @see #offerLast(Consumer) 
         * @see #offer(Consumer) 
         * @see #offer(SnakeCommand) 
         * @see #addLast(SnakeCommand) 
         * @see #push(SnakeCommand) 
         */
        public boolean offerLast(SnakeCommand command){
            return offerLast(getActionForCommand(
                    Objects.requireNonNull(command)));
        }
        /**
         * This attempts to insert the given {@code Consumer} at the end of this 
         * action queue, and returns whether this was successful at doing so. If 
         * this action queue is capacity restricted, then this method is 
         * generally preferable to the {@link #add(Consumer) add(Consumer)} 
         * method, which can fail to insert an element only by throwing an 
         * exception. <p>
         * 
         * This method is equivalent to calling {@link #offerLast(Consumer) 
         * offerLast(Consumer)}.
         * 
         * @param e The {@code Consumer} to add (cannot be null).
         * @return {@code true} if the {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @see #offerFirst(Consumer) 
         * @see #offerFirst(SnakeCommand) 
         * @see #offerLast(Consumer) 
         * @see #offerLast(SnakeCommand) 
         * @see #offer(SnakeCommand) 
         * @see #add(Consumer) 
         * @see #push(Consumer) 
         */
        @Override
        public boolean offer(Consumer<Snake> e) {
            return offerLast(e);
        }
        /**
         * This attempts to insert a {@code Consumer} at the end of this action 
         * queue that will perform the given command, and returns whether this 
         * was successful at doing so. If this action queue is capacity 
         * restricted, then this method is generally preferable to the {@link 
         * #add(SnakeCommand) add(SnakeCommand)} method, which can fail to 
         * insert an element only by throwing an exception. <p>
         * 
         * This method is equivalent to calling {@link #offerLast(SnakeCommand) 
         * offerLast(SnakeCommand)}.
         * 
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @return {@code true} if a {@code Consumer} was added to this action 
         * queue, else {@code false}.
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @see #offerFirst(Consumer) 
         * @see #offerFirst(SnakeCommand) 
         * @see #offerLast(Consumer) 
         * @see #offerLast(SnakeCommand) 
         * @see #offer(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #push(SnakeCommand) 
         */
        public boolean offer(SnakeCommand command){
            return offerLast(command);
        }
        /**
         * This attempts to insert the given {@code Consumer} at the front of 
         * this action queue, throwing an {@code IllegalStateException} if doing 
         * so would violate any capacity restrictions. If this action queue is 
         * capacity restricted, then it is generally preferable to use the 
         * {@link #offerFirst(Consumer) offerFirst(Consumer)} method.
         * @param e The {@code Consumer} to add (cannot be null).
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerFirst(Consumer) 
         * @see #addFirst(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #addLast(SnakeCommand) 
         * @see #add(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #push(Consumer) 
         */
        @Override
        public void addFirst(Consumer<Snake> e) {
            if (!offerFirst(e))     // If this failed to insert the given action
                throw new IllegalStateException("Action queue is full");
        }
        /**
         * This attempts to insert a {@code Consumer} at the front of this 
         * action queue that will perform the given command, throwing an {@code 
         * IllegalStateException} if doing so would violate any capacity 
         * restrictions. If this action queue is capacity restricted, then it is 
         * generally preferable to use the {@link #offerFirst(SnakeCommand) 
         * offerFirst(SnakeCommand)} method.
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerFirst(SnakeCommand) 
         * @see #addFirst(Consumer) 
         * @see #addLast(Consumer) 
         * @see #addLast(SnakeCommand) 
         * @see #add(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #push(SnakeCommand) 
         */
        public void addFirst(SnakeCommand command){
            addFirst(getActionForCommand(Objects.requireNonNull(command)));
        }
        /**
         * This attempts to insert the given {@code Consumer} at the end of this 
         * action queue, throwing an {@code IllegalStateException} if doing so 
         * would violate any capacity restrictions. If this action queue is 
         * capacity restricted, then it is generally preferable to use the 
         * {@link #offerLast(Consumer) offerLast(Consumer)} method.
         * @param e The {@code Consumer} to add (cannot be null).
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerLast(Consumer) 
         * @see #addFirst(Consumer) 
         * @see #addFirst(SnakeCommand) 
         * @see #addLast(SnakeCommand) 
         * @see #add(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #push(Consumer) 
         */
        @Override
        public void addLast(Consumer<Snake> e) {
            if (!offerLast(e))      // If this failed to insert the given action
                throw new IllegalStateException("Action queue is full");
        }
        /**
         * This attempts to insert a {@code Consumer} at the end of this action 
         * queue that will perform the given command, throwing an {@code 
         * IllegalStateException} if doing so would violate any capacity 
         * restrictions. If this action queue is capacity restricted, then it is 
         * generally preferable to use the {@link #offerLast(SnakeCommand) 
         * offerLast(SnakeCommand)} method.
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerLast(SnakeCommand) 
         * @see #addFirst(Consumer) 
         * @see #addFirst(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #add(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #push(SnakeCommand) 
         */
        public void addLast(SnakeCommand command){
            addLast(getActionForCommand(Objects.requireNonNull(command)));
        }
        /**
         * This attempts to insert the given {@code Consumer} at the end of this 
         * action queue, throwing an {@code IllegalStateException} if doing so 
         * would violate any capacity restrictions. If this action queue is 
         * capacity restricted, then it is generally preferable to use the 
         * {@link #offer(Consumer) offer(Consumer)} method. <p>
         * 
         * This method is equivalent to {@link #addLast(Consumer) 
         * addLast(Consumer)}.
         * 
         * @param e The {@code Consumer} to add (cannot be null).
         * @return {@code true} (as specified by {@link Collection#add 
         * Collection.add}).
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offer(Consumer) 
         * @see #addFirst(Consumer) 
         * @see #addFirst(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #addLast(SnakeCommand) 
         * @see #add(SnakeCommand) 
         * @see #push(Consumer) 
         */
        @Override
        public boolean add(Consumer<Snake> e) {
            addLast(e);
            return true;
        }
        /**
         * This attempts to insert a {@code Consumer} at the end of this action 
         * queue that will perform the given command, throwing an {@code 
         * IllegalStateException} if doing so would violate any capacity 
         * restrictions. If this action queue is capacity restricted, then it is 
         * generally preferable to use the {@link #offer(SnakeCommand) 
         * offer(SnakeCommand)} method. <p>
         * 
         * This method is equivalent to calling {@link #add(Consumer) 
         * add(Consumer)} with the {@code Consumer} returned by the protected 
         * {@link #getActionForCommand(SnakeCommand) getActionForCommand} 
         * method.
         * 
         * @param command The command for the {@code Consumer} to add (cannot be 
         * null).
         * @return {@code true} (the value returned by {@link #add(Consumer) 
         * add(Consumer)})
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerLast(SnakeCommand) 
         * @see #addFirst(Consumer) 
         * @see #addFirst(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #add(SnakeCommand) 
         * @see #addLast(Consumer) 
         * @see #push(SnakeCommand) 
         */
        public boolean add(SnakeCommand command){
            return add(getActionForCommand(Objects.requireNonNull(command)));
        }
        /**
         * This attempts to push the given {@code Consumer} onto the stack 
         * represented by this action queue, throwing an {@code 
         * IllegalStateException} if doing so would violate any capacity 
         * restrictions. <p>
         * 
         * This method is equivalent to {@link #addFirst(Consumer) 
         * addFirst(Consumer)}.
         * 
         * @param e The {@code Consumer} to push (cannot be null).
         * @throws NullPointerException If the given {@code Consumer} is null.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerFirst(Consumer) 
         * @see #addFirst(Consumer) 
         * @see #push(SnakeCommand) 
         */
        @Override
        public void push(Consumer<Snake> e) {
            addFirst(e);
        }
        /**
         * This attempts to push a {@code Consumer} onto the stack represented 
         * by this action queue that will perform the given command, throwing an 
         * {@code IllegalStateException} if doing so would violate any capacity 
         * restrictions. <p>
         * 
         * This method is equivalent to {@link #addFirst(SnakeCommand) 
         * addFirst(SnakeCommand)}.
         * 
         * @param command The command for the {@code Consumer} to push (cannot 
         * be null).
         * @throws NullPointerException If the given command is null.
         * @throws IllegalArgumentException If no {@code Consumer} is available 
         * for the given command.
         * @throws IllegalStateException If the {@code Consumer} cannot be added 
         * at this time due to capacity restrictions.
         * @see #offerFirst(SnakeCommand) 
         * @see #addFirst(SnakeCommand) 
         * @see #push(Consumer) 
         */
        public void push(SnakeCommand command){
            addFirst(command);
        }
        /**
         * This retrieves, but does not remove, the first element of this action 
         * queue, or null if this action queue is {@link #isEmpty empty}.
         * @return The head of this action queue, or null if this action queue 
         * is empty.
         * @see #peekLast 
         * @see #peek 
         * @see #peekNext 
         * @see #getFirst 
         * @see #pollFirst 
         * @see #removeFirst 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> peekFirst() {
            return queue.peekFirst();
        }
        /**
         * This retrieves, but does not remove, the last element of this action 
         * queue, or null if this action queue is {@link #isEmpty empty}.
         * @return The tail of this action queue, or null if this action queue 
         * is empty.
         * @see #peekFirst
         * @see #peek 
         * @see #peekNext 
         * @see #getLast 
         * @see #pollLast 
         * @see #removeLast 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> peekLast() {
            return queue.peekLast();
        }
        /**
         * This retrieves, but does not remove, the first element of this action 
         * queue, or null if this action queue is {@link #isEmpty empty}. <p> 
         * 
         * This method is equivalent to {@link #peekFirst peekFirst}.
         * 
         * @return The head of this action queue, or null if this action queue 
         * is empty.
         * @see #peekFirst 
         * @see #peekLast 
         * @see #peekNext 
         * @see #element 
         * @see #poll 
         * @see #remove() 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> peek() {
            return peekFirst();
        }
        /**
         * This retrieves, but does not remove, the first {@code Consumer} in 
         * this action queue that can be performed by the {@link #getSnake 
         * parent snake}. The {@code Consumer} that is returned is the first 
         * {@code Consumer} in this action queue that will not be skipped, with 
         * all {@code Consumer}s preceding it ignored. In other words, this 
         * returns the first {@code Consumer} in this action queue that, if all 
         * the {@code Consumer}s preceding it were removed, {@link 
         * #willSkipNextAction willSkipNextAction} would return {@code false}. 
         * If this action queue is either {@link #isEmpty empty} or contains no 
         * such {@code Consumer} for which {@code willSkipNextAction} would 
         * return {@code false} if it were the head of the queue, then this will 
         * return null.
         * 
         * @return The first {@code Consumer} in this action queue that can be 
         * performed by the snake, or null if no such {@code Consumer} is in 
         * this action queue.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #peek 
         * @see #peekFirst 
         * @see #peekLast 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#getActionQueue 
         * @see Snake#doNextAction 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         */
        public Consumer<Snake> peekNext(){
            if (isEmpty())          // If the action queue is empty
                return null;
                // This creates an iterator to go through the queue
            Iterator<Consumer<Snake>> itr = iterator();
                // This gets the current action being checked
            Consumer<Snake> action = itr.next();
            while (itr.hasNext()){  // While there are actions in the iterator
                    // Get the action that comes after the current one
                Consumer<Snake> next = itr.next();
                    // If the current action will not be skipped
                if (!willSkipAction(action,next))
                    return action;
                action = next;
            }   // If the last action is to be skipped, return null. Otherwise, 
            return (willSkipAction(action,null))? null : action;   // return it.
        }
        /**
         * This retrieves, but does not remove, the first element of this action 
         * queue. This method differs from {@link #peekFirst peekFirst} only in 
         * that it throws an exception if this action queue is {@link #isEmpty 
         * empty}.
         * @return The head of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peekFirst
         * @see #getLast 
         * @see #element 
         * @see #getNext 
         * @see #pollFirst 
         * @see #removeFirst 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> getFirst() {
            return queue.getFirst();
        }
        /**
         * This retrieves, but does not remove, the last element of this action 
         * queue. This method differs from {@link #peekLast peekLast} only in 
         * that it throws an exception if this action queue is {@link #isEmpty 
         * empty}.
         * @return The tail of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peekLast 
         * @see #getFirst 
         * @see #element 
         * @see #getNext 
         * @see #pollLast 
         * @see #removeLast 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> getLast() {
            return queue.getLast();
        }
        /**
         * This retrieves, but does not remove, the first element of this action 
         * queue. This method differs from {@link #peek peek} only in that it 
         * throws an exception if this action queue is {@link #isEmpty empty}. 
         * <p>
         * This method is equivalent to {@link #getFirst getFirst}.
         * 
         * @return The head of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peek 
         * @see #getFirst 
         * @see #getLast 
         * @see #getNext 
         * @see #poll 
         * @see #remove() 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> element() {
            return getFirst();
        }
        /**
         * This retrieves, but does not remove, the first {@code Consumer} in 
         * this action queue that can be performed by the {@link #getSnake 
         * parent snake}. The {@code Consumer} that is returned is the first 
         * {@code Consumer} in this action queue that will not be skipped, with 
         * all {@code Consumer}s preceding it ignored. In other words, this 
         * returns the first {@code Consumer} in this action queue that, if all 
         * the {@code Consumer}s preceding it were removed, {@link 
         * #willSkipNextAction willSkipNextAction} would return {@code false}. 
         * This method differs from {@link #peekNext peekNext} only in that it 
         * throws an exception if this action queue is either {@link #isEmpty 
         * empty} or contains no such {@code Consumer} for which {@code 
         * willSkipNextAction} would return {@code false} if the {@code 
         * Consumer} was the head of the queue. In other words, where {@code 
         * peekNext} would return null, this would throw an exception.
         * 
         * @return The first {@code Consumer} in this action queue that can be 
         * performed by the snake.
         * @throws NoSuchElementException If this action queue is either empty 
         * or does not contain a {@code Consumer} that can be performed by the 
         * snake.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see #peekNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #element 
         * @see #getFirst 
         * @see #getLast 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#getActionQueue 
         * @see Snake#doNextAction 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         */
        public Consumer<Snake> getNext(){
                // Peek at the next action to return
            Consumer<Snake> action = peekNext();    
            if (action == null) // If the next action to return is null
                throw new NoSuchElementException();
            return action;
        }
        /**
         * This retrieves and removes the first element of this action queue, or 
         * returns null if this action queue is {@link #isEmpty empty}.
         * @return The head of this action queue, or null if this action queue 
         * is empty.
         * @see #peekFirst 
         * @see #getFirst 
         * @see #pollLast 
         * @see #poll 
         * @see #pollNext 
         * @see #removeFirst 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> pollFirst() {
            return queue.pollFirst();
        }
        /**
         * This retrieves and removes the last element of this action queue, or 
         * returns null if this action queue is {@link #isEmpty empty}.
         * @return The tail of this action queue, or null if this action queue 
         * is empty.
         * @see #peekLast 
         * @see #getLast 
         * @see #pollFirst 
         * @see #poll 
         * @see #pollNext 
         * @see #removeLast 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> pollLast() {
            return queue.pollLast();
        }
        /**
         * This retrieves and removes the first element of this action queue, or 
         * returns null if this action queue is {@link #isEmpty empty}. <p> 
         * 
         * This method is equivalent to {@link #pollFirst pollFirst}.
         * 
         * @return The head of this action queue, or null if this action queue 
         * is empty.
         * @see #peek
         * @see #element 
         * @see #pollFirst
         * @see #pollLast 
         * @see #pollNext 
         * @see #remove()
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> poll() {
            return pollFirst();
        }
        /**
         * This retrieves and removes the first {@code Consumer} in this action 
         * queue that can be performed by the {@link #getSnake parent snake}. 
         * The {@code Consumer} that is removed and returned is the first {@code 
         * Consumer} in this action queue that will not be skipped, with all 
         * {@code Consumer}s preceding it discarded. In other words, this 
         * removes and returns the first {@code Consumer} in this action queue 
         * that, once all the {@code Consumer}s preceding it have been removed, 
         * {@link #willSkipNextAction willSkipNextAction} would return {@code 
         * false}. If this action queue is {@link #isEmpty empty}, then this 
         * will return null. If this action queue does not contain any {@code 
         * Consumer}s for which {@code willSkipNextAction} would return {@code 
         * false} if it were the head of the queue, then this will empty this 
         * action queue and return null. <p>
         * 
         * This is equivalent to {@link #poll polling} this action queue until 
         * either {@code willSkipNextAction} returns {@code false} or this 
         * action queue is empty, and then polling one more time to dequeue the 
         * desired {@code Consumer}.
         * 
         * @return The first {@code Consumer} in this action queue that can be 
         * performed by the snake, or null if no such {@code Consumer} is in 
         * this action queue.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see #peekNext 
         * @see #getNext 
         * @see #removeNext 
         * @see #popNext 
         * @see #peek 
         * @see #pollFirst 
         * @see #pollLast 
         * @see #poll 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#getActionQueue 
         * @see Snake#doNextAction 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         */
        public Consumer<Snake> pollNext(){
            if (isEmpty()){         // If the action queue is empty
                prevAction = null;  // Clear the previously returned action
                return null;
            }
                // This will get the next action for the snake to perform
            Consumer<Snake> action;
            do{ // A do while loop to get the next action to perform
                action = poll();    // Poll the next action in the queue
            }   // While the queue is not empty and the action should be skipped
            while (!isEmpty() && willSkipAction(action));
                // If the retrieved action should be skipped (this accounts for 
                // whether the action was the last action in the queue, yet 
            if (willSkipAction(action))     // should still be skipped)
                action = null;
            prevAction = action;     // Store the action that will be returned
            return action;
        }
        /**
         * This retrieves and removes the first element of this action queue. 
         * This method differs from {@link #pollFirst pollFirst} only in that it 
         * throws an exception if this action queue is {@link #isEmpty empty}.
         * @return The head of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peekFirst
         * @see #getFirst
         * @see #pollFirst 
         * @see #removeLast 
         * @see #remove() 
         * @see #removeNext 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> removeFirst() {
            return queue.removeFirst();
        }
        /**
         * This retrieves and removes the last element of this action queue. 
         * This method differs from {@link #pollLast pollLast} only in that it 
         * throws an exception if this action queue is {@link #isEmpty empty}.
         * @return The tail of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peekLast 
         * @see #getLast 
         * @see #pollLast 
         * @see #removeFirst 
         * @see #remove() 
         * @see #removeNext 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> removeLast() {
            return queue.removeLast();
        }
        /**
         * This retrieves and removes the first element of this action queue. 
         * This method differs from {@link #poll poll} only in that it throws an 
         * exception if this action queue is {@link #isEmpty empty}. <p> 
         * 
         * This method is equivalent to {@link #removeFirst removeFirst}.
         * 
         * @return The head of this action queue.
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peek 
         * @see #element 
         * @see #poll 
         * @see #removeFirst 
         * @see #removeLast 
         * @see #removeNext 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> remove() {
            return removeFirst();
        }
        /**
         * This retrieves and removes the first {@code Consumer} in this action 
         * queue that can be performed by the {@link #getSnake parent snake}. 
         * The {@code Consumer} that is removed and returned is the first {@code 
         * Consumer} in this action queue that will not be skipped, with all 
         * {@code Consumer}s preceding it discarded. In other words, this 
         * removes and returns the first {@code Consumer} in this action queue 
         * that, once all the {@code Consumer}s preceding it have been removed, 
         * {@link #willSkipNextAction willSkipNextAction} would return {@code 
         * false}. This method differs from {@link #pollNext pollNext} only in 
         * that it throws an exception if this action queue is either {@link 
         * #isEmpty empty} or contains no such {@code Consumer} for which {@code 
         * willSkipNextAction} would return {@code false} if the {@code 
         * Consumer} was the head of the queue. In other words, where {@code 
         * pollNext} would return null, this would throw an exception. <p>
         * 
         * This is equivalent to {@link #remove() removing} the head of this 
         * action queue until either {@code willSkipNextAction} returns {@code 
         * false} or this action queue is empty, and then removing the head one 
         * more time to dequeue the desired {@code Consumer}.
         * 
         * @return The first {@code Consumer} in this action queue that can be 
         * performed by the snake.
         * @throws NoSuchElementException If this action queue is either empty 
         * or does not contain a {@code Consumer} that can be performed by the 
         * snake.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #popNext 
         * @see #peek 
         * @see #element 
         * @see #removeFirst 
         * @see #removeLast 
         * @see #remove() 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#getActionQueue 
         * @see Snake#doNextAction 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         */
        public Consumer<Snake> removeNext(){
                // Poll the next action to return
            Consumer<Snake> action = pollNext();  
            if (action == null)     // If the next action to return is null
                throw new NoSuchElementException();
            return action;
        }
        /**
         * This pops an element from the stack represented by this action queue. 
         * In other words, this removes and returns the first element of this 
         * action queue. <p>
         * 
         * This method is equivalent to {@link #removeFirst removeFirst}.
         * 
         * @return The element at the front of this action queue (which is the 
         * top of the stack represented by this action queue).
         * @throws NoSuchElementException If this action queue is empty.
         * @see #peekFirst 
         * @see #peek 
         * @see #getFirst 
         * @see #element 
         * @see #pollFirst 
         * @see #poll 
         * @see #removeFirst 
         * @see #remove() 
         * @see #popNext 
         * @see #isEmpty 
         * @see #size 
         */
        @Override
        public Consumer<Snake> pop() {
            return removeFirst();
        }
        /**
         * This pops the first {@code Consumer} from the stack represented by 
         * this action queue that can be performed by the {@link #getSnake 
         * parent snake}. The {@code Consumer} that is popped is the first 
         * {@code Consumer} in the stack that will not be skipped, with all 
         * {@code Consumer}s preceding it discarded. In other words, this pops 
         * the first {@code Consumer} in this action queue that, once all the 
         * {@code Consumer}s preceding it have been removed, {@link 
         * #willSkipNextAction willSkipNextAction} would return {@code false}. 
         * <p>
         * This is equivalent to {@link #removeNext removeNext}, which is 
         * equivalent to {@link #pop popping} the stack represented by this 
         * action queue until either {@code willSkipNextAction} returns {@code 
         * false} or this action queue is empty, and then popping the stack one 
         * more time to retrieve the desired {@code Consumer}.
         * 
         * @return The first {@code Consumer} in this action queue that can be 
         * performed by the snake (which is the closest {@code Consumer} to the 
         * top of the stack represented by this action queue).
         * @throws NoSuchElementException If this action queue is empty.
         * @see #willSkipNextAction 
         * @see #getSkipsRepeatedActions 
         * @see #setSkipsRepeatedActions 
         * @see #getSnake 
         * @see #peekNext 
         * @see #getNext 
         * @see #pollNext 
         * @see #removeNext 
         * @see #peek 
         * @see #element 
         * @see #pop 
         * @see #isEmpty 
         * @see #size 
         * @see Snake#getActionQueue 
         * @see Snake#doNextAction 
         * @see Snake#getSkipsRepeatedActions 
         * @see Snake#canPerformAction(Consumer) 
         */
        public Consumer<Snake> popNext(){
            return removeNext();
        }
        /**
         * This removes the first occurrence of the specified element from this 
         * action queue. If this action queue does not contain the element, then 
         * no change will be made. More formally, this removes the first element 
         * {@code e} such that {@code Objects.equals(o, e)} if one is present. 
         * If this action queue contained the specified element (or 
         * equivalently, if this action queue changed as a result of the call), 
         * then this will return {@code true}.
         * @param o The element to be removed from this action queue, if 
         * present.
         * @return {@code true} if an element was removed from this action 
         * queue, else {@code false}.
         * @see #removeLastOccurrence 
         * @see #remove(Object) 
         */
        @Override
        public boolean removeFirstOccurrence(Object o) {
            return queue.removeFirstOccurrence(o);
        }
        /**
         * This removes the last occurrence of the specified element from this 
         * action queue. If this action queue does not contain the element, then 
         * no change will be made. More formally, this removes the last element 
         * {@code e} such that {@code Objects.equals(o, e)} if one is present. 
         * If this action queue contained the specified element (or 
         * equivalently, if this action queue changed as a result of the call), 
         * then this will return {@code true}.
         * @param o The element to be removed from this action queue, if 
         * present.
         * @return {@code true} if an element was removed from this action 
         * queue, else {@code false}.
         * @see #removeFirstOccurrence 
         * @see #remove(Object) 
         */
        @Override
        public boolean removeLastOccurrence(Object o) {
            return queue.removeLastOccurrence(o);
        }
        /**
         * This removes the first occurrence of the specified element from this 
         * action queue. If this action queue does not contain the element, then 
         * no change will be made. More formally, this removes the first element 
         * {@code e} such that {@code Objects.equals(o, e)} if one is present. 
         * If this action queue contained the specified element (or 
         * equivalently, if this action queue changed as a result of the call), 
         * then this will return {@code true}. <p>
         * 
         * This method is equivalent to {@link #removeFirstOccurrence 
         * removeFirstOccurrence}.
         * 
         * @param o The element to be removed from this action queue, if 
         * present.
         * @return {@code true} if an element was removed from this action 
         * queue, else {@code false}.
         * @see #removeFirstOccurrence 
         * @see #removeLastOccurrence 
         */
        @Override
        public boolean remove(Object o){
            return removeFirstOccurrence(o);
        }
        /**
         * This removes all the elements from this action queue. The action 
         * queue will be empty after this call returns.
         */
        @Override
        public void clear(){
            queue.clear();
        }
        /**
         * This returns an iterator over the elements in this action queue in 
         * sequential order. That is to say, the iterator iterates over the 
         * elements in this action queue in order from first (head) to last 
         * (tail). This is the same order that the elements would be dequeued 
         * via successful calls to either {@link #poll poll}, {@link #remove 
         * remove}, or {@link #pop pop}.
         * @return An iterator over the elements in this action queue in proper 
         * sequence.
         */
        @Override
        public Iterator<Consumer<Snake>> iterator() {
            return queue.iterator();
        }
        /**
         * This returns an iterator over the elements in this action queue in 
         * reverse sequential order. That is to say, the iterator iterates over 
         * the elements in this action queue in order from last (tail) to first 
         * (head). This is the same order that the elements would be dequeued 
         * via successful calls to {@link #pollLast pollLast} or {@link 
         * #removeLast removeLast}.
         * @return An iterator over the elements in this action queue in reverse 
         * sequence.
         */
        @Override
        public Iterator<Consumer<Snake>> descendingIterator() {
            return queue.descendingIterator();
        }
        /**
         * This returns an array containing all of the elements in this action 
         * queue in proper sequence (from the first element to the last 
         * element). <p>
         * 
         * The returned array will be "safe" in that no references to the array 
         * will be maintained by this action queue. (In other words, this method 
         * must allocate a new array). The caller is thus free to modify the 
         * returned array.
         * 
         * @return An array containing all of the elements in this action queue.
         */
        @Override
        public Object[] toArray(){
            return queue.toArray();
        }
        /**
         * This creates and returns a copy of this action queue. 
         * @return A clone of this action queue.
         */
        @Override
        public ActionQueue clone(){
            try{    // Create a clone of this action queue
                ActionQueue clone = (ActionQueue) super.clone();
                    // Set the clone's internal queue to a clone of this action 
                clone.queue = queue.clone();    // queue's internal queue
                return clone;
            }
            catch(CloneNotSupportedException ex){
                throw new AssertionError();
            }
        }
    }
}
