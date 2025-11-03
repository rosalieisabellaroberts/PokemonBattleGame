/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author dilro
 */
public class BattleStartGUI extends JPanel
{
    private JTextArea battleLog;
    private JPanel middlePanel;
    private JLabel trainerLabel;    
    private JLabel starterPokemonLabel; 
    private SetupGUI gui;
    private BattleManager battleManager;
    private Pokemon starterPokemon; 
    private Trainer trainer;
    private Connection connection;
    
    // Constructor to set starterPokemon for trainer 
    public BattleStartGUI(Trainer trainer, Connection connection, SetupGUI gui, BattleManager battleManager)
    {
        this.trainer = trainer;
        this.connection = connection;
        this.starterPokemon = trainer.getStarterPokemon();
        this.gui = gui;
        this.battleManager = battleManager;
        
        setLayout(new BorderLayout());
        
        // TOP TEXT AREA (battle log)
        // Create a new JTextArea
        battleLog = new JTextArea();
        // Set as not editable for users, only showing displaying battle log output
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Consolas", Font.PLAIN, 14));
        
        // TOP RIGHT IMAGE ICON (professor oak)
        ImageIcon oakIcon = new ImageIcon("C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\oak.png");
        JLabel oakLabel = new JLabel(oakIcon);
        oakLabel.setHorizontalAlignment(JLabel.CENTER);
        oakLabel.setVerticalAlignment(JLabel.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(oakLabel, BorderLayout.CENTER);
        
        // TOP SPLIT PANE (battle log and professor oak)
        JScrollPane scrollPane = new JScrollPane(battleLog);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, imagePanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.NORTH);
        
        // MIDDLE PANEL (trainer and starter pokemon)
        middlePanel = new JPanel(new FlowLayout());
        // Create labels for trainer and starter pokemon 
        trainerLabel = new JLabel(new ImageIcon("C:\\Users\\dilro\\OneDrive\\Documents\\NetBeansProjects\\PokemonBattleGame\\src\\trainer.png"));
        starterPokemonLabel = new JLabel(new ImageIcon(new ImageIcon(starterPokemon.getImagePath()).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        // Add labels to middle panel
        middlePanel.add(trainerLabel);
        middlePanel.add(starterPokemonLabel);
        add(middlePanel, BorderLayout.CENTER);
        
        // Make the panel focusable so it can receive key events and detect any key pressed by user
        setFocusable(true);
        
        // Request focus when panel is first displayed to receive key events
        addHierarchyListener(new java.awt.event.HierarchyListener() 
        {
            @Override
            // Check if the showing state of the component has changed
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) 
            {
                // If SHOWING_CHANGED flag is raised, component has changed (been shown or hidden)
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0)
                {
                    // If panel is visible on screen
                    if (isShowing())
                    {
                        // Request focus so the panel can receive keyboard events
                        requestFocusInWindow();
                    }
                }
            }
        });
        
        // Add a key press to detect any key press from the user 
        addKeyListener(new java.awt.event.KeyAdapter() 
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e)
            {
                // Start battle logic if key is pressed
                startBattleLogic();
                
                // Remove key listener so it only triggers once
                removeKeyListener(this);
            }
        });
    }
    
    // Method for starting battle logic
    private void startBattleLogic() 
    {
        // Run battle in background thread
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    battleManager.startBattle(connection);
                } catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }          
    
    // Method for adding messages to the window 
    public void appendMessage(String message)
    {
        battleLog.append(message + "\n");
        
        // Auto scroll in output area
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
    
    // Setter method
    public void setBattleManager(BattleManager battleManager) 
    {
        this.battleManager = battleManager;
    }
}
    

