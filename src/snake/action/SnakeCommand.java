/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package snake.action;

import java.util.*;
import snake.*;

/**
 * This is an enumeration type for the instructions used to instruct a {@link 
 * Snake snake} to perform a predefined action via their {@link Snake#doCommand 
 * doCommand} method. <p>
 * 
 * This also provides immutable implementations of {@link SnakeActionCommand 
 * SnakeActionCommand} for each command, which can be accessed either via the 
 * map returned by the {@link #getCommandActionMap getCommandActionMap} method 
 * or via the {@link #getAction getAction} method of a given command.
 * 
 * @author Milo Steier
 * @see Snake
 * @see Snake#doCommand
 * @see SnakeActionCommand
 * @see DefaultSnakeActionCommand
 * @see getCommandActionMap 
 */
public enum SnakeCommand implements SnakeConstants{
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#move(int) move} {@link #UP_DIRECTION up}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#move(int)
     * @see #UP_DIRECTION
     */
    MOVE_UP(UP_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#move(int) move} {@link #DOWN_DIRECTION down}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#move(int)
     * @see #DOWN_DIRECTION
     */
    MOVE_DOWN(DOWN_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#move(int) move} {@link #LEFT_DIRECTION left}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#move(int)
     * @see #LEFT_DIRECTION
     */
    MOVE_LEFT(LEFT_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#move(int) move} {@link #RIGHT_DIRECTION right}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#move(int)
     * @see #RIGHT_DIRECTION
     */
    MOVE_RIGHT(RIGHT_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link Snake#move() 
     * move} {@link Snake#getDirectionFaced forward}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#move(int)
     * @see Snake#move()
     * @see Snake#getDirectionFaced 
     */
    MOVE_FORWARD(0),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#add(int) add} the tile {@link #UP_DIRECTION above} its head.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#add(int) 
     * @see #UP_DIRECTION
     */
    ADD_UP(UP_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#add(int) add} the tile {@link #DOWN_DIRECTION below} its head.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#add(int) 
     * @see #DOWN_DIRECTION
     */
    ADD_DOWN(DOWN_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#add(int) add} the tile {@link #LEFT_DIRECTION to the left of} its 
     * head.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#add(int) 
     * @see #LEFT_DIRECTION
     */
    ADD_LEFT(LEFT_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#add(int) add} the tile {@link #RIGHT_DIRECTION to the right of} its 
     * head.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#add(int) 
     * @see #RIGHT_DIRECTION
     */
    ADD_RIGHT(RIGHT_DIRECTION),
    /**
     * This is the instruction for a {@link Snake snake} to {@link Snake#add() 
     * add} the tile {@link Snake#getDirectionFaced in front} of it.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#add(int)
     * @see Snake#add()
     * @see Snake#getDirectionFaced 
     */
    ADD_FORWARD(0),
    /**
     * This is the instruction for a {@link Snake snake} to {@link Snake#flip 
     * flip}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#flip 
     */
    FLIP,
    /**
     * This is the instruction for a {@link snake.Snake snake} to be {@link 
     * snake.Snake#revive revived}.
     * @see snake.Snake
     * @see snake.Snake#doCommand
     * @see snake.Snake#revive 
     */
    REVIVE,
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#removeTail remove its tail}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#removeTail 
     */
    REMOVE_TAIL,
    /**
     * This is the instruction for a {@link Snake snake} to {@link 
     * Snake#doDefaultAction invoke} its {@link Snake#getDefaultAction default 
     * action}.
     * @see Snake
     * @see Snake#doCommand
     * @see Snake#doDefaultAction 
     * @see Snake#getDefaultAction 
     */
    DEFAULT_ACTION;
    /**
     * This stores an integer representing the direction associated with this 
     * command. This may be null if no direction is associated with this 
     * command. If this is zero, then this command is associated with the 
     * forward direction.
     * @since 1.1.0
     */
    private final Integer direction;
    /**
     * This constructs a {@code SnakeCommand} with the given direction.
     * @param direction The direction that this command is to be associated 
     * with ({@code 0} for forward), or null.
     * @since 1.1.0
     */
    private SnakeCommand(Integer direction){
        this.direction = direction;
    }
    /**
     * This constructs a {@code SnakeCommand} with no direction associated with 
     * it.
     */
    private SnakeCommand(){
        this(null);
    }
    /**
     * This returns an integer representing the direction associated with this 
     * command. If this associated with the direction a snake is facing, then 
     * this will return zero. If this command is not associated with any 
     * direction, then this will return null. Otherwise, this will return the 
     * direction flag for the direction associated with this command.
     * @return Either the direction flag associated with this command, zero if 
     * this command uses the direction a snake is facing, or null if this 
     * command has no direction associated with it.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see Snake#getDirectionFaced 
     * @since 1.1.0
     */
    public Integer getDirection(){
        return direction;
    }
    /**
     * This returns an immutable implementation of {@link SnakeActionCommand 
     * SnakeActionCommand} that will perform this command. This is equivalent 
     * to, for this {@code command}, calling {@link #getCommandActionMap 
     * getCommandActionMap()}{@code .get(command)}.
     * @return An action that performs this command.
     * @see SnakeActionCommand
     * @see Snake#doCommand 
     * @see #getCommandActionMap 
     * @since 1.1.0
     */
    public SnakeActionCommand getAction(){
        return getCommandActionMap().get(this);
    }
    /**
     * This returns an integer representing the direction associated with the 
     * given command. If the given command is associated with the direction a 
     * snake is facing, then this will return zero. If the given command is not 
     * associated with any direction, then this will return null. Otherwise, 
     * this will return the direction flag for the direction associated with the 
     * given command.
     * @param cmd The snake command to get the direction for.
     * @return Either the direction flag associated with the command, zero if 
     * the command uses the direction a snake is facing, or null if the command 
     * has no direction associated with it.
     * @see #UP_DIRECTION
     * @see #DOWN_DIRECTION
     * @see #LEFT_DIRECTION
     * @see #RIGHT_DIRECTION
     * @see Snake#getDirectionFaced 
     * @see #MOVE_UP
     * @see #MOVE_DOWN
     * @see #MOVE_LEFT
     * @see #MOVE_RIGHT
     * @see #MOVE_FORWARD
     * @see #ADD_UP
     * @see #ADD_DOWN
     * @see #ADD_LEFT
     * @see #ADD_RIGHT
     * @see #ADD_FORWARD
     * @see SnakeCommand#getDirection 
     * @deprecated Replaced with the non-static {@link SnakeCommand#getDirection 
     * SnakeCommand.getDirection} method.
     */
    @Deprecated
    public static Integer getDirectionOf(SnakeCommand cmd){
        return cmd.getDirection();
    }
    /**
     * This is a map used to map the snake commands to immutable snake command 
     * actions that perform their respective command. This is initially null, 
     * and is initialized the first time it is requested.
     */
    private static EnumMap<SnakeCommand,SnakeActionCommand> commandMap = null;
    /**
     * This is an immutable implementation of SnakeActionCommand used in the map 
     * returned by {@link #getCommandActionMap getCommandActionMap}, which maps 
     * the snake commands to default {@code SnakeActionCommands}.
     */
    private static final class SimpleSnakeActionCommand extends SnakeActionCommand{
        /**
         * This is the command for this action.
         */
        public final SnakeCommand command;
        /**
         * This constructs a SimpleSnakeActionCommand with the given command.
         * @param command The snake command for the action this performs (cannot 
         * be null).
         * @throws NullPointerException If the command is null.
         */
        SimpleSnakeActionCommand(SnakeCommand command){
            if (command == null)    // If the command is null.
                throw new NullPointerException();
            this.command = command;
        }
        /**
         * {@inheritDoc }
         */
        @Override
        public SnakeCommand getCommand() {
            return command;
        }
        /**
         * {@inheritDoc }
         */
        @Override
        public String toString(){
            return getClass().getSimpleName() + "["+paramString()+"]";
        }
    }
    /**
     * This returns an unmodifiable map that maps the commands to immutable 
     * implementations of {@link SnakeActionCommand SnakeActionCommand} which 
     * perform their respective commands.
     * @return A map of the commands and actions that will perform them.
     * @see SnakeActionCommand
     * @see Snake#doCommand 
     * @see #getAction 
     */
    public static Map<SnakeCommand,SnakeActionCommand>getCommandActionMap(){
        if (commandMap == null){    // If the map has not been initialized yet
            commandMap = new EnumMap<>(SnakeCommand.class);
                // A for loop to map a SnakeActionCommand to each command
            for (SnakeCommand cmd : SnakeCommand.values())
                commandMap.put(cmd, new SimpleSnakeActionCommand(cmd));
        }
        return Collections.unmodifiableMap(commandMap);
    }
    /**
     * This returns an immutable implementation of {@link SnakeActionCommand 
     * SnakeActionCommand} that will perform the given command. This is 
     * equivalent to invoking the {@link #getAction getAction} method on the 
     * given command. 
     * @param command The command to get the action for (cannot be null).
     * @return An action that performs the given command.
     * @throws NullPointerException If the command is null.
     * @see SnakeActionCommand
     * @see Snake#doCommand 
     * @see #getCommandActionMap 
     * @see #getAction 
     * @deprecated Replaced with the non-static {@link SnakeCommand#getAction 
     * SnakeCommand.getAction} method.
     */
    @Deprecated
    public static SnakeActionCommand getActionForCommand(SnakeCommand command){
        if (command == null)    // If the command is null
            throw new NullPointerException();
        return command.getAction();
    }
}
