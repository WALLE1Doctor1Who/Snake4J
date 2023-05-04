/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package snake;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import snake.action.*;
import snake.event.*;
import snake.icons.*;
import snake.playfield.*;

/**
 * This is an implementation of the game Snake. 
 * @author Milo Steier
 */
public class SnakeGame4J extends javax.swing.JFrame implements SnakeConstants{
    /**
     * This is the command line argument for enabling debug mode.
     */
    public static final String DEBUG_ARGUMENT = "-debug";
    /**
     * This is the path to the image to display as the icon for the program.
     */
    public static final String SNAKE4J_ICON_FILE = "/images/Snake4J Icon.png";
    /**
     * This is a string containing the version number for this version of the 
     * program.
     */
    public static final String SNAKE4J_VERSION_INFO = "1.0.0.0";
    /**
     * This creates a new SnakeGame4J with the given value for the debug mode.
     * @param debugMode Whether the game will be in debug mode.
     */
    public SnakeGame4J(boolean debugMode) {
            // This sets the icon for this program
        super.setIconImage(new ImageIcon(this.getClass()
                .getResource(SNAKE4J_ICON_FILE)).getImage());
        initComponents();   // This initializes the components for the program
        gameOverlayLayers = new JComponent[]{
            pauseMenuPanel,     // The pause menu
            gameConfigPanel,    // The new game config menu
            settingsPanel,      // The settings menu
            resultsPanel        // The results screen
        };  // Show the debug menu button only if we are in debug mode
        debugMenuButton.setVisible(debugMode);
            // Only enable the debug menu button if we are in debug mode
        debugMenuButton.setEnabled(debugMode);  
            // Don't show the pause title label right now, since we're not 
        pauseTitleLabel.setVisible(false);  // currently in game
        setVisibleGameOverlayLayer(0);// Show only the pause menu and play field
            // Create a new snake on the play field model and set its allowed 
        snake = new Snake(playField.getModel()).setAllowedFails(1);// fails to 1
            // Add a snake listener for the player one snake
        snake.addSnakeListener((SnakeEvent evt) -> {
            playerOneSnakeChanged(evt);
        });
            // Create a timer to run the gameplay loop
        timer = new Timer((Integer)delaySpinner.getValue(),(ActionEvent evt)->{
            doGamePlayLoop(evt);
        });
            // Get the input map for the play field when it's in the focused 
            // window
        InputMap inputMap = playField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            // Get the action map for the play field
        ActionMap actionMap = playField.getActionMap(); 
            // An array containing the key codes for the movement keys
        int[] keyCodes = {
            KeyEvent.VK_W,      // Key code for W
            KeyEvent.VK_A,      // Key code for A
            KeyEvent.VK_S,      // Key code for S
            KeyEvent.VK_D       // Key code for D
        };
            // An array containing the actions that coorespond to the movement 
        Action[] actions = {    // keys
            new SnakeMoveInputAction(UP_DIRECTION),     // Move Up
            new SnakeMoveInputAction(LEFT_DIRECTION),   // Move Left
            new SnakeMoveInputAction(DOWN_DIRECTION),   // Move Down
            new SnakeMoveInputAction(RIGHT_DIRECTION)   // Move Right
        };  // A for loop to add the movement actions to the play field
        for (int i = 0; i < actions.length; i++){
                // Get the name of the action to add to the play field
            String name = (String)actions[i].getValue(Action.NAME);
                // Add the key stroke for this action to the play field's input 
            inputMap.put(KeyStroke.getKeyStroke(keyCodes[i], 0), name); // map
                // Add this action to the play field's action map
            actionMap.put(name, actions[i]);    
        }   // Get the keystroke for the escape key
        KeyStroke escKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
            // An array containing the actions for the escape key for the layers 
            // and play field 
        Action[] escActions = new Action[gameOverlayLayers.length+1];
            // The play field's escape action which pauses the game
        escActions[0] = new AbstractAction("pauseGame"){
            @Override
            public void actionPerformed(ActionEvent evt) {
                setPaused(true);    // Pause the game
            }
        };   // The pause menu's escape action which unpauses the game
        escActions[1] = new AbstractAction("unpauseGame"){
            @Override
            public void actionPerformed(ActionEvent evt) {
                resumeGameButtonActionPerformed(evt);
            }
        };  // The remaining layers get the same action
        for (int i = 2; i < escActions.length; i++){
                // These layers' escape action return to the pause menu
            escActions[i] = new AbstractAction("exitMenu"){
                @Override
                public void actionPerformed(ActionEvent evt) {
                    returnToPauseMenuActionPerformed(evt);
                }
            };
        }   // A for loop to add the escape actions, starting at the play field, 
            // and then adding escape actions to the overlay layers
        for (int i = -1; i < gameOverlayLayers.length; i++){
                // Get the component to add an action to. The first is the play 
                // field, and the remaining are the overlay layers
            JComponent c = (i == -1) ? playField : gameOverlayLayers[i];
                // Get the name of the action to add to the component
            String name = (String)escActions[i+1].getValue(Action.NAME);
                // Add the esc key stroke to the component's input map
            c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStroke,name);
                // Add this action to the component's action map
            c.getActionMap().put(name, escActions[i+1]);
        }
    }
    /**
     * This creates a new SnakeGame4J that is not in debug mode.
     */
    public SnakeGame4J() {
        this(false);
    }
    /**
     * This is the method invoked to perform the gameplay loop. This method is 
     * invoked by the timer each tick.
     * @param evt The ActionEvent to process.
     * @see Snake#doNextAction 
     */
    private void doGamePlayLoop(ActionEvent evt){
        if (snake.isValid())        // If the snake is in a valid state
            snake.doNextAction();
    }
    /**
     * This is the method invoked when a change occurs to player one's snake. 
     * @param evt The SnakeEvent to process.
     */
    private void playerOneSnakeChanged(SnakeEvent evt){
            // Determine which action to perform based off the event's ID
        switch(evt.getID()){    
                // If the snake consumed an apple
            case(SnakeEvent.SNAKE_CONSUMED_APPLE):  
                setRandomApple(); // Set a random empty tile to be an apple tile
                break;
                // If a tile was added to the snake
            case(SnakeEvent.SNAKE_ADDED_TILE):
                    // If the snake does not encompass the entire play field yet
                if (snake.size() != playField.getTileCount())
                    break;  // Skip this event
            case(SnakeEvent.SNAKE_CRASHED): // If the snake crashed
                    // Set the game to be in game over, and if the snake has not 
                    // crashed, then the player has won (the snake must either 
                    // have crashed or added the last tile in the play field)
                setGameOver(!snake.isCrashed());
        }
    }
    /**
     * This sets a random empty tile in the play field to be an apple tile. If 
     * there are no more empty tiles, then this does nothing.
     */
    private void setRandomApple(){
            // Get a random empty tile in the play field
        Tile tile = playField.getRandomEmptyTile(rand);
            // If the random tile is not null (if there are any empty tiles in 
        if (tile != null)       // the play field)
            tile.setApple();    // Set the tile to be an apple tile
    }
    /**
     * This sets the currently visible game overlay layer. Only the overlay 
     * layer cooresponding to the given {@code layerIndex} will be visible after 
     * this (along with the play field). <p>
     * 
     * The overlay layer that will be made visible for a given {@code 
     * layerIndex} are as follows:  <br><br>
     * <ul>
     * <li>0: Pause Menu</li>
     * <li>1: New Game Config Menu</li>
     * <li>2: Settings Menu</li>
     * <li>3: Results Screen</li>
     * </ul> <br>
     * 
     * Any {@code layerIndex} not listed above will result in no overlay layers 
     * being visible (i.e. only the play field will be visible). 
     * 
     * @param layerIndex The index of the overlay layer to make visible. If this 
     * is not a valid index in the {@code gameOverlayLayers} array, then only 
     * the play field will be visible.
     */
    private void setVisibleGameOverlayLayer(int layerIndex){
            // A for loop to go through the overlay layers
        for (int i = 0; i < gameOverlayLayers.length; i++)
                // Only make this layer visible if it's index matches the given 
            gameOverlayLayers[i].setVisible(layerIndex == i);   // layer index
    }
    /**
     * This sets the game into its game over state and displays the results 
     * screen. 
     * @param isVictory Whether the player won the game.
     * @see #setInGameplay
     * @see #setVisibleGameOverlayLayer 
     */
    private void setGameOver(boolean isVictory){
        setInGameplay(false);           // We are no longer in gameplay
            // Show the win label if the player won
        resultWinLabel.setVisible(isVictory);
            // Show the lose label if the player lost
        resultLostLabel.setVisible(!isVictory);
            // State how long the snake managed to get
        resultLengthLabel.setText("Length: "+snake.size());
        setVisibleGameOverlayLayer(3);  // Show the results screen
    }
    /**
     * This sets whether a game is currently in progress. When this is set to 
     * {@code true}, the resume game button will be enabled, the pause menu will 
     * show "Paused" instead of the program name and version. When this is set 
     * to {@code false}, the resume game button will be disabled, the pause menu 
     * will show the program name and version instead of "Paused". This method 
     * will also set whether the game is paused to {@code !inGame}.
     * @param inGame Whether a game is currently in progress.
     * @see #isInGameplay 
     * @see #setPaused 
     * @see #isPaused 
     */
    protected void setInGameplay(boolean inGame){
        resumeGameButton.setEnabled(inGame);
        titleLabel.setVisible(!inGame);
        versionLabel.setVisible(!inGame);
        pauseTitleLabel.setVisible(inGame);
        inGameToggle.setSelected(inGame);
        setPaused(!inGame);                 // Set whether the game is paused
    }
    /**
     * This returns whether a game is currently in progress. 
     * @return Whether a game is currently in progress.
     * @see #isPaused 
     */
    protected boolean isInGameplay(){
        return resumeGameButton.isEnabled();
    }
    /**
     * This sets whether the game is paused. If {@code paused} is {@code true}, 
     * then the pause menu will be made visible. If {@code paused} is {@code 
     * false}, then only the play field will be visible.
     * @param paused Whether the game is paused.
     * @see #isPaused 
     * @see #isInGameplay 
     * @see #setInGameplay 
     */
    protected void setPaused(boolean paused){
            // If the game is now paused, show the pause menu. Otherwise, only 
        setVisibleGameOverlayLayer((paused)?0:-1);  // show the play field
        pauseToggle.setSelected(paused);
        playField.setEnabled(!paused);
        nextTickButton.setEnabled(paused);
        if (paused){                // If the game is paused
            timer.stop();           // Stop the timer
        }
        else{
            timer.restart();        // Start the timer
        }
    }
    /**
     * This returns whether the game is currently paused.
     * @return Whether the game is currently paused.
     * @see #isInGameplay 
     */
    protected boolean isPaused(){
        return !playField.isEnabled();
    }
