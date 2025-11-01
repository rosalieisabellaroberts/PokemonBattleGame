/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pokemonbattlegame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author dilro
 */
public class BattleStartGUI extends JPanel
{
    private JTextArea battleLog;
    private JTextField inputField;
    private InputListener listener;
    private JPanel middlePanel;
    // Allow space for 4 pokeballs (in event pikachu is selected)
    private JLabel trainerLabel;    
    private JLabel starterPokemonLabel; 
    private Pokemon starterPokemon;
    
    
    // Constructor to set starterPokemon for trainer 
    public BattleStartGUI(Pokemon starterPokemon)
    {
        this.starterPokemon = starterPokemon;
        
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
        
        // BOTTOM INPUT FIELD (user input)
        inputField = new JTextField();
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(inputField, BorderLayout.SOUTH);
               
        // Add action listener for enter key 
        inputField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Get and trim user input from inputField
                String userInput = inputField.getText().trim();
                
                // If there is user input 
                if (!userInput.isEmpty())
                {
                    // Append user message to output area 
                    appendMessage("You: " + userInput);
                    // Reset text field
                    inputField.setText("");
                    
                    // If event listener is not null
                    if (listener != null)
                    {
                        // Pass user input to game logic 
                        listener.onInputSubmitted(userInput);
                    }
                }
            }         
        });
    }
    
    // Method for adding messages to the window 
    public void appendMessage(String message)
    {
        battleLog.append(message + "\n");
        
        // Auto scroll in output area
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
    
    // Set input listener for game logic
    public void setInputListener(InputListener listener)
    {
        this.listener = listener;
    }
    
    // Input callbacks 
    public interface InputListener
    {
        void onInputSubmitted(String input);
    }
}

