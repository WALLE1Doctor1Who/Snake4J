/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.event.EventListenerList;
import snake.action.DefaultSnakeAction;
import snake.action.SnakeCommand;
import snake.event.SnakeEvent;
import snake.event.SnakeListener;
import snake.playfield.PlayFieldModel;
import snake.playfield.Tile;

/**
 * This is a collection of {@link Tile tiles} used to represent a snake in the 
 * game snake.
 * @author Milo Steier
 */
public class Snake implements SnakeConstants, Iterable<Tile>{
    /**
     * This is the flag used to indicate that a snake has consumed an apple.
     */
    protected static final int APPLE_CONSUMED_FLAG = 0x01;
    /**
     * This is the flag used to set whether a snake can eat apples.
     */
    protected static final int APPLE_CONSUMPTION_ENABLED_FLAG = 0x02;
    /**
     * This is the flag used to set whether eating an apple will cause a snake 
     * to grow in length.
     */
    protected static final int APPLE_GROWTH_ENABLED_FLAG = 0x04;
    /**
     * This is the flag used to set whether a snake will wrap around to the 
     * other side of the play field when it reaches an edge. When not set, a 
     * snake will crash into the edge of the play field instead of wrapping 
     * around to the other side.
     */
    protected static final int WRAP_AROUND_FLAG = 0x08;
    /**
     * This is the flag used to set which player a snake belongs to. This does 
     * not necessarily have to be an actual player, since it could be used for 
     * a non-player controlled snake. This is primarily used to indicate what 
     * {@link Tile#getType() type} of {@link Tile#isSnake() snake tiles} 
     * should a snake be composed of.
     * @see SnakeConstants#ALTERNATE_TYPE_FLAG
     * @see Tile#isSnake()
     * @see Tile#getType()
     */
    protected static final int PLAYER_FLAG = SnakeConstants.ALTERNATE_TYPE_FLAG;
    /**
     * This is the flag used to indicate whether a snake is flipped around. This 
     * is primarily for use in non-player controlled snakes that act as moving 
     * obstacles so that they can flip around without turning.
     */
    protected static final int FLIPPED_FLAG = 0x20;
    /**
     * This is the flag used to indicate that a snake has crashed. A snake has 
     * crashed when it has failed to add or move more than the allotted amount 
     * of failures for that snake.
     */
    protected static final int CRASHED_FLAG = 0x40;
    /**
     * This is the flag used to indicate whether the default action of a snake 
     * is enabled.
     */
    protected static final int DEFAULT_ACTION_ENABLED_FLAG = 0x80;
    
//    protected static final int RECENTLY_FAILED_FLAG = 0x100;
    /**
     * This stores the flags that are set initially when a snake is constructed.
     */
    private static final int DEFAULT_FLAG_SETTINGS = APPLE_CONSUMPTION_ENABLED_FLAG | 
            APPLE_GROWTH_ENABLED_FLAG | WRAP_AROUND_FLAG | DEFAULT_ACTION_ENABLED_FLAG;
    /**
     * This stores the flags that are cleared when a snake is reset.
     */
    private static final int RESET_AFFECTED_FLAGS = APPLE_CONSUMED_FLAG | 
            FLIPPED_FLAG | CRASHED_FLAG;// | RECENTLY_FAILED_FLAG;
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
     * This identifies that the snake's default action has been changed.
     */
    public static final String DEFAULT_ACTION_PROPERTY_CHANGED = 
            "SnakeDefaultActionPropertyChanged";
    /**
     * This identifies that the snake's default action has been enabled or 
     * disabled.
     */
    public static final String DEFAULT_ACTION_ENABLED_PROPERTY_CHANGED = 
            "SnakeDefaultActionEnabledPropertyChanged";
    /**
     * This identifies that the number of failures that have to occur when 
     * attempting to move or add before the snake declares itself to have 
     * crashed has changed.
     */
    public static final String ALLOWED_FAILS_PROPERTY_CHANGED = 
            "SnakeAllowedFailsPropertyChanged";
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
     * queue when a snake is "flipped".
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
     * This stores the amount of times this snake has sequentially failed to add 
     * or move. This gets reset whenever the snake does not fail.
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
     * initially null, and will be initialized when an action is added to it.
     */
    private ArrayDeque<Consumer<Snake>> actionQueue = null;
    