//    /**
//     * This returns whether the game is in debug mode.
//     * @return Whether the game is in debug mode.
//     */
//    private boolean isDebugMode(){
//        return debugMenuButton.isEnabled();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        debugFrame = new javax.swing.JFrame();
        printButton = new javax.swing.JButton();
        flipSnakeButton = new javax.swing.JButton();
        reviveSnakeButton = new javax.swing.JButton();
        pauseToggle = new javax.swing.JToggleButton();
        inGameToggle = new javax.swing.JToggleButton();
        winTestButton = new javax.swing.JButton();
        loseTestButton = new javax.swing.JButton();
        // The label for the show layer combo
        javax.swing.JLabel showLayerLabel = new javax.swing.JLabel();
        showLayerCombo = new javax.swing.JComboBox<>();
        nextTickButton = new javax.swing.JButton();
        // The label for the save layer combo box
        javax.swing.JLabel saveLayerLabel = new javax.swing.JLabel();
        saveLayerCombo = new javax.swing.JComboBox<>();
        saveLayerButton = new javax.swing.JButton();
        fieldOpaqueToggle = new javax.swing.JCheckBox();
        addAppleButton = new javax.swing.JButton();
        inftyFailsButton = new javax.swing.JButton();
        fc = new javax.swing.JFileChooser();
        layeredPane = new javax.swing.JLayeredPane();
        playField = new snake.JPlayField();
        pauseMenuPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        pauseTitleLabel = new javax.swing.JLabel();
        // A filler object to distance the title labels and the buttons
        javax.swing.Box.Filler titleButtonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        // A filler object to add a gap between the buttons and the left side of
        // the panel
        javax.swing.Box.Filler buttonLeftFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        pauseButtonsPanel = new javax.swing.JPanel();
        startGameButton = new javax.swing.JButton();
        resumeGameButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        debugMenuButton = new javax.swing.JButton();
        // A filler object to add a gap between the buttons and the right side of
        // the panel
        javax.swing.Box.Filler buttonRightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        // A filler object to fill the space between the pause buttons and the
        // controls
        javax.swing.Box.Filler buttonCtrlFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        ctrlPanel = new javax.swing.JPanel();
        ctrlTitleLabel = new javax.swing.JLabel();
        p1CtrlIconLabel = new javax.swing.JLabel();
        p1CtrlTextLabel = new javax.swing.JLabel();
        pauseCtrlIconLabel = new javax.swing.JLabel();
        pauseCtrlTextLabel = new javax.swing.JLabel();
        // A filler object to fill the space between the controls and the
        // bottom of the pause menu panel
        javax.swing.Box.Filler ctrlBottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        gameConfigPanel = new javax.swing.JPanel();
        gameConfigTitleLabel = new javax.swing.JLabel();
        // This is a filler object to add spacing between the config title
        // and the components used to configure the game
        javax.swing.Box.Filler configTitleCompFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        // A filler object to move the config components over to the right
        javax.swing.Box.Filler configLeftFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        // A filler object to move the config components over to the left
        javax.swing.Box.Filler configRightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        gameSettingsPanel = new javax.swing.JPanel();
        allowedFailsLabel = new javax.swing.JLabel();
        allowedFailsSpinner = new javax.swing.JSpinner();
        rLabel = new javax.swing.JLabel();
        cLabel = new javax.swing.JLabel();
        cSpinner = new javax.swing.JSpinner();
        rSpinner = new javax.swing.JSpinner();
        delayLabel = new javax.swing.JLabel();
        delaySpinner = new javax.swing.JSpinner();
        wrapAroundToggle = new javax.swing.JToggleButton();
        // A filler object to space the config objects from the buttons
        javax.swing.Box.Filler configSettingButtonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        gameSettingsStartButton = new javax.swing.JButton();
        gameSettingsCancelButton = new javax.swing.JButton();
        gameSettingsResetButton = new javax.swing.JButton();
        // A filler object to add space between the config buttons and the bottom
        // of the config panel
        javax.swing.Box.Filler configBottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        settingsPanel = new javax.swing.JPanel();
        settingsTitleLabel = new javax.swing.JLabel();
        // A filler object to separate the settings title from the settings
        // components
        javax.swing.Box.Filler settingsTitleFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        // A filler object to move the settings components over to the right
        javax.swing.Box.Filler settingsLeftFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        // A filler object to move the settings components over to the left
        javax.swing.Box.Filler settingsRightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        settingCompPanel = new javax.swing.JPanel();
        hqToggle = new javax.swing.JToggleButton();
        // A filler object to separate the settings objects from the buttons
        javax.swing.Box.Filler settingsButtonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        settingsDoneButton = new javax.swing.JButton();
        settingsResetButton = new javax.swing.JButton();
        // A filler object to move the settings components upwards
        javax.swing.Box.Filler settingsBottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        resultsPanel = new javax.swing.JPanel();
        resultsTitleLabel = new javax.swing.JLabel();
        // A filler object to separate the results title from the results labels
        javax.swing.Box.Filler resultsTitleFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        // A filler object to move the results components over to the right
        javax.swing.Box.Filler resultsLeftFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        // A filler object to move the results components over to the left
        javax.swing.Box.Filler resultsRightFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        resultWinLabel = new javax.swing.JLabel();
        resultLostLabel = new javax.swing.JLabel();
        resultLengthLabel = new javax.swing.JLabel();
        // A filler object to separate the results labels from the buttons
        javax.swing.Box.Filler resultsButtonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        resultMenuButton = new javax.swing.JButton();
        resultStartGameButton = new javax.swing.JButton();
        // A filler object to move the results components upwards
        javax.swing.Box.Filler resultsBottomFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        debugFrame.setTitle("Snake! Snake! SNAKE!!!");
        debugFrame.setMinimumSize(new java.awt.Dimension(400, 300));

        printButton.setText("Print Data");
        printButton.setToolTipText("Prints debug data to console.");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        flipSnakeButton.setText("Flip Snake");
        flipSnakeButton.setToolTipText("Flips the snake.");
        flipSnakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipSnakeButtonActionPerformed(evt);
            }
        });

        reviveSnakeButton.setText("Revive Snake");
        reviveSnakeButton.setToolTipText("Revives the snake.");
        reviveSnakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviveSnakeButtonActionPerformed(evt);
            }
        });

        pauseToggle.setText("Pause Game");
        pauseToggle.setToolTipText("Toggles whether the game is paused.");
        pauseToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseToggleActionPerformed(evt);
            }
        });

        inGameToggle.setText("In Gameplay");
        inGameToggle.setToolTipText("Toggles whether we are currently in gameplay.");
        inGameToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inGameToggleActionPerformed(evt);
            }
        });

        winTestButton.setText("Win");
        winTestButton.setToolTipText("Pressing this wins the game.");
        winTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                winTestButtonActionPerformed(evt);
            }
        });

        loseTestButton.setText("Lose");
        loseTestButton.setToolTipText("Pressing this loses the game.");
        loseTestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loseTestButtonActionPerformed(evt);
            }
        });

        showLayerLabel.setLabelFor(showLayerCombo);
        showLayerLabel.setText("Show Overlay Layer:");

        showLayerCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Pause", "Game Config", "Settings", "Results" }));
        showLayerCombo.setToolTipText("Sets which overlay layer to show");
        showLayerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showLayerComboActionPerformed(evt);
            }
        });

        nextTickButton.setText("Next Tick");
        nextTickButton.setToolTipText("This forwards the game by one tick.");
        nextTickButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextTickButtonActionPerformed(evt);
            }
        });

        saveLayerLabel.setLabelFor(saveLayerCombo);
        saveLayerLabel.setText("Save Layer:");

        saveLayerCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Visible", "Play Field", "Pause", "Game Config", "Settings", "Results" }));
        saveLayerCombo.setToolTipText("This selects which layer to save to a file.");

        saveLayerButton.setText("Save Layer");
        saveLayerButton.setToolTipText("This saves the layer(s) currently selected");
        saveLayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveLayerButtonActionPerformed(evt);
            }
        });

        fieldOpaqueToggle.setSelected(true);
        fieldOpaqueToggle.setText("Field Is Opaque");
        fieldOpaqueToggle.setToolTipText("This toggles whether the play field is opaque.");
        fieldOpaqueToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldOpaqueToggleActionPerformed(evt);
            }
        });

        addAppleButton.setText("Add Apple");
        addAppleButton.setToolTipText("This sets a random empty tile to be an apple tile.");
        addAppleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAppleButtonActionPerformed(evt);
            }
        });

        inftyFailsButton.setText("Grant Infinite Grace Turns");
        inftyFailsButton.setToolTipText("Grants infinite lives.");
        inftyFailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inftyFailsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout debugFrameLayout = new javax.swing.GroupLayout(debugFrame.getContentPane());
        debugFrame.getContentPane().setLayout(debugFrameLayout);
        debugFrameLayout.setHorizontalGroup(
            debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(debugFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(debugFrameLayout.createSequentialGroup()
                        .addComponent(printButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pauseToggle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inGameToggle))
                    .addGroup(debugFrameLayout.createSequentialGroup()
                        .addComponent(inftyFailsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flipSnakeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reviveSnakeButton))
                    .addGroup(debugFrameLayout.createSequentialGroup()
                        .addComponent(winTestButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loseTestButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextTickButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldOpaqueToggle))
                    .addGroup(debugFrameLayout.createSequentialGroup()
                        .addComponent(saveLayerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveLayerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveLayerButton))
                    .addGroup(debugFrameLayout.createSequentialGroup()
                        .addComponent(showLayerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showLayerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addAppleButton)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        debugFrameLayout.setVerticalGroup(
            debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(debugFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printButton)
                    .addComponent(pauseToggle)
                    .addComponent(inGameToggle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flipSnakeButton)
                    .addComponent(reviveSnakeButton)
                    .addComponent(inftyFailsButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(winTestButton)
                    .addComponent(loseTestButton)
                    .addComponent(nextTickButton)
                    .addComponent(fieldOpaqueToggle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(showLayerLabel)
                        .addComponent(showLayerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addAppleButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(debugFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveLayerLabel)
                    .addComponent(saveLayerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveLayerButton))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        fc.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Snake4J");
        setMinimumSize(new java.awt.Dimension(420, 340));

        layeredPane.setLayout(new javax.swing.OverlayLayout(layeredPane));

        playField.setColumnCount(16);
        playField.setRowCount(16);
        playField.setEnabled(false);
        playField.setOpaque(true);

        javax.swing.GroupLayout playFieldLayout = new javax.swing.GroupLayout(playField);
        playField.setLayout(playFieldLayout);
        playFieldLayout.setHorizontalGroup(
            playFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        playFieldLayout.setVerticalGroup(
            playFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        layeredPane.add(playField);

        pauseMenuPanel.setFont(pauseMenuPanel.getFont().deriveFont(pauseMenuPanel.getFont().getStyle() | java.awt.Font.BOLD, pauseMenuPanel.getFont().getSize()+4));
        pauseMenuPanel.setOpaque(false);
        pauseMenuPanel.setLayout(new java.awt.GridBagLayout());

        titleLabel.setFont(pauseMenuPanel.getFont());
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Snake4J By Milo Steier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        pauseMenuPanel.add(titleLabel, gridBagConstraints);

        versionLabel.setFont(pauseMenuPanel.getFont());
        versionLabel.setForeground(new java.awt.Color(255, 255, 255));
        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionLabel.setText("Version "+SNAKE4J_VERSION_INFO);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 12);
        pauseMenuPanel.add(versionLabel, gridBagConstraints);

        pauseTitleLabel.setFont(pauseMenuPanel.getFont());
        pauseTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        pauseTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pauseTitleLabel.setText("Paused");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        pauseMenuPanel.add(pauseTitleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        pauseMenuPanel.add(titleButtonFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        pauseMenuPanel.add(buttonLeftFiller, gridBagConstraints);

        pauseButtonsPanel.setOpaque(false);
        pauseButtonsPanel.setLayout(new java.awt.GridBagLayout());

        startGameButton.setFont(pauseMenuPanel.getFont());
        startGameButton.setText("Start New Game");
        startGameButton.setOpaque(false);
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.5;
        pauseButtonsPanel.add(startGameButton, gridBagConstraints);

        resumeGameButton.setFont(pauseMenuPanel.getFont());
        resumeGameButton.setText("Resume Game");
        resumeGameButton.setEnabled(false);
        resumeGameButton.setOpaque(false);
        resumeGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeGameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        pauseButtonsPanel.add(resumeGameButton, gridBagConstraints);

        settingsButton.setFont(pauseMenuPanel.getFont());
        settingsButton.setText("Settings");
        settingsButton.setOpaque(false);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pauseButtonsPanel.add(settingsButton, gridBagConstraints);

        debugMenuButton.setFont(pauseMenuPanel.getFont());
        debugMenuButton.setText("Debug Menu");
        debugMenuButton.setOpaque(false);
        debugMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugMenuButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        pauseButtonsPanel.add(debugMenuButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        pauseMenuPanel.add(pauseButtonsPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.25;
        pauseMenuPanel.add(buttonRightFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.25;
        pauseMenuPanel.add(buttonCtrlFiller, gridBagConstraints);

        ctrlPanel.setOpaque(false);
        ctrlPanel.setLayout(new java.awt.GridBagLayout());

        ctrlTitleLabel.setFont(pauseMenuPanel.getFont());
        ctrlTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        ctrlTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ctrlTitleLabel.setText("Controls:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        ctrlPanel.add(ctrlTitleLabel, gridBagConstraints);

        p1CtrlIconLabel.setFont(pauseMenuPanel.getFont());
        p1CtrlIconLabel.setIcon(new WASDControlIcon());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        ctrlPanel.add(p1CtrlIconLabel, gridBagConstraints);

        p1CtrlTextLabel.setFont(pauseMenuPanel.getFont());
        p1CtrlTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        p1CtrlTextLabel.setText("- Move the snake");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        ctrlPanel.add(p1CtrlTextLabel, gridBagConstraints);

        pauseCtrlIconLabel.setFont(pauseMenuPanel.getFont());
        pauseCtrlIconLabel.setIcon(new EscControlIcon());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        ctrlPanel.add(pauseCtrlIconLabel, gridBagConstraints);

        pauseCtrlTextLabel.setFont(pauseMenuPanel.getFont());
        pauseCtrlTextLabel.setForeground(new java.awt.Color(255, 255, 255));
        pauseCtrlTextLabel.setText("- Pause Game");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        ctrlPanel.add(pauseCtrlTextLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.9;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 13, 12);
        pauseMenuPanel.add(ctrlPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 0.5;
        pauseMenuPanel.add(ctrlBottomFiller, gridBagConstraints);

        layeredPane.setLayer(pauseMenuPanel, 1);
        layeredPane.add(pauseMenuPanel);

        gameConfigPanel.setFont(pauseMenuPanel.getFont());
        gameConfigPanel.setOpaque(false);
        gameConfigPanel.setLayout(new java.awt.GridBagLayout());

        gameConfigTitleLabel.setFont(gameConfigPanel.getFont());
        gameConfigTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        gameConfigTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gameConfigTitleLabel.setLabelFor(rSpinner);
        gameConfigTitleLabel.setText("New Game Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        gameConfigPanel.add(gameConfigTitleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        gameConfigPanel.add(configTitleCompFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gameConfigPanel.add(configLeftFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gameConfigPanel.add(configRightFiller, gridBagConstraints);

        gameSettingsPanel.setOpaque(false);
        gameSettingsPanel.setLayout(new java.awt.GridBagLayout());

        allowedFailsLabel.setFont(gameConfigPanel.getFont());
        allowedFailsLabel.setForeground(new java.awt.Color(255, 255, 255));
        allowedFailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        allowedFailsLabel.setLabelFor(allowedFailsSpinner);
        allowedFailsLabel.setText("Grace Turns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gameSettingsPanel.add(allowedFailsLabel, gridBagConstraints);

        allowedFailsSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, 20, 1));
        allowedFailsSpinner.setToolTipText("The number of grace turns available to save yourself (Default is 1)");
        allowedFailsSpinner.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        gameSettingsPanel.add(allowedFailsSpinner, gridBagConstraints);

        rLabel.setFont(gameConfigPanel.getFont());
        rLabel.setForeground(new java.awt.Color(255, 255, 255));
        rLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        rLabel.setLabelFor(rSpinner);
        rLabel.setText("Rows:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gameSettingsPanel.add(rLabel, gridBagConstraints);

        cLabel.setFont(gameConfigPanel.getFont());
        cLabel.setForeground(new java.awt.Color(255, 255, 255));
        cLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cLabel.setLabelFor(cSpinner);
        cLabel.setText("Columns:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gameSettingsPanel.add(cLabel, gridBagConstraints);

        cSpinner.setModel(new javax.swing.SpinnerNumberModel(playField.getColumnCount(), DefaultPlayFieldModel.MINIMUM_COLUMN_COUNT, DefaultPlayFieldModel.MAXIMUM_COLUMN_COUNT, 1));
        cSpinner.setToolTipText("The number of columns (Default is 16)");
        cSpinner.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        gameSettingsPanel.add(cSpinner, gridBagConstraints);

        rSpinner.setModel(new javax.swing.SpinnerNumberModel(playField.getRowCount(), DefaultPlayFieldModel.MINIMUM_ROW_COUNT, DefaultPlayFieldModel.MAXIMUM_ROW_COUNT, 1));
        rSpinner.setToolTipText("The number of rows (Default is 16)");
        rSpinner.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        gameSettingsPanel.add(rSpinner, gridBagConstraints);

        delayLabel.setFont(gameConfigPanel.getFont());
        delayLabel.setForeground(new java.awt.Color(255, 255, 255));
        delayLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        delayLabel.setLabelFor(delaySpinner);
        delayLabel.setText("Tick Speed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gameSettingsPanel.add(delayLabel, gridBagConstraints);

        delaySpinner.setModel(new javax.swing.SpinnerNumberModel(120, 1, 1000, 1));
        delaySpinner.setToolTipText("The time between ticks, in milliseconds (Default is 120ms)");
        delaySpinner.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        gameSettingsPanel.add(delaySpinner, gridBagConstraints);

        wrapAroundToggle.setFont(gameConfigPanel.getFont());
        wrapAroundToggle.setSelected(true);
        wrapAroundToggle.setText("Snake Wraps Around");
        wrapAroundToggle.setToolTipText("Whether the snake is allowed to wrap around when it reaches the edge of the play field (Default is selected)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        gameSettingsPanel.add(wrapAroundToggle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        gameConfigPanel.add(gameSettingsPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        gameConfigPanel.add(configSettingButtonFiller, gridBagConstraints);

        gameSettingsStartButton.setFont(gameConfigPanel.getFont());
        gameSettingsStartButton.setText("Start");
        gameSettingsStartButton.setOpaque(false);
        gameSettingsStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameSettingsStartButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        gameConfigPanel.add(gameSettingsStartButton, gridBagConstraints);

        gameSettingsCancelButton.setFont(gameConfigPanel.getFont());
        gameSettingsCancelButton.setText("Cancel");
        gameSettingsCancelButton.setOpaque(false);
        gameSettingsCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToPauseMenuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 12);
        gameConfigPanel.add(gameSettingsCancelButton, gridBagConstraints);

        gameSettingsResetButton.setFont(gameConfigPanel.getFont());
        gameSettingsResetButton.setText("Reset");
        gameSettingsResetButton.setToolTipText("This will reset the game settings to their default values.");
        gameSettingsResetButton.setOpaque(false);
        gameSettingsResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameSettingsResetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 13, 12);
        gameConfigPanel.add(gameSettingsResetButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        gameConfigPanel.add(configBottomFiller, gridBagConstraints);

        layeredPane.setLayer(gameConfigPanel, 2);
        layeredPane.add(gameConfigPanel);

        settingsPanel.setFont(pauseMenuPanel.getFont());
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new java.awt.GridBagLayout());

        settingsTitleLabel.setFont(settingsPanel.getFont());
        settingsTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        settingsTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        settingsTitleLabel.setText("Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        settingsPanel.add(settingsTitleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        settingsPanel.add(settingsTitleFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        settingsPanel.add(settingsLeftFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        settingsPanel.add(settingsRightFiller, gridBagConstraints);

        settingCompPanel.setOpaque(false);
        settingCompPanel.setLayout(new javax.swing.BoxLayout(settingCompPanel, javax.swing.BoxLayout.LINE_AXIS));

        hqToggle.setFont(settingsPanel.getFont());
        hqToggle.setSelected(playField.isHighQuality());
        hqToggle.setText("Render in High Quality");
        hqToggle.setToolTipText("Whether the game will be rendered in high quality (Default is selected)");
        hqToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hqToggleActionPerformed(evt);
            }
        });
        settingCompPanel.add(hqToggle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 13, 12);
        settingsPanel.add(settingCompPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        settingsPanel.add(settingsButtonFiller, gridBagConstraints);

        settingsDoneButton.setFont(settingsPanel.getFont());
        settingsDoneButton.setText("Done");
        settingsDoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToPauseMenuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 13, 0);
        settingsPanel.add(settingsDoneButton, gridBagConstraints);

        settingsResetButton.setFont(settingsPanel.getFont());
        settingsResetButton.setText("Reset");
        settingsResetButton.setToolTipText("This will reset the settings back to their default values.");
        settingsResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsResetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 13, 12);
        settingsPanel.add(settingsResetButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        settingsPanel.add(settingsBottomFiller, gridBagConstraints);

        layeredPane.setLayer(settingsPanel, 3);
        layeredPane.add(settingsPanel);

        resultsPanel.setFont(pauseMenuPanel.getFont());
        resultsPanel.setOpaque(false);
        resultsPanel.setLayout(new java.awt.GridBagLayout());

        resultsTitleLabel.setFont(resultsPanel.getFont());
        resultsTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        resultsTitleLabel.setText("Results");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 13, 12);
        resultsPanel.add(resultsTitleLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        resultsPanel.add(resultsTitleFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        resultsPanel.add(resultsLeftFiller, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        resultsPanel.add(resultsRightFiller, gridBagConstraints);

        resultWinLabel.setFont(resultsPanel.getFont());
        resultWinLabel.setForeground(new java.awt.Color(255, 255, 255));
        resultWinLabel.setText("Congratulations! You win!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        resultsPanel.add(resultWinLabel, gridBagConstraints);

        resultLostLabel.setFont(resultsPanel.getFont());
        resultLostLabel.setForeground(new java.awt.Color(255, 255, 255));
        resultLostLabel.setText("Sorry, you lost.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
        resultsPanel.add(resultLostLabel, gridBagConstraints);

        resultLengthLabel.setFont(resultsPanel.getFont());
        resultLengthLabel.setForeground(new java.awt.Color(255, 255, 255));
        resultLengthLabel.setText("Length: 0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 12);
        resultsPanel.add(resultLengthLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.25;
        resultsPanel.add(resultsButtonFiller, gridBagConstraints);

        resultMenuButton.setFont(resultsPanel.getFont());
        resultMenuButton.setText("Return To Main Menu");
        resultMenuButton.setOpaque(false);
        resultMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToPauseMenuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 0, 12);
        resultsPanel.add(resultMenuButton, gridBagConstraints);

        resultStartGameButton.setFont(resultsPanel.getFont());
        resultStartGameButton.setText("Start New Game");
        resultStartGameButton.setOpaque(false);
        resultStartGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 13, 12);
        resultsPanel.add(resultStartGameButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        resultsPanel.add(resultsBottomFiller, gridBagConstraints);

        layeredPane.setLayer(resultsPanel, 4);
        layeredPane.add(resultsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * This configures a new game based of the current settings and starts the 
     * new game.
     * @param evt The ActionEvent to process.
     */
    private void gameSettingsStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameSettingsStartButtonActionPerformed
            // Set the rows and columns
        playField.setRowCount((Integer) rSpinner.getValue());
        playField.setColumnCount((Integer) cSpinner.getValue());
            // Get the delay for the timer
        int delay = (Integer) delaySpinner.getValue();
        timer.setInitialDelay(delay);   // Set the initial delay
        timer.setDelay(delay);          // Set the interval delay
            // Set whether the snake can wrap around based off user input
        snake.setWrapAroundEnabled(wrapAroundToggle.isSelected());
            // Set the snake's allowed number of failures 
        snake.setAllowedFails((Integer)allowedFailsSpinner.getValue());
        playField.clearTiles();         // Clear the play field
            // Initialize the snake to be roughly in the middle of the play field
        snake.initialize(Math.floorDiv(playField.getRowCount(), 2), 
                Math.floorDiv(playField.getColumnCount(), 2));
            // Set a random empty tile to be an apple tile
        playField.getRandomEmptyTile(rand).setApple();
        setInGameplay(true);    // We are now in game play
    }//GEN-LAST:event_gameSettingsStartButtonActionPerformed
    /**
     * This enters the settings menu from the pause menu.
     * @param evt The ActionEvent to process.
     */
    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        setVisibleGameOverlayLayer(2);  // Show the pause menu
    }//GEN-LAST:event_settingsButtonActionPerformed
    /**
     * This shows the menu for configuring the game before starting it.
     * @param evt The ActionEvent to process.
     */
    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        rSpinner.setValue(playField.getRowCount());
        cSpinner.setValue(playField.getColumnCount());
        delaySpinner.setValue(timer.getDelay());
        wrapAroundToggle.setSelected(snake.isWrapAroundEnabled());
        allowedFailsSpinner.setValue(snake.getAllowedFails());
        setVisibleGameOverlayLayer(1);  // Show the start game menu
    }//GEN-LAST:event_startGameButtonActionPerformed
    /**
     * This unpauses and resumes any game that is currently in progress if the 
     * game is in gameplay.
     * @param evt The ActionEvent to process.
     */
    private void resumeGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeGameButtonActionPerformed
        if (isInGameplay())     // If we are in gameplay
            setPaused(false);   // Unpause the game
    }//GEN-LAST:event_resumeGameButtonActionPerformed
    /**
     * This shows the debug menu.
     * @param evt The ActionEvent to process.
     */
    private void debugMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugMenuButtonActionPerformed
        debugFrame.setVisible(true);
    }//GEN-LAST:event_debugMenuButtonActionPerformed
    /**
     * This prints a bunch of debug data to the console.
     * @param evt The ActionEvent to process.
     */
    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        System.out.println("Snake: " + snake);
        System.out.println("Snake is Valid: " + snake.isValid());
        System.out.println("Snake Crashed: " + snake.isCrashed());
        System.out.println("Snake Can Wrap Around: " + snake.isWrapAroundEnabled());
        System.out.println("Snake Allowed Fails: " + snake.getAllowedFails());
        System.out.println("Snake Fail Count: " + snake.getFailCount());
        System.out.println("Snake Length: " + snake.size());
        System.out.println("Total Amount of Tiles: " + playField.getTileCount());
        System.out.println("Action Queue: " + snake.getActionQueue());
        System.out.println("Snake's next action: " + snake.getActionQueue().peekNext());
        System.out.println("Play Field Size: " + playField.getPlayFieldBounds());
        System.out.println("Empty Tiles: " + playField.getEmptyTiles());
        System.out.println("Empty Tile Count: " + playField.getEmptyTileCount());
        System.out.println("Timer Delay: " + timer.getDelay() + "ms");
    }//GEN-LAST:event_printButtonActionPerformed
    /**
     * This returns to the pause menu from whatever layer is currently being 
     * displayed.
     * @param evt The ActionEvent to process.
     */
    private void returnToPauseMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToPauseMenuActionPerformed
        setVisibleGameOverlayLayer(0);
    }//GEN-LAST:event_returnToPauseMenuActionPerformed
    /**
     * This resets the game configuration to the default configuration.
     * @param evt The ActionEvent to process.
     */
    private void gameSettingsResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameSettingsResetButtonActionPerformed
        rSpinner.setValue(16);
        cSpinner.setValue(16);
        delaySpinner.setValue(120);
        wrapAroundToggle.setSelected(true);
        allowedFailsSpinner.setValue(1);
    }//GEN-LAST:event_gameSettingsResetButtonActionPerformed
    /**
     * This resets the settings to the default settings.
     * @param evt The ActionEvent to process.
     */
    private void settingsResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsResetButtonActionPerformed
            // Get the high quality value from the renderer
        Boolean hq = playField.getRenderer().isHighQuality(playField);
            // Set if the toggle is selected if the high quality value is 
        hqToggle.setSelected(hq != null && hq); // non-null and true.
        playField.setHighQuality(hqToggle.isSelected());
    }//GEN-LAST:event_settingsResetButtonActionPerformed
    /**
     * This sets whether the game renders in high quality.
     * @param evt The ActionEvent to process.
     */
    private void hqToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hqToggleActionPerformed
        playField.setHighQuality(hqToggle.isSelected());
    }//GEN-LAST:event_hqToggleActionPerformed
    /**
     * This flips the snake from the debug menu.
     * @param evt The ActionEvent to process.
     */
    private void flipSnakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flipSnakeButtonActionPerformed
        if (snake.isValid())    // If the snake is in a valid state
            snake.flip();       // Flip the snake
    }//GEN-LAST:event_flipSnakeButtonActionPerformed
    /**
     * This revives the snake from the debug menu.
     * @param evt The ActionEvent to process.
     */
    private void reviveSnakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviveSnakeButtonActionPerformed
        if (snake.isValid())    // If the snake is in a valid state
            snake.revive();     // Revive the snake
    }//GEN-LAST:event_reviveSnakeButtonActionPerformed
    /**
     * This forces the game into a win senario.
     * @param evt The ActionEvent to process.
     */
    private void winTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_winTestButtonActionPerformed
        setGameOver(true);      // Go into game over with a victory
    }//GEN-LAST:event_winTestButtonActionPerformed
    /**
     * This forces the game into a lose senario.
     * @param evt The ActionEvent to process.
     */
    private void loseTestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loseTestButtonActionPerformed
        setGameOver(false);     // Go into game over with a defeat
    }//GEN-LAST:event_loseTestButtonActionPerformed
    /**
     * This toggles whether the game is paused from the debug menu.
     * @param evt The ActionEvent to process.
     */
    private void pauseToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseToggleActionPerformed
        setPaused(pauseToggle.isSelected());
    }//GEN-LAST:event_pauseToggleActionPerformed
    /**
     * This toggles whether a game is in progress from the debug menu.
     * @param evt The ActionEvent to process.
     */
    private void inGameToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inGameToggleActionPerformed
        setInGameplay(inGameToggle.isSelected());
    }//GEN-LAST:event_inGameToggleActionPerformed
    /**
     * This sets which overlay layer to show over the play field.
     * @param evt The ActionEvent to process.
     */
    private void showLayerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showLayerComboActionPerformed
            // Offset everything by one, so that the first item turns off the 
            // overlays, and the others coorespond with their appropriate 
        setVisibleGameOverlayLayer(showLayerCombo.getSelectedIndex()-1);//indexes
    }//GEN-LAST:event_showLayerComboActionPerformed
    /**
     * This forwards the game by one tick.
     * @param evt The ActionEvent to process.
     */
    private void nextTickButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextTickButtonActionPerformed
        doGamePlayLoop(evt);    // Run the next loop
    }//GEN-LAST:event_nextTickButtonActionPerformed
    /**
     * This saves the currently selected layer(s) to a file.
     * @param evt The ActionEvent to process.
     */
    private void saveLayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveLayerButtonActionPerformed
            //Open the save file dialog and see if the user wants to save a file
        if (fc.showSaveDialog(debugFrame) == JFileChooser.APPROVE_OPTION){
                // Get the file the user selected
            File file = fc.getSelectedFile();
                // If the file name does not end with the PNG file extension
            if (!file.toString().toLowerCase().endsWith(".png")){
                    // Apppend the PNG file extension to the end
                file = new File(file.toString()+".png");
                fc.setSelectedFile(file);
            }   // Create a buffered image to render the layer(s) to
            BufferedImage img = new BufferedImage(layeredPane.getWidth(),
                    layeredPane.getHeight(),BufferedImage.TYPE_INT_ARGB);
                // Create a graphics object to render to the image
            Graphics2D g = img.createGraphics();
                // Get the index of the selected layer, offset by two
            int layerIndex = saveLayerCombo.getSelectedIndex()-2;
            Component c;    // The layer to save
            switch (layerIndex){    // Determine which layer to save
                case(-2):           // If the index is -2 (all visible layers)
                    c = layeredPane;// Save the entire layered pane
                    break;
                case(-1):           // If the index is -1 (the play field)
                    c = playField;  // Save the play field
                    break;
                default:            // Get the overlay layer to save
                    c = gameOverlayLayers[layerIndex];  
            }
            c.paintAll(g);          // Render the component to the image
            g.dispose();            // Dispose of the graphics context
            try {   // Save the file
                ImageIO.write(img, "png", file);
                JOptionPane.showMessageDialog(debugFrame,
                        "The file was saved successfully.",
                        "File Saved Successfully",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                System.out.println("Error: " +ex);
                JOptionPane.showMessageDialog(debugFrame,
                        "The file failed to save:\n"+ex,
                        "Failed to Save File",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveLayerButtonActionPerformed
    /**
     * This sets whether the field is opaque.
     * @param evt The ActionEvent to process.
     */
    private void fieldOpaqueToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldOpaqueToggleActionPerformed
        playField.setOpaque(fieldOpaqueToggle.isSelected());
    }//GEN-LAST:event_fieldOpaqueToggleActionPerformed
    /**
     * This sets a random empty tile in the play field to be an apple tile. If 
     * there are no more empty tiles, then this does nothing.
     * @param evt The ActionEvent to process.
     * @see #setRandomApple 
     */
    private void addAppleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAppleButtonActionPerformed
        setRandomApple();       // Set a random tile to be an apple
    }//GEN-LAST:event_addAppleButtonActionPerformed
    /**
     * This sets the allowed number of failures for the snake to be negative, so 
     * that the snake has infinite lives.
     * @param evt The ActionEvent to process.
     */
    private void inftyFailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inftyFailsButtonActionPerformed
            // Set it to -1 to make the snake unable to crash
        snake.setAllowedFails(-1);  
    }//GEN-LAST:event_inftyFailsButtonActionPerformed
    /**
     * This runs the main snake game program.
     * @param args The command line arguments.
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {   // A for loop to find the Nimbus LnF
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    // If the Nimbus LnF has been found
                if ("Nimbus".equals(info.getName())) {  
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SnakeGame4J.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
                // Check to see  whether we should be in debug mode or not, 
                // first by getting whether there are any command line arguments
            boolean debug = args != null && args.length > 0;
            if (debug)  // If there are any command line arguments, get whether 
                        // the debug argument is amongst the arguments
                debug = Arrays.asList(args).contains(DEBUG_ARGUMENT);
            new SnakeGame4J(debug).setVisible(true);
        });
    }
    /**
     * This is a random number generator to generate random numbers for the 
     * game.
     */
    private Random rand = new Random();
    /**
     * This is the snake for player one.
     */
    private Snake snake;
    /**
     * This is the timer that runs the game loop.
     */
    private Timer timer;
    /**
     * This is an array storing the layers that make up the UI of the game 
     * snake. They are stored in this order: <br><br>
     * <ul>
     * <li>0: Pause Menu</li>
     * <li>1: New Game Config Menu</li>
     * <li>2: Settings Menu</li>
     * <li>3: Results Screen</li>
     * </ul>
     */
    private JComponent[] gameOverlayLayers;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    /**
    * This is a button for setting a random empty tile to be an apple tile.
    */
    private javax.swing.JButton addAppleButton;
    /**
    * This is the label for the allowed fails spinner.
    */
    private javax.swing.JLabel allowedFailsLabel;
    /**
    * This is the spinner used to set the allowed number of failures the
    * snake can make before it crashes.
    */
    private javax.swing.JSpinner allowedFailsSpinner;
    /**
    * This is the label for the spinner to set the number of columns.
    */
    private javax.swing.JLabel cLabel;
    /**
    * The spinner for setting how many columns there will be in the play field.
    */
    private javax.swing.JSpinner cSpinner;
    /**
    * The panel containing the labels that show the controls.
    */
    private javax.swing.JPanel ctrlPanel;
    /**
    * This is the label displaying the title for the controls.
    */
    private javax.swing.JLabel ctrlTitleLabel;
    /**
    * This is a frame showing the debug controls to help with debugging
    * the program.
    */
    private javax.swing.JFrame debugFrame;
    /**
    * The button for accessing the debug menus. This button is only visible
    * in debug mode.
    */
    private javax.swing.JButton debugMenuButton;
    /**
    * This is the label for the spinner that sets the delay between
    * game ticks.
    */
    private javax.swing.JLabel delayLabel;
    /**
    * The spinner for setting how many milliseconds to wait between game ticks.
    */
    private javax.swing.JSpinner delaySpinner;
    /**
    * A file chooser for saving debug information.
    */
    private javax.swing.JFileChooser fc;
    /**
    * This is a check box for toggling whether the play field is opaque.
    */
    private javax.swing.JCheckBox fieldOpaqueToggle;
    /**
    * This is a button to flip the snake.
    */
    private javax.swing.JButton flipSnakeButton;
    /**
    * The panel for configuring a new game of snake.
    */
    private javax.swing.JPanel gameConfigPanel;
    /**
    * The label displaying the title for the game settings menu.
    */
    private javax.swing.JLabel gameConfigTitleLabel;
    /**
    * This is the button that cancels starting a new game.
    */
    private javax.swing.JButton gameSettingsCancelButton;
    /**
    * This is a panel displaying the spinners and buttons used to configure
    * settings for a new game.
    */
    private javax.swing.JPanel gameSettingsPanel;
    /**
    * This is the button for resetting the game settings to their default values.
    */
    private javax.swing.JButton gameSettingsResetButton;
    /**
    * This is the button that actually starts the game.
    */
    private javax.swing.JButton gameSettingsStartButton;
    /**
    * This is the toggle button for setting whether the game is rendered
    * in high quality.
    */
    private javax.swing.JToggleButton hqToggle;
    /**
    * This is used to toggle whether a game is currently being played or not.
    */
    private javax.swing.JToggleButton inGameToggle;
    /**
    * This is a button to make it so the snake cannot crash.
    * In other words, this grants infinite lives.
    */
    private javax.swing.JButton inftyFailsButton;
    /**
    * This is the layered pane displaying the layers that make up the game.
    */
    private javax.swing.JLayeredPane layeredPane;
    /**
    * This button forces a lose senario.
    */
    private javax.swing.JButton loseTestButton;
    /**
    * This is a button for forwarding the game by one tick.
    */
    private javax.swing.JButton nextTickButton;
    /**
    * The label displaying the icon showing the controls for player one.
    */
    private javax.swing.JLabel p1CtrlIconLabel;
    /**
    * The label stating what the player one controls do.
    */
    private javax.swing.JLabel p1CtrlTextLabel;
    /**
    * A panel containing the buttons for the pause menu.
    */
    private javax.swing.JPanel pauseButtonsPanel;
    /**
    * The label displaying the icon showing the button to press to pause
    * the game.
    */
    private javax.swing.JLabel pauseCtrlIconLabel;
    /**
    * The label stating that this is the button for pausing the game.
    */
    private javax.swing.JLabel pauseCtrlTextLabel;
    /**
    * The panel displaying the pause menu for the game.
    */
    private javax.swing.JPanel pauseMenuPanel;
    /**
    * The label displaying the title for the pause menu when in
    * gameplay.
    */
    private javax.swing.JLabel pauseTitleLabel;
    /**
    * This is used to toggle whether the game is paused.
    */
    private javax.swing.JToggleButton pauseToggle;
    /**
    * The play field on which the game is played on.
    */
    private snake.JPlayField playField;
    /**
    * This is a button used in debugging to print debug data.
    */
    private javax.swing.JButton printButton;
    /**
    * This is the label for the spinner to set the number of rows.
    */
    private javax.swing.JLabel rLabel;
    /**
    * The spinner for setting how many rows there will be in the play field.
    */
    private javax.swing.JSpinner rSpinner;
    /**
    * This is the label stating what length the player managed to reach.
    */
    private javax.swing.JLabel resultLengthLabel;
    /**
    * This is the label to display when the player looses.
    */
    private javax.swing.JLabel resultLostLabel;
    /**
    * This is the button on the results menu to go back to the pause menu.
    */
    private javax.swing.JButton resultMenuButton;
    /**
    * This is the button on the results menu to allow the player to start
    * a new game from the results menu.
    */
    private javax.swing.JButton resultStartGameButton;
    /**
    * This is the label to display when the player wins.
    */
    private javax.swing.JLabel resultWinLabel;
    /**
    * This is the panel displaying the results screen at the end of a game.
    */
    private javax.swing.JPanel resultsPanel;
    /**
    * The label displaying the title for the results menu.
    */
    private javax.swing.JLabel resultsTitleLabel;
    /**
    * The button for resuming the current game of Snake.
    */
    private javax.swing.JButton resumeGameButton;
    /**
    * This button revives the snake.
    */
    private javax.swing.JButton reviveSnakeButton;
    /**
    * This is a button for saving layers that are currently being displayed.
    */
    private javax.swing.JButton saveLayerButton;
    /**
    * The combo box for selecting the layer(s) to save to a file.
    */
    private javax.swing.JComboBox<String> saveLayerCombo;
    /**
    * A panel to contain the components for setting the settings.
    */
    private javax.swing.JPanel settingCompPanel;
    /**
    * The button to change the settings for the game.
    */
    private javax.swing.JButton settingsButton;
    /**
    * This is the button to return from the settings.
    */
    private javax.swing.JButton settingsDoneButton;
    /**
    * This is the panel used to configure some of the settings for the
    * game snake. The settings that can be configured are the settings
    * that don't necessitate a new game, such as changes to the graphics.
    */
    private javax.swing.JPanel settingsPanel;
    /**
    * This is the button for resetting the settings to their default values.
    */
    private javax.swing.JButton settingsResetButton;
    /**
    * The label displaying the title for the settings menu.
    */
    private javax.swing.JLabel settingsTitleLabel;
    /**
    * A combo box for setting the overlay layer being shown.
    */
    private javax.swing.JComboBox<String> showLayerCombo;
    /**
    * The button for starting a new game of snake.
    */
    private javax.swing.JButton startGameButton;
    /**
    * The label displaying the title for the pause menu that
    * displays the name of the program when not in gameplay.
    */
    private javax.swing.JLabel titleLabel;
    /**
    * The label displaying the version of the program.
    */
    private javax.swing.JLabel versionLabel;
    /**
    * This button forces a win senario.
    */
    private javax.swing.JButton winTestButton;
    /**
    * This is the toggle button used to toggle whether the snake is
    * allowed to wrap around the edges of the play field.
    */
    private javax.swing.JToggleButton wrapAroundToggle;
    // End of variables declaration//GEN-END:variables
    /**
     * This is an Action that adds the command to move the snake in a given 
     * direction to the snake's action queue.
     * 
     * @author Milo Steier
     * @see Snake
     * @see SnakeCommand
     * @see Snake#getActionQueue 
     */
    private class SnakeMoveInputAction extends AbstractAction{
        /**
         * The direction in which the snake is to move.
         */
        private final int direction;
        /**
         * This constructs a SnakeMoveInputAction with the given name and 
         * direction for the snake to move in.
         * @param name The name for this action.
         * @param direction The direction in which the snake is to move in (must 
         * be either {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
         * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}).
         * @throws IllegalArgumentException If the given direction is not one of 
         * the four direction flags.
         */
        public SnakeMoveInputAction(String name, int direction){
            super(name);
                // Check the direction for the snake
            this.direction = SnakeUtilities.requireSingleDirection(direction);
        }
        /**
         * This constructs a SnakeMoveInputAction with the given direction for 
         * the snake to move in.
         * @param direction The direction in which the snake is to move in (must 
         * be either {@link #UP_DIRECTION}, {@link #DOWN_DIRECTION}, {@link 
         * #LEFT_DIRECTION}, or {@link #RIGHT_DIRECTION}).
         * @throws IllegalArgumentException If the given direction is not one of 
         * the four direction flags.
         */
        public SnakeMoveInputAction(int direction){
            super();
                // Check the direction for the snake
            this.direction = SnakeUtilities.requireSingleDirection(direction);
                // Set the name for this action based off the command it gives 
                // to the snake
            super.putValue(NAME, "Snake->"+SnakeMoveInputAction.this.getCommand().toString());
        }
        /**
         * This returns the direction in which the snake will move in.
         * @return The direction in which the snake will move in.
         * @see #UP_DIRECTION
         * @see #DOWN_DIRECTION
         * @see #LEFT_DIRECTION
         * @see #RIGHT_DIRECTION
         * @see #getCommand
         */
        public int getDirection(){
            return direction;
        }
        /**
         * This returns the snake command to be added to the snake's action 
         * queue when this action is performed. 
         * @return {@link SnakeCommand#MOVE_UP SnakeCommand.MOVE_UP} if the 
         * direction is {@link #UP_DIRECTION}, {@link SnakeCommand#MOVE_DOWN 
         * SnakeCommand.MOVE_DOWN} if the direction is {@link #DOWN_DIRECTION}, 
         * {@link SnakeCommand#MOVE_LEFT SnakeCommand.MOVE_LEFT} if the 
         * direction is {@link #LEFT_DIRECTION}, {@link SnakeCommand#MOVE_RIGHT 
         * SnakeCommand.MOVE_RIGHT} if the direction is {@link 
         * #RIGHT_DIRECTION}, or null if the direction is none of the previously 
         * mentioned directions.
         * @see #UP_DIRECTION
         * @see #DOWN_DIRECTION
         * @see #LEFT_DIRECTION
         * @see #RIGHT_DIRECTION
         * @see SnakeCommand#MOVE_UP
         * @see SnakeCommand#MOVE_DOWN
         * @see SnakeCommand#MOVE_LEFT
         * @see SnakeCommand#MOVE_RIGHT
         * @see #getDirection 
         */
        public SnakeCommand getCommand(){
            switch(direction){             // Determine the direction to move in
                case(UP_DIRECTION):         // If moving up
                    return SnakeCommand.MOVE_UP;
                case(DOWN_DIRECTION):       // If moving down
                    return SnakeCommand.MOVE_DOWN;
                case(LEFT_DIRECTION):       // If moving left
                    return SnakeCommand.MOVE_LEFT;
                case(RIGHT_DIRECTION):      // If moving right
                    return SnakeCommand.MOVE_RIGHT;
            }
            return null;
        }
        /**
         * {@inheritDoc }
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            snake.getActionQueue().offer(getCommand());
        }
    }
}