    public Snake(PlayFieldModel model){
        Snake.this.setModel(model);
        flags = DEFAULT_FLAG_SETTINGS;
        name = null;
        allowedFails = -1;
        Snake.this.setDefaultAction(SnakeCommand.MOVE_FORWARD);
        changeSupport = new PropertyChangeSupport(this);
    }
    
    public Snake(PlayFieldModel model, Tile head){
        this(model);
        Snake.this.initialize(head);
    }
    
    public Snake(PlayFieldModel model, int row, int column){
        this(model);
        Snake.this.initialize(row, column);
    }
    /**
     * This returns an integer storing the flags used to store the settings for 
     * this snake and control its state.
     * @return An integer containing the flags for this snake.
     * @see #getFlag(int) 
     * @see #setFlag(int, boolean) 
     * @see #toggleFlag(int) 
     * @see #APPLE_CONSUMED_FLAG
     * @see #APPLE_CONSUMPTION_ENABLED_FLAG
     * @see #APPLE_GROWTH_ENABLED_FLAG
     * @see #WRAP_AROUND_FLAG
     * @see #PLAYER_FLAG
     * @see #FLIPPED_FLAG
     * @see #CRASHED_FLAG
     * @see #DEFAULT_ACTION_ENABLED_FLAG
     */
    protected int getFlags(){
        return flags;
    }
    /**
     * This gets whether the given flag is set.
     * @param flag The flag to check for.
     * @return 
     */
    protected boolean getFlag(int flag){
        return SnakeUtilities.getFlag(flags, flag);
    }
    
    protected boolean setFlag(int flag, boolean value){
        int old = flags;
        flags = SnakeUtilities.setFlag(flags, flag, value);
        return flags != old;
    }
    
    protected boolean toggleFlag(int flag){
        int old = flags;
        flags = SnakeUtilities.toggleFlag(flags, flag);
        return flags != old;
    }
    
    public PlayFieldModel getModel(){
        return model;
    }
    
    public Snake setModel(PlayFieldModel model){
        if (model == null)      // If the model is null
            throw new NullPointerException("Play field model cannot be null");
        if (Objects.equals(this.model, model))
            return this;
        PlayFieldModel old = this.model;
        this.model = model;
        firePropertyChange(MODEL_PROPERTY_CHANGED,old,model);
        return (old != null) ? reset(old) : this;
    }
    
    protected Snake reset(PlayFieldModel model){
        resetFailCount();
        clearActionQueue();
        boolean reset = setFlag(RESET_AFFECTED_FLAGS,false);
        if (!snakeBody.isEmpty()){
            reset = true;
            boolean adjusting = model.getTilesAreAdjusting();
            model.setTilesAreAdjusting(true);
            for (Tile tile : snakeBody)
                tile.clear();
            snakeBody.clear();
            model.setTilesAreAdjusting(adjusting);
            
        }
        if (reset)
            fireSnakeChange(SnakeEvent.SNAKE_RESET,0);
        return this;
    }
    
    public Snake initialize(Tile head){
        if (head == null)
            throw new NullPointerException("Head tile cannot be null");
        if (!model.contains(head))
            throw new IllegalArgumentException("Head tile is not in play field");
        if (!head.isEmpty() && !head.equals(getHead()))
            throw new IllegalArgumentException("Head tile is not empty");
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        reset(model);
        pushHead(head.clear().setFacingLeft(true));
        model.setTilesAreAdjusting(adjusting);
        fireSnakeChange(SnakeEvent.SNAKE_INITIALIZED);
        return this;
    }
    
    public Snake initialize(int row, int column){
        return initialize(model.getTile(row, column));
    }
    
    public boolean isValid(){
        return model != null && !isEmpty() && getHead() != null && 
                getHead().getDirectionsFacedCount() == 1;
    }
    
    protected void checkIfInvalidState(){
        if (!isValid())
            throw new IllegalStateException("Snake "+((name!=null)?name+" ":"")+
                    "is not in a valid state");
    }
    
    public Tile getHead(){
        return (isFlipped()) ? snakeBody.peekLast() : snakeBody.peekFirst();
    }
    
    public Tile getTail(){
        if (length() < 2)
            return null;
        else if (isFlipped())
            return snakeBody.peekFirst();
        else
            return snakeBody.peekLast();
    }
    
    public int length(){
        return snakeBody.size();
    }
    
    public boolean isEmpty(){
        return snakeBody.isEmpty();
    }
    
    public boolean contains(Tile tile){
        return snakeBody.contains(tile);
    }
    
    public boolean contains(int row, int column){
        return model.contains(row, column) && contains(model.getTile(row, column));
    }
    
    @Override
    public Iterator<Tile> iterator() {
        return new SnakeIterator();
    }
    
    public Tile[] toArray(){
        Tile[] tiles = new Tile[length()];
        int i = 0;
        for (Tile tile : this){
            tiles[i++] = tile;
            if (i >= tiles.length)
                break;
        }
        return tiles;
    }
    
    public List<Tile> toList(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile : this)
            tiles.add(tile);
        return tiles;
    }
    
    protected void pushHead(Tile tile){
        Objects.requireNonNull(tile);
        if (getHead() != null){
            if (getTail() == null)      // If there is no tail yet
                getHead().clear().setType(getPlayerType());
            getHead().alterDirection(tile);
        }
        tile.setType(getPlayerType());
        if (isFlipped())            // If the snake is flipped
            snakeBody.addLast(tile);
        else
            snakeBody.addFirst(tile);
    }
    
    protected Tile pollTail(){
            // Remove the tail of the snake. If the snake is flipped, this will 
            // be the first tile in the queue. Otherwise, this will be the last 
            // tile in the queue
        Tile tile = (isFlipped()) ? snakeBody.pollFirst():snakeBody.pollLast();
        if (getTail() != null){ // If there is still a tail
            getTail().alterDirection(tile);
        }
        if (tile != null)
            tile.clear();
        return tile;
    }
    
    
    
    public boolean isFacingUp(){
        return getHead() != null && getHead().isFacingUp();
    }
    
    public boolean isFacingDown(){
        return getHead() != null && getHead().isFacingDown();
    }
    
    public boolean isFacingLeft(){
        return getHead() != null && getHead().isFacingLeft();
    }
    
    public boolean isFacingRight(){
        return getHead() != null && getHead().isFacingRight();
    }
    
    public int getDirectionFaced(){
        return (getHead() != null) ? getHead().getDirectionsFaced() : 0;
    }
    
    private int checkDirection(int direction){
        if (SnakeUtilities.getDirectionCount(direction) == 0)
            direction |= getDirectionFaced();
        return SnakeUtilities.requireSingleDirection(direction);
    }
    
    public Tile getAdjacentToHead(int direction){
        checkIfInvalidState();
        return model.getAdjacentTile(getHead(),checkDirection(direction),
                isWrapAroundEnabled());
    }
    
    public Tile getTileBeingFaced(){
        return getAdjacentToHead(0);
    }
    
    
    
    public String getName(){
        return name;
    }
    
    public Snake setName(String name){
        if (Objects.equals(this.name, name))
            return this;
        String old = this.name;
        this.name = name;
        firePropertyChange(NAME_PROPERTY_CHANGED,old,name);
        return this;
    }
    
    public boolean getPlayerType(){
        return getFlag(PLAYER_FLAG);
    }
    
    public Snake setPlayerType(boolean value){
        if (setFlag(PLAYER_FLAG,value)){
            firePropertyChange(PLAYER_TYPE_PROPERTY_CHANGED,value);
            if (!isEmpty()){
                boolean adjusting = model.getTilesAreAdjusting();
                model.setTilesAreAdjusting(true);
                for (Tile tile : snakeBody)
                    tile.setType(value);
                model.setTilesAreAdjusting(adjusting);
            }
        }
        return this;
    }
    
    public boolean isFlipped(){
        return getFlag(FLIPPED_FLAG);
    }
    
    protected Snake setFlipped(boolean value){
        checkIfInvalidState();
        if (setFlag(FLIPPED_FLAG,value)){
            if (getTail() == null)
                getHead().flip();
            fireSnakeChange(SnakeEvent.SNAKE_FLIPPED);
        }
        return this;
    }
    
    public Snake flip(){
        return setFlipped(!isFlipped());
    }
    
    public boolean isWrapAroundEnabled(){
        return getFlag(WRAP_AROUND_FLAG);
    }
    
    public Snake setWrapAroundEnabled(boolean enabled){
        if (setFlag(WRAP_AROUND_FLAG,enabled))  // If the wrap around flag has changed
            firePropertyChange(WRAP_AROUND_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    
    public boolean isAppleConsumptionEnabled(){
        return getFlag(APPLE_CONSUMPTION_ENABLED_FLAG);
    }
    
    public Snake setAppleConsumptionEnabled(boolean enabled){
        if (setFlag(APPLE_CONSUMPTION_ENABLED_FLAG,enabled))
            firePropertyChange(APPLE_CONSUMPTION_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    
    public boolean getApplesCauseGrowth(){
        return getFlag(APPLE_GROWTH_ENABLED_FLAG);
    }
    
    public Snake setApplesCauseGrowth(boolean value){
        if (setFlag(APPLE_GROWTH_ENABLED_FLAG,value))
            firePropertyChange(APPLES_CAUSE_GROWTH_PROPERTY_CHANGED,value);
        return this;
    }
    
    public boolean hasConsumedApple(){
        return getFlag(APPLE_CONSUMED_FLAG);
    }
    
    protected void setConsumedApple(boolean value, Integer direction){
        checkIfInvalidState();
        setFlag(APPLE_CONSUMED_FLAG,value);
        if (value)
            fireSnakeChange(SnakeEvent.SNAKE_CONSUMED_APPLE,direction);
    }
    
    protected void setConsumedApple(boolean value){
        setConsumedApple(value,null);
    }
    
    public boolean isCrashed(){
        return getFlag(CRASHED_FLAG);
    }
    
    protected void setCrashed(boolean value, Integer direction){
        checkIfInvalidState();
        if (setFlag(CRASHED_FLAG,value)){
            fireSnakeChange((value)?SnakeEvent.SNAKE_CRASHED:
                    SnakeEvent.SNAKE_REVIVED,direction);
        }
    }
    
    protected void setCrashed(boolean value){
        setCrashed(value,null);
    }
    
    protected void updateCrashed(Integer direction){
        setCrashed(getAllowedFails() >= 0 && getFailCount() > getAllowedFails(),
                direction);
    }
    
    public int getAllowedFails(){
        return allowedFails;
    }
    
    public Snake setAllowedFails(int value){
        if (allowedFails == value)  // If the new value matches the old one
            return this;
        int old = allowedFails;     // Gets the old value
        allowedFails = value;
        firePropertyChange(ALLOWED_FAILS_PROPERTY_CHANGED,old,value);
        if (isValid())
            revive();
        return this;
    }
    
    protected int getFailCount(){
        return failCount;
    }
    
    protected void setFailCount(int count){
        failCount = count;
    }
    
    protected void incrementFailCount(){
        failCount++;
    }
    
    protected void resetFailCount(){
        failCount = 0;
    }
    
    public Snake revive(){
        checkIfInvalidState();
        resetFailCount();
        updateCrashed(null);
        return this;
    }
    
    protected boolean canAddTile(Tile tile){
        if (tile == null)               // If the tile is null
            return false;
        if (tile.isApple())             // If the tile is an apple tile
            return isAppleConsumptionEnabled();
        return tile.isEmpty();
    }
    
    public boolean canMoveInDirection(int direction){
        return canAddTile(getAdjacentToHead(direction));
    }
    
    public boolean canMoveForward(){
        return canMoveInDirection(0);
    }
    
    protected boolean addOrMove(int direction, boolean move){
        checkIfInvalidState();
        direction = checkDirection(direction);
        if (isCrashed()){
            fireSnakeFailed(direction);
            return false;
        }
        Tile tile = getAdjacentToHead(direction);
        if (!canAddTile(tile)){
            if (direction != SnakeUtilities.invertDirections(getDirectionFaced()))
                incrementFailCount();
            fireSnakeFailed(direction);
            updateCrashed(direction);
            return false;
        }
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        boolean ateApple = tile.isApple();
        pushHead(tile.setState(direction));
        resetFailCount();
        setConsumedApple(ateApple,direction);
            // If the snake is being added to or the snake ate an apple and 
            // apples cause the snake to grow.
        if (!move || (ateApple && getApplesCauseGrowth()))
            fireSnakeChange(SnakeEvent.SNAKE_ADDED_TILE,direction);
        else{
            pollTail(); // Remove the current tail
            fireSnakeChange(SnakeEvent.SNAKE_MOVED,direction);
        }
        model.setTilesAreAdjusting(adjusting);
        return true;
    }
    
    public boolean add(int direction){
        return addOrMove(direction,false);
    }
    
    public boolean add(){
        return add(0);
    }
    
    public boolean move(int direction){
        return addOrMove(direction,true);
    }
    
    public boolean move(){
        return move(0);
    }
    
    public Tile removeTail(){
        checkIfInvalidState();
        if (getTail() == null){
            fireSnakeFailed(0);
            return null;
        }
        boolean adjusting = model.getTilesAreAdjusting();
        model.setTilesAreAdjusting(true);
        Tile tile = pollTail();
        model.setTilesAreAdjusting(adjusting);
        if (tile == null)
            fireSnakeFailed(0);
        else
            fireSnakeChange(SnakeEvent.SNAKE_REMOVED_TILE,0);
        return tile;
    }
    
    public Consumer<Snake> getDefaultAction(){
        return defaultAction;
    }
    
    public Snake setDefaultAction(Consumer<Snake> action){
            // If the old default action matches the current one
        if (defaultAction == action)
            return this;
        Consumer<Snake> old = defaultAction;
        defaultAction = action;
        firePropertyChange(DEFAULT_ACTION_PROPERTY_CHANGED,old,defaultAction);
        return this;
    }
    
    public Snake setDefaultAction(SnakeCommand cmd){
        if (cmd == SnakeCommand.DEFAULT_ACTION)
            throw new IllegalArgumentException("The default action should not invoke the default action");
        return setDefaultAction((cmd != null)?new DefaultSnakeAction(cmd):null);
    }
    
    public boolean isDefaultActionEnabled(){
        return getFlag(DEFAULT_ACTION_ENABLED_FLAG);
    }
    
    public Snake setDefaultActionEnabled(boolean enabled){
            // If the default action enabled flag changed
        if (setFlag(DEFAULT_ACTION_ENABLED_FLAG,enabled))
            firePropertyChange(DEFAULT_ACTION_ENABLED_PROPERTY_CHANGED,enabled);
        return this;
    }
    
    protected void doAction(Consumer<? super Snake> action){
        if (action == null)
            return;
        checkIfInvalidState();
        action.accept(this);
    }
    
    public boolean doDefaultAction(){
        checkIfInvalidState();
        if (getDefaultAction() != null && isDefaultActionEnabled()){
            doAction(getDefaultAction());
            return true;
        }
        return false;
    }
    
    public boolean doCommand(SnakeCommand cmd){
        checkIfInvalidState();
        switch(Objects.requireNonNull(cmd,"Snake command cannot be null")){
            case MOVE_UP: 
            case MOVE_DOWN:
            case MOVE_LEFT:
            case MOVE_RIGHT:
            case MOVE_FORWARD:
                return move(SnakeCommand.getDirectionOf(cmd));
            case ADD_UP:
            case ADD_DOWN:
            case ADD_LEFT:
            case ADD_RIGHT:
            case ADD_FORWARD:
                return add(SnakeCommand.getDirectionOf(cmd));
            case FLIP:
                boolean flipped = isFlipped();
                flip();
                return flipped != isFlipped();
            case REVIVE:
                revive();
                return !isCrashed();
            case REMOVE_TAIL:
                return removeTail() != null;
            case DEFAULT_ACTION:
                return doDefaultAction();
            default:
                fireSnakeFailed();
                return false;
        }
    }
    
    public ArrayDeque<Consumer<Snake>> getActionQueue(){
        if (actionQueue == null)
            actionQueue = new ArrayDeque<>();
        return actionQueue;
    }
    
    public boolean pushAction(Consumer<Snake> action){
        if (action == null)
            return false;
        ArrayDeque<Consumer<Snake>> queue = getActionQueue();
        return queue != null && queue.add(action);
    }
    
    public boolean pushAction(SnakeCommand cmd){
        return (cmd != null) ? pushAction(new DefaultSnakeAction(cmd)) : false;
    }
    
    public Consumer<Snake> pollNextAction(){
        return (actionQueue != null) ? actionQueue.poll() : null;
    }
    
    public Consumer<Snake> peekNextAction(){
        return (actionQueue != null) ? actionQueue.peekFirst() : null;
    }
    
    public Consumer<Snake> peekLastAction(){
        return (actionQueue != null) ? actionQueue.peekLast() : null;
    }
    
    public int getActionQueueSize(){
        return (actionQueue != null) ? actionQueue.size() : 0;
    }
    
    public boolean isActionQueueEmpty(){
        return actionQueue == null || actionQueue.isEmpty();
    }
    
    public void clearActionQueue(){
        if (actionQueue != null)    // If the action queue has been initialized
            actionQueue.clear();
    }
    
//    protected Consumer<? super Snake> getNextAction(){
//        Consumer<Snake> action = null;
//        while (action == null && !isActionQueueEmpty()){
//            action = pollNextAction();
////            if (Objects.equals(action, peekNextAction()))
//        }
//        return action;
//    }
//    
//    public void doNextAction(){
//        Consumer<? super Snake> next = getNextAction();
//        if (next != null)
//            doAction(next);
//        else if (isDefaultActionEnabled())
//            doDefaultAction();
//    }
    
    
    
    
    /**
     * This returns a String representation of this Snake. This is primarily 
     * used for debugging purposes.
     * @return A String representation of this Snake.
     */
    protected String paramString(){
        return Objects.toString(getName(),"")+
                ((isValid())?"":",invalid")+
                ",head="+Objects.toString(getHead(),"")+
                ",tail="+Objects.toString(getTail(),"")+
                ",length="+length()+
                ",flags="+getFlags()+
                ",allowedFails="+getAllowedFails()+
                ",failCount="+getFailCount()+
                ",defaultAction="+Objects.toString(getDefaultAction(), "");
    }
    @Override
    public String toString(){
        return this.getClass().getName() + "[" + paramString() + "]";
    }
    /**
     * This returns an array of all the objects currently registered as 
     * <code><em>Foo</em>Listener</code>s on this PlayField. 
     * <code><em>Foo</em>Listener</code>s are registered via the 
     * <code>add<em>Foo</em>Listener</code> method. <p>
     * The listener type can be specified using a class literal, such as 
     * <code><em>Foo</em>Listener.class</code>. If no such listeners exist, 
     * then an empty array will be returned.
     * @param <T> The type of {@code EventListener} being requested.
     * @param listenerType The type of listeners being requested. This should 
     * be an interface that descends from {@code EventListener}.
     * @return An array of the objects registered as the given listener type on 
     * this PlayField, or an empty array if no such listeners have been added.
     * @see #getPropertyChangeListeners() 
     * @see #getPlayFieldListeners() 
     * @see #getChangeListeners() 
     */
    @SuppressWarnings("unchecked")
    public <T extends EventListener> T[] getListeners(Class<T> listenerType){
            // If we're getting the PropertyChangeListeners
        if (listenerType == PropertyChangeListener.class)
            return (T[])getPropertyChangeListeners();
        else
            return listenerList.getListeners(listenerType);
    }
    
    public void addSnakeListener(SnakeListener l){
        if (l != null)
            listenerList.add(SnakeListener.class, l);
    }
    
    public void removeSnakeListener(SnakeListener l){
        listenerList.remove(SnakeListener.class, l);
    }
    
    public SnakeListener[] getSnakeListeners(){
        return listenerList.getListeners(SnakeListener.class);
    }
    
    protected void fireSnakeChange(SnakeEvent evt){
        if (evt == null)
            return;
        for (SnakeListener l : getSnakeListeners()){
            if (l != null)
                l.snakeChanged(evt);
        }
    }
    
    protected void fireSnakeChange(int id, Integer direction){
        if (direction == null)
            direction = (getHead()!=null)?getHead().getDirectionsFaced():0;
        fireSnakeChange(new SnakeEvent(this,id,direction));
    }
    
    protected void fireSnakeChange(int id){
        fireSnakeChange(id,null);
    }
    
    protected void fireSnakeFailed(Integer direction){
        fireSnakeChange(SnakeEvent.SNAKE_FAILED,direction);
    }
    
    protected void fireSnakeFailed(){
        fireSnakeFailed(null);
    }
    /**
     * This adds a PropertyChangeListener to this snake. This listener is 
     * registered for all bound properties of this snake. 
     * @param l The listener to be added.
     * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     * @see #removePropertyChangeListener(java.beans.PropertyChangeListener) 
     * @see #getPropertyChangeListeners() 
     */
    public void addPropertyChangeListener(PropertyChangeListener l){
        changeSupport.addPropertyChangeListener(l);
    }
    /**
     * This removes a PropertyChangeListener from this snake. This method should 
     * be used to remove PropertyChangeListeners that were registered for all 
     * bound properties of this snake. 
     * @param l The listener to be removed.
     * @see #addPropertyChangeListener(java.beans.PropertyChangeListener) 
     * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     * @see #getPropertyChangeListeners() 
     */
    public void removePropertyChangeListener(PropertyChangeListener l){
        changeSupport.removePropertyChangeListener(l);
    }
    /**
     * This returns an array of all PropertyChangeListeners that are registered 
     * on this snake.
     * @return An array of the PropertyChangeListeners that have been added, or 
     * an empty array if no listeners have been added.
     * @see #getPropertyChangeListeners(java.lang.String) 
     * @see #addPropertyChangeListener(java.beans.PropertyChangeListener) 
     * @see #removePropertyChangeListener(java.beans.PropertyChangeListener) 
     */
    public PropertyChangeListener[] getPropertyChangeListeners(){
        return changeSupport.getPropertyChangeListeners();
    }
    /**
     * This adds a PropertyChangeListener to this snake that listens for a 
     * specific property.
     * @param propertyName The name of the property to listen for.
     * @param l The listener to be added.
     * @see #addPropertyChangeListener(java.beans.PropertyChangeListener) 
     * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     * @see #getPropertyChangeListeners(java.lang.String) 
     */
    public void addPropertyChangeListener(String propertyName, 
            PropertyChangeListener l){
        changeSupport.addPropertyChangeListener(propertyName, l);
    }
    /**
     * This removes a PropertyChangeListener to this snake that listens for a 
     * specific property. This method should be used to remove 
     * PropertyChangeListeners that were registered for a specific property
     * @param propertyName The name of the property.
     * @param l The listener to be removed.
     * @see #removePropertyChangeListener(java.beans.PropertyChangeListener)
     * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     * @see #getPropertyChangeListeners(java.lang.String) 
     */
    public void removePropertyChangeListener(String propertyName, 
            PropertyChangeListener l){
        changeSupport.removePropertyChangeListener(propertyName, l);
    }
    /**
     * This returns an array of all PropertyChangeListeners that are registered 
     * on this snake for a specific property.
     * @param propertyName The name of the property.
     * @return An array of the PropertyChangeListeners that have been added for 
     * the specified property, or an empty array if no listeners have been 
     * added or the specified property is null.
     * @see #getPropertyChangeListeners() 
     * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener) 
     */
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName){
        return changeSupport.getPropertyChangeListeners(propertyName);
    }
    /**
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for Object properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for boolean properties.
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
     * This fires a PropertyChangeEvent with the given property name and 
     * new boolean value. The old value is assumed to be the inverse of the new 
     * value.
     * @param propertyName The name of the property.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, boolean newValue){
        firePropertyChange(propertyName, !newValue, newValue);
    }
    /**
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for integer properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for byte properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for character properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for short properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for long properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for float properties.
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
     * This fires a PropertyChangeEvent with the given property name, old value, 
     * and new value. This method is for double properties.
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, double oldValue, 
            double newValue){
        firePropertyChange(propertyName,Double.valueOf(oldValue),
                Double.valueOf(newValue));
    }

    
    
    private class SnakeIterator implements Iterator<Tile>{
        /**
         * The iterator from the ArrayDeque used to store the snake body. This 
         * is either the ascending iterator or the descending iterator 
         * depending on whether the snake is flipped.
         */
        private final Iterator<Tile> iterator;
        /**
         * This stores whether the snake was flipped or not when this iterator 
         * was constructed.
         */
        private final boolean flipped;
        /**
         * This constructs a SnakeIterator.
         */
        public SnakeIterator(){
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
            return iterator.next();
        }
    }
    
    
}
